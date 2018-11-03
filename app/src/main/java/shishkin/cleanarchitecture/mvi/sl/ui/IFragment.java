package shishkin.cleanarchitecture.mvi.sl.ui;

import android.view.View;


import androidx.annotation.IdRes;
import shishkin.cleanarchitecture.mvi.sl.Subscriber;
import shishkin.cleanarchitecture.mvi.sl.model.ModelView;
import shishkin.cleanarchitecture.mvi.sl.state.Stateable;

/**
 * Интерфейс фрагмента
 */
@SuppressWarnings("unused")
public interface IFragment extends Subscriber, IView, ModelView, Stateable {
    /**
     * Найти view во фрагменте
     *
     * @param <V> the type view
     * @param id  the id view
     * @return the view
     */
    <V extends View> V findView(@IdRes int id);

    /**
     * получить IActivitySubscriber фрагмента
     *
     * @return IActivitySubscriber фрагмента
     */
    IActivity getActivitySubscriber();

    /**
     * Получить корневой View объект
     *
     * @return корневой View
     */
    View getRootView();

    /**
     * Закрыть
     */
    void exit();

    /**
     * Событие - право разрешено
     *
     * @param permission право
     */
    void onPermisionGranted(final String permission);

    /**
     * Событие - право запрещено
     *
     * @param permission право
     */
    void onPermisionDenied(final String permission);

    /**
     * Запросить право приложению
     *
     * @param permission право
     */
    void grantPermission(String permission);

    /**
     * Запросить право приложению
     *
     * @param listener    имя слушателя
     * @param permission  право
     * @param helpMessage сообщение если право запрещено спрашивать
     */
    void grantPermission(String listener, String permission, String helpMessage);
}
