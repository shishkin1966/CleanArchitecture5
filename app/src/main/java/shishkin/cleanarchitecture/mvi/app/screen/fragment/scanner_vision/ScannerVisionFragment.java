package shishkin.cleanarchitecture.mvi.app.screen.fragment.scanner_vision;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;


import java.io.IOException;


import shishkin.cleanarchitecture.mvi.R;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.sl.ErrorSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsContentFragment;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class ScannerVisionFragment extends AbsContentFragment<ScannerVisionModel> implements ScannerVisionView {

    public static ScannerVisionFragment newInstance() {
        return new ScannerVisionFragment();
    }

    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private SurfaceView cameraView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle state) {
        return inflater.inflate(R.layout.fragment_scanner_vision, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cameraView = findView(R.id.surface_view);

        barcodeDetector = new BarcodeDetector.Builder(SLUtil.getContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(SLUtil.getContext(), barcodeDetector)
                .setRequestedFps(15.0f)
                .setRequestedPreviewSize(960, 960)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (SLUtil.getActivityUnion().checkPermission(Manifest.permission.CAMERA)) {
                        cameraSource.start(cameraView.getHolder());
                    }
                } catch (IOException e) {
                    ErrorSpecialistImpl.getInstance().onError(getName(), e);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections detections) {
                final SparseArray barcodes = detections.getDetectedItems();
                if (barcodes.size() > 0) {
                    exit();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        cameraSource.release();
        barcodeDetector.release();
    }

    @Override
    public String getName() {
        return ScannerVisionFragment.class.getName();
    }

    @Override
    public ScannerVisionModel createModel() {
        return new ScannerVisionModel(this);
    }

}
