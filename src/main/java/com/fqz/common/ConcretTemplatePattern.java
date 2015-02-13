package com.fqz.common;

/**
 * Author: qianzhong.fu
 * Date: 2015/2/13
 * Time: 13:55
 */
public class ConcretTemplatePattern extends TemplatePattern {
    @Override
    protected void start() {
        System.out.println("Start ...");
    }

    @Override
    protected void proceed() {
        System.out.println("Proceeding ...");
    }

    @Override
    protected void end() {
        System.out.println("End ...");
    }
}
