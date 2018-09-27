package shishkin.cleanarchitecture.mvi.sl;

import android.view.LayoutInflater;


import shishkin.cleanarchitecture.mvi.sl.event.ShowFragmentEvent;
import shishkin.cleanarchitecture.mvi.sl.event.ShowKeyboardEvent;
import shishkin.cleanarchitecture.mvi.sl.event.ShowMessageEvent;
import shishkin.cleanarchitecture.mvi.sl.event.StartActivityEvent;
import shishkin.cleanarchitecture.mvi.sl.event.StartActivityForResultEvent;
import shishkin.cleanarchitecture.mvi.sl.event.StartChooseActivityEvent;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsActivity;
import shishkin.cleanarchitecture.mvi.sl.ui.IActivity;
import shishkin.cleanarchitecture.mvi.sl.ui.IView;

/**
 * Интерфейс объединения Activity
 */
@SuppressWarnings("unused")
public interface ActivityUnion extends IView, Union<IActivity> {

    /**
     * Обрабатывает событие - показать сообщение на экран
     *
     * @param event событие
     */
    void showSnackbar(ShowMessageEvent event);

    /**
     * Обрабатывает событие - показать Toast на экран
     *
     * @param event событие
     */
    void showMessage(ShowMessageEvent event);

    /**
     * Обрабатывает событие - скрыть клавиатуру
     */
    void hideKeyboard();

    /**
     * Обрабатывает событие - показать клавиатуру
     *
     * @param event событие
     */
    void showKeyboard(ShowKeyboardEvent event);

    /**
     * Получить LayoutInflater
     *
     * @return the LayoutInflater
     */
    LayoutInflater getInflater();

    /**
     * Получить фрагмент по его id.
     *
     * @param <F> тип фрагмента
     * @param cls класс фрагмента
     * @param id  the id
     * @return фрагмент
     */
    <F> F getFragment(final Class<F> cls, final int id);

    /**
     * Показать фрагмент
     *
     * @param event событие
     */
    void showFragment(ShowFragmentEvent event);

    /**
     * Переключиться на фрагмент
     *
     * @param name имя фрагмента
     */
    void switchToFragment(String name);

    /**
     * Переключиться на top фрагмент
     */
    void switchToTopFragment();

    /**
     * Перейти по BackPress
     */
    void back();

    /**
     * Получить AbstractActivity
     *
     * @return the AbstractActivity
     */
    <C> C getActivity();

    /**
     * Получить AbstractActivity
     *
     * @param name имя activity
     * @return the AbstractActivity
     */
    <C> C getActivity(String name);

    /**
     * Получить AbstractActivity
     *
     * @param name     имя activity
     * @param validate флаг - проверять activity на валидность
     * @return the AbstractActivity
     */
    <C> C getActivity(String name, boolean validate);

    /**
     * Проверить наличие записей в BackStack
     *
     * @return true - записей нет
     */
    boolean isBackStackEmpty();

    /**
     * Получить кол-во записей в BackStack
     *
     * @return кол-во записей
     */
    int getBackStackEntryCount();

    /**
     * Проверить наличие Top фрагмента
     *
     * @return true - Top фрагмент есть
     */
    boolean hasTop();

    /**
     * Start Activity
     *
     * @param event событие
     */
    void startActivity(StartActivityEvent event);

    /**
     * Start Choose Activity
     *
     * @param event событие
     */
    void startChooseActivity(StartChooseActivityEvent event);

    /**
     * Start Activity for Result
     *
     * @param event событие
     */
    void startActivityForResult(StartActivityForResultEvent event);

    /**
     * Показать Activity
     *
     * @param activity Activity
     */
    void showActivity(AbsActivity activity);

    /**
     * Проверить право у приложения
     *
     * @param permission право
     */
    boolean checkPermission(String permission);

    /**
     * запросить право приложению
     *
     * @param permission право
     */
    void grantPermission(String permission);
}
