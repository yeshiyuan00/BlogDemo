package com.yesy.test;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.ysy.blogdemo.R;

import java.util.Timer;
import java.util.TimerTask;

public class WaveProgressView extends AppCompatActivity {

    private com.github.zeng1990java.widget.WaveProgressView waveProgressView;
    private int progress;

    private Handler MyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x123:
                    waveProgressView.setProgress(progress);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        waveProgressView = (com.github.zeng1990java.widget.WaveProgressView)
                findViewById(R.id.wave_progress_view_2);
        waveProgressView.setMax(100);
        Timer timer = new Timer();

        progress = 0;
        waveProgressView.setProgress(progress);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                progress++;
                if (progress > 100) {
                    progress = 0;
                }
                Message msg = new Message();
                msg.what = 0x123;
                MyHandler.sendMessage(msg);
            }
        }, 100, 100);
    }
}
