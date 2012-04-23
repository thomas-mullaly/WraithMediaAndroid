package com.wraithmedia.ui;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.wraithmedia.R;
import com.wraithmedia.core.service.ServiceToken;
import com.wraithmedia.playback.MediaPlaybackService;
import com.wraithmedia.playback.MediaPlaybackServiceConnectionCallback;
import com.wraithmedia.playback.MediaPlaybackServiceConnector;

public class PlaybackControlsFragment extends Fragment {
    private boolean mBoundToMediaPlaybackService = false;
    private MediaPlaybackService mMediaPlayerService;
    private MediaPlaybackServiceConnector mServiceConnector;
    private ServiceToken mServiceToken;
    private Button mPlayPauseToggleButton;

    private final MediaPlaybackServiceConnectionCallback mServiceConnectionCallback = new MediaPlaybackServiceConnectionCallback() {
        public void onServiceConnected(ComponentName componentName, MediaPlaybackService mediaPlaybackService) {
            mMediaPlayerService = mediaPlaybackService;
            mBoundToMediaPlaybackService = true;
        }

        public void onServiceDisconnected(ComponentName componentName) {
            mBoundToMediaPlaybackService = false;
            mMediaPlayerService = null;
        }
    };

    private final BroadcastReceiver mPlaybackStateChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isPlaying = intent.getExtras().getBoolean(MediaPlaybackService.MUSIC_PLAYBACK_BROADCAST_EXTRA_IS_PLAYING);

            mPlayPauseToggleButton.setText(isPlaying ? R.string.playback_controls_pause : R.string.playback_controls_play);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle bundle) {
        return inflator.inflate(R.layout.playback_controls_layout, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        registerPlaybackStateChangeReceiver();

        mServiceConnector = new MediaPlaybackServiceConnector();
        mServiceToken = mServiceConnector.bindToService(getActivity(), mServiceConnectionCallback);

        mPlayPauseToggleButton = (Button)getView().findViewById(R.id.playback_controls_play_pause_toggle_button);
        mPlayPauseToggleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (mBoundToMediaPlaybackService) {
                    mMediaPlayerService.togglePlayPause();
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();

        getActivity().unregisterReceiver(mPlaybackStateChangedReceiver);
        mServiceConnector.unbindFromService(mServiceToken);
    }

    private void registerPlaybackStateChangeReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MediaPlaybackService.MUSIC_PLAYBACK_BROADCAST_PLAYSTATE_CHANGED);

        getActivity().registerReceiver(mPlaybackStateChangedReceiver, intentFilter);
    }
}
