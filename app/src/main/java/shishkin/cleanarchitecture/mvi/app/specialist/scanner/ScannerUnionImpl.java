package shishkin.cleanarchitecture.mvi.app.specialist.scanner;

import android.support.annotation.NonNull;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.app.screen.fragment.scanner.ScannerFragment;
import shishkin.cleanarchitecture.mvi.sl.AbsSmallUnion;
import shishkin.cleanarchitecture.mvi.sl.event.ShowFragmentEvent;

/**
 * Объединение, предоставляющее сервис сканирования подписчикам.
 */
public class ScannerUnionImpl extends AbsSmallUnion<ScannerSubscriber> implements ScannerUnion {
    public static final String NAME = ScannerUnionImpl.class.getName();

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
                SLUtil.onError(NAME, e);
            }
        }
        for (ScannerSubscriber subscriber : getReadySubscribers()) {
            subscriber.onScan(text);
        }
    }
}
