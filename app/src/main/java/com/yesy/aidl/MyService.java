package com.yesy.aidl;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ysy.blogdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: yeshiyuan
 * Date: 7/19/16.
 */
public class MyService extends Service {

    private final static String TAG = "MyService";
    private static final String PACKAGE_SAYHI = "com.ysy.aidltest";

    private NotificationManager mNotificationManager;
    private boolean mCanRun = true;
    private List<Student> mStudents = new ArrayList<Student>();


    //这里实现了aidl中得抽象函数
    private final IMyService.Stub mBinder = new IMyService.Stub() {
        @Override
        public List<Student> getStudent() throws RemoteException {
            synchronized (mStudents) {
                return mStudents;
            }

        }

        @Override
        public void addStudent(Student student) throws RemoteException {
            synchronized (mStudents) {
                if (!mStudents.contains(student)) {
                    mStudents.add(student);
                }

            }
        }

        //在这里可以做权限认证，return false意味着客户端的调用就会失败，比如下面，只允许包名为com.example.test的客户端通过，
        //其他apk将无法完成调用过程
        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String packageName = null;
            String[] packages = MyService.this.getPackageManager().getPackagesForUid(getCallingUid());
            if (packages != null && packages.length > 0) {
                packageName = packages[0];
            }
            Log.d(TAG, "onTransact: " + packageName);

            if (!PACKAGE_SAYHI.equals(packageName)) {
                return true;
            }

            return super.onTransact(code, data, reply, flags);
        }

    };


    @Override
    public void onCreate() {

        Thread thr = new Thread(null, new ServiceWorker(), "BackgroundService");
        thr.start();

        synchronized (mStudents) {
            for (int i = 1; i < 6; i++) {
                Student student = new Student();
                student.name = "student#" + i;
                student.age = i * 5;
                mStudents.add(student);
            }
        }

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, String.format("on bind,intent = %s", intent.toString()));
        displayNotificationMessage("服务已启动");

        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        mCanRun = false;
        super.onDestroy();
    }


    private void displayNotificationMessage(String message)
    {
        Notification notification = new Notification(R.drawable.b1, message,
                System.currentTimeMillis());
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_ALL;
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MyActivity.class), 0);
        notification.setLatestEventInfo(this, "我的通知", message,
                contentIntent);
        mNotificationManager.notify(R.id.app_notification_id + 1, notification);
    }



    class ServiceWorker implements Runnable
    {
        long counter = 0;

        @Override
        public void run()
        {
            // do background processing here.....
            while (mCanRun)
            {
                Log.d("scott", "" + counter);
                counter++;
                try
                {
                    Thread.sleep(2000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

}
