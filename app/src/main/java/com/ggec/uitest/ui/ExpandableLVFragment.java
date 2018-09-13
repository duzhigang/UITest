package com.ggec.uitest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import com.ggec.uitest.data.Grade;
import com.ggec.uitest.R;
import com.ggec.uitest.data.Student;
import com.ggec.uitest.adapter.MyExpandableListAdapter;

import java.util.ArrayList;

/**
 * Created by ggec on 2018/8/31.
 * 测试将一个ArrayList<>数据分成不同的组显示
 */

public class ExpandableLVFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "ExpandableLVFragment";

    private MyExpandableListAdapter adapter;
    private ArrayList<Grade> grades;
    private ArrayList<Grade> primaryGrades = new ArrayList<>();
    private ArrayList<Student> seniorStudents = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        grades = produceData();
        Log.e(TAG,"onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG,"onCreateView");
        View view = inflater.inflate(R.layout.fragment_expandable_lv, container, false);
        String[] kinds = {"初级组", "高级组"};
        updateAllGroup();
        adapter = new MyExpandableListAdapter(getActivity(), kinds, primaryGrades, seniorStudents);
        ExpandableListView elvGrades = (ExpandableListView) view.findViewById(R.id.elv_grades);
        elvGrades.setGroupIndicator(null);
        elvGrades.setAdapter(adapter);
        // 将子Group默认展开
        for (int i = 0; i < 2; i++) {
            elvGrades.expandGroup(i);
        }
        elvGrades.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (groupPosition == 0 && childPosition < primaryGrades.size() - 1) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    GradeFragment fragment = new GradeFragment();
                    fragment.setStudents(primaryGrades.get(childPosition));
                    ft.addToBackStack(null);
                    ft.replace(R.id.lv_frame, fragment).commit();
                }
                return false;
            }
        });

        view.findViewById(R.id.btn_expandable_lv_modify_frag).setOnClickListener(this);
        view.findViewById(R.id.btn_expandable_lv_one).setOnClickListener(this);
        view.findViewById(R.id.btn_expandable_lv_two).setOnClickListener(this);
        view.findViewById(R.id.btn_expandable_lv_three).setOnClickListener(this);
        view.findViewById(R.id.btn_expandable_lv_four).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_expandable_lv_modify_frag:
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ModifyDataFragment fragment = new ModifyDataFragment();
                fragment.setTargetFragment(ExpandableLVFragment.this, 1);
                ft.addToBackStack(null);
                ft.replace(R.id.lv_frame, fragment).commit();
                break;
            case R.id.btn_expandable_lv_one:
                grades.get(2).name = "修改G3的名字";
                updateAllGroup();
                adapter.notifyDataSetChanged();
                break;
            case R.id.btn_expandable_lv_two:
                grades.get(1).students.get(3).name = "修改G2S4";
                updateAllGroup();
                adapter.notifyDataSetChanged();
                break;
            case R.id.btn_expandable_lv_three:
                Grade grade = new Grade("增加G5");
                grade.id = 5;
                Student stu51 = new Student("新增的一个分组", 51, "西瓜一");
                grade.students.add(stu51);
                grades.add(grade);
                updateAllGroup();
                adapter.notifyDataSetChanged();
                break;
            case R.id.btn_expandable_lv_four:
                Student stu43 = new Student("增加G4S", 42, "葡萄三");
                grades.get(3).students.add(stu43);
                updateAllGroup();
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG,"onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            grades.get(0).name = data.getStringExtra("result");
            updateAllGroup();
            adapter.notifyDataSetChanged();
        }
    }

    private void updateAllGroup() {
        primaryGrades.clear();
        seniorStudents.clear();
        for (Grade grade : grades) {
            if (grade.id == 0) {
                seniorStudents.addAll(grade.students);
            } else {
                primaryGrades.add(grade);
            }
        }
        Log.e(TAG,"初级组年级个数为：" + primaryGrades.size() + ",高级组的学生个数为：" + seniorStudents.size());
        String lastGradeName = "添加新的Grade";
        String lastStudentName = "添加新的学生";
        Grade newGrade = new Grade(lastGradeName);
        primaryGrades.add(newGrade);
        Student newStudent = new Student(lastStudentName);
        seniorStudents.add(newStudent);
    }

    private ArrayList<Grade> produceData() {
        ArrayList<Grade> grades = new ArrayList<>();
        ArrayList<Student> students1 = new ArrayList<>();
        Student stu11 = new Student("张一", 11, "苹果一");
        Student stu12 = new Student("张二", 12, "苹果二");
        Student stu13 = new Student("张三", 13, "苹果三");
        students1.add(stu11);
        students1.add(stu12);
        students1.add(stu13);
        Grade grade1 = new Grade("一年级",1, students1);
        grades.add(grade1);

        ArrayList<Student> students2 = new ArrayList<>();
        Student stu21 = new Student("李一", 21, "桔子一");
        Student stu22 = new Student("李二", 22, "桔子二");
        Student stu23 = new Student("李三", 23, "桔子三");
        Student stu24 = new Student("李四", 24, "桔子四");
        Student stu25 = new Student("李五", 25, "桔子五");
        students2.add(stu21);
        students2.add(stu22);
        students2.add(stu23);
        students2.add(stu24);
        students2.add(stu25);
        Grade grade2 = new Grade("二年级",2, students2);
        grades.add(grade2);

        ArrayList<Student> students3 = new ArrayList<>();
        Student stu31 = new Student("王一", 31, "香蕉一");
        Student stu32 = new Student("王二", 32, "香蕉二");
        Student stu33 = new Student("王三", 33, "香蕉三");
        Student stu34 = new Student("王四", 34, "香蕉四");
        students3.add(stu31);
        students3.add(stu32);
        students3.add(stu33);
        students3.add(stu34);
        Grade grade3 = new Grade("三年级",3, students3);
        grades.add(grade3);

        ArrayList<Student> students4 = new ArrayList<>();
        Student stu41 = new Student("孙一", 41, "葡萄一");
        Student stu42 = new Student("孙二", 42, "葡萄二");
        students4.add(stu41);
        students4.add(stu42);
        Grade grade4 = new Grade("成人年级",0, students4);
        grades.add(grade4);
        return grades;
    }
}
