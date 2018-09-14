package shishkin.cleanarchitecture.mvi.sl.data;

import com.google.gson.Gson;


import shishkin.cleanarchitecture.mvi.sl.ErrorSpecialistImpl;

/**
 * Created by Shishkin on 21.12.2017.
 */

@SuppressWarnings("unused")
public abstract class AbsEntity {

    public String toString() {
        return toJson();
    }

    public String toJson() {
        if (this == null) return null;

        return new Gson().toJson(this);
    }

    public static <T> T fromJson(String json, Class<T> klass) {
        if (json == null) return null;

        try {
            return new Gson().fromJson(json, klass);
        } catch (Exception e) {
            ErrorSpecialistImpl.getInstance().onError(klass.getSimpleName(), e);
        }
        return null;
    }
}
