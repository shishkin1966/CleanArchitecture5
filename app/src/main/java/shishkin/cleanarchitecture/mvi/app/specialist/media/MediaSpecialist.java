package shishkin.cleanarchitecture.mvi.app.specialist.media;

import shishkin.cleanarchitecture.mvi.sl.Specialist;

public interface MediaSpecialist extends Specialist {

    void playMedia(int resId);

    int pauseMedia();

    void resumeMedia(int position);

    void resumeMedia();

    void stopMedia();

    boolean isStopMedia();

    void releaseMedia();

}
