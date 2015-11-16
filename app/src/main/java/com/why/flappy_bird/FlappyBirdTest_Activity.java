package com.why.flappy_bird;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.ysy.blogdemo.R;

/**
 * User: ysy
 * Date: 2015/8/31
 */
public class FlappyBirdTest_Activity extends Activity {
    private CopyOfGameFlabbyBird mGame;
    private Button btn_grade;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_flappybirf);

        mGame = (CopyOfGameFlabbyBird) findViewById(R.id.flappybird);
        btn_grade = (Button) findViewById(R.id.btn_grade);
        mGame.setGameOverListener(new CopyOfGameFlabbyBird.GameOverListener() {
            @Override
            public void gameOver() {
                FlappyBirdTest_Activity.this.finish();
            }
        });

    }

}
