package com.xlw.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xlw.onroad.R;

/**
 * Created by hxsd on 2015/7/8.
 */
public class GuideAdapter extends PagerAdapter{

    private LayoutInflater inflater;
    private int[] drawables = {	R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};

    public GuideAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return drawables.length;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (int)(Integer)arg0.getTag() == (int)(Integer)arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View item = inflater.inflate(R.layout.item_guide, null);
        View img = item.findViewById(R.id.img);
        img.setBackgroundResource(drawables[position]);
        item.setTag(drawables[position]);

        container.addView(item, 0);
        return drawables[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        for (int i = 0; i < container.getChildCount(); i++) {
            View item = container.getChildAt(i);
            int drawable = (Integer) item.getTag();
            if(drawable == (Integer)object)
                container.removeView(item);
        }
//		super.destroyItem(container, position, object);
    }
}
