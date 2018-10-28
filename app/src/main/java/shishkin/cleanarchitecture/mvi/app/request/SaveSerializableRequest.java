package shishkin.cleanarchitecture.mvi.app.request;

import java.io.Serializable;


import shishkin.cleanarchitecture.mvi.sl.SL;
import shishkin.cleanarchitecture.mvi.sl.SerializableDiskStorageSpecialist;
import shishkin.cleanarchitecture.mvi.sl.SerializableDiskStorageSpecialistImpl;
import shishkin.cleanarchitecture.mvi.sl.request.AbsRequest;

/**
 * Created by Shishkin on 06.12.2017.
 */

public class SaveSerializableRequest extends AbsRequest {

    public static final String NAME = SaveSerializableRequest.class.getName();

    private Serializable object;
    private String key;

    public SaveSerializableRequest(String key, Serializable object) {
        this.object = object;
        this.key = key;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isDistinct() {
        return false;
    }

    @Override
    public void run() {
        final SerializableDiskStorageSpecialist specialist = SL.getInstance().get(SerializableDiskStorageSpecialistImpl.NAME);
        if (specialist != null) {
            specialist.put(key, object);
        }
    }
}
