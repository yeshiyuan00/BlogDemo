package com.yesy.drccurve;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.ysy.blogdemo.R;

public class DrcCurveActivity extends AppCompatActivity {

    private DrcCurve drcCurve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drc_curve);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drcCurve = (DrcCurve) findViewById(R.id.id_dc_curve);

        drcCurve.setDataChangeListener(new DrcCurve.OnDataChangeListener() {
            @Override
            public void dataChange() {
                Log.e("Test:", "ET.x=" + drcCurve.getET().x);
            }
        });
        DrcContrller drcContrller = new DrcContrller();

       /* double[] x = new double[1000];
        for (int i = 0; i < 1000; i++) {
            x[i] = 0.90;
            drcContrller.getGn(x[i]);
        }*/

    }

}
