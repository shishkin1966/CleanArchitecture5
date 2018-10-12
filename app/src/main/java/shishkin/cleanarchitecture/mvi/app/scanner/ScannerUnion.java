package shishkin.cleanarchitecture.mvi.app.scanner;

import android.graphics.Bitmap;


import me.dm7.barcodescanner.zxing.ZXingScannerView;
import shishkin.cleanarchitecture.mvi.sl.SmallUnion;

/**
 * Объединение, предоставляющее сервис сканирования подписчикам.
 */
@SuppressWarnings("unused")
public interface ScannerUnion extends SmallUnion<ScannerSubscriber>, ZXingScannerView.ResultHandler {

    /**
     * Начать сканирование
     */
    void scan();

    void scanVision();

    void decodeVision(Bitmap bitmap);
}

