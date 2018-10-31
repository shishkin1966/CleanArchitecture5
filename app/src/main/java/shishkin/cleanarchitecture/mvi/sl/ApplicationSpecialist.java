package shishkin.cleanarchitecture.mvi.sl;

import android.content.Context;

/**
 * Интерфейс специалиста приложения.
 */
@SuppressWarnings("unused")
public interface ApplicationSpecialist extends Specialist {

    /**
     * Получить Context приложения
     *
     * @return Context приложения
     */
    Context getApplicationContext();

    /**
     * Событие - приложение обновлено
     *
     * @param oldVersion старая версия приложения
     * @param newVersion новая версия приложения
     */
    void onApplicationUpdated(int oldVersion, int newVersion);

    /**
     * Получить версию приложения
     *
     * @return версия приложения
     */
    int getVersion();

    /**
     * Флаг - приложение остановлено
     *
     * @return true = приложение остановлено
     */
    boolean isStoped();

    /**
     * Событие  - старт приложениея
     */
    void onStart();

    /**
     * Событие  - остановка приложениея
     */
    void onStop();

    /**
     * Событие - приложение уходит в Background
     */
    void onBackgroundApplication();

    /**
     * Событие - приложение выходит из Background
     */
    void onResumeApplication();

    /**
     * Флаг -  выгружать(kill) приложение при остановке(finish)
     */
    boolean isKillOnFinish();

    /**
     * Флаг - приложение находится в Background
     */
    boolean isInBackground();

    /**
     * Получить путь хранения данных на SDCard
     *
     * @return путь хранения данных на SDCard
     */
    String getExternalDataPath();

    /**
     * Получить путь хранения данных
     *
     * @return путь хранения данных
     */
    String getDataPath();

}