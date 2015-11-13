package com.util;

import android.content.Context;

/**
 * User: qii
 * Date: 12-11-28
 */
public class SettingUtility {


    private static final String GRADECOUNT = "gradecount";

    private Context mContext;

    public SettingUtility(Context context) {
        this.mContext = context;
    }

    public void setGradeCount() {
        SettingHelper.setEditor(getContext(), GRADECOUNT, getGradeCount() + 1);
    }

    public int getGradeCount() {
        return SettingHelper.getSharedPreferences(getContext(), GRADECOUNT, 0);
    }

    private Context getContext() {
        return mContext;
    }


}
