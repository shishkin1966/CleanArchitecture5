package shishkin.cleanarchitecture.mvi.app.scanner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.FirebaseApp;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.scanner.ScannerFragment;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.scanner_vision.ScannerVisionFragment;
import shishkin.cleanarchitecture.mvi.sl.AbsSmallUnion;
import shishkin.cleanarchitecture.mvi.sl.ErrorSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.event.ShowFragmentEvent;

public class ScannerUnionImpl extends AbsSmallUnion<ScannerSubscriber> implements ScannerUnion {
    public static final String NAME = ScannerUnionImpl.class.getName();

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isPersistent() {
        return false;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (ScannerUnion.class.isInstance(o)) ? 0 : 1;
    }

    @Override
    public void onRegister() {
        FirebaseApp.initializeApp(SLUtil.getContext());
    }

    @Override
    public void scan() {
        if (SLUtil.getActivityUnion().hasSubscribers()) {
            SLUtil.getActivityUnion().showFragment(new ShowFragmentEvent(ScannerFragment.newInstance()));
        }
    }

    @Override
    public void handleResult(Result result) {
        String text = result.getText();
        if (result.getBarcodeFormat() == BarcodeFormat.QR_CODE) {
            try {
                if (text.startsWith("ST00011|")) {
                    text = new String(text.getBytes("ISO8859_1"), "cp1251");
                } else if (text.startsWith("ST00012|")) {
                } else if (text.startsWith("ST00013|")) {
                    text = new String(text.getBytes("ISO8859_1"), "KOI8-R");
                }
            } catch (Exception e) {
                ErrorSpecialistImpl.getInstance().onError(NAME, e);
            }
        }
        for (WeakReference<ScannerSubscriber> ref : getSubscribers()) {
            if (ref != null && ref.get() != null && ref.get().validate()) {
                ref.get().onScan(text);
            }
        }
    }

    @Override
    public void scanVision() {
        SLUtil.getActivityUnion().showFragment(new ShowFragmentEvent(ScannerVisionFragment.newInstance()));
        /*
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(SLUtil.getContext().getPackageManager()) != null) {
            SLUtil.getActivityUnion().startActivityForResult(new StartActivityForResultEvent(intent, REQUEST_IMAGE_CAPTURE));
        }
        */
    }

    @Override
    public void decodeVision(Bitmap b) {
        if (b == null) return;

        try {
            Bitmap bitmap = decodeBitmap(b);
            final BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(SLUtil.getContext()).setBarcodeFormats(Barcode.ALL_FORMATS).build();
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<Barcode> barcodes = barcodeDetector.detect(frame);
            if (barcodes.size() > 0) {
                final StringBuilder sb = new StringBuilder();
                for (int i = 0; i < barcodes.size(); i++) {
                    sb.append(barcodes.get(barcodes.keyAt(i)).displayValue);
                }
                final String text = sb.toString();
                for (WeakReference<ScannerSubscriber> ref : getSubscribers()) {
                    if (ref != null && ref.get() != null && ref.get().validate()) {
                        ref.get().onScan(text);
                    }
                }
            }
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
        }
        /*
        try {
            final FirebaseVisionBarcodeDetectorOptions options =
                    new FirebaseVisionBarcodeDetectorOptions.Builder()
                            .setBarcodeFormats(
                                    FirebaseVisionBarcode.FORMAT_ALL_FORMATS)
                            .build();
            final FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
            final FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options);
            final Task<List<FirebaseVisionBarcode>> result = detector.detectInImage(image)
                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionBarcode> barcodes) {
                            final StringBuilder sb = new StringBuilder();
                            for (FirebaseVisionBarcode barcode : barcodes) {
                                sb.append(barcode.getDisplayValue() + "\n");
                            }
                            final String text = sb.toString();
                            for (WeakReference<ScannerSubscriber> ref : getSubscribers()) {
                                if (ref != null && ref.get() != null && ref.get().validate()) {
                                    ref.get().onScan(text);
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
        }
        */
    }

    private Bitmap decodeBitmap(Bitmap bitmap) {
        int targetW = 600;
        int targetH = 600;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getInputStream(bitmap), null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeStream(getInputStream(bitmap), null, bmOptions);
    }

    private InputStream getInputStream(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        return new ByteArrayInputStream(bitmapdata);
    }

}
