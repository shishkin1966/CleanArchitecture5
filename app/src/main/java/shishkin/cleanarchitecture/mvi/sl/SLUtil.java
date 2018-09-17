package shishkin.cleanarchitecture.mvi.sl;

import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


import java.util.List;


import shishkin.cleanarchitecture.mvi.common.utils.SafeUtils;
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

    public static RequestSpecialist getRequestModule() {
        return SL.getInstance().get(RequestSpecialistImpl.NAME);
    }

    public static ObservableUnion getObservableUnion() {
        return SL.getInstance().get(ObservableUnionImpl.NAME);
    }

    public static DbProvider getDbProvider() {
        return SL.getInstance().get(DbProviderImpl.NAME);
    }

    public static DataSpecialist getDataModule() {
        return SL.getInstance().get(DataSpecialistImpl.NAME);
    }

    public static <C> C getActivity() {
        final ActivityUnion union = getActivityUnion();
        if (union != null) {
            return union.getActivity();
        }
        return null;
    }

    public static <C> C getActivity(final String name) {
        final ActivityUnion union = getActivityUnion();
        if (union != null) {
            return union.getActivity(name);
        }
        return null;
    }

    public static <C> C getActivity(final String name, final boolean validate) {
        final ActivityUnion union = getActivityUnion();
        if (union != null) {
            return union.getActivity(name, validate);
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
     * Читать почту
     *
     * @param subscriber почтовый подписчик
     */
    public static void readMail(final MailSubscriber subscriber) {
        final MailSpecialist union = SL.getInstance().get(MailSpecialistImpl.NAME);
        if (union != null) {
            final List<Mail> list = union.getMail(subscriber);
            for (Mail mail : list) {
                if (subscriber.getState() == ViewStateObserver.STATE_RESUME) {
                    mail.read(subscriber);
                    union.removeMail(mail);
                }
            }
        }
    }
}
