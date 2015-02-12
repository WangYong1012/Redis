package com.fqz.common;

/**
 * Author: qianzhong.fu
 * Date: 2015/2/12
 * Time: 9:23
 */
public class SingletonClass {
    private static SingletonClass sc = new SingletonClass();
    private SingletonClass(){
    }
    public static SingletonClass getInstance(){
        return SingletonInnerClass.sc;
    }

    private static class SingletonInnerClass{
        private static SingletonClass sc = new SingletonClass();
    }

}
