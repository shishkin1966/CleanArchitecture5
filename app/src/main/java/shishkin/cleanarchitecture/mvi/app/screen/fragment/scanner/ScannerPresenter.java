package shishkin.cleanarchitecture.mvi.app.screen.fragment.scanner;

import android.Manifest;

import com.google.zxing.BarcodeFormat;


import java.util.ArrayList;
import java.util.List;


import me.dm7.barcodescanner.zxing.ZXingScannerView;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.viewdata.ScannerViewData;
import shishkin.cleanarchitecture.mvi.sl.event.ShowDialogEvent;
import shishkin.cleanarchitecture.mvi.sl.presenter.AbsPresenter;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class ScannerPresenter extends AbsPresenter<ScannerModel> implements ZXingScannerView.ResultHandler {

    public static final String NAME = ScannerPresenter.class.getName();

    private ScannerViewData viewData;

    public ScannerPresenter(ScannerModel model) {
        super(model);

        viewData = SLUtil.getCacheSpecialist().get(ScannerViewData.NAME, ScannerViewData.class);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isRegister() {
        return true;
    }

    private ZXingScannerView getScannerView() {
        return getModel().getView().getScannerView();
    }

    @Override
    public void onStart() {
        if (viewData == null) {
            viewData = SLUtil.getCacheSpecialist().get(ScannerViewData.NAME, ScannerViewData.class);
            if (viewData == null) {
                viewData = new ScannerViewData();
            }
        }
        if (!SLUtil.getActivityUnion().checkPermission(Manifest.permission.CAMERA)) {
            SLUtil.getActivityUnion().grantPermission(Manifest.permission.CAMERA);
        }
    }

    private void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<>();
        for (int index : viewData.getSelectedIndices()) {
            formats.add(ZXingScannerView.ALL_FORMATS.get(index));
        }
        getScannerView().setFormats(formats);
    }

    @Override
    public void onResumeView() {
        super.onResumeView();

        getScannerView().setResultHandler(this);
        setupFormats();
        getScannerView().startCamera(viewData.getCameraId());
        getScannerView().setFlash(false);
        getScannerView().setAutoFocus(true);
    }

    @Override
    public void onPauseView() {
        super.onPauseView();

        getScannerView().stopCamera();
    }

    @Override
    public void handleResult(com.google.zxing.Result result) {
        SLUtil.getActivityUnion().showDialog(new ShowDialogEvent(-1, null, "Код", result.getText()));
        getModel().getView().exit();
    }

    @Override
    public void onDestroyView() {
        SLUtil.getCacheSpecialist().put(ScannerViewData.NAME, viewData);

        super.onDestroyView();
    }

}

