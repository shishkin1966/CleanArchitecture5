package shishkin.cleanarchitecture.mvi.sl.paged;

import shishkin.cleanarchitecture.mvi.sl.request.Request;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

public interface Paginator extends ResponseListener {

    void hasData();

    void reset();

    Request getRequest(int currentPosition, int currentPageSize);

    int getPrefetchSize();

    ResponseListener getListener();

}
