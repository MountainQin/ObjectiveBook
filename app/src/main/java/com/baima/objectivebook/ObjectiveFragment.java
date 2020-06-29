package com.baima.objectivebook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.baima.objectivebook.entities.Objective;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class ObjectiveFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static final String TAG = "baima";
    private List<Objective> objectiveList = new ArrayList<>();
    private int objectiveType;
    private ListView lv_objective;
    private ObjectiveAdapter adapter;
    private AlertDialog deleteObjectiveDialog;

    public ObjectiveFragment(int objectiveType) {
        this.objectiveType = objectiveType;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_objective, container, false);
        lv_objective = view.findViewById(R.id.lv_objective);
        lv_objective.setOnItemClickListener(this);
        lv_objective.setOnItemLongClickListener(this);

        adapter = new ObjectiveAdapter(getActivity(), objectiveList);
        lv_objective.setAdapter(adapter);
        refreshListData();
        return view;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Objective objective = objectiveList.get(position);
        showDeleteObjectiveDialog(objective);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//改变完成状态
        Objective objective = objectiveList.get(position);
        boolean finish = objective.isFinish();
        objective.setFinish(!finish);
        //
        if (!objective.isFinish()) {
            objective.setToDefault("finish");
        }
        objective.update(objective.getId());
        refreshListData();

        String s;
        if (objective.isFinish()) {
            s = "已完成";
        } else {
            s = "未完成";
        }
        Toast.makeText(getActivity(), objective.getTitle() + s, Toast.LENGTH_SHORT).show();
    }

    public AlertDialog getDeleteObjectiveDialog() {
        return deleteObjectiveDialog;
    }

    //刷新列表数据
    public void refreshListData() {
        if (adapter != null) {
            objectiveList.clear();
//            List<Objective> list = LitePal.where("objectiveType=? and finish=?", String.valueOf(objectiveType), String.valueOf(false))
            List<Objective> list = LitePal.where("objectiveType=? and finish=?", String.valueOf(objectiveType), "0")
                    .order("id desc").find(Objective.class);
//            List<Objective> list1 = LitePal.where("objectiveType=? and finish=?", String.valueOf(objectiveType), String.valueOf(true))
            List<Objective> list1 = LitePal.where("objectiveType=? and finish=?", String.valueOf(objectiveType), "1")
                    .order("id desc").find(Objective.class);
            objectiveList.addAll(list);
            objectiveList.addAll(list1);
            adapter.notifyDataSetChanged();
        }
    }

    //删除目标对话框
    private void showDeleteObjectiveDialog(final Objective objective) {
        deleteObjectiveDialog = new AlertDialog.Builder(getActivity())
                .setTitle("提示")
                .setMessage("你确定删除这个目标吗？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        objectiveList.remove(objective);
                        adapter.notifyDataSetChanged();
                        objective.delete();
                    }
                })
                .show();
    }
}
