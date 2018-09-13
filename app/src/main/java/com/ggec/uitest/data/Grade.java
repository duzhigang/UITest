package com.ggec.uitest.data;

import java.util.ArrayList;

/**
 * Created by ggec on 2018/8/1.
 */

public class Grade {
    public String name;
    public int id = -1;
    public ArrayList<Student> students = new ArrayList<>();

    public Grade(String name) {
        this.name = name;
    }

    public Grade(String name, int id, ArrayList<Student> students) {
        this.name = name;
        this.id = id;
        this.students = students;
    }
}
