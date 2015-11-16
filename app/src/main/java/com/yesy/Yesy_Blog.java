package com.yesy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.why.LuckyPan_Activity;
import com.why.QQListViewActivity;
import com.why.VerticalLinearLayoutActivity;
import com.why.drawerlayout.VDHBlogActivity;
import com.why.recylerview.RecyleView_Activity;
import com.why.viewpagerindicator.ViewPagerIndicator_Activity;
import com.yesy.drccurve.DrcCurveActivity;
import com.ysy.blogdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ysy on 2015/5/5.
 */
public class Yesy_Blog extends Activity implements AdapterView.OnItemClickListener {

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

        list_show.add(0, "DrcCurve");

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                list_show);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                Intent intent0 = new Intent(Yesy_Blog.this, DrcCurveActivity.class);
                startActivity(intent0);
                break;
            case 1:
                Intent intent1 = new Intent(Yesy_Blog.this, QQListViewActivity.class);
                startActivity(intent1);
                break;
            case 2:
                Intent intent2 = new Intent(Yesy_Blog.this, VerticalLinearLayoutActivity.class);
                startActivity(intent2);
                break;
            case 3:
                Intent intent3 = new Intent(Yesy_Blog.this, LuckyPan_Activity.class);
                startActivity(intent3);
                break;
            case 4:
                Intent intent4 = new Intent(Yesy_Blog.this, ViewPagerIndicator_Activity.class);
                startActivity(intent4);
                break;
            case 5:
                Intent intent5 = new Intent(Yesy_Blog.this, VDHBlogActivity.class);
                startActivity(intent5);
                break;
            case 6:
                Intent intent6 = new Intent(Yesy_Blog.this, RecyleView_Activity.class);
                startActivity(intent6);
                break;
        }
    }
}
