package com.fqz.common;

import com.fqz.exception.RedisException;
import com.fqz.model.Result;

/**
 * Author: qianzhong.fu
 * Date: 2015/2/9
 * Time: 17:17
 */
public class RedisExceptionHandler extends ExceptionHandler {

    @Override
    public Result handleException(Exception ex) {
        Result result = new Result();
        result.setCode(-400);
        result.setMessage("error processing redis : "+ex.getMessage());
        return result;
    }
    @Override
     public Class<? extends Throwable> getExceptionClass() {
        return RedisException.class;
    }
}
