package shishkin.cleanarchitecture.mvi.sl.request;

import bolts.Continuation;
import bolts.Task;
import bolts.TaskCompletionSource;
import retrofit2.Call;
import retrofit2.Response;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.common.net.Connectivity;
import shishkin.cleanarchitecture.mvi.sl.ApplicationSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.Subscriber;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.mail.ResultMail;

/**
 * Created by Shishkin on 04.12.2017.
 */

public abstract class AbsNetResultMailRequest<T> extends AbsResultMailRequest implements ResultMailRequest {

    public AbsNetResultMailRequest(final String listener) {
        super(listener);
    }

    @Override
    public void run() {
        if (!validate()) return;

        getFromNet().continueWith((Continuation<Result<T>, Void>) task -> {
            if (validate() && task.getResult() != null) {
                SLUtil.getMailUnion().addMail(new ResultMail(getOwnerName(), task.getResult().setOrder(Result.LAST).setName(getName())).setName(getName()).setCopyTo(getCopyTo()));
            }
            return null;
        }, Task.UI_THREAD_EXECUTOR);
    }

    public abstract Call<T> getData();

    public Task<Result<T>> getFromNet() {
        final TaskCompletionSource<Result<T>> newTask = new TaskCompletionSource<>();

        if (validate()) {
            if (Connectivity.isNetworkConnected(ApplicationSpecialistImpl.getInstance())) {
                getData().enqueue(new BaseCallback<T>(this) {
                    @Override
                    public void proceedResponse(final Response response, final Subscriber request) {
                        super.proceedResponse(response, request);

                        newTask.setResult(getResult());
                    }

                    @Override
                    public void proceedError(final Throwable t, final Subscriber request) {
                        super.proceedError(t, request);

                        newTask.setResult(getResult());
                    }
                });
            } else {
                newTask.setResult(new Result<>());
            }
        } else {
            newTask.setResult(new Result<>());
        }
        return newTask.getTask();
    }
}
