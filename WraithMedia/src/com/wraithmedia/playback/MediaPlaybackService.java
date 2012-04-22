package com.wraithmedia.playback;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

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
            mMediaPlayer.start();
            broadcastChange(MUSIC_PLAYBACK_BROADCAST_PLAYSTATE_CHANGED);
        } catch (IOException ex) {
            Log.e(MUSIC_PLAYER_SERVICE_NAME, "Error opening file", ex);
        }
    }

    public void pause() {
        mMediaPlayer.pause();
    }

    public void resume() {
        mMediaPlayer.start();
    }

    private void broadcastChange(String what) {
        Intent broadcastIntent = new Intent(what);
        broadcastIntent.putExtra(MUSIC_PLAYBACK_BROADCAST_EXTRA_IS_PLAYING, mMediaPlayer.isPlaying());
        sendBroadcast(broadcastIntent);
    }
}