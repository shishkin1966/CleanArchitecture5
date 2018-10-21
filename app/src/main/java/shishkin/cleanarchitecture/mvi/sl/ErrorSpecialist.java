package shishkin.cleanarchitecture.mvi.sl;

import shishkin.cleanarchitecture.mvi.sl.data.ExtError;

/**
 * Интерфейс специалиста обработки ошибок
 */
@SuppressWarnings("unused")
public interface ErrorSpecialist extends Specialist {
    /**
     * Событие - ошибка
     *
     * @param source источник ошибки
     * @param e      Exception
     */
    void onError(String source, Exception e);

    /**
     * Событие - ошибка
     *
     * @param source    источник ошибки
     * @param throwable Throwable
     */
    void onError(String source, Throwable throwable);

    /**
     * Событие - ошибка
     *
     * @param source         источник ошибки
     * @param e              Exception
     * @param displayMessage текст ошибки пользователю
     */
    void onError(String source, Exception e, String displayMessage);

    /**
     * Событие - ошибка
     *
     * @param source    источник ошибки
     * @param message   текст ошибки пользователю
     * @param isDisplay true - отображать на сообщение на дисплее, false - сохранять в журнале
     */
    void onError(final String source, final String message, final boolean isDisplay);

    /**
     * Событие - ошибка
     *
     * @param extError ошибка
     */
    void onError(final ExtError extError);

    /**
     * Получить путь к файлу лога ошибок
     *
     * @return путь к файлу лога ошибок
     */
    String getPath();
}
