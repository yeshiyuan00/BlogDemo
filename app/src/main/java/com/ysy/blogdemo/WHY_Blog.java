package com.ysy.blogdemo;

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
import com.why.flappy_bird.Menu_Activity;
import com.why.flowlayout.FlowLayoutActivity;
import com.why.okhttp.Test_Activity;
import com.why.recylerview.RecyleView_Activity;
import com.why.viewpagerindicator.ViewPagerIndicator_Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ysy on 2015/5/5.
 */
public class WHY_Blog extends Activity implements AdapterView.OnItemClickListener {

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

        list_show.add(0, "Custom ViewGroup");
        list_show.add(1, "ListViewItemSlideDeleteBtnShow");
        list_show.add(2, "VerticalLinearLayout");
        list_show.add(3, "LuckyPan");
        list_show.add(4, "ViewPagerIndicator");
        list_show.add(5, "ViewDragHelper");
        list_show.add(6, "RecylerView");
        list_show.add(7, "okhttp");
        list_show.add(8, "flappy bird");
        list_show.add(9, "FlowLayout");

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                list_show);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                Intent intent0 = new Intent(WHY_Blog.this, CustomViewGroupTest.class);
                startActivity(intent0);
                break;
            case 1:
                Intent intent1 = new Intent(WHY_Blog.this, QQListViewActivity.class);
                startActivity(intent1);
                break;
            case 2:
                Intent intent2 = new Intent(WHY_Blog.this, VerticalLinearLayoutActivity.class);
                startActivity(intent2);
                break;
            case 3:
                Intent intent3 = new Intent(WHY_Blog.this, LuckyPan_Activity.class);
                startActivity(intent3);
                break;
            case 4:
                Intent intent4 = new Intent(WHY_Blog.this, ViewPagerIndicator_Activity.class);
                startActivity(intent4);
                break;
            case 5:
                Intent intent5 = new Intent(WHY_Blog.this, VDHBlogActivity.class);
                startActivity(intent5);
                break;
            case 6:
                Intent intent6 = new Intent(WHY_Blog.this, RecyleView_Activity.class);
                startActivity(intent6);
                break;
            case 7:
                Intent intent7 = new Intent(WHY_Blog.this, Test_Activity.class);
                startActivity(intent7);
                break;
            case 8:
                Intent intent8 = new Intent(WHY_Blog.this, Menu_Activity.class);
                startActivity(intent8);
                break;
            case 9:
                Intent intent9 = new Intent(WHY_Blog.this, FlowLayoutActivity.class);
                startActivity(intent9);
                break;
        }
    }
}
