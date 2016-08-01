package com.ysy.aidltest;

import android.content.Context;
import android.location.Location;

/**
 * Author: yeshiyuan
 * Date: 7/19/16.
 */
public class MyLocation {

    private Context mContext;

    public MyLocation(Context context) {
        mContext = context;
    }

    public void setLocation(Location location) {
        location.setLatitude(location.getLatitude() - 59.0);
        location.setLongitude(location.getLongitude() + 37.55);
    }
}
