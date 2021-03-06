package com.googlebrother;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.googlebrother.collapseview.CollapseActivity;
import com.yesy.aidl.MyActivity;
import com.yesy.circleBar.MainActivity;
import com.yesy.coordinatorlayout.CoordinatorSampleList;
import com.yesy.cropPhoto.CropPhotoActivity;
import com.yesy.expandablerecycleview.ExpandableRecyclerViewActivity;
import com.yesy.test.WaveProgressView;
import com.ysy.blogdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ysy on 2015/5/5.
 */
public class Google_Brother_Blog extends AppCompatActivity implements AdapterView.OnItemClickListener {

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

        list_show.add(0, "CollapseView");


        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                list_show);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                Intent intent0 = new Intent(Google_Brother_Blog.this, CollapseActivity.class);
                startActivity(intent0);
                break;
            case 1:
                Intent intent1 = new Intent(Google_Brother_Blog.this, WaveProgressView.class);
                startActivity(intent1);
                break;
            case 2:
                Intent intent2 = new Intent(Google_Brother_Blog.this,MainActivity.class);
                startActivity(intent2);
                break;
            case 3:
                Intent intent3 = new Intent(Google_Brother_Blog.this, ExpandableRecyclerViewActivity.class);
                startActivity(intent3);
                break;
            case 4:
                Intent intent4 = new Intent(Google_Brother_Blog.this,CropPhotoActivity.class);
                startActivity(intent4);
                break;
            case 5:
                Intent intent5 = new Intent(Google_Brother_Blog.this, CoordinatorSampleList.class);
                startActivity(intent5);
                break;
            case 6:
                Intent intent6 = new Intent(Google_Brother_Blog.this, MyActivity.class);
                startActivity(intent6);
                break;
        }
    }
}
