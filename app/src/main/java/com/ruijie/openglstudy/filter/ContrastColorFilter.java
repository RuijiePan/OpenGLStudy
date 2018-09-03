package com.ruijie.openglstudy.filter;

import android.content.Context;
import android.opengl.GLES20;

/**
 * Created by panruijie on 18-9-3.
 */
public class ContrastColorFilter extends AbsFilter {

    private ColorFilter.Filter mFilter;
    private int mHasChangeType;
    private int mHasChangeColor;

    public ContrastColorFilter(Context context, ColorFilter.Filter filter) {
        super(context, "filter/half_color_vertex.sh", "filter/half_color_fragment.sh");
        this.mFilter = filter;
    }

    @Override
    void onDrawSet() {
        GLES20.glUniform1i(mHasChangeType, mFilter.getType());
        GLES20.glUniform3fv(mHasChangeColor, 1, mFilter.data(),0);
    }

    @Override
    void onDrawCreatedSet(int mProgram) {
        mHasChangeType = GLES20.glGetUniformLocation(mProgram,"vChangeType");
        mHasChangeColor = GLES20.glGetUniformLocation(mProgram,"vChangeColor");
    }
}
