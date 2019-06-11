package com.ggec.uitest.ui.jdkversion;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.ggec.uitest.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * 测试JDK1.8之后开始增加的Lambda和Stream功能
 * */

@RequiresApi(api = Build.VERSION_CODES.N)
public class LambdaActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = "LambdaActivity";

    List<Person> javaProgrammers = new ArrayList<Person>() {
        {
            add(new Person("Elsdon", "Jaycob", "Java programmer", "male", 43, 2000));
            add(new Person("Tamsen", "Brittany", "Java programmer", "female", 23, 1500));
            add(new Person("Floyd", "Donny", "Java programmer", "male", 33, 1800));
            add(new Person("Sindy", "Jonie", "Java programmer", "female", 32, 1600));
            add(new Person("Vere", "Hervey", "Java programmer", "male", 22, 1200));
            add(new Person("Maude", "Jaimie", "Java programmer", "female", 27, 1900));
            add(new Person("Shawn", "Randall", "Java programmer", "male", 30, 2300));
            add(new Person("Jayden", "Corrina", "Java programmer", "female", 35, 1700));
            add(new Person("Palmer", "Dene", "Java programmer", "male", 33, 2000));
            add(new Person("Addison", "Pam", "Java programmer", "female", 34, 1300));
        }
    };

    List<Person> phpProgrammers = new ArrayList<Person>() {
        {
            add(new Person("Jarrod", "Pace", "PHP programmer", "male", 34, 1550));
            add(new Person("Clarette", "Cicely", "PHP programmer", "female", 23, 1200));
            add(new Person("Victor", "Channing", "PHP programmer", "male", 32, 1600));
            add(new Person("Tori", "Sheryl", "PHP programmer", "female", 21, 1000));
            add(new Person("Osborne", "Shad", "PHP programmer", "male", 32, 1100));
            add(new Person("Rosalind", "Layla", "PHP programmer", "female", 25, 1300));
            add(new Person("Fraser", "Hewie", "PHP programmer", "male", 36, 1100));
            add(new Person("Quinn", "Tamara", "PHP programmer", "female", 21, 1000));
            add(new Person("Alvin", "Lance", "PHP programmer", "male", 38, 1600));
            add(new Person("Evonne", "Shari", "PHP programmer", "female", 40, 1800));
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lambda);
        findViewById(R.id.btn_lambda_simple).setOnClickListener(this);
        findViewById(R.id.btn_lambda_fun1).setOnClickListener(this);
        findViewById(R.id.btn_lambda_fun2).setOnClickListener(this);
        findViewById(R.id.btn_lambda_fun3).setOnClickListener(this);
        findViewById(R.id.btn_lambda_fun4).setOnClickListener(this);
        findViewById(R.id.btn_lambda_fun5).setOnClickListener(this);
        findViewById(R.id.btn_lambda_fun6).setOnClickListener(this);
        findViewById(R.id.btn_lambda_fun7).setOnClickListener(this);
    }

