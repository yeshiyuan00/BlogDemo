package com.yesy.drccurve;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * User: ysy
 * Date: 2015/11/5
 * Time: 11:06
 */
public class MyPoint implements Parcelable {

    public float x;
    public float y;

    public MyPoint() {
    }

    public MyPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public MyPoint(MyPoint src) {
        this.x = src.x;
        this.y = src.y;
    }

    /**
     * Set the point's x and y coordinates
     */
    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Negate the point's coordinates
     */
    public final void negate() {
        x = -x;
        y = -y;
    }

    /**
     * Offset the point's coordinates by dx, dy
     */
    public final void offset(float dx, float dy) {
        x += dx;
        y += dy;
    }

    /**
     * Returns true if the point's coordinates equal (x,y)
     */
    public final boolean equals(float x, float y) {
        return this.x == x && this.y == y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyPoint point = (MyPoint) o;

        if (x != point.x) return false;
        if (y != point.y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        float result = x;
        result = 31 * result + y;
        return (int) result;
    }

    @Override
    public String toString() {
        return "MyPoint(" + x + ", " + y + ")";
    }

    /**
     * Parcelable interface methods
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Write this point to the specified parcel. To restore a point from
     * a parcel, use readFromParcel()
     *
     * @param out The parcel to write the point's coordinates into
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeFloat(x);
        out.writeFloat(y);
    }

    public static final Parcelable.Creator<MyPoint> CREATOR = new Parcelable.Creator<MyPoint>() {
        /**
         * Return a new point from the data in the specified parcel.
         */
        public MyPoint createFromParcel(Parcel in) {
            MyPoint r = new MyPoint();
            r.readFromParcel(in);
            return r;
        }

        /**
         * Return an array of rectangles of the specified size.
         */
        public MyPoint[] newArray(int size) {
            return new MyPoint[size];
        }
    };

    /**
     * Set the point's coordinates from the data stored in the specified
     * parcel. To write a point to a parcel, call writeToParcel().
     *
     * @param in The parcel to read the point's coordinates from
     */
    public void readFromParcel(Parcel in) {
        x = in.readFloat();
        y = in.readFloat();
    }
}
