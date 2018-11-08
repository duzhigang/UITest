package com.ggec.uitest.ui.listview;

/**
 * Created by ggec on 2018/8/1.
 */

public class Student {
    public String name;
    public int age;
    public String fruit;

    public Student(String name) {
        this.name = name;
    }

    public Student(String name, int age, String fruit) {
        this.name = name;
        this.age = age;
        this.fruit = fruit;
    }
}
