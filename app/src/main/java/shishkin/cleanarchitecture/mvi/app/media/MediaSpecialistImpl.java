package shishkin.cleanarchitecture.mvi.app.media;

import android.media.MediaPlayer;
import android.support.annotation.NonNull;


import shishkin.cleanarchitecture.mvi.app.ApplicationController;
import shishkin.cleanarchitecture.mvi.sl.AbsSpecialist;

public class MediaSpecialistImpl extends AbsSpecialist implements MediaSpecialist {
    public static final String NAME = MediaSpecialistImpl.class.getName();
    private MediaPlayer player;
    private int position = -1;

    private boolean isStop = false;

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
        position = -1;
        player = MediaPlayer.create(ApplicationController.getInstance(), resId);
        player.start();
    }

    @Override
    public int pause() {
        if (player != null) {
            player.pause();
            position = player.getCurrentPosition();
            return position;
        }
        return 0;
    }

    @Override
    public void resume(int position) {
        if (player != null) {
            if (position < 0) {
                return;
            }
            this.position = position;
            player.seekTo(position);
            player.start();
        }
    }

    @Override
    public void resume() {
        if (player != null) {
            if (position < 0) {
                return;
            }
            player.seekTo(position);
            player.start();
        }
    }

    @Override
    public void stop() {
        if (player != null) {
            isStop = true;
            position = -1;
            player.stop();
        }
    }

    @Override
    public boolean isStop() {
        return isStop;
    }

}
