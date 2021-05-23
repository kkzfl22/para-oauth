package com.paraview.oauth.bean;

public class R {

    private String code;
    private String error;

    public R(String code, String error) {
        this.code = code;
        this.error = error;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
