package shishkin.cleanarchitecture.mvi.app.media;

import shishkin.cleanarchitecture.mvi.sl.Specialist;

public interface MediaSpecialist extends Specialist {

    void play(int resId);

    int pause();

    void resume(int position);

    void resume();

    void stop();

    boolean isStop();

}
