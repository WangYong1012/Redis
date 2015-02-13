package com.fqz.model;

import java.util.List;

/**
 * Author: qianzhong.fu
 * Date: 2015/2/13
 * Time: 11:00
 */
public class Person {
    private int age;
    private String name;
    private short sex;
    private double height;
    private List<String> favors;
    public Person(){}

    public Person(int age, String name, short sex, double height, List<String> favors) {
        this.age = age;
        this.name = name;
        this.sex = sex;
        this.height = height;
        this.favors = favors;
    }

}
