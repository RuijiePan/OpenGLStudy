package com.ruijie.openglstudy.filter;

import android.content.Context;
import android.opengl.GLES20;

/**
 * Created by panruijie on 18-9-3.
 */
public class ColorFilter extends AbsFilter {

    private Filter mFilter;
    private int mHasChangeColor;
    private int mHasChangeType;

    public ColorFilter(Context context, Filter filter) {
        super(context, "filter/default_vertex.sh", "filter/color_fragment.sh");
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

    public enum Filter {

        NONE(0, new float[]{0.0f, 0.0f, 0.0f}),
        GRAY(1, new float[]{0.299f, 0.587f, 0.114f}),
        COOL(2, new float[]{0.0f, 0.0f, 0.1f}),
        WARM(2, new float[]{0.1f, 0.1f, 0.0f}),
        BLUR(3, new float[]{0.006f, 0.004f, 0.002f}),
        MAGN(4, new float[]{0.0f, 0.0f, 0.4f});

        private int vChangeType;
        private float[] data;

        Filter(int vChangeType, float[] data) {
            this.vChangeType = vChangeType;
            this.data = data;
        }

        public int getType() {
            return vChangeType;
        }

        public float[] data() {
            return data;
        }

    }
}
