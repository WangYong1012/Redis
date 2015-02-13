package com.fqz.common;

import com.fqz.model.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: qianzhong.fu
 * Date: 2015/2/13
 * Time: 11:10
 * PersonBuilder 是用来构建Person对象的。
 * Builder模式，用来构造复杂的对象，复杂可以理解为
 * 对象属性较多、对象的构建过程叫多等。
 * 构建过程均可以在build()方法中进行处理。
 */
public class PersonBuilder {
    private int age;
    private String name;
    private short sex;
    private double height;
    private List<String> favors = new ArrayList<>();

    public PersonBuilder setAge(int age) {
        this.age = age;
        return this;
    }

    public PersonBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PersonBuilder setSex(short sex) {
        this.sex = sex;
        return this;
    }

    public PersonBuilder setHeight(double height) {
        this.height = height;
        return this;
    }

    public PersonBuilder addFavor(String favor) {
        this.favors.add(favor);
        return this;
    }

    public Person build(){
        return new Person(this.age,this.name,this.sex,this.height,this.favors);
    }
}
