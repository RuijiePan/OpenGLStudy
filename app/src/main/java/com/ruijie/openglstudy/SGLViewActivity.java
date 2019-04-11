package com.ruijie.openglstudy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.ruijie.openglstudy.R;
import com.ruijie.openglstudy.filter.ColorFilter;
import com.ruijie.openglstudy.filter.ContrastColorFilter;
import com.ruijie.openglstudy.image.SGLView;

/**
 * Created by panruijie on 18-9-3.
 * 图片处理
 */
public class SGLViewActivity extends AppCompatActivity {

    private SGLView mGLView;
    private boolean mIsHalf;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGLView = findViewById(R.id.glView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter,menu);
        return super.onCreateOptionsMenu(menu);
    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mDeal:
                mIsHalf = !mIsHalf;
                if (mIsHalf) {
                    item.setTitle("处理一半");
                } else {
                    item.setTitle("全部处理");
                }
                mGLView.getRender().refresh();
                break;
            case R.id.mDefault:
                mGLView.setFilter(new ContrastColorFilter(this, ColorFilter.Filter.NONE));
                break;
            case R.id.mGray:
                mGLView.setFilter(new ContrastColorFilter(this, ColorFilter.Filter.GRAY));
                break;
            case R.id.mCool:
                mGLView.setFilter(new ContrastColorFilter(this, ColorFilter.Filter.COOL));
                break;
            case R.id.mWarm:
                mGLView.setFilter(new ContrastColorFilter(this, ColorFilter.Filter.WARM));
                break;
            case R.id.mBlur:
                mGLView.setFilter(new ContrastColorFilter(this, ColorFilter.Filter.BLUR));
                break;
            case R.id.mMagn:
                mGLView.setFilter(new ContrastColorFilter(this, ColorFilter.Filter.MAGN));
                break;

        }
        mGLView.getRender().getFilter().setHalf(mIsHalf);
        mGLView.requestRender();
        return super.onOptionsItemSelected(item);
    }*/
}
