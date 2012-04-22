package com.wraithmedia;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ComponentName;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import com.wraithmedia.core.service.ServiceToken;
import com.wraithmedia.media.MediaSelectionListener;
import com.wraithmedia.playback.MediaPlaybackService;
import com.wraithmedia.playback.MediaPlaybackServiceConnectionCallback;
import com.wraithmedia.playback.MediaPlaybackServiceConnector;
import com.wraithmedia.ui.TabListener;
import com.wraithmedia.ui.displays.SongsDisplay;

public class MediaListDisplayActivity extends Activity implements MediaSelectionListener {
    private MediaPlaybackService mMediaPlaybackService;
    private boolean mBoundToMediaPlayerService = false;
    private MediaPlaybackServiceConnector mMediaPlaybackServiceConnector;
    private ServiceToken mMediaPlaybackServiceToken;

    private final MediaPlaybackServiceConnectionCallback mMediaPlayerServiceConnection = new MediaPlaybackServiceConnectionCallback() {
        public void onServiceConnected(ComponentName componentName, MediaPlaybackService service) {
            mMediaPlaybackService = service;
            mBoundToMediaPlayerService = true;
        }

        public void onServiceDisconnected(ComponentName componentName) {
            mBoundToMediaPlayerService = false;
        }
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_list_display_layout);

        setupActionBar();
    }

    @Override
    public void onStart() {
        super.onStart();

        mMediaPlaybackServiceConnector = new MediaPlaybackServiceConnector();
        mMediaPlaybackServiceToken = mMediaPlaybackServiceConnector.bindToService(this, mMediaPlayerServiceConnection);
    }
    
    @Override
    public void onStop() {
        super.onStop();

        if (mBoundToMediaPlayerService) {
            mMediaPlaybackServiceConnector.unbindFromService(mMediaPlaybackServiceToken);
            mBoundToMediaPlayerService = false;
        }
    }

    private void setupActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);

        ActionBar.Tab tab = actionBar.newTab().setText(R.string.action_bar_songs)
                .setTabListener(new TabListener<SongsDisplay>(this, SongsDisplay.SONGS_DISPLAY_TAG_NAME, SongsDisplay.class));
        actionBar.addTab(tab);
    }

    public void onMediaSelected(Cursor cursor) {
        if (mBoundToMediaPlayerService) {
            mMediaPlaybackService.playMedia(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
        }
    }
}
