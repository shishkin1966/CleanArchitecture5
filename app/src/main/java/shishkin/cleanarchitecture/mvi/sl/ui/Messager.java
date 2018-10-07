package shishkin.cleanarchitecture.mvi.sl.ui;

import shishkin.cleanarchitecture.mvi.sl.event.ShowMessageEvent;

public interface Messager {
    /**
     * Показать сообщение на экран
     *
     * @param event событие
     */
    void showMessage(ShowMessageEvent event);
}
