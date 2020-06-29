package com.baima.objectivebook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.litepal.LitePal;

public class DBOpenHelper extends SQLiteOpenHelper {

    public DBOpenHelper(Context context) {
        super(context, "sqlite.db", null,
                LitePal.getDatabase().getVersion());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("baima", "on crate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("baima", "on upgrade");
    }
}
