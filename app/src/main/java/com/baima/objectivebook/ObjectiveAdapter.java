package com.baima.objectivebook;

import android.content.Context;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baima.objectivebook.entities.Objective;

import java.util.List;

public class ObjectiveAdapter extends BaseAdapter {

    private Context context;
    private List<Objective> objectiveList;

    public ObjectiveAdapter(Context context, List<Objective> objectiveList) {
        this.context = context;
        this.objectiveList = objectiveList;
    }

    @Override
    public int getCount() {
        return objectiveList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_objective, null);
            holder = new ViewHolder();
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.tv_status = convertView.findViewById(R.id.tv_status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Objective objective = objectiveList.get(position);
        holder.tv_title.setText(objective.getTitle());
        if (objective.isFinish()) {
            holder.tv_status.setVisibility(View.VISIBLE);
        }else{
            holder.tv_status.setVisibility(View.GONE);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView tv_title;
        TextView tv_status;
    }
}
