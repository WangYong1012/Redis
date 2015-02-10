package com.fqz.exception;

/**
 * Author: qianzhong.fu
 * Date: 2015/2/9
 * Time: 16:14
 */
public class RedisException extends Exception {
    private int code;
    public RedisException(String message){
        super(message);
        this.code = 500;
    }
    public RedisException(int code,String message){
        super(message);
        this.code = code;
    }
}
