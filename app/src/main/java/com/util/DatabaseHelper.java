package com.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.why.flappy_bird.Score;

/**
 * User: qii
 * Date: 12-7-30
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper singleton = null;

    private static final String DATABASE_NAME = "flappy.db";

    private static final int DATABASE_VERSION = 1;

    static final String CREATE_ACCOUNT_TABLE_SQL = "create table " + Score.TABLE_NAME
            + "("
            + Score.UID + " integer primary key autoincrement,"
            + Score.GRADE + " integer"
            + ");";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_ACCOUNT_TABLE_SQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (singleton == null) {
            singleton = new DatabaseHelper(context);
        }
        return singleton;
    }

}
