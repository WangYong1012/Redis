package com.fqz.interceptor;

import com.fqz.common.ExceptionHandlerFactory;
import com.fqz.model.Result;
import com.fqz.model.ResultType;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Author: qianzhong.fu
 * Date: 2015/2/6
 * Time: 17:50
 */
@Component
public class ExceptionInterceptor implements MethodInterceptor {
    @Autowired
    ExceptionHandlerFactory exceptionHandlerFactory;
    public Object afterHandler(Object responseData){
        Result result = new Result(ResultType.Success);
        result.setData(responseData);
        return result;
    }

    @Override
     public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        try {
            Object data = methodInvocation.proceed();
            return afterHandler(data);
        }catch (Exception ex){
            return exceptionHandlerFactory.handleException(ex);
        }
    }

}
