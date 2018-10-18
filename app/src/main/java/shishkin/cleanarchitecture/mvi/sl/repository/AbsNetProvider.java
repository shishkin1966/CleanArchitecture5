package shishkin.cleanarchitecture.mvi.sl.repository;

import android.content.Context;
import android.support.annotation.NonNull;


import java.util.concurrent.TimeUnit;


import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import shishkin.cleanarchitecture.mvi.sl.ApplicationSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.RequestSpecialist;
import shishkin.cleanarchitecture.mvi.sl.RequestSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.SL;
import shishkin.cleanarchitecture.mvi.sl.data.Result;
import shishkin.cleanarchitecture.mvi.sl.request.Request;

public abstract class AbsNetProvider<T> extends AbsProvider implements NetProvider<T> {
    private static long CONNECT_TIMEOUT = 30; // 30 sec
    private static long READ_TIMEOUT = 10; // 10 min
    private Retrofit mRetrofit;
    private T mApi;

    public AbsNetProvider() {
        final Context context = ApplicationSpecialistImpl.getInstance();
        if (context == null) {
            return;
        }

        mRetrofit = new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .client(getOkHttpClient())
                .addConverterFactory(getConverterFactory())
                .build();
        mApi = mRetrofit.create(getApiClass());
    }

    public Converter.Factory getConverterFactory() {
        return GsonConverterFactory.create();
    }

    public T getApi() {
        return mApi;
    }

    public abstract Class<T> getApiClass();

    public abstract String getBaseUrl();

    public OkHttpClient getOkHttpClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.MINUTES)
                .retryOnConnectionFailure(false);
        return builder.build();
    }

    @Override
    public void request(final Request request) {
        if (request != null && validate()) {
            ((RequestSpecialist) SL.getInstance().get(RequestSpecialistImpl.NAME)).request(this, request);
        }
    }

    @Override
    public Result<Boolean> validateExt() {
        return new Result<>(mRetrofit != null && mApi != null);
    }

    @Override
    public boolean isPersistent() {
        return false;
    }

    @Override
    public void onUnRegister() {
    }

    @Override
    public void onRegister() {
    }

    public void cancelRequests(String listener) {
        ((RequestSpecialist) SL.getInstance().get(RequestSpecialistImpl.NAME)).cancelRequests(this, listener);
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (NetProvider.class.isInstance(o)) ? 0 : 1;
    }

}


