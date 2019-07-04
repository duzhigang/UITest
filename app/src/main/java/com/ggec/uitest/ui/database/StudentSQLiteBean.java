package com.ggec.uitest.ui.database;

import com.ggec.uitest.ui.listview.Student;

public class StudentSQLiteBean {
    private String name;
    private int age;
    private String sex;

    public StudentSQLiteBean(String name, int age, String sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
