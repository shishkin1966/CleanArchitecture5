package shishkin.cleanarchitecture.mvi.sl.request;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shishkin.cleanarchitecture.mvi.sl.ErrorSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.Subscriber;
import shishkin.cleanarchitecture.mvi.sl.data.Result;

/**
 * Created by Shishkin on 15.11.2017.
 */

public class BaseCallback<T> implements Callback<T> {

    private Subscriber mSubscriber;
    private Result mResult;

    public BaseCallback(Subscriber subscriber) {
        mSubscriber = subscriber;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        proceedResponse(response, mSubscriber);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        proceedError(t, mSubscriber);
    }

    public void proceedResponse(final Response response, final Subscriber request) {
        if (response.isSuccessful()) {
            mResult = new Result<>().setData(response.body()).setOrder(Result.LAST).setName(request.getName());
        } else {
            try {
                final String error = response.errorBody().string();
                mResult = new Result<>().setError(request.getName(), error).setOrder(Result.LAST).setName(request.getName());
            } catch (Exception e) {
                mResult = new Result<>().setError(request.getName(), e.getLocalizedMessage()).setOrder(Result.LAST).setName(request.getName());
                ErrorSpecialistImpl.getInstance().onError(request.getName(), e);
            }
        }
    }

    public void proceedError(final Throwable t, final Subscriber request) {
        if (t != null && t.getMessage() != null) {
            mResult = new Result<>().setError(request.getName(), t.getLocalizedMessage()).setOrder(Result.LAST).setName(request.getName());
        }
    }

    public Result<T> getResult() {
        return (Result<T>) mResult;
    }
}
