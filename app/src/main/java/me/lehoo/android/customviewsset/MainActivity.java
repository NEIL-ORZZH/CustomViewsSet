package me.lehoo.android.customviewsset;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import me.lehoo.android.autoscrollviewpager.AutoScrollViewPagerActivity;
import me.lehoo.android.pulltorefreshlistview.PullToRefreshListViewActivity;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews(){
        Button goToPullToRefreshListViewBtn = (Button) findViewById(R.id.pull_to_refresh_list_view_btn);
        goToPullToRefreshListViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToListViewIntent = new Intent(MainActivity.this, PullToRefreshListViewActivity.class);
                startActivity(goToListViewIntent);
            }
        });

        Button goToAutoScrollViewPagerBtn = (Button) findViewById(R.id.auto_scroll_view_pager_btn);
        goToAutoScrollViewPagerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToViewPagerIntent = new Intent(MainActivity.this, AutoScrollViewPagerActivity.class);
                startActivity(goToViewPagerIntent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
