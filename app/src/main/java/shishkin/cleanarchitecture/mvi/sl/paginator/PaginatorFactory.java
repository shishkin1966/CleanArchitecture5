package shishkin.cleanarchitecture.mvi.sl.paginator;

import shishkin.cleanarchitecture.mvi.sl.ErrorSpecialistImpl;

public class PaginatorFactory {

    public Paginator create(final String name) {
        try {
            return (Paginator) Class.forName(name).newInstance();
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(this.getClass().getName(), e);
        }
        return null;
    }
}
