package com.wraithmedia.playback;

import android.content.ComponentName;

public interface MediaPlaybackServiceConnectionCallback {
    void onServiceConnected(ComponentName componentName, MediaPlaybackService mediaPlaybackService);
    void onServiceDisconnected(ComponentName componentName);
}
