package com.wraithmedia.visualizer.effects;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class FullscreenAlphaBlendEffect implements Effect {
    public FullscreenAlphaBlendEffect(float alpha) {
        mAlpha = alpha;

        initQuadVertices();
    }

    public void draw(GL10 gl) {
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glCullFace(GL10.GL_BACK);

        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, mVertexBuffer);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glColor4f(.0f, .0f, .0f, mAlpha);
        gl.glDrawElements(GL10.GL_TRIANGLES, mIndices.length, GL10.GL_UNSIGNED_SHORT, mIndicesBuffer);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisable(GL10.GL_CULL_FACE);
    }

    private void initQuadVertices() {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(mVertices.length * FLOAT_SIZE);
        byteBuffer.order(ByteOrder.nativeOrder());
        mVertexBuffer = byteBuffer.asFloatBuffer();
        mVertexBuffer.put(mVertices);
        mVertexBuffer.position(0);

        ByteBuffer indicesByteBuffer = ByteBuffer.allocateDirect(mIndices.length * 2);
        indicesByteBuffer.order(ByteOrder.nativeOrder());
        mIndicesBuffer = indicesByteBuffer.asShortBuffer();
        mIndicesBuffer.put(mIndices);
        mIndicesBuffer.position(0);
    }

    private float mVertices[] = { -1.0f,1.0f, -1.0f,-1.0f, 1.0f,-1.0f, 1.0f,1.0f };
    private FloatBuffer mVertexBuffer;

    private short mIndices[] = {0,1,2,0,2,3};
    private ShortBuffer mIndicesBuffer;

    private float mAlpha;

    private static final int FLOAT_SIZE = 4;
}

