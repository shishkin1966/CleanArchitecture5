package shishkin.cleanarchitecture.mvi.app;

import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


import shishkin.cleanarchitecture.mvi.app.storage.StorageSpecialist;
import shishkin.cleanarchitecture.mvi.app.storage.StorageSpecialistImpl;
import shishkin.cleanarchitecture.mvi.common.utils.SafeUtils;
import shishkin.cleanarchitecture.mvi.sl.ActivityUnion;
import shishkin.cleanarchitecture.mvi.sl.ActivityUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.ApplicationSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.DataSpecialist;
import shishkin.cleanarchitecture.mvi.sl.DataSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.MailUnion;
import shishkin.cleanarchitecture.mvi.sl.MailUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.ObservableUnion;
import shishkin.cleanarchitecture.mvi.sl.ObservableUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.PresenterUnion;
import shishkin.cleanarchitecture.mvi.sl.PresenterUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.RequestSpecialist;
import shishkin.cleanarchitecture.mvi.sl.RequestSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.SL;
import shishkin.cleanarchitecture.mvi.sl.SpecialistSubscriber;
import shishkin.cleanarchitecture.mvi.sl.UseCasesSpecialist;
import shishkin.cleanarchitecture.mvi.sl.UseCasesSpecialistImpl;
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

    public static ActivityUnion getActivityUnion() {
        return SL.getInstance().get(ActivityUnionImpl.NAME);
    }

    public static PresenterUnion getPresenterUnion() {
        return SL.getInstance().get(PresenterUnionImpl.NAME);
    }

    public static UseCasesSpecialist getUseCasesUnion() {
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

    public static DataSpecialist getDataSpecialist() {
        return SL.getInstance().get(DataSpecialistImpl.NAME);
    }

    public static MailUnion getMailUnion() {
        return SL.getInstance().get(MailUnionImpl.NAME);
    }

    public static StorageSpecialist getStorageSpecialist() {
        return SL.getInstance().get(StorageSpecialistImpl.NAME);
    }

    public static <C> C getActivity() {
        final ActivityUnion union = getActivityUnion();
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
        final ActivityUnion union = getActivityUnion();
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
        final ActivityUnion union = getActivityUnion();
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
        final MailUnion union = getMailUnion();
        if (union != null) {
            union.addMail(mail);
        }
    }

}