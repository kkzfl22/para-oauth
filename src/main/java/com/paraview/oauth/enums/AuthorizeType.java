package com.paraview.oauth.enums;

public enum AuthorizeType {

    /**
     * 密码模式
     * */
    PASSWORD("password"),

    /**
     * 授权码模式
     * */
    AUTHORIZATION_CODE("authorization_code");

    AuthorizeType(String name){
        this.name = name;
    }

    private String name;

    public String value(){
        return this.name;
    }

}
