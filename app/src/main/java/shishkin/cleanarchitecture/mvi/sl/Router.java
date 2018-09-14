package shishkin.cleanarchitecture.mvi.sl;

import android.support.v4.app.Fragment;

@SuppressWarnings("unused")
public interface Router extends SpecialistSubscriber {

    /**
     * Переключиться на фрагмент
     *
     * @param name имя фрагмента
     */
    boolean switchToFragment(String name);

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
    void showFragment(Fragment fragment, boolean addToBackStack, boolean clearBackStack,
                      boolean animate, boolean allowingStateLoss);

    /**
     * Событие -  on back pressed.
     */
    void onBackPressed();

    /**
     * Получить фрагмент
     *
     * @param <F> тип фрагмента
     * @param cls класс фрагмента
     * @param id  the id
     * @return фрагмент
     */
    <F> F getFragment(Class<F> cls, final int id);

    /**
     * Получить content фрагмент.
     *
     * @param <F> тип фрагмента
     * @param cls класс фрагмента
     * @return content фрагмент
     */
    <F> F getContentFragment(Class<F> cls);

    /**
     * закрыть activity
     */
    void finish();

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
     * Переключится на Top фрагмент
     */
    void switchToTopFragment();

    /**
     * Проверить наличие Top фрагмента
     *
     * @return true - Top фрагмент есть
     */
    boolean hasTopFragment();

    /**
     * Получить id контейнера, в котором будет отображен фрагмент
     *
     * @return id ресурса
     */
    int getContentResId();
}
