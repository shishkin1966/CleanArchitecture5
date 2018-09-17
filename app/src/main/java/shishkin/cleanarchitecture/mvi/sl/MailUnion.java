package shishkin.cleanarchitecture.mvi.sl;

import java.util.List;


import shishkin.cleanarchitecture.mvi.sl.mail.Mail;

/**
 * Интерфейс объединения, предоставляющего почтовый сервис подписчикам
 */
@SuppressWarnings("unused")
public interface MailUnion extends SmallUnion<MailSubscriber> {

    /**
     * Получить почту подписчика
     *
     * @param subscriber подписчик
     * @return the list
     */
    List<Mail> getMail(MailSubscriber subscriber);

    /**
     * Добавить почтовое сообщение
     *
     * @param mail the mail
     */
    void addMail(Mail mail);

    /**
     * Удалить почтовое сообщение
     *
     * @param mail the mail
     */
    void removeMail(Mail mail);

    /**
     * Удалить все сообщения
     */
    void clearMail();

    /**
     * Удалить сообщения подписчика
     *
     * @param subscriber подписчик
     */
    void clearMail(final MailSubscriber subscriber);

}
