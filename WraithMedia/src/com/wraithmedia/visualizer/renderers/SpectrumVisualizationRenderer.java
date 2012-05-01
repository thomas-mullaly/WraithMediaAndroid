package com.wraithmedia.visualizer.renderers;

import com.wraithmedia.visualizer.effects.FullscreenAlphaBlendEffect;
import com.wraithmedia.visualizer.PlaybackInfo;
import com.wraithmedia.visualizer.renderers.VisualizationRenderer;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class SpectrumVisualizationRenderer implements VisualizationRenderer {

    public SpectrumVisualizationRenderer() {
        super();

        mAlphaBlendEffect = new FullscreenAlphaBlendEffect(0.095f);
        mSpectrumLinesBuffer = null;
        mSpectrumLines = null;
    }

    public void renderFrame(GL10 gl, PlaybackInfo playbackInfo, int height, int width) {
        //gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_LINE_SMOOTH);
        gl.glHint(GL10.GL_LINE_SMOOTH_HINT, GL10.GL_NICEST);

        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glPushMatrix();
        gl.glLoadIdentity();

        mAlphaBlendEffect.draw(gl);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();

        gl.glLineWidth(1);

        gl.glTranslatef(-1, 0, 0);
        gl.glScalef(2, 1, 1);

        if(mSpectrumLinesBuffer == null && playbackInfo.getWaveformData() != null) {
            initializeSpectrumLines(playbackInfo.getWaveformData().length);
        }

        if(playbackInfo.getWaveformData() != null){
            updateSpectrumData(playbackInfo.getWaveformData());
            gl.glVertexPointer(2, GL10.GL_FLOAT, 0, mSpectrumLinesBuffer);

            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

            gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
            gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, mSpectrumLines.length / 2);

            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        }
    }

    private void updateSpectrumData(byte[] waveformData) {
        int spectrumIndex = 0;
        for(int i = 0; i < waveformData.length; ++i) {
            mSpectrumLines[spectrumIndex] = ((float)i / waveformData.length);

            // WORKAROUND: The waveform data we receive is unsigned, but Java, by default, assumes
            // all numerical values are signed and has no native support for unsigned values.
            // So naturally any value > 127 in the waveform data will be treated as a negative value.
            // In order to get rid of the sign bit, we have to store the value into a short (so we have
            // enough precision) and then mask away the sign bit.
            // Damn you Java. Damn you.
            short grr = waveformData[i];
            mSpectrumLines[spectrumIndex+1] = (grr & 0xFF) / 250.0f;
            spectrumIndex += 2;
        }

        mSpectrumLinesBuffer.put(mSpectrumLines);
        mSpectrumLinesBuffer.position(0);
    }
    private void initializeSpectrumLines(int bandCount) {
        mSpectrumLines = new float[bandCount * 2];

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(FLOAT_SIZE * bandCount * 2);
        byteBuffer.order(ByteOrder.nativeOrder());

        mSpectrumLinesBuffer = byteBuffer.asFloatBuffer();
    }

    private FullscreenAlphaBlendEffect mAlphaBlendEffect;
    private FloatBuffer mSpectrumLinesBuffer;
    private float[] mSpectrumLines;
    
    private static final int FLOAT_SIZE = 4;
}
