package com.fqz.common;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: qianzhong.fu
 * Date: 2015/2/9
 * Time: 16:42
 * 简单工厂模式，回忆抽象工厂模式
 */
public class ExceptionHandlerFactory {
    private Map<Class<? extends Throwable>,ExceptionHandler> exceptionHandlerMap = new HashMap<>();

    /**
     * 通过Spring bean的注入来实现；
     * 若没有引入Spring，则使用static块来实现初始化。
     * @param handlers
     */
    public void setExceptionHandlerMap(List<ExceptionHandler> handlers){
        exceptionHandlerMap.clear();
        for(ExceptionHandler handler : handlers)
            exceptionHandlerMap.put(handler.getExceptionClass(),handler);

    }

    private ExceptionHandler getExceptionHandler(Exception ex) {
        ExceptionHandler handler = exceptionHandlerMap.get(ex.getClass());
        if(handler == null)
            handler = exceptionHandlerMap.get(Exception.class);
        return handler;
    }

    public Object handleException(Exception ex) {
        return getExceptionHandler(ex).handleException(ex);
    }
}
