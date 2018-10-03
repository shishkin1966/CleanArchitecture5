package shishkin.cleanarchitecture.mvi.app.media;

import shishkin.cleanarchitecture.mvi.sl.Specialist;

public interface MediaSpecialist extends Specialist {

    void play(int resId);

    void pause();

    void resume(int lenght);

    void stop();

}
