package com.yesy.expandablerecycleview;

import java.util.Date;

/**
 * Author: yeshiyuan
 * Date: 5/24/16.
 */
public class CrimeChild {
    private Date mDate;
    private boolean mSolved;

    public CrimeChild(Date date, boolean solved) {
        mDate = date;
        mSolved = solved;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }
}
