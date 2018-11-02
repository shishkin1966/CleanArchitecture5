package shishkin.cleanarchitecture.mvi.sl.ui;

import androidx.annotation.IdRes;
import android.view.View;


import shishkin.cleanarchitecture.mvi.sl.SpecialistSubscriber;
import shishkin.cleanarchitecture.mvi.sl.state.Stateable;

/**
 * Интерфейс activity
 */
@SuppressWarnings("unused")
public interface IActivity extends IView, SpecialistSubscriber, Stateable {
    /**
     * Найти view в activity
     *
     * @param <V> the type view
     * @param id  the id view
     * @return the view
     */
    <V extends View> V findView(@IdRes final int id);

    /**
     * Установить цвет status bar телефона
     *
     * @param color цвет Status Bar
     */
    void setStatusBarColor(final int color);

    /**
     * Закрепить текущую ориентацию
     */
    void lockOrientation();

    /**
     * Закрепить ориентацию
     *
     * @param orientation ориентация
     */
    void lockOrientation(int orientation);

    /**
     * Разрешить любую ориентацию
     */
    void unlockOrientation();

    /**
     * Закрыть
     */
    void exit();

    /**
     * очистить Back Stack
     */
    void clearBackStack();

    /**
     * Событие - предоставлено право
     *
     * @param permission право
     */
    void onPermisionGranted(String permission);

    /**
     * Событие - право запрещено
     *
     * @param permission право
     */
    void onPermisionDenied(String permission);

    /**
     * Получить корневой View объект
     *
     * @return корневой View
     */
    View getRootView();

    /**
     * Получить Activity
     *
     * @return Activity activity
     */
    AbsActivity getActivity();

    /**
     * Включить полноэкранный режим
     */
    void setFullScreen();

    /**
     * Скрыть клавиатуру
     */
    void hideKeyboard();

    /**
     * Показать клавиатуру
     */
    void showKeyboard(View view);

    /**
     * Показать клавиатуру
     *
     * @param mode режим отображения клавиатуры
     */
    void showKeyboard(View view, int mode);

}
