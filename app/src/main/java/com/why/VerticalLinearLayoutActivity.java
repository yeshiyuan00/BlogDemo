package com.why;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.ysy.blogdemo.R;

import customview.VerticalLinearLayout;

/**
 * Created by ysy on 2015/5/7.
 */
public class VerticalLinearLayoutActivity extends Activity {

    private VerticalLinearLayout mMianLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_verticallinear);

        mMianLayout = (VerticalLinearLayout) findViewById(R.id.id_main_ly);
        mMianLayout.setOnPageChangeListener(new VerticalLinearLayout.OnPageChangeListener() {
            @Override
            public void onPageChange(int currentPage) {
                Toast.makeText(VerticalLinearLayoutActivity.this, "第" + (currentPage + 1) + "页", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
