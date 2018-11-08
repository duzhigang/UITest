package com.ggec.uitest.ui.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ggec.uitest.R;

import java.util.ArrayList;

/**
 * Created by ggec on 2018/8/1.
 */

public class StudentAdapter extends BaseAdapter {
    private static final String TAG = "StudentAdapter";

    private ArrayList<Student> students;
    private Context mContext;

    public StudentAdapter(Context context, ArrayList<Student> students) {
        this.mContext = context;
        this.students = students;
    }

    @Override
    public int getCount() {
        return students.size();
    }

    @Override
    public Object getItem(int position) {
        return students.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.student_item, null);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_student_name);
            viewHolder.tvAge = (TextView) convertView.findViewById(R.id.tv_student_age);
            viewHolder.tvFruit = (TextView) convertView.findViewById(R.id.tv_student_fruit);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvName.setText(students.get(position).name);
        viewHolder.tvAge.setText(String.valueOf(students.get(position).age));
        viewHolder.tvFruit.setText(students.get(position).fruit);
        return convertView;
    }

    private static class ViewHolder {
        TextView tvName;
        TextView tvAge;
        TextView tvFruit;
    }
}
