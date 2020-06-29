package com.baima.objectivebook;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baima.objectivebook.entities.Objective;
import com.baima.objectivebook.util.DateFormatUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    public static final String[] objectiveTypes = {"今日目标", "短期目标", "长期目标", "最终 目标"};

    private static final int ADD = 1;

    private TextView tv_date;
    private ViewPager view_pager;
    private LinearLayout ll_tab;
    private List<Fragment> fragmentList;
    private int currentItem;
    private TextView tv_saying;
    private List<String> sayings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_today_objective:
            case R.id.tv_short_objective:
            case R.id.tv_long_objective:
            case R.id.tv_final_objective:
                //ViewPager设置对应的item
                view_pager.setCurrentItem(ll_tab.indexOfChild(v));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD:
                    //刷新 对应Fragment
                    int type = data.getIntExtra(AddEditActivity.OBJECTIVE_TYPE, 0);
                    ((ObjectiveFragment) fragmentList.get(type))
                            .refreshListData();
                    view_pager.setCurrentItem(type);
                    break;
            }
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE:
                currentItem = view_pager.getCurrentItem();
                setTitle(objectiveTypes[currentItem]);
                setSaying();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_add:
                Intent intent = new Intent(this, AddEditActivity.class);
                intent.putExtra(AddEditActivity.OBJECTIVE_TYPE, currentItem);
                startActivityForResult(intent, ADD);
                break;
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        setSaying();
    }

    @Override
    public void onBackPressed() {
        //如果删除的对话框显示 先取消显示
        ObjectiveFragment fragment = (ObjectiveFragment) fragmentList.get(currentItem);
        AlertDialog deleteObjectiveDialog = fragment.getDeleteObjectiveDialog();
        if (deleteObjectiveDialog != null && deleteObjectiveDialog.isShowing()) {
            deleteObjectiveDialog.dismiss();
            return;
        }
        super.onBackPressed();
    }

    private void initView() {
        tv_date = findViewById(R.id.tv_date);
        tv_saying = findViewById(R.id.tv_saying);
        view_pager = findViewById(R.id.view_pager);
        ll_tab = findViewById(R.id.ll_tab);
        TextView tv_today_objective = findViewById(R.id.tv_today_objective);
        TextView tv_short_objective = findViewById(R.id.tv_short_objective);
        TextView tv_long_objective = findViewById(R.id.tv_long_objective);
        TextView tv_final_objective = findViewById(R.id.tv_final_objective);

        tv_date.setText(DateFormatUtil.format(new Date(), "yyyy年MM月dd日"));
        sayings = getSayings();
        setSaying();

        fragmentList = new ArrayList<>();
        ObjectiveFragment todayFragment = new ObjectiveFragment(Objective.TYPE_TODAY);
        ObjectiveFragment shortFragment = new ObjectiveFragment(Objective.TYPE_SHORT);
        ObjectiveFragment longFragment = new ObjectiveFragment(Objective.TYPE_LONG);
        ObjectiveFragment finalFragment = new ObjectiveFragment(Objective.TYPE_FINAL);

        fragmentList.add(todayFragment);
        fragmentList.add(shortFragment);
        fragmentList.add(longFragment);
        fragmentList.add(finalFragment);

        ObjectiveFragmentPagerAdapter adapter = new ObjectiveFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        view_pager.setAdapter(adapter);

        view_pager.addOnPageChangeListener(this);
        tv_today_objective.setOnClickListener(this);
        tv_short_objective.setOnClickListener(this);
        tv_long_objective.setOnClickListener(this);
        tv_final_objective.setOnClickListener(this);

        setTitle(objectiveTypes[view_pager.getCurrentItem()]);
    }

    //获取 assets下的名人名言大全
    private List<String> getSayings() {
        List<String> sayingList = new ArrayList<>();
        try {
            InputStream inputStream = getAssets().open("saying.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                if (((String) line).matches("^(\\d)+\\..+")) {
//                    line=line.substring(line.charAt('.'),line.length());
                    String[] split = line.split("^\\d+\\.");
                    if (split.length > 1) {
                        sayingList.add(split[1]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sayingList;
    }

    //设置名人名言，随机
    private void setSaying() {
        int i = new Random().nextInt(sayings.size());
        tv_saying.setText(sayings.get(i));
    }
}
