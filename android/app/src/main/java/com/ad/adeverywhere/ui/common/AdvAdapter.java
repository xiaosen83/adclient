package com.ad.adeverywhere.ui.common;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by cwang on 9/18/16.
 */
public class AdvAdapter extends PagerAdapter {
    private List<View> mViews = null;

    public AdvAdapter(List<View> views){
        mViews = views;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
        //super.destroyItem(container, position, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViews.get(position), 0);
        return mViews.get(position);
    }
}
