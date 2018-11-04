package shishkin.cleanarchitecture.mvi.sl.mail;

import java.util.List;


import shishkin.cleanarchitecture.mvi.sl.MessagerSubscriber;
import shishkin.cleanarchitecture.mvi.sl.Subscriber;

@SuppressWarnings("unused")
public interface Mail extends Subscriber {

    /**
     * Прочитать письмо.
     *
     * @param subscriber подписчик получения почты
     */
    void read(MessagerSubscriber subscriber);

    /**
     * Проверить наличие адреса. Проверяются поля Получатель и CopyTo
     *
     * @param address адрес
     * @return true если адрес найден
     */
    boolean contains(String address);

    /**
     * Получить Id письма. Установка Id проводиться MailUnion
     *
     * @return the id
     */
    Long getId();

    /**
     * Установить Id письма. Установка Id проводиться MailUnion
     *
     * @param id id письма
     * @return письмо
     */
    Mail setId(Long id);

    /**
     * Скопировать письмо
     *
     * @return письмо
     */
    Mail copy();

    /**
     * Получить поле CopyTo
     *
     * @return поле CopyTo
     */
    List<String> getCopyTo();

    /**
     * Установить поле CopyTo
     *
     * @param copyTo список адресов
     * @return письмо
     */
    Mail setCopyTo(List<String> copyTo);

    /**
     * Получить адрес получателя
     *
     * @return адрес
     */
    String getAddress();

    /**
     * Установить адрес получателя
     *
     * @param address адрес получателя
     * @return письмо
     */
    Mail setAddress(String address);

    /**
     * Флаг - контролировать сервером наличие копий письма.
     * При добавлении письма все старые копии стираются
     *
     * @return true - контролировать на дубликаты
     */
    boolean isCheckDublicate();

    /**
     * Получить адрес отправителя
     *
     * @return адрес отправителя
     */
    String getSender();

    /**
     * Установить адрес отправителя
     *
     * @param sender адрес отправителя
     * @return письмо
     */
    Mail setSender(String sender);

    /**
     * Флаг - при удалении подписчика сообщение с сервера не удаляется
     *
     * @return true - при удалении подписчика сообщение с сервера не удаляется
     */
    boolean isSticky();

    /**
     * Получить время окончания жизни письма
     *
     * @return время окончания жизни письма
     */
    long getEndTime();

    /**
     * Установить время окончания жизни письма. При чтении почты с сервера,
     * просроченные сообщения удаляются
     *
     * @param keepAliveTime время окончания жизни письма
     * @return письмо
     */
    Mail setEndTime(long keepAliveTime);

}
