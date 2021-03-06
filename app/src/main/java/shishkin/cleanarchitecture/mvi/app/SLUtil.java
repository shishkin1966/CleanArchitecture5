package shishkin.cleanarchitecture.mvi.app;

import android.content.Context;


import androidx.room.RoomDatabase;
import shishkin.cleanarchitecture.mvi.app.net.NetCbProviderImpl;
import shishkin.cleanarchitecture.mvi.app.net.NetProviderImpl;
import shishkin.cleanarchitecture.mvi.app.specialist.calculation.CalculationUnion;
import shishkin.cleanarchitecture.mvi.app.specialist.calculation.CalculationUnionImpl;
import shishkin.cleanarchitecture.mvi.app.specialist.idle.IdleSpecialist;
import shishkin.cleanarchitecture.mvi.app.specialist.idle.IdleSpecialistImpl;
import shishkin.cleanarchitecture.mvi.app.specialist.job.JobSpecialist;
import shishkin.cleanarchitecture.mvi.app.specialist.job.JobSpecialistImpl;
import shishkin.cleanarchitecture.mvi.app.specialist.location.LocationUnion;
import shishkin.cleanarchitecture.mvi.app.specialist.location.LocationUnionImpl;
import shishkin.cleanarchitecture.mvi.app.specialist.media.MediaSpecialist;
import shishkin.cleanarchitecture.mvi.app.specialist.media.MediaSpecialistImpl;
import shishkin.cleanarchitecture.mvi.app.specialist.notification.NotificationSpecialist;
import shishkin.cleanarchitecture.mvi.app.specialist.notification.NotificationSpecialistImpl;
import shishkin.cleanarchitecture.mvi.app.specialist.scanner.ScannerUnion;
import shishkin.cleanarchitecture.mvi.app.specialist.scanner.ScannerUnionImpl;
import shishkin.cleanarchitecture.mvi.common.utils.SafeUtils;
import shishkin.cleanarchitecture.mvi.sl.ApplicationSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.CacheSpecialist;
import shishkin.cleanarchitecture.mvi.sl.CacheSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.DataSpecialist;
import shishkin.cleanarchitecture.mvi.sl.DataSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.ErrorSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.MessagerUnion;
import shishkin.cleanarchitecture.mvi.sl.MessagerUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.ObservableUnion;
import shishkin.cleanarchitecture.mvi.sl.ObservableUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.PaginatorUnion;
import shishkin.cleanarchitecture.mvi.sl.PaginatorUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.PreferencesSpecialist;
import shishkin.cleanarchitecture.mvi.sl.PreferencesSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.PresenterUnion;
import shishkin.cleanarchitecture.mvi.sl.PresenterUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.RequestSpecialist;
import shishkin.cleanarchitecture.mvi.sl.RequestSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.SL;
import shishkin.cleanarchitecture.mvi.sl.SecureStorageSpecialist;
import shishkin.cleanarchitecture.mvi.sl.SecureStorageSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.SerializableDiskStorageSpecialist;
import shishkin.cleanarchitecture.mvi.sl.SerializableDiskStorageSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.SpecialistSubscriber;
import shishkin.cleanarchitecture.mvi.sl.Subscriber;
import shishkin.cleanarchitecture.mvi.sl.UseCasesSpecialist;
import shishkin.cleanarchitecture.mvi.sl.UseCasesSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.ViewUnion;
import shishkin.cleanarchitecture.mvi.sl.ViewUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.mail.Mail;
import shishkin.cleanarchitecture.mvi.sl.repository.DbProvider;
import shishkin.cleanarchitecture.mvi.sl.repository.DbProviderImpl;
import shishkin.cleanarchitecture.mvi.sl.state.ViewStateObserver;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsActivity;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsContentActivity;
import shishkin.cleanarchitecture.mvi.sl.ui.AbsContentFragment;

/**
 * Инструменты администратора
 */
@SuppressWarnings("unused")
public class SLUtil {

    public static Context getContext() {
        return ApplicationSpecialistImpl.getInstance();
    }

    public static ViewUnion getViewUnion() {
        return SL.getInstance().get(ViewUnionImpl.NAME);
    }

    public static PresenterUnion getPresenterUnion() {
        return SL.getInstance().get(PresenterUnionImpl.NAME);
    }

    public static UseCasesSpecialist getUseCasesSpecialist() {
        return SL.getInstance().get(UseCasesSpecialistImpl.NAME);
    }

    public static RequestSpecialist getRequestSpecialist() {
        return SL.getInstance().get(RequestSpecialistImpl.NAME);
    }

    public static ObservableUnion getObservableUnion() {
        return SL.getInstance().get(ObservableUnionImpl.NAME);
    }

    public static DbProvider getDbProvider() {
        return SL.getInstance().get(DbProviderImpl.NAME);
    }

    public static NetProviderImpl getNetProvider() {
        return SL.getInstance().get(NetProviderImpl.NAME);
    }

    public static NetCbProviderImpl getNetCbProvider() {
        return SL.getInstance().get(NetCbProviderImpl.NAME);
    }

    public static DataSpecialist getDataSpecialist() {
        return SL.getInstance().get(DataSpecialistImpl.NAME);
    }

    public static MessagerUnion getMessagerUnion() {
        return SL.getInstance().get(MessagerUnionImpl.NAME);
    }

    public static CacheSpecialist getCacheSpecialist() {
        return SL.getInstance().get(CacheSpecialistImpl.NAME);
    }

    public static SecureStorageSpecialist getSecureStorageSpecialist() {
        return SL.getInstance().get(SecureStorageSpecialistImpl.NAME);
    }

