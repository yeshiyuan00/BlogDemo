package com.why.recylerview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.why.LuckyPan_Activity;
import com.why.drawerlayout.VDHBlogActivity;
import com.why.viewpagerindicator.ViewPagerIndicator_Activity;
import com.ysy.blogdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ysy on 2015/5/5.
 */
public class RecyleView_Activity extends Activity implements AdapterView.OnItemClickListener {

    private TextView text;
    private ListView list;
    private List<String> list_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        text = (TextView) findViewById(R.id.text);
        list = (ListView) findViewById(R.id.list);
        list_show = new ArrayList<String>();

        list_show.add(0, "RecylerView1");
        list_show.add(1, "RecylerView2");
        list_show.add(2, "RecylerView3");

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                list_show);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                Intent intent0 = new Intent(RecyleView_Activity.this, RecylerView1_Activity.class);
                startActivity(intent0);
                break;
            case 1:
                Intent intent1 = new Intent(RecyleView_Activity.this, RecylerView2_Activity.class);
                startActivity(intent1);
                break;
            case 2:
                Intent intent2 = new Intent(RecyleView_Activity.this, RecylerView3_Activity.class);
                startActivity(intent2);
                break;
            case 3:
                Intent intent3 = new Intent(RecyleView_Activity.this, LuckyPan_Activity.class);
                startActivity(intent3);
                break;
            case 4:
                Intent intent4 = new Intent(RecyleView_Activity.this, ViewPagerIndicator_Activity.class);
                startActivity(intent4);
                break;
            case 5:
                Intent intent5 = new Intent(RecyleView_Activity.this, VDHBlogActivity.class);
                startActivity(intent5);
                break;
        }
    }
}
