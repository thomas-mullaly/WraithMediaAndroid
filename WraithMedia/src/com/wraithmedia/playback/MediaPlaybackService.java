package com.wraithmedia.playback;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

import com.wraithmedia.WraithMediaActivity;
import com.wraithmedia.R;

public class MediaPlaybackService extends Service {
    private final String MUSIC_PLAYER_SERVICE_NAME = getClass().getName();

    public static final String MUSIC_PLAYBACK_BROADCAST_PLAYSTATE_CHANGED = "com.wraithmedia.playback.PLAYSTATE_CHANGED";

    public static final String MUSIC_PLAYBACK_BROADCAST_EXTRA_IS_PLAYING = "isplaying";

    private Binder mServiceBinder;
    private MediaPlayer mMediaPlayer;

    public class MediaPlaybackServiceBinder extends Binder {
        public MediaPlaybackService getService() {
            return MediaPlaybackService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (mServiceBinder == null){
            mServiceBinder = new MediaPlaybackServiceBinder();
        }

        return mServiceBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = new MediaPlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaPlayer.release();
    }

    public void playMedia(final String uri) {
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(uri);
            mMediaPlayer.prepare();
            startMediaPlayer();
        } catch (IOException ex) {
            Log.e(MUSIC_PLAYER_SERVICE_NAME, "Error opening file", ex);
        }
    }

    public void pause() {
        pauseMediaPlayer();
    }

    public void resume() {
        startMediaPlayer();
    }

    public void togglePlayPause() {
        if(mMediaPlayer.isPlaying()) {
            pauseMediaPlayer();
        } else {
            startMediaPlayer();
        }
    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    public int getMediaSessionId() {
        return mMediaPlayer.getAudioSessionId();
    }

    private void pauseMediaPlayer() {
        stopForeground(true);
        mMediaPlayer.pause();
        broadcastChange(MUSIC_PLAYBACK_BROADCAST_PLAYSTATE_CHANGED);
    }

    private void startMediaPlayer() {
        Notification notification = new Notification(R.drawable.ic_launcher, "Derp", System.currentTimeMillis());
        Intent i = new Intent(this, WraithMediaActivity.class);

        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);

        notification.setLatestEventInfo(this, "Wraith Media Player", "Now Playing", pendingIntent);
        notification.flags |= Notification.FLAG_NO_CLEAR;

        startForeground(1, notification);

        mMediaPlayer.start();
        broadcastChange(MUSIC_PLAYBACK_BROADCAST_PLAYSTATE_CHANGED);
    }

    private void broadcastChange(String what) {
        Intent broadcastIntent = new Intent(what);
        broadcastIntent.putExtra(MUSIC_PLAYBACK_BROADCAST_EXTRA_IS_PLAYING, mMediaPlayer.isPlaying());
        sendBroadcast(broadcastIntent);
    }
}
