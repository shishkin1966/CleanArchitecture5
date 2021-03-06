package shishkin.cleanarchitecture.mvi.app.screen.fragment.scanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsContentFragment;

/**
 * Created by Shishkin on 17.03.2018.
 */

public class ScannerFragment extends AbsContentFragment<ScannerModel> implements ScannerView {

    public static ScannerFragment newInstance() {
        return new ScannerFragment();
    }

    private ZXingScannerView scannerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle state) {
        scannerView = new ZXingScannerView(getActivity());
        return scannerView;
    }

    @Override
    public String getName() {
        return ScannerFragment.class.getName();
    }

    @Override
    public ScannerModel createModel() {
        return new ScannerModel(this);
    }

    @Override
    public ZXingScannerView getScannerView() {
        return scannerView;
    }
}
