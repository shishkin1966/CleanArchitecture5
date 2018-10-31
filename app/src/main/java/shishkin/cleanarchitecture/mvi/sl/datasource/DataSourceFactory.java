package shishkin.cleanarchitecture.mvi.sl.datasource;

import android.arch.paging.DataSource;


import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.ErrorSpecialistImpl;

public class DataSourceFactory {
    private static final String NAME = DataSourceFactory.class.getName();

    public static <T extends DataSource> T create(final String name) {
        if (StringUtils.isNullOrEmpty(name)) return null;

        try {
            return (T) Class.forName(name).newInstance();
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(NAME, e);
        }
        return null;
    }
}
