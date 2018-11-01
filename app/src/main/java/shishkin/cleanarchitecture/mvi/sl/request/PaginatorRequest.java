package shishkin.cleanarchitecture.mvi.sl.request;

public interface PaginatorRequest extends Request {
    int getCurrentPosition();

    int getCurrentPageSize();
}
