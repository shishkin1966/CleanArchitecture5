package shishkin.cleanarchitecture.mvi.app.barcode;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;


import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import shishkin.cleanarchitecture.mvi.BuildConfig;
import shishkin.cleanarchitecture.mvi.app.ApplicationConstant;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.common.utils.ApplicationUtils;
import shishkin.cleanarchitecture.mvi.common.utils.CloseUtils;
import shishkin.cleanarchitecture.mvi.sl.AbsSpecialist;
import shishkin.cleanarchitecture.mvi.sl.ApplicationSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.ErrorSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.event.StartActivityForResultEvent;

public class ScannerSpecialistImpl extends AbsSpecialist implements ScannerSpecialist {

    public static final String NAME = ScannerSpecialistImpl.class.getName();

    private BarcodeDetector detector;
    private Uri uri;

    @Override
    public void onRegister() {
        detector = new BarcodeDetector.Builder(ApplicationSpecialistImpl.getInstance())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();
    }

    @Override
    public void scan(Uri uri) {
        if (ApplicationUtils.checkPermission(SLUtil.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            SLUtil.getActivityUnion().startActivityForResult(new StartActivityForResultEvent(intent, ApplicationConstant.ScannerSpecialist_REQUEST));
        }
    }

    @Override
    public void scan() {
        if (ApplicationUtils.checkPermission(SLUtil.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            try {
                final File dir = new File(Environment.getExternalStorageDirectory(), BuildConfig.APPLICATION_ID);
                dir.mkdirs();
                final File photo = new File(Environment.getExternalStorageDirectory(), BuildConfig.APPLICATION_ID + "/picture.jpg");
                if (photo.exists()) {
                    photo.delete();
                }
                uri = FileProvider.getUriForFile(ApplicationSpecialistImpl.getInstance(),BuildConfig.APPLICATION_ID + ".provider", photo);
                scan(uri);
            } catch (Exception e) {
                ErrorSpecialistImpl.getInstance().onError(NAME, e);
            }
        }
    }

    @Override
    public List<String> decode() {
        return decode(uri);
    }

    @Override
    public List<String> decode(Uri uri) {
        final List<String> list = new ArrayList<>();
        if (validate()) {
            try {
                if (validate()) {
                    final Bitmap bitmap = decodeBitmapUri(SLUtil.getContext(), uri);
                    if (bitmap != null) {
                        final Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                        final SparseArray<Barcode> barcodes = detector.detect(frame);
                        for (int index = 0; index < barcodes.size(); index++) {
                            Barcode code = barcodes.valueAt(index);
                            list.add(code.rawValue);
                        }
                        bitmap.recycle();
                    }
                }
            } catch (Exception e) {
                ErrorSpecialistImpl.getInstance().onError(NAME, e);
            }
        }
        return list;
    }

    private Bitmap decodeBitmapUri(Context context, Uri uri) {
        InputStream stream = null;
        try {
            int targetW = 600;
            int targetH = 600;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            stream = context.getContentResolver().openInputStream(uri);
            BitmapFactory.decodeStream(stream, null, bmOptions);
            CloseUtils.close(stream);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;

            stream = context.getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(stream, null, bmOptions);
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
        } finally {
            CloseUtils.close(stream);
        }
        return null;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (ScannerSpecialist.class.isInstance(o)) ? 0 : 1;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Result<Boolean> validateExt() {
        return new Result<>(detector == null || (detector != null && detector.isOperational()));
    }


}
