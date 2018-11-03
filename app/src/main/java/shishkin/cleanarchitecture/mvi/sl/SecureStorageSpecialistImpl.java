package shishkin.cleanarchitecture.mvi.sl;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import com.google.common.base.Charsets;


import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.locks.ReentrantLock;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.security.auth.x500.X500Principal;


import androidx.annotation.NonNull;
import io.paperdb.Paper;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.common.utils.CollectionsUtils;
import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.data.Result;


@SuppressWarnings("unused")
public class SecureStorageSpecialistImpl extends AbsSpecialist implements SecureStorageSpecialist {

    public static final String NAME = SecureStorageSpecialistImpl.class.getName();

    private static final String ANDROID_KEYSTORE_INSTANCE = "AndroidKeyStore";

    private KeyStore keyStore;
    private String alias;
    private ReentrantLock lock = new ReentrantLock();

    private static final String HASH_ALGORITHM = "SHA-1";
    private static final String KEY_ALGORITHM = "RSA";
    private static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";
    private static final String TRANSFORMATION_M = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    private boolean isHardwareSupport = false;

    @Override
    public Result<Boolean> validateExt() {
        return new Result<>(ApplicationUtils.hasKitKat());
    }

    @Override
    public void onRegister() {
        lock = new ReentrantLock();

        try {
            final MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            final byte[] hash = digest.digest(NAME.getBytes(Charsets.UTF_8));
            alias = StringUtils.byteArrayToHex(hash);

            final Context context = SLUtil.getContext();
            if (context != null) {
                Paper.init(context);
            }

            if (validate()) {
                keyStore = KeyStore.getInstance(ANDROID_KEYSTORE_INSTANCE);
                keyStore.load(null);
                isHardwareSupport = checkKeyPair();
            } else {
                SLUtil.onError(NAME, "Not support os version", false);
            }
        } catch (Exception e) {
            SLUtil.onError(NAME, e);
        }
    }

    private boolean checkKeyPair() {
        if (!validate()) {
            return false;
        }

        lock.lock();

        try {
            if (!keyStore.containsAlias(alias)) {
                if (createKeyPair()) {
                    return true;
                }
            } else {
                return true;
            }
        } catch (Exception e) {
            SLUtil.onError(NAME, e);
        } finally {
            lock.unlock();
        }
        return false;
    }

