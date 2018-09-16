package shishkin.cleanarchitecture.mvi.sl;


import java.util.List;


import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;

/**
 * Абстрактный администратор
 */
@SuppressWarnings("unused")
public abstract class AbsServiceLocator implements ServiceLocator {

    private static final String NAME = AbsServiceLocator.class.getName();

    private Secretary<Specialist> mSecretary = new Secretary();

    public String getShortName(final String name) {
        return StringUtils.last(name, "\\.");
    }

    @Override
    public <C> C get(final String name) {
        if (!exists(name)) {
            if (!register(name)) {
                return null;
            }
        }

        try {
            final String moduleName = getShortName(name);
            if (mSecretary.get(moduleName) != null) {
                return (C) mSecretary.get(moduleName);
            } else {
                mSecretary.remove(moduleName);
            }
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
        }
        return null;
    }

    @Override
    public boolean exists(final String moduleName) {
        if (StringUtils.isNullOrEmpty(moduleName)) {
            return false;
        }

        return mSecretary.containsKey(getShortName(moduleName));
    }

    @Override
    public boolean register(final Specialist newModule) {
        if (newModule != null && !StringUtils.isNullOrEmpty(newModule.getName())) {
            if (mSecretary.containsKey(getShortName(newModule.getName()))) {
                if (newModule.compareTo(get(getShortName(newModule.getName()))) != 0) {
                    return false;
                }
                if (!unregister(newModule.getName())) {
                    return false;
                }
            }

            try {
                // регистрируем специалиста у других специалистов
                if (SpecialistSubscriber.class.isInstance(newModule)) {
                    final List<String> types = ((SpecialistSubscriber) newModule).getSpecialistSubscription();
                    if (types != null) {
                        for (int i = 0; i < types.size(); i++) {
                            types.set(i, getShortName(types.get(i)));
                        }

                        for (String type : types) {
                            if (mSecretary.containsKey(type)) {
                                ((SmallUnion) mSecretary.get(type)).register(newModule);
                            }
                        }
                    }
                }

                // регистрируем других специалистов у специалиста
                if (SmallUnion.class.isInstance(newModule)) {
                    final String type = getShortName(newModule.getName());
                    for (Specialist module : mSecretary.values()) {
                        if (SpecialistSubscriber.class.isInstance(module)) {
                            final List<String> types = ((SpecialistSubscriber) module).getSpecialistSubscription();
                            if (types != null) {
                                for (int i = 0; i < types.size(); i++) {
                                    types.set(i, getShortName(types.get(i)));
                                }

                                if (types.contains(type)) {
                                    if (!getShortName(module.getName()).equalsIgnoreCase(getShortName(newModule.getName()))) {
                                        ((SmallUnion) newModule).register(module);
                                    }
                                }
                            }
                        }
                    }
                }
                mSecretary.put(getShortName(newModule.getName()), newModule);
                newModule.onRegister();
            } catch (Exception e) {
                ErrorSpecialistImpl.getInstance().onError(NAME, e);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean register(String name) {
        final Specialist module = getSpecialistFactory().create(name);
        if (module != null) {
            return register(module);
        }
        return false;
    }

    @Override
    public boolean unregister(final String name) {
        if (!StringUtils.isNullOrEmpty(name)) {
            try {
                final String moduleName = getShortName(name);
                if (mSecretary.containsKey(moduleName)) {
                    final Specialist module = mSecretary.get(moduleName);
                    if (module != null) {
                        if (!module.isPersistent()) {
                            // нельзя отменить подписку у объединения с подписчиками
                            if (SmallUnion.class.isInstance(module)) {
                                if (((SmallUnion) module).hasSubscribers()) {
                                    return false;
                                }
                            }

                            module.onUnRegister();

                            // отменяем регистрацию у других специалистов
                            if (SpecialistSubscriber.class.isInstance(module)) {
                                final List<String> subscribers = ((SpecialistSubscriber) module).getSpecialistSubscription();
                                for (String subscriber : subscribers) {
                                    final Specialist moduleSubscriber = mSecretary.get(getShortName(subscriber));
                                    if (moduleSubscriber != null && SmallUnion.class.isInstance(moduleSubscriber)) {
                                        ((SmallUnion) moduleSubscriber).unregister(module);
                                    }
                                }
                            }
                            mSecretary.remove(moduleName);
                        }
                    } else {
                        mSecretary.remove(moduleName);
                    }
                }
            } catch (Exception e) {
                ErrorSpecialistImpl.getInstance().onError(NAME, e);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean register(final SpecialistSubscriber subscriber) {
        if (subscriber != null && !StringUtils.isNullOrEmpty(subscriber.getName())) {
            try {
                final List<String> types = subscriber.getSpecialistSubscription();
                if (types != null) {
                    // регистрируемся subscriber у специалистов
                    for (String subscriberType : types) {
                        final String moduleName = getShortName(subscriberType);
                        if (mSecretary.containsKey(moduleName)) {
                            ((SmallUnion) mSecretary.get(moduleName)).register(subscriber);
                        } else {
                            register(subscriberType);
                            if (mSecretary.containsKey(moduleName)) {
                                ((SmallUnion) mSecretary.get(moduleName)).register(subscriber);
                            } else {
                                ErrorSpecialistImpl.getInstance().onError(NAME, "Not found subscriber type: " + subscriberType, false);
                                return false;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                ErrorSpecialistImpl.getInstance().onError(NAME, e);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean unregister(final SpecialistSubscriber subscriber) {
        if (subscriber != null) {
            try {
                final List<String> types = subscriber.getSpecialistSubscription();
                if (types != null) {
                    for (int i = 0; i < types.size(); i++) {
                        types.set(i, getShortName(types.get(i)));
                    }

                    for (Specialist module : mSecretary.values()) {
                        if (SmallUnion.class.isInstance(module)) {
                            final String subscriberType = getShortName(module.getName());
                            if (!StringUtils.isNullOrEmpty(subscriberType) && types.contains(subscriberType)) {
                                ((SmallUnion) module).unregister(subscriber);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                ErrorSpecialistImpl.getInstance().onError(NAME, e);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean setCurrentSubscriber(final SpecialistSubscriber subscriber) {
        try {
            if (subscriber != null) {
                final List<String> types = subscriber.getSpecialistSubscription();
                if (types != null) {
                    for (int i = 0; i < types.size(); i++) {
                        types.set(i, getShortName(types.get(i)));
                    }

                    for (Specialist module : mSecretary.values()) {
                        if (Union.class.isInstance(module)) {
                            final String moduleSubscriberType = getShortName(module.getName());
                            if (!StringUtils.isNullOrEmpty(moduleSubscriberType)) {
                                if (types.contains(moduleSubscriberType)) {
                                    ((Union) module).setCurrentSubscriber(subscriber);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
            return false;
        }
        return true;
    }
}
