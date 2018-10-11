package shishkin.cleanarchitecture.mvi.app.screen.fragment.scanner;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import shishkin.cleanarchitecture.mvi.sl.ui.IFragment;

/**
 * Created by Shishkin on 17.03.2018.
 */

public interface ScannerView extends IFragment {
    ZXingScannerView getScannerView();
}