    private boolean createKeyPair() {
        KeyPairGenerator keyPairGenerator = null;

        try {
            /**
             * On Android Marshmellow we can use new security features
             */
            if (ApplicationUtils.hasMarshmallow()) {
                keyPairGenerator = KeyPairGenerator.getInstance(
                        KEY_ALGORITHM, ANDROID_KEYSTORE_INSTANCE);

                keyPairGenerator.initialize(
                        new KeyGenParameterSpec.Builder(
                                alias,
                                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                                .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
                                .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                                .setAlgorithmParameterSpec(new RSAKeyGenParameterSpec(2048, RSAKeyGenParameterSpec.F4))
                                .setKeySize(2048)
                                .build());
            } else {
                if (ApplicationUtils.hasKitKat()) {
                    final Calendar start = new GregorianCalendar();
                    final Calendar end = new GregorianCalendar();
                    end.add(Calendar.ERA, 1);

                    final Context context = SLUtil.getContext();
                    KeyPairGeneratorSpec keyPairGeneratorSpec =
                            new KeyPairGeneratorSpec.Builder(context)
                                    .setAlias(alias)
                                    // The subject used for the self-signed certificate of the generated pair
                                    .setSubject(new X500Principal("CN=" + context.getPackageName()))
                                    // The serial number used for the self-signed certificate of the
                                    // generated pair.
                                    .setKeyType(KeyProperties.KEY_ALGORITHM_RSA)
                                    .setKeySize(2048)
                                    .setSerialNumber(BigInteger.valueOf(1967))
                                    .setStartDate(start.getTime())
                                    .setEndDate(end.getTime())
                                    .build();

                    keyPairGenerator = KeyPairGenerator
                            .getInstance(KEY_ALGORITHM, ANDROID_KEYSTORE_INSTANCE);
                    keyPairGenerator.initialize(keyPairGeneratorSpec);
                }
            }
            final KeyPair keyPair = keyPairGenerator.generateKeyPair();
            if (keyPair == null) {
                return false;
            }
        } catch (Exception e) {
            SLUtil.onError(NAME, e);
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteKeyPair() {
        lock.lock();

        try {
            if (keyStore.containsAlias(alias)) {
                keyStore.deleteEntry(alias);
                if (keyStore.containsAlias(alias)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            SLUtil.onError(NAME, e);
        } finally {
            lock.unlock();
        }
        return false;
    }

    private String encryptBase64(final String data) {
        if (!isHardwareSupport) return null;

        if (!checkKeyPair()) {
            return null;
        }

        lock.lock();

        KeyStore.PrivateKeyEntry privateKeyEntry = null;
        try {
            privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
        } catch (Exception e) {
            SLUtil.onError(NAME, e);
        }

        try {
            if (privateKeyEntry != null) {
                final PublicKey publicKey = privateKeyEntry.getCertificate().getPublicKey();
                return encryptBase64(data, publicKey);
            }
        } catch (Exception e) {
            SLUtil.onError(NAME, e);
        } finally {
            lock.unlock();
        }

        return null;
    }

    private String encryptBase64(final String utf8string, final PublicKey publicKey) {
        if (utf8string == null) {
            return null;
        }

        if (publicKey == null) {
            return null;
        }
        try {
            final byte[] dataBytes = utf8string.getBytes(Charsets.UTF_8);
            Cipher cipher = null;
            if (ApplicationUtils.hasMarshmallow()) {
                cipher = Cipher.getInstance(TRANSFORMATION_M);
            } else {
                cipher = Cipher.getInstance(TRANSFORMATION);
            }
            if (ApplicationUtils.hasO()) {
                OAEPParameterSpec sp = new OAEPParameterSpec("SHA-256",
                        "MGF1", new MGF1ParameterSpec("SHA-1"),
                        PSource.PSpecified.DEFAULT);
                cipher.init(Cipher.ENCRYPT_MODE, publicKey, sp);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            }
            return Base64.encodeToString(cipher.doFinal(dataBytes), Base64.DEFAULT);
        } catch (Exception e) {
            SLUtil.onError(NAME, e);
        }
        return null;
    }

    private String decryptBase64(final String base64string, final PrivateKey privateKey) {
        if (base64string == null) {
            return null;
        }

        if (privateKey == null) {
            return null;
        }

        try {
            final byte[] dataBytes = Base64.decode(base64string, Base64.DEFAULT);
            Cipher cipher = null;
            if (ApplicationUtils.hasMarshmallow()) {
                cipher = Cipher.getInstance(TRANSFORMATION_M);
            } else {
                cipher = Cipher.getInstance(TRANSFORMATION);
            }
            if (ApplicationUtils.hasO()) {
                OAEPParameterSpec sp = new OAEPParameterSpec("SHA-256",
                        "MGF1", new MGF1ParameterSpec("SHA-1"),
                        PSource.PSpecified.DEFAULT);
                cipher.init(Cipher.DECRYPT_MODE, privateKey, sp);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
            }
            return new String(cipher.doFinal(dataBytes));
        } catch (Exception e) {
            SLUtil.onError(NAME, e);
        }
        return null;
    }

    private String decryptBase64(final String base64string) {
        if (!isHardwareSupport) return null;

        if (!checkKeyPair()) {
            return null;
        }

        lock.lock();

        KeyStore.PrivateKeyEntry privateKeyEntry = null;
        try {
            privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
        } catch (Exception e) {
            SLUtil.onError(NAME, e);
        }

        try {
            if (privateKeyEntry != null) {
                final PrivateKey privateKey = privateKeyEntry.getPrivateKey();
                return decryptBase64(base64string, privateKey);
            }
        } catch (Exception e) {
            SLUtil.onError(NAME, e);
        } finally {
            lock.unlock();
        }
        return null;
    }

    @Override
    public boolean put(@NonNull final String key, @NonNull final String data) {
        if (!isHardwareSupport) return false;

        lock.lock();

        try {
            final MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            final byte[] hash = digest.digest(key.getBytes(Charsets.UTF_8));
            final String alias = StringUtils.byteArrayToHex(hash);

            if (checkKeyPair()) {
                final String dataEncrypted = encryptBase64(data);
                if (dataEncrypted != null) {
                    Paper.book(this.alias).delete(alias);
                    final byte[] byteEncrypted = dataEncrypted.getBytes(Charsets.UTF_8);
                    Paper.book(this.alias).write(alias, byteEncrypted);
                    final byte[] dataRestored = Paper.book(this.alias).read(alias, null);
                    if (CollectionsUtils.equals(byteEncrypted, dataRestored)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            SLUtil.onError(NAME, e);
        } finally {
            lock.unlock();
        }
        return false;
    }

    @Override
    public String get(@NonNull final String key) {
        if (!isHardwareSupport) return null;

        lock.lock();

        try {
            final MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            final byte[] hash = digest.digest(key.getBytes(Charsets.UTF_8));
            final String alias = StringUtils.byteArrayToHex(hash);

            final byte[] data = Paper.book(this.alias).read(alias, null);
            if (data != null) {
                return decryptBase64(new String(data));
            }
        } catch (Exception e) {
            SLUtil.onError(NAME, e);
        } finally {
            lock.unlock();
        }
        return null;
    }

    @Override
    public void clear(@NonNull final String key) {
        lock.lock();

        try {
            final MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            final byte[] hash = digest.digest(key.getBytes(Charsets.UTF_8));
            final String alias = StringUtils.byteArrayToHex(hash);

            Paper.book(this.alias).delete(alias);
        } catch (Exception e) {
            SLUtil.onError(NAME, e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (SecureStorageSpecialist.class.isInstance(o)) ? 0 : 1;
    }

    @Override
    public String getName() {
        return NAME;
    }

}
