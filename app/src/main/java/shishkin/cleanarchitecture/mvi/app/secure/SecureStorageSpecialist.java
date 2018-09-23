package shishkin.cleanarchitecture.mvi.app.secure;

import android.support.annotation.NonNull;


import shishkin.cleanarchitecture.mvi.sl.Specialist;

public interface SecureStorageSpecialist extends Specialist {

    boolean deleteKeyPair();

    boolean put(@NonNull final String key, @NonNull final String data);

    String get(@NonNull final String key);

    void clear(@NonNull final String key);
}
