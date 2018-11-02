package shishkin.cleanarchitecture.mvi.sl.paginator;

import java.util.ArrayList;
import java.util.List;


import shishkin.cleanarchitecture.mvi.common.utils.StringUtils;
import shishkin.cleanarchitecture.mvi.sl.MailUnion;
import shishkin.cleanarchitecture.mvi.sl.MailUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.PaginatorUnionImpl;
import shishkin.cleanarchitecture.mvi.sl.RequestSpecialist;
import shishkin.cleanarchitecture.mvi.sl.RequestSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.SL;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.mail.ResultMail;
import shishkin.cleanarchitecture.mvi.sl.request.PaginatorRequest;

public abstract class NetPaginator implements Paginator {

    private static int PREFETCH_SIZE = 20;

    private String listener;
    private int currentPageSize = 0;
    private int currentPosition = 0;
    private int requestPosition = -1;
    private List<Integer> pageSize;
    private boolean bof = false;

    public NetPaginator() {
        setPageSize(10);
    }

    @Override
    public NetPaginator setListener(String listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public NetPaginator setInitialPageSize(int initialPageSize) {
        setPageSize(initialPageSize);
        return this;
    }

    @Override
    public NetPaginator setPageSize(List<Integer> pageSize) {
        if (pageSize != null && !pageSize.isEmpty()) {
            this.pageSize = pageSize;
        }
        return this;
    }

    private void setPageSize(int initialPageSize) {
        if (initialPageSize > 0) {
            pageSize = new ArrayList<>();
            pageSize.add(initialPageSize);
            pageSize.add(initialPageSize * 2);
            pageSize.add(initialPageSize * 4);
        }
    }

    @Override
    public int getPrefetchSize() {
        if (pageSize.isEmpty()) return PREFETCH_SIZE;
        return pageSize.get(pageSize.size() - 1);
    }

    private int getNextPageSize() {
        if (pageSize.isEmpty()) return PREFETCH_SIZE;

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
                            final MailUnion union = SL.getInstance().get(MailUnionImpl.NAME);
                            if (union != null) {
                                union.addMail(new ResultMail(listener, result));
                            }
                        }
                    } else {
                        bof = true;
                    }
                } else {
                    bof = true;
                }
            } else {
                if (validate()) {
                    final MailUnion union = SL.getInstance().get(MailUnionImpl.NAME);
                    if (union != null) {
                        union.addMail(new ResultMail(listener, result));
                    }
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
    public void reset() {
        final RequestSpecialist specialist = SL.getInstance().get(RequestSpecialistImpl.NAME);
        if (specialist != null) {
            specialist.cancelRequests(this.getName());
        }
        init();
        hasData();
    }

    @Override
    public void stop() {
        final RequestSpecialist specialist = SL.getInstance().get(RequestSpecialistImpl.NAME);
        if (specialist != null) {
            specialist.cancelRequests(this.getName());
        }
        bof = true;
    }

    private void request(int currentPosition, int currentPageSize) {
        final RequestSpecialist specialist = SL.getInstance().get(RequestSpecialistImpl.NAME);
        if (specialist != null) {
            specialist.request(this, getRequest(currentPosition, currentPageSize));
        }
    }

    public abstract PaginatorRequest getRequest(int currentPosition, int currentPageSize);

    @Override
    public Result<Boolean> validateExt() {
        return new Result<>(true);
    }

    @Override
    public boolean validate() {
        return validateExt().getData();
    }

    @Override
    public List<String> getSpecialistSubscription() {
        return StringUtils.arrayToList(PaginatorUnionImpl.NAME);
    }
}
