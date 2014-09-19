package me.lehoo.android.autoscrollviewpager;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import me.lehoo.android.autoscrollviewpager.adapter.ScrollPagerAdapter;
import me.lehoo.android.autoscrollviewpager.widget.AutoScrollViewPager;


public class AutoScrollViewPagerActivity extends ActionBarActivity {

    private AutoScrollViewPager mViewPager;
    private ScrollPagerAdapter mAdapter;


    private int[] numberList = {1, 2, 3};
    private int[] imageList = {R.drawable.index1, R.drawable.index2, R.drawable.index3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_scroll_view_pager);
        initView();
    }

    private void initView(){
        mViewPager = (AutoScrollViewPager) findViewById(R.id.mViewPager);
        mAdapter = new ScrollPagerAdapter(AutoScrollViewPagerActivity.this, numberList, imageList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setInterval(3000);
        mViewPager.startAutoScroll();
        mViewPager.setScrollDurationFactor(1.0f);
        mViewPager.setCycle(true);
        mViewPager.setStopScrollWhenTouch(true);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {
                switch (i){
                    case 1:
                        mViewPager.stopAutoScroll();
                        break;
                    case 0:
                        mViewPager.startAutoScroll();
                        break;
                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.auto_scroll_view_pager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
