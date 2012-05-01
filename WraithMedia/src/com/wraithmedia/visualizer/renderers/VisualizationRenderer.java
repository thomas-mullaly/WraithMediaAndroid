package com.wraithmedia.visualizer.renderers;

import javax.microedition.khronos.opengles.GL10;

import com.wraithmedia.visualizer.PlaybackInfo;

public interface VisualizationRenderer {
    void renderFrame(GL10 gl, PlaybackInfo playbackInfo, int width, int height);
}
