package shishkin.cleanarchitecture.mvi.app.specialist.media;

import android.media.MediaPlayer;
import android.os.PowerManager;


import androidx.annotation.NonNull;
import shishkin.cleanarchitecture.mvi.app.SLUtil;
import shishkin.cleanarchitecture.mvi.sl.AbsSpecialist;

public class MediaSpecialistImpl extends AbsSpecialist implements MediaSpecialist, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    public static final String NAME = MediaSpecialistImpl.class.getName();
    private MediaPlayer player;
    private int position = -1;

    private boolean isStop = false;

    @Override
    public void onUnRegister() {
        releaseMedia();
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
    public void playMedia(int resId) {
        position = -1;
        player = MediaPlayer.create(SLUtil.getContext(), resId);
        player.setWakeMode(SLUtil.getContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        player.setOnPreparedListener(this);
        player.start();
    }

    @Override
    public int pauseMedia() {
        if (player != null) {
            player.pause();
            position = player.getCurrentPosition();
            return position;
        }
        return 0;
    }

    @Override
    public void resumeMedia(int position) {
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
    public void resumeMedia() {
        if (player != null) {
            if (position < 0) {
                return;
            }
            player.seekTo(position);
            player.start();
        }
    }

    @Override
    public void stopMedia() {
        if (player != null) {
            isStop = true;
            position = -1;
            player.stop();
        }
    }

    @Override
    public void stop() {
        releaseMedia();
    }

    @Override
    public void releaseMedia() {
        stopMedia();
        if (player != null) {
            player.release();
        }
    }

    @Override
    public boolean isStopMedia() {
        if (player == null) return true;

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