    public static NotificationSpecialist getNotificationSpecialist() {
        return SL.getInstance().get(NotificationSpecialistImpl.NAME);
    }

    public static PreferencesSpecialist getPreferencesSpecialist() {
        return SL.getInstance().get(PreferencesSpecialistImpl.NAME);
    }

    public static LocationUnion getLocationUnion() {
        return SL.getInstance().get(LocationUnionImpl.NAME);
    }

    public static JobSpecialist getJobSpecialist() {
        return SL.getInstance().get(JobSpecialistImpl.NAME);
    }

    public static MediaSpecialist getMediaSpecialist() {
        return SL.getInstance().get(MediaSpecialistImpl.NAME);
    }

    public static ScannerUnion getScannerUnion() {
        return SL.getInstance().get(ScannerUnionImpl.NAME);
    }

    public static IdleSpecialist getIdleSpecialist() {
        return SL.getInstance().get(IdleSpecialistImpl.NAME);
    }

    public static CalculationUnion getCalculationUnion() {
        return SL.getInstance().get(CalculationUnionImpl.NAME);
    }

    public static SerializableDiskStorageSpecialist getSerializableDiskStorageSpecialist() {
        return SL.getInstance().get(SerializableDiskStorageSpecialistImpl.NAME);
    }

    public static PaginatorUnion getPaginatorUnion() {
        return SL.getInstance().get(PaginatorUnionImpl.NAME);
    }

    public static <C> C getActivity() {
        final ViewUnion union = getViewUnion();
        if (union != null) {
            return union.getActivity();
        }
        return null;
    }

    /**
     * Зарегистрировать подписчика специалиста
     *
     * @param subscriber подписчик специалиста
     */
    public static void register(final SpecialistSubscriber subscriber) {
        SL.getInstance().register(subscriber);
    }

    /**
     * Зарегистрировать специалиста
     *
     * @param specialist специалист
     */
    public static void register(final String specialist) {
        SL.getInstance().register(specialist);
    }

    /**
     * Отменить регистрацию специалиста
     *
     * @param specialist специалист
     */
    public static void unregister(final String specialist) {
        SL.getInstance().unregister(specialist);
    }

    /**
     * Отменить регистрацию подписчика специалиста
     *
     * @param subscriber подписчик специалиста
     */
    public static void unregister(final SpecialistSubscriber subscriber) {
        SL.getInstance().unregister(subscriber);
    }

    /**
     * Сделать подписчика текущим
     *
     * @param subscriber подписчик специалиста
     */
    public static void setCurrentSubscriber(final SpecialistSubscriber subscriber) {
        SL.getInstance().setCurrentSubscriber(subscriber);
    }

    /**
     * Получить content fragment.
     *
     * @return the content fragment
     */
    public static AbsContentFragment getContentFragment() {
        final ViewUnion union = getViewUnion();
        if (union != null) {
            final AbsActivity activity = union.getActivity();
            if (activity != null && AbsContentActivity.class.isInstance(activity)) {
                return SafeUtils.cast(((AbsContentActivity) activity).getContentFragment(AbsContentFragment.class));
            }
        }
        return null;
    }

    /**
     * Проверить существование валидной activity.
     *
     * @return true - существует
     */
    public static boolean isValidActivity() {
        final ViewUnion union = getViewUnion();
        if (union != null) {
            final AbsActivity activity = union.getActivity();
            if (activity != null && activity.validate() && (activity.getState() == ViewStateObserver.STATE_RESUME || activity.getState() == ViewStateObserver.STATE_PAUSE)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Получить БД
     *
     * @return the db
     */
    public static <T extends RoomDatabase> T getDb() {
        final DbProvider provider = getDbProvider();
        if (provider != null) {
            return (T) provider.getDb();
        }
        return null;
    }

    /**
     * Добавить почтовое сообщение
     *
     * @param mail почтовое сообщение
     */
    public static void addMail(final Mail mail) {
        final MessagerUnion union = getMessagerUnion();
        if (union != null) {
            union.addMail(mail);
        }
    }

    /**
     * Добавить не обязательное почтовое сообщение
     *
     * @param mail почтовое сообщение
     */
    public static void addNotMandatoryMail(final Mail mail) {
        final MessagerUnion union = getMessagerUnion();
        if (union != null) {
            union.addNotMandatoryMail(mail);
        }
    }

    /**
     * Заменить почтовое сообщение
     *
     * @param mail почтовое сообщение
     */
    public static void replaceMail(final Mail mail) {
        final MessagerUnion union = getMessagerUnion();
        if (union != null) {
            union.replaceMail(mail);
        }
    }

    /**
     * Событие - ошибка
     *
     * @param object источник ошибки
     * @param e      Exception
     */
    public static void onError(Object object, Exception e) {
        if (object instanceof Subscriber) {
            ErrorSpecialistImpl.getInstance().onError(((Subscriber) object).getName(), e);
        } else {
            ErrorSpecialistImpl.getInstance().onError(object.getClass().getName(), e);
        }
    }

    /**
     * Событие - ошибка
     *
     * @param source источник ошибки
     * @param e      Exception
     */
    public static void onError(String source, Exception e) {
        ErrorSpecialistImpl.getInstance().onError(source, e);
    }

    /**
     * Событие - ошибка
     *
     * @param source    источник ошибки
     * @param message   текст ошибки пользователю
     * @param isDisplay true - отображать на сообщение на дисплее, false - сохранять в журнале
     */
    public static void onError(final String source, final String message, final boolean isDisplay) {
        ErrorSpecialistImpl.getInstance().onError(source, message, isDisplay);
    }
}
