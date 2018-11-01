package shishkin.cleanarchitecture.mvi.sl.paged;

import android.support.annotation.NonNull;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import shishkin.cleanarchitecture.mvi.sl.RequestSpecialist;
import shishkin.cleanarchitecture.mvi.sl.RequestSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.SL;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.request.PaginatorRequest;
import shishkin.cleanarchitecture.mvi.sl.request.ResponseListener;

public abstract class NetPaginator implements Paginator {

    private static int PREFETCH_SIZE = 50;

    private WeakReference<ResponseListener> listener;
    private int currentPageSize = 0;
    private int currentPosition = 0;
    private int requestPosition = -1;
    private List<Integer> pageSize = Arrays.asList(new Integer[]{5, 10, 20, 50});
    private boolean bof = false;

    public NetPaginator(@NonNull ResponseListener listener) {
        this.listener = new WeakReference<>(listener);
    }

    public NetPaginator(@NonNull ResponseListener listener, int minPageSize) {
        this.listener = new WeakReference<>(listener);

        if (minPageSize > 0) {
            pageSize = new ArrayList<>();
            pageSize.add(minPageSize);
            pageSize.add(minPageSize * 2);
            pageSize.add(minPageSize * 4);
            pageSize.add(minPageSize * 10);
        }
    }

    public int getPrefetchSize() {
        if (pageSize.isEmpty()) {
            return PREFETCH_SIZE;
        }
        return pageSize.get(pageSize.size() - 1);
    }

    private int getNextPageSize() {
        for (int i = 0; i < pageSize.size(); i++) {
            if (pageSize.get(i) > currentPageSize) {
                return pageSize.get(i);
            }
        }
        return pageSize.get(pageSize.size() - 1);
    }

    @Override
    public void hasData() {
        if (!bof) {
            if (currentPosition > requestPosition) {
                requestPosition = currentPosition;
                currentPageSize = getNextPageSize();
                request(currentPosition, currentPageSize);
            }
        }
    }

    @Override
    public void response(Result result) {
        if (!bof) {
            if (!result.hasError()) {
                final List list = (List) result.getData();
                if (list != null) {
                    if (!list.isEmpty()) {
                        currentPosition += list.size();
                        if (validate()) {
                            listener.get().response(result);
                        }
                    } else {
                        bof = true;
                    }
                } else {
                    bof = true;
                }
            } else {
                if (validate()) {
                    listener.get().response(result);
                }
            }
        }
    }

    private void init() {
        currentPosition = 0;
        currentPageSize = 0;
        requestPosition = -1;
        bof = false;
    }

    @Override
    public void clear() {
        final RequestSpecialist specialist = SL.getInstance().get(RequestSpecialistImpl.NAME);
        if (specialist != null) {
            specialist.cancelRequests(this.getName());
        }
        init();
        hasData();
    }

    private void request(int currentPosition, int currentPageSize) {
        final RequestSpecialist specialist = SL.getInstance().get(RequestSpecialistImpl.NAME);
        if (specialist != null) {
            specialist.request(this, getRequest(currentPosition, currentPageSize));
        }
    }

    public abstract PaginatorRequest getRequest(int currentPosition, int currentPageSize);

    @Override
    public ResponseListener getListener() {
        if (listener != null && listener.get() != null) {
            return listener.get();
        }
        return null;
    }

    @Override
    public Result<Boolean> validateExt() {
        if (listener != null && listener.get() != null) {
            return listener.get().validateExt();
        } else {
            return new Result<>(false);
        }
    }

    @Override
    public boolean validate() {
        return validateExt().getData();
    }
}
