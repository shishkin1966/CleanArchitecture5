package shishkin.cleanarchitecture.mvi.sl.request;

import shishkin.cleanarchitecture.mvi.sl.paged.Paginator;

public abstract class AbsPaginatorRequest<T> extends AbsResultRequest<T> implements PaginatorRequest {

    private int currentPosition;
    private int currentPageSize;

    public AbsPaginatorRequest(Paginator paginator, int currentPosition, int currentPageSize) {
        super(paginator);

        this.currentPosition = currentPosition;
        this.currentPageSize = currentPageSize;
    }

    @Override
    public boolean isDistinct() {
        return true;
    }

    @Override
    public int getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public int getCurrentPageSize() {
        return currentPageSize;
    }

}
