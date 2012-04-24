package com.wraithmedia.ui;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wraithmedia.R;
import com.wraithmedia.core.service.ServiceToken;
import com.wraithmedia.media.MediaTimeFormatter;
import com.wraithmedia.playback.MediaPlaybackService;
import com.wraithmedia.playback.MediaPlaybackServiceConnectionCallback;
import com.wraithmedia.playback.MediaPlaybackServiceConnector;

public class PlaybackControlsFragment extends Fragment {
    private static final int TIMEOUT_PERIOD = 250;

    private boolean mBoundToMediaPlaybackService = false;
    private MediaPlaybackService mMediaPlayerService;
    private MediaPlaybackServiceConnector mServiceConnector;
    private ServiceToken mServiceToken;
    private Button mPlayPauseToggleButton;
    private SeekBar mSeekBar;
    private TextView mCurrentPositionText;
    private TextView mDurationText;
    private Handler mTimerHandler;
    private final MediaTimeFormatter mMediaTimeFormatter = new MediaTimeFormatter();

    private final MediaPlaybackServiceConnectionCallback mServiceConnectionCallback = new MediaPlaybackServiceConnectionCallback() {
        public void onServiceConnected(ComponentName componentName, MediaPlaybackService mediaPlaybackService) {
            mMediaPlayerService = mediaPlaybackService;
            mBoundToMediaPlaybackService = true;

            if(mMediaPlayerService.isPlaying()) {
                mTimerHandler.post(mTimerTask);
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            mTimerHandler.removeCallbacks(mTimerTask);
            mBoundToMediaPlaybackService = false;
            mMediaPlayerService = null;
        }
    };

    private final BroadcastReceiver mPlaybackStateChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isPlaying = intent.getExtras().getBoolean(MediaPlaybackService.MUSIC_PLAYBACK_BROADCAST_EXTRA_IS_PLAYING);

            mTimerHandler.removeCallbacks(mTimerTask);

            if (isPlaying) {
                mTimerHandler.post(mTimerTask);
            }

            mPlayPauseToggleButton.setText(isPlaying ? R.string.playback_controls_pause : R.string.playback_controls_play);
        }
    };

    private final Runnable mTimerTask = new Runnable() {
        public void run() {
            mSeekBar.setProgress(mMediaPlayerService.getCurrentPosition());
            mSeekBar.setMax(mMediaPlayerService.getDuration());

            mCurrentPositionText.setText(mMediaTimeFormatter.formatMediaTime(mMediaPlayerService.getCurrentPosition() / 1000));
            mDurationText.setText(mMediaTimeFormatter.formatMediaTime(mMediaPlayerService.getDuration() / 1000));

            mTimerHandler.postDelayed(mTimerTask, TIMEOUT_PERIOD);
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

        mTimerHandler = new Handler();

        mPlayPauseToggleButton = (Button)getView().findViewById(R.id.playback_controls_play_pause_toggle_button);
        mPlayPauseToggleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (mBoundToMediaPlaybackService) {
                    mMediaPlayerService.togglePlayPause();
                }
            }
        });

        mSeekBar = (SeekBar)getView().findViewById(R.id.playback_controls_seekbar);
        mCurrentPositionText = (TextView)getView().findViewById(R.id.playback_controls_current_position_text);
        mDurationText = (TextView)getView().findViewById(R.id.playback_controls_duration_text);
    }

    @Override
    public void onStop() {
        super.onStop();

        mTimerHandler.removeCallbacks(mTimerTask);

        getActivity().unregisterReceiver(mPlaybackStateChangedReceiver);
        mServiceConnector.unbindFromService(mServiceToken);
    }

    private void registerPlaybackStateChangeReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MediaPlaybackService.MUSIC_PLAYBACK_BROADCAST_PLAYSTATE_CHANGED);

        getActivity().registerReceiver(mPlaybackStateChangedReceiver, intentFilter);
    }
}
