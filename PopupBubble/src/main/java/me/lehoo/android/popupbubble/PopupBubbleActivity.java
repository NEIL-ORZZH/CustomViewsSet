package me.lehoo.android.popupbubble;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import me.lehoo.android.popupbubble.widget.PopupBubble;


public class PopupBubbleActivity extends ActionBarActivity {

    private ListView mListView;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_bubble);

        mListView = (ListView) findViewById(R.id.list_view);

        mListView.setAdapter(new MyAdapter(getData()));
    }

    private ArrayList<String> getData(){
        ArrayList<String> dataList = new ArrayList<String>();
        for(int i=0; i < 40; i++){
            dataList.add("这是第 "+ i + " 个列表项目");
        }
        return dataList;
    }

    public class MyAdapter extends BaseAdapter {
        private ArrayList<String> mDataList;

        public MyAdapter(ArrayList<String> mDataList) {
            this.mDataList = mDataList;
        }

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView == null){
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_layout, null);
                viewHolder.titleView = (TextView) convertView.findViewById(R.id.title_label);
                viewHolder.voteButton = (Button) convertView.findViewById(R.id.option_btn);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final String dataString = mDataList.get(position);
            viewHolder.titleView.setText(dataString);

            viewHolder.voteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "position = " + position, Toast.LENGTH_SHORT).show();
                    //
                    PopupBubble bubble = new PopupBubble(v, dataString);
                    bubble.show();
                }
            });

            return convertView;
        }
    }

    public class ViewHolder{
        private TextView titleView;
        private Button voteButton;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.popup_bubble, menu);
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
