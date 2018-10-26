package shishkin.cleanarchitecture.mvi.sl;

import android.support.annotation.NonNull;


import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.delegate.RequestDelegate;
import shishkin.cleanarchitecture.mvi.sl.request.AbsRequest;
import shishkin.cleanarchitecture.mvi.sl.request.Request;
import shishkin.cleanarchitecture.mvi.sl.task.AwaitTask;
import shishkin.cleanarchitecture.mvi.sl.task.IExecutor;
import shishkin.cleanarchitecture.mvi.sl.task.RequestExecutor;
import shishkin.cleanarchitecture.mvi.sl.task.RequestThreadPoolExecutor;

/**
 * Интерфейс специалиста выполнения запросов
 */
public class RequestSpecialistImpl extends AbsSpecialist implements RequestSpecialist {

    public static final String NAME = RequestSpecialistImpl.class.getName();

    private RequestThreadPoolExecutor mSequentiallyThreadPoolExecutor;
    private static long KEEP_ALIVE_TIME = 10; // 10 мин
    private RequestDelegate mRequestDelegate = new RequestDelegate();

    public RequestSpecialistImpl() {
        final BlockingQueue serQueue = new LinkedBlockingQueue<AbsRequest>();
        mSequentiallyThreadPoolExecutor = new RequestThreadPoolExecutor(1, 1, KEEP_ALIVE_TIME, TimeUnit.MINUTES, serQueue);
    }

    @Override
    public void request(final Object sender, final Request request) {
        if (request != null && validate()) {
            mRequestDelegate.processing(sender, request);
        }
    }

    @Override
    public void requestSequentially(final Object sender, final Request request) {
        if (request != null && validate()) {
            mSequentiallyThreadPoolExecutor.addRequest(request);
        }
    }

    @Override
    public void onUnRegister() {
        for (IExecutor executor : mRequestDelegate.getAll()) {
            executor.clear();
            executor.shutdown();
        }
        mSequentiallyThreadPoolExecutor.clear();
        mSequentiallyThreadPoolExecutor.shutdown();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Result<Boolean> validateExt() {
        return new Result<>(super.validateExt().getData() && mRequestDelegate != null
                && mSequentiallyThreadPoolExecutor != null);
    }

    @Override
    public void cancelRequests(String listener) {
        List<RequestExecutor> list = mRequestDelegate.getAll();
        for (RequestExecutor executor : list) {
            executor.cancelRequests(listener);
        }
        mSequentiallyThreadPoolExecutor.cancelRequests(listener);
    }

    @Override
    public Result await(AwaitTask task) {
        try {
            return task.execute().get();
        } catch (Exception e) {
            final ErrorSpecialist specialist = SL.getInstance().get(ErrorSpecialistImpl.NAME);
            if (specialist != null) {
                specialist.onError(getName(), e);
            }
            return new Result().setError(getName(), e);
        }
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (RequestSpecialist.class.isInstance(o)) ? 0 : 1;
    }
}
