package shishkin.cleanarchitecture.mvi.sl.ui;

import android.view.MenuItem;

/**
 * Created by Shishkin on 06.01.2018.
 */

public interface ToolbarFragment {

    /**
     * Событие - конфигурирование Toolbar
     */
    void prepareToolbar();

    /**
     * Установить заголовок Toolbar
     *
     * @param title заголовок
     */
    void setTitle(String title);

    /**
     * Установить цвет Toolbar
     *
     * @param color цвет
     */
    void setToolbarColor(int color);

    /**
     * Установить меню Toolbar
     *
     * @param menuId    idRes меню
     * @param isVisible флаг отобразить/скрыть меню
     */
    void setMenu(final int menuId, final boolean isVisible);

    /**
     * Событие - нажат элемент меню Toolbar
     *
     * @param item меню item
     */
    boolean onMenuItemClick(MenuItem item);

    /**
     * Установить состояние элемента меню Toolbar
     *
     * @param id    меню item id
     * @param state состояние меню item
     */
    void setMenuItemState(final int id, final int state);

    /**
     * Установить элемент Toolbar
     *
     * @param itemId    id Drawable ресурса
     * @param isVisible видимость элемента
     */
    void setItem(final int itemId, final boolean isVisible);

    /**
     * Установить видимость кнопки возврата
     *
     * @param visible видимость элемента
     */
    void setBackVisible(boolean visible);

}
