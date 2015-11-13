package com.why.flappy_bird;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ysy.blogdemo.R;

/**
 * User: ysy
 * Date: 2015/9/1
 */
public class Menu_Activity extends Activity {
    private Button btn_start, btn_grade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_flapybirdmenu);

        btn_grade = (Button) findViewById(R.id.btn_grade);
        btn_start = (Button) findViewById(R.id.btn_start);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu_Activity.this, FlappyBirdTest_Activity.class);
                startActivity(intent);
            }
        });

        btn_grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu_Activity.this, Score_Activity.class);
                startActivity(intent);
            }
        });
    }
}
