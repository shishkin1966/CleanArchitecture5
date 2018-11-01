package shishkin.cleanarchitecture.mvi.app.screen.fragment.paging_google;

import android.support.annotation.NonNull;


import shishkin.cleanarchitecture.mvi.app.request.AccountsPaginatorRequest;
import shishkin.cleanarchitecture.mvi.sl.paged.NetPaginator;
import shishkin.cleanarchitecture.mvi.sl.request.PaginatorRequest;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

public class AccountsPaginator extends NetPaginator {

    public static final String NAME = AccountsPaginator.class.getName();

    public AccountsPaginator(@NonNull ResponseListener listener) {
        super(listener, 10);
    }

    @Override
    public PaginatorRequest getRequest(int currentPosition, int currentPageSize) {
        return new AccountsPaginatorRequest(this, currentPosition, currentPageSize);
    }

    @Override
    public String getName() {
        return NAME;
    }

}
