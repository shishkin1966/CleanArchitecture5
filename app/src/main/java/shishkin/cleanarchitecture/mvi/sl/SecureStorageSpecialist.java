package shishkin.cleanarchitecture.mvi.sl;

import androidx.annotation.NonNull;

public interface SecureStorageSpecialist extends Specialist {

    boolean deleteKeyPair();

    boolean put(@NonNull final String key, @NonNull final String data);

    String get(@NonNull final String key);

    void clear(@NonNull final String key);
}
