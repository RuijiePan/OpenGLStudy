package com.ruijie.openglstudy.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;

import com.ruijie.openglstudy.filter.AbsFilter;
import com.ruijie.openglstudy.filter.ColorFilter;
import com.ruijie.openglstudy.filter.ContrastColorFilter;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by panruijie on 18-9-3.
 */
public class SGLRender implements GLSurfaceView.Renderer {

    private AbsFilter mAbsFilter;
    private Bitmap mBitmap;
    private int mWidth;
    private int mHeight;
    private boolean mFlagRefresh = false;
    private EGLConfig mEGLConfig;

    public SGLRender(Context context) {
        mAbsFilter = new ContrastColorFilter(context, ColorFilter.Filter.NONE);
    }

    public void setFilter(AbsFilter filter) {
        mFlagRefresh = true;
        this.mAbsFilter = filter;
        if (mBitmap != null) {
            mAbsFilter.setBitmap(mBitmap);
        }
    }

    public void refresh(){
        mFlagRefresh = true;
    }

    public AbsFilter getFilter(){
        return mAbsFilter;
    }

    public void setImage(Bitmap bitmap){
        this.mBitmap = bitmap;
        mAbsFilter.setBitmap(bitmap);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        this.mEGLConfig = eglConfig;
        mAbsFilter.onSurfaceCreated(gl10, eglConfig);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        mAbsFilter.onSurfaceChanged(gl10, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        if (mFlagRefresh && mWidth != 0 && mHeight != 0) {
            mAbsFilter.onSurfaceCreated(gl10, mEGLConfig);
            mAbsFilter.onSurfaceChanged(gl10, mWidth, mHeight);
            mFlagRefresh = false;
        }
        mAbsFilter.onDrawFrame(gl10);
    }
}