/*    public void simple(View view) {
        simple();
    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_lambda_simple:
                simple();
                break;
            case R.id.btn_lambda_fun1:
                fun1();
                break;
            case R.id.btn_lambda_fun2:
                fun2();
                break;
            case R.id.btn_lambda_fun3:
                fun3();
                break;
            case R.id.btn_lambda_fun4:
                fun4();
                break;
            case R.id.btn_lambda_fun5:
                fun5();
                break;
            case R.id.btn_lambda_fun6:
                fun6();
                break;
            case R.id.btn_lambda_fun7:
                fun7();
                break;
        }
    }

    private void simple( ) {
        String[] atp = {"Rafael Nadal", "Novak Djokovic",
                "Stanislas Wawrinka",
                "David Ferrer","Roger Federer",
                "Andy Murray","Tomas Berdych",
                "Juan Martin Del Potro"};
        List<String> players =  Arrays.asList(atp);
        // 以前的循环方式
        Log.i(TAG,"以前循环输出结果如下：");
        for (String player : players) {
            Log.v(TAG,player);
        }
        Log.i(TAG,"使用Lambda输出结果如下：");
        // 使用 lambda 表达式以及函数操作(functional operation)
        players.forEach((player) -> Log.v(TAG, player));
        // 在 Java 8 中使用双冒号操作符(double colon operator)
        Log.i(TAG,"双冒号操作符输出结果如下：");
        players.forEach(System.out::println);
    }

    private void fun1() {
        Log.i(TAG,"所有Java程序员的姓名：");
        javaProgrammers.forEach((p) -> Log.v(TAG,p.getFirstName() + "," + p.getLastName()));
        Log.i(TAG,"所有Php程序员的姓名：");
        phpProgrammers.forEach((p) -> Log.v(TAG,p.getFirstName() + "," + p.getLastName()));
    }

    private void fun2() {
        Log.i(TAG,"给程序员加薪 5% ：");
        Consumer<Person> giveRaise = e -> e.setSalary(e.getSalary() / 100 * 5 + e.getSalary());
        javaProgrammers.forEach(giveRaise);
        phpProgrammers.forEach(giveRaise);
        Log.i(TAG,"所有Java程序员加薪后的工资变化如下：");
        javaProgrammers.forEach((p) -> Log.v(TAG,p.getFirstName() + "," + p.getSalary()));
        Log.i(TAG,"所有Php程序员加薪后的工资变化如下：");
        phpProgrammers.forEach((p) -> Log.v(TAG,p.getFirstName() + "," + p.getSalary()));
    }

    private void fun3() {
        // 定义 filters
        Predicate<Person> ageFilter = (p) -> (p.getAge() > 25);
        Predicate<Person> salaryFilter = (p) -> (p.getSalary() > 1400);
        Predicate<Person> genderFilter = (p) -> ("female".equals(p.getGender()));

        Log.i(TAG,"下面是年龄大于 24岁且月薪在$1,400以上的女PHP程序员:");
        phpProgrammers.stream()
                .filter(ageFilter)
                .filter(salaryFilter)
                .filter(genderFilter)
                .forEach((p) -> Log.v(TAG, p.getFirstName() + "," +  p.getLastName()));
        // 重用filters
        Log.i(TAG,"年龄大于 24岁的女性 Java programmers:");
        javaProgrammers.stream()
                .filter(ageFilter)
                .filter(genderFilter)
                .forEach((p) -> System.out.printf("%s %s; ", p.getFirstName(), p.getLastName()));
        Log.i(TAG,"最前面的3个 Java programmers:");
        javaProgrammers.stream()
                .limit(3)
                .forEach((p) ->  Log.v(TAG, p.getFirstName() + "," +  p.getLastName()));


        Log.i(TAG,"最前面的3个女性 Java programmers:");
        javaProgrammers.stream()
                .filter(genderFilter)
                .limit(3)
                .forEach((p) -> Log.i(TAG, p.getFirstName() + ","  + p.getLastName()));
    }

    private void fun4() {
        Log.i(TAG,"根据name排序,并显示前5个Java programmers:");
        List<Person> sortedJavaProgrammers = javaProgrammers
                .stream()
                .sorted((p, p2) -> (p.getFirstName().compareTo(p2.getFirstName())))
                .limit(5)
                .collect(toList());
        sortedJavaProgrammers.forEach((p) -> Log.v(TAG,p.getFirstName() + "," + p.getLastName()));
        Log.i(TAG,"根据salary排序Java programmers:");
        sortedJavaProgrammers = javaProgrammers
                .stream()
                .sorted( (p, p2) -> (p.getSalary() - p2.getSalary()) )
                .collect( toList() );
        sortedJavaProgrammers.forEach((p) -> Log.v(TAG,p.getFirstName() + "," + p.getLastName()));
    }

    private void fun5() {
        Log.i(TAG, "工资最低的 Java programmer:");
        Person pers = javaProgrammers
                .stream()
                .min((p1, p2) -> (p1.getSalary() - p2.getSalary()))
                .get();
        Log.v(TAG, pers.getFirstName() + pers.getLastName() + "," + pers.getSalary());
        Log.i(TAG,"工资最高的 Java programmer:");
        Person person = javaProgrammers
                .stream()
                .max((p, p2) -> (p.getSalary() - p2.getSalary()))
                .get();
        Log.v(TAG, pers.getFirstName() + pers.getLastName() + "," + pers.getSalary());
    }

    private void fun6() {
        String phpDevelopers = phpProgrammers
                .stream()
                .map(Person::getFirstName)
                .collect(joining(" ; ")); // 在进一步的操作中可以作为标记(token)
        Log.i(TAG,"将 PHP programmers 的 first name 拼接成字符串：" + phpDevelopers);
        Set<String> javaDevFirstName = javaProgrammers
                .stream()
                .map(Person::getFirstName)
                .collect(toSet());
        Log.i(TAG,"将 Java programmers 的 first name 存放到 Set：" + javaDevFirstName);
        TreeSet<String> javaDevLastName = javaProgrammers
                .stream()
                .map(Person::getLastName)
                .collect(toCollection(TreeSet::new));
        Log.i(TAG,"将 Java programmers 的 first name 存放到 TreeSet：" + javaDevLastName);
    }

    private void fun7() {
        //计算 count, min, max, sum, and average for numbers
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        IntSummaryStatistics stats = numbers
                .stream()
                .mapToInt((x) -> x)
                .summaryStatistics();
        Log.v(TAG,"List中最大的数字 : " + stats.getMax());
        Log.v(TAG,"List中最小的数字 : " + stats.getMin());
        Log.v(TAG,"所有数字的总和   : " + stats.getSum());
        Log.v(TAG,"所有数字的平均值 : " + stats.getAverage());
    }
}
