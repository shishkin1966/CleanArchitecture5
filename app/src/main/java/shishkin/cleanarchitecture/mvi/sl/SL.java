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
        // Модуль приложения
        register(ApplicationSpecialistImpl.getInstance());

        // Модуль регистрации ошибок в приложении
        register(ErrorSpecialistImpl.getInstance());

        // Модуль регистрации падения приложения
        register(CrashSpecialist.NAME);

        // Объединение Activity
        register(ActivityUnionImpl.NAME);

        // Объединение презенторов
        register(PresenterUnionImpl.NAME);

        // Объединение Observables
        register(ObservableUnionImpl.NAME);

        // UseCase модуль
        register(UseCasesSpecialistImpl.NAME);

        // Модуль работы с БД
        register(DbProviderImpl.NAME);

        // Модуль выполнения запросов
        register(RequestSpecialistImpl.NAME);

        // Модуль преобразования данных
        register(DataSpecialistImpl.NAME);
    }

    @Override
    public SpecialistFactory getSpecialistFactory() {
        return mModuleFactory;
    }

    @Override
    public void onFinish() {
        SLUtil.getActivityUnion().hideKeyboard();
        SLUtil.getPresenterUnion().onFinishApplication();
        SLUtil.getActivityUnion().onFinishApplication();
    }

    @Override
    public String getName() {
        return NAME;
    }

}
