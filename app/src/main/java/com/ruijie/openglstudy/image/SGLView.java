package com.ruijie.openglstudy.image;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;

import com.ruijie.openglstudy.filter.AbsFilter;

import java.io.IOException;

/**
 * Created by panruijie on 18-9-3.
 *
 */
public class SGLView extends GLSurfaceView {

    private SGLRender mRender;

    public SGLView(Context context) {
        this(context, null);
    }

    public SGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEGLContextClientVersion(2);
        mRender = new SGLRender(getContext());
        setRenderer(mRender);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        Log.w("ruijie", "just test");
        try {
            mRender.setImage(BitmapFactory.decodeStream(getResources().getAssets().open("texture/fengj.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   /* public SGLRender getRender(){
        return mRender;
    }

    public void setFilter(AbsFilter filter){
        mRender.setFilter(filter);
    }*/
}
