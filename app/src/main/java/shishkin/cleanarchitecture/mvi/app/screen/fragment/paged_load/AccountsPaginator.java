package shishkin.cleanarchitecture.mvi.app.screen.fragment.paged_load;

import shishkin.cleanarchitecture.mvi.app.request.AccountsPaginatorRequest;
import shishkin.cleanarchitecture.mvi.sl.paginator.NetPaginator;
import shishkin.cleanarchitecture.mvi.sl.request.PaginatorRequest;

public class AccountsPaginator extends NetPaginator {

    public static final String NAME = AccountsPaginator.class.getName();

    @Override
    public PaginatorRequest getRequest(int currentPosition, int currentPageSize) {
        return new AccountsPaginatorRequest(this, currentPosition, currentPageSize);
    }

    @Override
    public String getName() {
        return NAME;
    }

}
