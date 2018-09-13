package com.ggec.uitest.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ggec.uitest.data.Grade;
import com.ggec.uitest.R;
import com.ggec.uitest.data.Student;

import java.util.ArrayList;

/**
 * Created by ggec on 2018/8/1.
 */

public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "MyExpandableListAdapter";

    private Context mContext;
    private String[] kinds;
    private ArrayList<Grade> primaryGrades;
    private ArrayList<Student> seniorStudents;

    public MyExpandableListAdapter(Context context, String[] kinds, ArrayList<Grade> primaryGrades, ArrayList<Student> seniorStudents) {
        this.mContext = context;
        this.kinds = kinds;
        this.primaryGrades = primaryGrades;
        this.seniorStudents = seniorStudents;
    }

    @Override
    public int getGroupCount() {
        return kinds.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition == 0) {
            return primaryGrades.size();
        } else {
            return seniorStudents.size();
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return kinds[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (groupPosition == 0) {
            return primaryGrades.get(childPosition);
        } else {
            return seniorStudents.get(childPosition);
        }
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder ;
        if (convertView == null) {
            groupViewHolder = new GroupViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.group_item, null);
            groupViewHolder.tvGroupName = (TextView) convertView.findViewById(R.id.tv_group_item_name);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.tvGroupName.setText(kinds[groupPosition]);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder;
        if (convertView == null) {
            childHolder = new ChildHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.child_grade_item, null);
            childHolder.tvChildName = (TextView) convertView.findViewById(R.id.tv_child_grade_name);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }
        String childName = "";
        if (groupPosition == 0) {
            childName = primaryGrades.get(childPosition).name;
        } else {
            childName = seniorStudents.get(childPosition).name;
        }
        Log.e(TAG,"childName = " + childName);
        childHolder.tvChildName.setText(childName);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private static class GroupViewHolder {
        TextView tvGroupName;
    }

    private static class ChildHolder {
        TextView tvChildName;
    }

    private int dp2Px(int dp){
        float scale = mContext.getResources().getDisplayMetrics().density;
        return  (int) (dp * scale + 0.5f);
    }
}
