package com.wraithmedia.visualizer;

import android.content.Context;
import android.media.audiofx.Visualizer;
import android.opengl.GLSurfaceView;

public class VisualizerGLSurfaceView extends GLSurfaceView implements PlaybackInfo, Visualizer.OnDataCaptureListener {
    public VisualizerGLSurfaceView(Context context, int mediaPlayerSessionId) {
        super(context);

        mWaveformData = null;

        mVisualizer = new Visualizer(mediaPlayerSessionId);
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(this, Visualizer.getMaxCaptureRate(), true, false);

        mVisualizer.setEnabled(true);

        setEGLContextClientVersion(1);
        setRenderer(new VisualizerGLRenderer(this));
    }

    @Override
    public void onPause() {
        super.onPause();
        mVisualizer.setEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mVisualizer.setEnabled(true);
    }

    public byte[] getWaveformData() {
        return mWaveformData;
    }

    public float[] getSpectrumData() {
        return new float[0];
    }

    public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveForm, int samplingRate) {
        mWaveformData = waveForm;
        requestRender();
    }

    public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
        // TODO: Actually handle FFT data.
    }

    private byte[] mWaveformData;
    private Visualizer mVisualizer;
}
