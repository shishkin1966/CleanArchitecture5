package shishkin.cleanarchitecture.mvi.sl;


import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;

/**
 * Created by Shishkin on 05.03.2018.
 */

public class ServiceLocatorSpecialistFactory implements SpecialistFactory {

    private static final String NAME = ServiceLocatorSpecialistFactory.class.getName();

    public ServiceLocatorSpecialistFactory() {
    }

    public <T extends Specialist> T create(final String name) {

        if (StringUtils.isNullOrEmpty(name)) return null;

        try {
            if (name.equals(ErrorSpecialistImpl.NAME)) {
                return (T) ErrorSpecialistImpl.getInstance();
            } else if (name.equals(ApplicationSpecialistImpl.NAME)) {
                return (T) ApplicationSpecialistImpl.getInstance();
            } else {
                return (T) Class.forName(name).newInstance();
            }
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
        }
        return null;
    }
}
