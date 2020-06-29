package com.baima.objectivebook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HelpActivity extends AppCompatActivity {

    private ListView lv_help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        lv_help = findViewById(R.id.lv_help);

        List<String> stringList = new ArrayList<>();
        try {
            InputStream inputStream = getAssets().open("help.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals("")) {
                    line = "\n";
                }
                stringList.add(line);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stringList);
                lv_help.setAdapter(adapter);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
