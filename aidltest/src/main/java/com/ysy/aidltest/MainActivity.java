package com.ysy.aidltest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.yesy.aidl.IMyService;
import com.yesy.aidl.Student;

public class MainActivity extends AppCompatActivity {

    private static final String ACTION_BIND_SERVICE = "com.ysy.aidl.MyService";
    private IMyService mIMyService;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIMyService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //通过服务端onBind方法返回的binder对象得到IMyService的实例，得到实例就可以调用它的方法了
            mIMyService = IMyService.Stub.asInterface(service);
            try {
                Student student = mIMyService.getStudent().get(0);
                showDialog(student.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.button1) {
                    Intent intentService = new Intent(ACTION_BIND_SERVICE);
                    intentService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intentService.setPackage("com.ysy.blogdemo");
                    MainActivity.this.bindService(intentService, mServiceConnection, BIND_AUTO_CREATE);
                }

            }
        });

        MyLocation myLocation = new MyLocation(this);
        Location location = new Location(LocationManager.GPS_PROVIDER);
        myLocation.setLocation(location);

    }

    public void showDialog(String message) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("scott")
                .setMessage(message)
                .setPositiveButton("确定", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        if (mIMyService != null) {
            unbindService(mServiceConnection);
        }
        super.onDestroy();
    }

}
