package shishkin.cleanarchitecture.mvi.sl;


import java.util.List;


import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;

/**
 * Абстрактный администратор
 */
@SuppressWarnings("unused")
public abstract class AbsServiceLocator implements ServiceLocator {

    private static final String NAME = AbsServiceLocator.class.getName();

    private Secretary<Specialist> secretary = new SecretaryImpl<>();

    private String getShortName(final String name) {
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
            final String specialistName = getShortName(name);
            if (secretary.get(specialistName) != null) {
                return (C) secretary.get(specialistName);
            } else {
                secretary.remove(specialistName);
            }
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
        }
        return null;
    }

    @Override
    public boolean exists(final String specialistName) {
        if (StringUtils.isNullOrEmpty(specialistName)) {
            return false;
        }

        return secretary.containsKey(getShortName(specialistName));
    }

    @Override
    public boolean register(final Specialist newSpecialist) {
        if (newSpecialist != null && !StringUtils.isNullOrEmpty(newSpecialist.getName())) {
            if (secretary.containsKey(getShortName(newSpecialist.getName()))) {
                if (newSpecialist.compareTo(get(getShortName(newSpecialist.getName()))) != 0) {
                    return false;
                }
                if (!unregister(newSpecialist.getName())) {
                    return false;
                }
            }

            try {
                // регистрируем специалиста у других специалистов
                if (SpecialistSubscriber.class.isInstance(newSpecialist)) {
                    final List<String> types = ((SpecialistSubscriber) newSpecialist).getSpecialistSubscription();
                    if (types != null) {
                        for (int i = 0; i < types.size(); i++) {
                            types.set(i, getShortName(types.get(i)));
                        }

                        for (String type : types) {
                            if (secretary.containsKey(type)) {
                                ((SmallUnion) secretary.get(type)).register(newSpecialist);
                            }
                        }
                    }
                }

                // регистрируем других специалистов у специалиста
                if (SmallUnion.class.isInstance(newSpecialist)) {
                    final String type = getShortName(newSpecialist.getName());
                    for (Specialist specialist : secretary.values()) {
                        if (SpecialistSubscriber.class.isInstance(specialist)) {
                            final List<String> types = ((SpecialistSubscriber) specialist).getSpecialistSubscription();
                            if (types != null) {
                                for (int i = 0; i < types.size(); i++) {
                                    types.set(i, getShortName(types.get(i)));
                                }

                                if (types.contains(type)) {
                                    if (!getShortName(specialist.getName()).equalsIgnoreCase(getShortName(newSpecialist.getName()))) {
                                        ((SmallUnion) newSpecialist).register(specialist);
                                    }
                                }
                            }
                        }
                    }
                }
                secretary.put(getShortName(newSpecialist.getName()), newSpecialist);
                newSpecialist.onRegister();
            } catch (Exception e) {
                ErrorSpecialistImpl.getInstance().onError(NAME, e);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean register(String name) {
        final Specialist specialist = getSpecialistFactory().create(name);
        if (specialist != null) {
            return register(specialist);
        }
        return false;
    }

    @Override
    public boolean unregister(final String name) {
        if (!StringUtils.isNullOrEmpty(name)) {
            try {
                final String specialistName = getShortName(name);
                if (secretary.containsKey(specialistName)) {
                    final Specialist specialist = secretary.get(specialistName);
                    if (specialist != null) {
                        if (!specialist.isPersistent()) {
                            // нельзя отменить подписку у объединения с подписчиками
                            if (SmallUnion.class.isInstance(specialist)) {
                                if (((SmallUnion) specialist).hasSubscribers()) {
                                    return false;
                                }
                            }

                            specialist.onUnRegister();

                            // отменяем регистрацию у других специалистов
                            if (SpecialistSubscriber.class.isInstance(specialist)) {
                                final List<String> subscribers = ((SpecialistSubscriber) specialist).getSpecialistSubscription();
                                for (String subscriber : subscribers) {
                                    final Specialist specialistSubscriber = secretary.get(getShortName(subscriber));
                                    if (specialistSubscriber != null && SmallUnion.class.isInstance(specialistSubscriber)) {
                                        ((SmallUnion) specialistSubscriber).unregister(specialist);
                                    }
                                }
                            }
                            secretary.remove(specialistName);
                        }
                    } else {
                        secretary.remove(specialistName);
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
                        if (!StringUtils.isNullOrEmpty(subscriberType)) {
                            final String specialistName = getShortName(subscriberType);
                            if (!StringUtils.isNullOrEmpty(specialistName)) {
                                if (secretary.containsKey(specialistName)) {
                                    ((SmallUnion) secretary.get(specialistName)).register(subscriber);
                                } else {
                                    register(subscriberType);
                                    if (secretary.containsKey(specialistName)) {
                                        ((SmallUnion) secretary.get(specialistName)).register(subscriber);
                                    } else {
                                        ErrorSpecialistImpl.getInstance().onError(NAME, "Not found subscriber type: " + subscriberType, false);
                                        return false;
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

                    for (Specialist specialist : secretary.values()) {
                        if (SmallUnion.class.isInstance(specialist)) {
                            final String subscriberType = getShortName(specialist.getName());
                            if (!StringUtils.isNullOrEmpty(subscriberType) && types.contains(subscriberType)) {
                                ((SmallUnion) specialist).unregister(subscriber);
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

                    for (Specialist specialist : secretary.values()) {
                        if (Union.class.isInstance(specialist)) {
                            final String specialistSubscriberType = getShortName(specialist.getName());
                            if (!StringUtils.isNullOrEmpty(specialistSubscriberType)) {
                                if (types.contains(specialistSubscriberType)) {
                                    ((Union) specialist).setCurrentSubscriber(subscriber);
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

    @Override
    public List<Specialist> getSpecialists() {
        return secretary.values();
    }

    @Override
    public String getPasport() {
        return getName();
    }
}
