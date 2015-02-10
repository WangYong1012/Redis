package com.fqz.model;

/**
 * Author: qianzhong.fu
 * Date: 2015/2/7
 * Time: 14:17
 */
public enum ResultType {
    Success(0,"Success"),
    INTERNAL_ERROR(-500,"Internal Error");
    private int code;
    private String message;

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
    ResultType(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
