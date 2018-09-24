package shishkin.cleanarchitecture.mvi.sl.repository;

import okhttp3.OkHttpClient;

/**
 * Created by Shishkin on 22.12.2017.
 */

public interface NetProvider<T> extends Provider {

    OkHttpClient getOkHttpClient();

    Class<T> getApiClass();

    String getBaseUrl();
}
