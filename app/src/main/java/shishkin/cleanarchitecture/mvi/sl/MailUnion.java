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
     * Заменить почтовое сообщение
     *
     * @param mail the mail
     */
    void replaceMail(Mail mail);

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

    /**
     * Читать почту
     *
     * @param subscriber почтовый подписчик
     */
    void readMail(final MailSubscriber subscriber);

    /**
     * Добавить список рассылки
     *
     * @param name      имя списка рассылки
     * @param addresses список рассылки
     */
    void addMailingList(String name, List<String> addresses);

    /**
     * Добавить список рассылки
     *
     * @param name      имя списка рассылки
     * @param addresses список рассылки
     */
    void addMailingList(String name, String[] addresses);

    /**
     * Удалить список рассылки
     *
     * @param name имя списка рассылки
     */
    void removeMailingList(String name);

    /**
     * Получить список рассылки
     *
     * @return список рассылки
     */
    List<String> getMailingList(String name);
}
