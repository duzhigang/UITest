package com.ggec.uitest.ui.database;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * 必须要用默认的构造函数
 * */
public class StudentLitePalBean extends LitePalSupport {
    // 下面这条语句表示这一列的name值具有唯一性，不允许插入多个相同name其它值不同的Item
    @Column(unique = true, defaultValue = "unknown")
    private String name;
    private int age;
    private String sex;

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
