package com.paraview.oauth.enums;

public enum AuthType {

    BASIC("Basic"),

    BEARER("Bearer");

    AuthType(String name){
        this.name = name;
    }

    private String name;

    public String value(){
        return this.name;
    }
}
