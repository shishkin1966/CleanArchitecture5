package shishkin.cleanarchitecture.mvi.sl;

import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.usecase.DataSourceSubscriber;

public class DataSourceFactory {
    private static final String NAME = DataSourceFactory.class.getName();

    public <T extends DataSourceSubscriber> T create(final String name) {
        if (StringUtils.isNullOrEmpty(name)) return null;

        try {
            return (T) Class.forName(name).newInstance();
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
        }
        return null;
    }
}
