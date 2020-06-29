package com.baima.objectivebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.baima.objectivebook.entities.Objective;

public class AddEditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String OBJECTIVE_TYPE="objectiveType";
    private Spinner spinner_objective_type;
    private EditText et_objective;
    private int objectiveType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        spinner_objective_type = findViewById(R.id.spinner_objective_type);
        et_objective = findViewById(R.id.et_objective);

        Intent intent = getIntent();
        objectiveType =intent.getIntExtra(OBJECTIVE_TYPE,0);;

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, MainActivity.objectiveTypes);
        spinner_objective_type.setAdapter(adapter);
        spinner_objective_type.setSelection(objectiveType);
        spinner_objective_type.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        objectiveType = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ok, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String s = et_objective.getText().toString().trim();
        if (TextUtils.isEmpty(s)) {
            Toast.makeText(this, "你输入的内容为空，请检查重试！", Toast.LENGTH_SHORT).show();
            return true;
        }

        Objective objective = new Objective();
        objective.setTitle(s);
        objective.setObjectiveType(objectiveType);
        objective.setTimestamp(System.currentTimeMillis());
        objective.save();
        Toast.makeText(this, "保存成功！", Toast.LENGTH_SHORT).show();

        Intent intent = getIntent();
        intent.putExtra(OBJECTIVE_TYPE,objectiveType);
        setResult(RESULT_OK, intent);
        finish();
        return true;
    }
}
