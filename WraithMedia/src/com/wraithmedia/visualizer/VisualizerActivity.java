package com.wraithmedia.visualizer;

import android.app.Activity;
import android.os.Bundle;

public class VisualizerActivity extends Activity {
    private VisualizerGLSurfaceView mVisualizerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle intent = getIntent().getExtras();
        int mediaPlayerSessionId = intent.getInt("mediaPlayerSessionId");

        mVisualizerView = new VisualizerGLSurfaceView(this, mediaPlayerSessionId);
        setContentView(mVisualizerView);
    }

    @Override
    public void onPause() {
        super.onPause();
        mVisualizerView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mVisualizerView.onResume();
    }
}
