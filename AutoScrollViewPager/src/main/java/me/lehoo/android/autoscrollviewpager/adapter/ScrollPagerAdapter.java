package me.lehoo.android.autoscrollviewpager.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import me.lehoo.android.autoscrollviewpager.R;

/**
 * Created by zhoulq on 14-9-19.
 */
public class ScrollPagerAdapter extends PagerAdapter {

    private Context context;
    private int[] numberList;
    private int[] imageList;
    private LayoutInflater inflater;

    public ScrollPagerAdapter(Context context, int[] numberList, int[] imageList) {
        this.context = context;
        this.numberList = numberList;
        this.imageList = imageList;
    }

    @Override
    public int getCount() {
        return numberList.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TextView numberView;
        ImageView imageView;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.pager_item_layout, container, false);
        numberView = (TextView) itemView.findViewById(R.id.number);
        imageView = (ImageView) itemView.findViewById(R.id.image);
        numberView.setText(numberList[position]+"");
        imageView.setImageResource(imageList[position]);

        ((ViewPager)container).addView((RelativeLayout)itemView, 0);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
