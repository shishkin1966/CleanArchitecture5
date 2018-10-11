package shishkin.cleanarchitecture.mvi.app.scanner;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import shishkin.cleanarchitecture.mvi.sl.SmallUnion;

/**
 * Интерфейс объединения, предоставляющее сервис геолокации подписчикам.
 */
@SuppressWarnings("unused")
public interface ScannerUnion extends SmallUnion<ScannerSubscriber>, ZXingScannerView.ResultHandler {

    /**
     * Начать сканирование
     */
    void scan();
}

