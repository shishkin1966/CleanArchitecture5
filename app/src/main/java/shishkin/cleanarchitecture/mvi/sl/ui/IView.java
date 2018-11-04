package shishkin.cleanarchitecture.mvi.sl.ui;

import shishkin.cleanarchitecture.mvi.sl.viewaction.ViewActionListener;

public interface IView extends Messager, ViewActionListener {

    /**
     * Показать progress bar.
     */
    void showProgressBar();

    /**
     * Скрыть progress bar.
     */
    void hideProgressBar();

}
