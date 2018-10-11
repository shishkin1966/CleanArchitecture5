package shishkin.cleanarchitecture.mvi.app.scanner;

import shishkin.cleanarchitecture.mvi.sl.SpecialistSubscriber;

public interface ScannerSubscriber extends SpecialistSubscriber {

    /**
     * Событие - произошло сканирование
     *
     * @param text сканированный текст
     */
    void onScan(String text);
}
