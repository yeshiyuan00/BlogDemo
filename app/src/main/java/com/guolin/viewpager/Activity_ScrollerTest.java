package com.guolin.viewpager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ysy.blogdemo.R;

/**
 * User: ysy
 * Date: 2016/2/15
 * Time: 9:07
 */
public class Activity_ScrollerTest extends Activity {

    private LinearLayout layout;

    private Button scrollToBtn;

    private Button scrollByBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollertest);

        layout = (LinearLayout) findViewById(R.id.layout);
        scrollByBtn = (Button) findViewById(R.id.scroll_by_btn);
        scrollToBtn = (Button) findViewById(R.id.scroll_to_btn);

        scrollByBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.scrollBy(-60, -100);
            }
        });

        scrollToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.scrollTo(-60, -100);
            }
        });
    }
}
