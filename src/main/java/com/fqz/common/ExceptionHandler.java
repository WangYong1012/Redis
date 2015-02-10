package com.fqz.common;

import com.fqz.model.Result;

/**
 * Author: qianzhong.fu
 * Date: 2015/2/9
 * Time: 16:44
 */
public abstract class ExceptionHandler {
    private Class<? extends Throwable> exceptionClass;
    public abstract Result handleException(Exception ex);

    public Class<? extends Throwable> getExceptionClass() {
        return exceptionClass;
    }
}
