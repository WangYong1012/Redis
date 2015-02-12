package com.fqz.common;

import com.fqz.model.Result;

/**
 * Author: qianzhong.fu
 * Date: 2015/2/10
 * Time: 9:48
 */
public class DefaultExceptionHandler extends ExceptionHandler  {
    @Override
    public Result handleException(Exception ex) {
        Result result = new Result();
        result.setCode(-500);
        result.setMessage("internal error : " + ex.getMessage() );
        return result;
    }
    @Override
    public Class<? extends Throwable> getExceptionClass() {
        return Exception.class;
    }
}
