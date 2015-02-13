package com.fqz;


import com.fqz.common.ConcretTemplatePattern;
import com.fqz.common.PersonBuilder;
import com.fqz.common.TemplatePattern;
import com.fqz.model.Person;

/**
 * Author: qianzhong.fu
 * Date: 2015/2/13
 * Time: 11:19
 */

public class Main {
    public static void main(String[] args) {
        PersonBuilder builder = new PersonBuilder();
        Person person = builder.setAge(25).setName("fqz")
                .setHeight(1.75).setSex((short) 1).addFavor("123").addFavor("343434").build();

        TemplatePattern template = new ConcretTemplatePattern();
        template.action();
    }
}
