package com.ruijie.openglstudy.filter;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import com.ruijie.openglstudy.utils.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by panruijie on 18-9-3.
 * 基础filter
 */
public abstract class AbsFilter implements GLSurfaceView.Renderer {

    private Context mContext;
    private int mProgramId;
    private int mGlPosition;
    private int mGlTexture;
    private int mGlCoordinate;
    private int mGlMatrix;
    private int mGlIsHalf;
    private int mGlUxy;

    private String mVertex;
    private String mFragment;

    private FloatBuffer mFbPos;
    private FloatBuffer mFbCoord;

    private boolean mIsHalf = false;
    private float mAspectRatioX;
    private float mAspectRatioY;
    private Bitmap mBitmap;

    private float[] mViewMatrix = new float[16];
    private float[] mProjectMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    private static final int FLOAT_BYTES = 4;

    /**
     * 顶点坐标
     */
    private static final float[] sPos = {
            -1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, 1.0f,
            1.0f, -1.0f
    };

    /**
     * 纹理坐标
     */
    private static final float[] sCoord = {
            0.25f, 0.25f,
            0.25f, 0.75f,
            0.75f, 0.25f,
            0.75f, 0.75f
    };

    public AbsFilter(Context context, String vertex, String fragment) {
        this.mContext = context;
        this.mVertex = vertex;
        this.mFragment = fragment;
        mFbPos = ByteBuffer.allocateDirect(sPos.length * FLOAT_BYTES)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mFbPos.put(sPos);
        mFbPos.position(0);

        mFbCoord = ByteBuffer.allocateDirect(sCoord.length * FLOAT_BYTES)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mFbCoord.put(sCoord);
        mFbCoord.position(0);
    }

    public void setHalf(boolean isHalf) {
        this.mIsHalf = isHalf;
    }

    public void setBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        mProgramId = ShaderUtils.createProgram(mContext.getResources(), mVertex, mFragment);
        mGlPosition = GLES20.glGetAttribLocation(mProgramId, "vPosition");
        mGlCoordinate = GLES20.glGetAttribLocation(mProgramId, "vCoordinate");
        mGlTexture = GLES20.glGetUniformLocation(mProgramId, "vTexture");
        mGlMatrix = GLES20.glGetUniformLocation(mProgramId, "vMatrix");
        mGlIsHalf = GLES20.glGetUniformLocation(mProgramId, "vIsHalf");
        mGlUxy = GLES20.glGetUniformLocation(mProgramId, "uXY");
        onDrawCreatedSet(mProgramId);
    }

    abstract void onDrawSet();

    abstract void onDrawCreatedSet(int programId);

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        if (width > height) {
            mAspectRatioX = width / (float) height;
            mAspectRatioY = 1;
        } else {
            mAspectRatioX = 1;
            mAspectRatioY = height / (float) width;
        }
        Matrix.orthoM(mProjectMatrix, 0, -mAspectRatioX, mAspectRatioX, -mAspectRatioY, mAspectRatioY, -1f, 1f);
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 1.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glUseProgram(mProgramId);
        onDrawSet();
        GLES20.glUniform1f(mGlIsHalf, mIsHalf ? 1 : 0);
        GLES20.glUniform1f(mGlUxy, mAspectRatioX / mAspectRatioY);
        GLES20.glUniformMatrix4fv(mGlMatrix, 1, false, mMVPMatrix, 0);
        GLES20.glEnableVertexAttribArray(mGlPosition);
        GLES20.glEnableVertexAttribArray(mGlCoordinate);
        GLES20.glUniform1f(mGlTexture, 0);
        checkTextureCreate();
        GLES20.glVertexAttribPointer(mGlPosition, 2, GLES20.GL_FLOAT, false, 0, mFbPos);
        GLES20.glVertexAttribPointer(mGlCoordinate, 2, GLES20.GL_FLOAT, false, 0, mFbCoord);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

    }

    private int checkTextureCreate() {
        if (mBitmap != null && !mBitmap.isRecycled()) {
            int[] texture = new int[1];
            //生成纹理
            GLES20.glGenTextures(1, texture, 0);
            //生成纹理
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
            //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            //根据以上指定的参数，生成一个2D纹理
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0);
            return texture[0];
        }
        return 0;
    }
}
