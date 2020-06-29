package com.baima.objectivebook;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        checkDBVersion();
    }

    private void checkDBVersion() {
        SQLiteDatabase database = Connector.getDatabase();
        DBOpenHelper dbOpenHelper = new DBOpenHelper(this);
        SQLiteDatabase readableDatabase = dbOpenHelper.getReadableDatabase();

        database.close();
        readableDatabase.close();


        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
