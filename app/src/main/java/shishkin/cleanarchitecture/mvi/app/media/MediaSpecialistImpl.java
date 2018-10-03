package shishkin.cleanarchitecture.mvi.app.media;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.support.annotation.NonNull;


import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.sl.AbsSpecialist;

public class MediaSpecialistImpl extends AbsSpecialist implements MediaSpecialist, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    public static final String NAME = MediaSpecialistImpl.class.getName();
    private MediaPlayer player;
    private int position = -1;

    private boolean isStop = false;

    @Override
    public void onUnRegister() {
        release();
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
        player = MediaPlayer.create(SLUtil.getContext(), resId);
        player.setWakeMode(SLUtil.getContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        player.setOnPreparedListener(this);
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
    public void release() {
        stop();
        if (player != null) {
            player.release();
        }
    }

    @Override
    public boolean isStop() {
        return isStop;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stop();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }
}
