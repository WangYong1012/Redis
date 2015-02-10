package com.fqz.model;

/**
 * Author: qianzhong.fu
 * Date: 2015/2/7
 * Time: 13:25
 */
public class Result {
    private int code;
    private String message;
    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Result(){
    }

    public Result(ResultType resultType){
        this.code = resultType.getCode();
        this.message = resultType.getMessage();
    }
}
