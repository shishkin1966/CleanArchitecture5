package shishkin.cleanarchitecture.mvi.sl.model;

import androidx.fragment.app.Fragment;

public interface ModelRouter {

    /**
     * Завершить приложение
     */
    void finishApplication();

    /**
     * Переключиться на top фрагмент
     */
    void switchToTop();

    /**
     * Показать фрагмент
     *
     * @param fragment фрагмент
     */
    void showFragment(Fragment fragment);

    /**
     * Показать фрагмент с allowingStateLoss
     *
     * @param fragment          фрагмент
     * @param allowingStateLoss флаг - разрешить allowingStateLoss
     */
    void showFragment(Fragment fragment, boolean allowingStateLoss);

    /**
     * Показать фрагмент
     *
     * @param fragment          фрагмент
     * @param addToBackStack    флаг - добавить в back stack
     * @param clearBackStack    флаг - очистить back stack
     * @param animate           флаг - использовать анимацию
     * @param allowingStateLoss флаг - разрешить allowingStateLoss
     */
    void showFragment(Fragment fragment, boolean addToBackStack,
                      boolean clearBackStack,
                      boolean animate, boolean allowingStateLoss);
}
