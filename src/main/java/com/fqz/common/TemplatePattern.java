package com.fqz.common;

/**
 * Author: qianzhong.fu
 * Date: 2015/2/13
 * Time: 13:47
 * Template模式，应用在方法中包含若干子方法，且子方法严格按照顺序执行。
 * 其中，子方法是可以被rewrite的方法;而父方法则是不允许被rewrite的，因此常常设定为final。
 */
public abstract class TemplatePattern {
    protected abstract void start();
    protected abstract void proceed();
    protected abstract void end();

    public final void action(){
        start();

        proceed();

        end();
    }
}
