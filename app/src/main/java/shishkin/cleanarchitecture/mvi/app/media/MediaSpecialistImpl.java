package shishkin.cleanarchitecture.mvi.app.media;

import android.media.MediaPlayer;
import android.support.annotation.NonNull;


import shishkin.cleanarchitecture.mvi.app.ApplicationController;
import shishkin.cleanarchitecture.mvi.sl.AbsSpecialist;

public class MediaSpecialistImpl extends AbsSpecialist implements MediaSpecialist {
    public static final String NAME = MediaSpecialistImpl.class.getName();
    private MediaPlayer player;

    @Override
    public void onUnRegister() {
        stop();
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return (MediaSpecialist.class.isInstance(o)) ? 0 : 1;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void play(int resId) {
        player = MediaPlayer.create(ApplicationController.getInstance(), resId);
        player.start();
    }

    @Override
    public int pause() {
        if (player != null) {
            player.pause();
            return player.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void resume(int position) {
        if (player != null) {
            player.seekTo(position);
            player.start();
        }
    }

    @Override
    public void stop() {
        if (player != null) {
            player.stop();
        }
    }
}
