package com.wraithmedia;

import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.viewpagerindicator.PageIndicator;
import com.wraithmedia.core.service.ServiceToken;
import com.wraithmedia.media.MediaSelectionListener;
import com.wraithmedia.playback.MediaPlaybackService;
import com.wraithmedia.playback.MediaPlaybackServiceConnectionCallback;
import com.wraithmedia.playback.MediaPlaybackServiceConnector;
import com.wraithmedia.ui.MediaViewsPagerAdapter;
import com.wraithmedia.visualizer.VisualizerActivity;

public class WraithMediaActivity extends FragmentActivity implements MediaSelectionListener {
    private MediaPlaybackService mMediaPlaybackService;
    private boolean mBoundToMediaPlayerService = false;
    private MediaPlaybackServiceConnector mMediaPlaybackServiceConnector;
    private ServiceToken mMediaPlaybackServiceToken;
    private ViewPager mViewPager;
    private MediaViewsPagerAdapter mViewsAdapater;
    private PageIndicator mViewPageIndicator;

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
        setContentView(R.layout.wraith_media_activity_layout);

        setupViewPager();
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

    public void onMediaSelected(Cursor cursor) {
        if (mBoundToMediaPlayerService) {
            mMediaPlaybackService.playMedia(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));

            Intent i = new Intent(this, VisualizerActivity.class);
            i.putExtra("mediaPlayerSessionId", mMediaPlaybackService.getMediaSessionId());

            startActivity(i);
        }
    }

    private void setupViewPager() {
        mViewsAdapater = new MediaViewsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager)findViewById(R.id.media_list_display_view_pager);
        mViewPager.setAdapter(mViewsAdapater);

        mViewPageIndicator = (PageIndicator)findViewById(R.id.media_list_display_view_pager_titles);
        mViewPageIndicator.setViewPager(mViewPager);
    }
}
