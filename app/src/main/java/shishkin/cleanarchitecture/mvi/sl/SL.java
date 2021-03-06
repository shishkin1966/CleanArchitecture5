package shishkin.cleanarchitecture.mvi.sl;


import shishkin.cleanarchitecture.mvi.sl.repository.DbProviderImpl;

@SuppressWarnings("unused")
public class SL extends AbsServiceLocator {

    public static final String NAME = SL.class.getName();

    private static volatile SL sInstance;
    private SpecialistFactory mModuleFactory = new ServiceLocatorSpecialistFactory();

    public static void instantiate() {
        if (sInstance == null) {
            synchronized (SL.class) {
                if (sInstance == null) {
                    sInstance = new SL();
                }
            }
        }
    }

    public static SL getInstance() {
        if (sInstance == null) {
            instantiate();
        }
        return sInstance;
    }

    private SL() {
        onStart();
    }

    @Override
    public void onStart() {
        // специалист приложения
        register(ApplicationSpecialistImpl.getInstance());

        // специалист регистрации ошибок в приложении
        register(ErrorSpecialistImpl.getInstance());

        // специалист регистрации падения приложения
        register(CrashSpecialist.NAME);

        // Почтовый специалист
        register(MessagerUnionImpl.NAME);

        // Объединение Activity
        register(ViewUnionImpl.NAME);

        // Объединение презенторов
        register(PresenterUnionImpl.NAME);

        // Объединение Observables
        register(ObservableUnionImpl.NAME);

        // специалист работы с БД
        register(DbProviderImpl.NAME);

        // специалист выполнения запросов
        register(RequestSpecialistImpl.NAME);

        // специалист преобразования данных
        register(DataSpecialistImpl.NAME);

        // специалист Memory Cache
        register(CacheSpecialistImpl.NAME);

        // специалист Preferences
        register(PreferencesSpecialistImpl.NAME);

    }

    @Override
    public SpecialistFactory getSpecialistFactory() {
        return mModuleFactory;
    }

    @Override
    public void onStop() {
        ((ViewUnion) get(ViewUnionImpl.NAME)).hideKeyboard();

        ((PresenterUnion) SL.getInstance().get(PresenterUnionImpl.NAME)).stop();
        ((ViewUnion) SL.getInstance().get(ViewUnionImpl.NAME)).stop();
    }

    @Override
    public String getName() {
        return NAME;
    }

}
