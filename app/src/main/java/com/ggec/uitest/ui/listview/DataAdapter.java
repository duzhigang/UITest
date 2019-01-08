package com.ggec.uitest.ui.listview;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.ggec.uitest.R;

import java.util.ArrayList;

public class DataAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private String[] kinds;
    private ArrayList<String> groupOne;
    private ArrayList<String> groupTwo;

    DataAdapter(Context context, String[] kinds, ArrayList<String> group1, ArrayList<String> group2) {
        this.mContext = context;
        this.kinds = kinds;
        this.groupOne = group1;
        this.groupTwo = group2;
    }

    @Override
    public int getGroupCount() {
        return kinds.length;
    }

    @Override
    public int getChildrenCount(int i) {
        if (i == 0) {
            return groupOne.size() + 1;
        } else {
            return groupTwo.size() + 1;
        }
    }

    @Override
    public Object getGroup(int i) {
        return kinds[i];
    }

    @Override
    public Object getChild(int i, int i1) {
        if (i == 0) {
            return groupOne.get(i1);
        } else {
            return groupTwo.get(i1);
        }
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        GroupViewHolder groupViewHolder;
        if (view == null) {
            groupViewHolder = new GroupViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.one_item_left, null);
            groupViewHolder.tvGroupName = view.findViewById(R.id.tv_one_item_left_name);
            groupViewHolder.tvGroupName.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            view.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) view.getTag();
        }
        groupViewHolder.tvGroupName.setText(kinds[i]);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if (i == 0) {
            if (b) {
                view = LayoutInflater.from(mContext).inflate(R.layout.group_item, null);
                TextView tv = view.findViewById(R.id.tv_group_item_name);
                tv.setText("第一个底部Item");
            } else {
                view = LayoutInflater.from(mContext).inflate(R.layout.one_item_left, null);
                TextView tv = view.findViewById(R.id.tv_one_item_left_name);
                tv.setText(groupOne.get(i1));
            }
        } else {
            if (b) {
                view = LayoutInflater.from(mContext).inflate(R.layout.group_item, null);
                TextView tv = view.findViewById(R.id.tv_group_item_name);
                tv.setText("第二个底部Item");
            } else {
                view = LayoutInflater.from(mContext).inflate(R.layout.one_item_left, null);
                TextView tv = view.findViewById(R.id.tv_one_item_left_name);
                tv.setText(groupTwo.get(i1));
            }
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private static class GroupViewHolder {
        TextView tvGroupName;
    }

    private static class ChildViewHolder {
        TextView tvChildName;
    }
}
