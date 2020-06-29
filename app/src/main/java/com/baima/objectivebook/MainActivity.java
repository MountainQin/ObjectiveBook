package com.baima.objectivebook;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
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
    private MediaPlayer mediaPlayer;
    private View TextViewtv_continuation_play;
    private View textViewtv_continuation_play;
    private TextView tv_continuation_play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_saying:
                playSaying(false);
                break;
            case R.id.tv_continuation_play:
                String s = tv_continuation_play.getText().toString();
                if ("连续播放".equals(s)) {
                    playSaying(true);
                    tv_continuation_play.setText("停止播放");
                } else {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        tv_continuation_play.setText("连续播放");
                    }
                }
                break;
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
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
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
        textViewtv_continuation_play = TextViewtv_continuation_play;
        tv_continuation_play = findViewById(R.id.tv_continuation_play);
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

        tv_saying.setOnClickListener(this);
        tv_continuation_play.setOnClickListener(this);
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
        if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
            int i = new Random().nextInt(sayings.size());
            tv_saying.setText(sayings.get(i));
        }
    }

    private void playAssetsSound(String path, final boolean continuation) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        mediaPlayer.reset();
        try {
            AssetFileDescriptor assetFileDescriptor = getAssets().openFd(path);
            mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    tv_continuation_play.setText("停止播放");
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    tv_continuation_play.setText("连续播放");
                    if (continuation) {
                        playSaying(continuation);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playSaying(boolean continuation) {
        int i = new Random().nextInt(sayings.size());
        tv_saying.setText(sayings.get(i));
        playAssetsSound("SayingSound/" + i + ".mp3", continuation);
    }
}
