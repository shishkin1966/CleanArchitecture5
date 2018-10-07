package shishkin.cleanarchitecture.mvi.sl.ui;

public interface IView extends Messager {

    /**
     * Показать progress bar.
     */
    void showProgressBar();

    /**
     * Скрыть progress bar.
     */
    void hideProgressBar();

}
