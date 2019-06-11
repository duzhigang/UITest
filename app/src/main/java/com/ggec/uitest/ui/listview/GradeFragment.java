package com.ggec.uitest.ui.listview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ggec.uitest.R;

import java.util.ArrayList;

/**
 * Created by ggec on 2018/8/31.
 */

public class GradeFragment extends Fragment {
    private static final String TAG = "GradeFragment";

    private Grade grade;
    private ArrayList<Student> students;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grade, container, false);
        TextView tvTitle = view.findViewById(R.id.tv_grade_fragment_title);
        tvTitle.setText(grade.name);
        StudentAdapter adapter = new StudentAdapter(getActivity(), grade.students);
        ListView lvStudents = view.findViewById(R.id.lv_students);
        lvStudents.setAdapter(adapter);
        return view;
    }

    public void setStudents(Grade grade) {
        this.grade = grade;
    }
}
