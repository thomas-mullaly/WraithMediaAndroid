package com.wraithmedia.visualizer;

import android.opengl.GLSurfaceView;
import com.wraithmedia.visualizer.renderers.SpectrumVisualizationRenderer;
import com.wraithmedia.visualizer.renderers.VisualizationRenderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class VisualizerGLRenderer implements GLSurfaceView.Renderer {

    public VisualizerGLRenderer(PlaybackInfo playbackInfo) {
        mPlaybackInfo = playbackInfo;

        mRenderer = new SpectrumVisualizationRenderer();
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig) {
        // Set the background color to black ( rgba ).
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        gl.glDisable(GL10.GL_DEPTH_TEST);
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        mHeight = height;
        mWidth = width;
    }

    public void onDrawFrame(GL10 gl) {
        mRenderer.renderFrame(gl, mPlaybackInfo, mWidth, mHeight);
    }

    private VisualizationRenderer mRenderer;
    private PlaybackInfo mPlaybackInfo;
    
    private int mWidth;
    private int mHeight;
}

