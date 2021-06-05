package com.paraview;

import cn.hutool.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

public class MyTest {

    public static void main(String[] args) {
        Map param = new HashMap();
        param.put("username", "user1");
        param.put("grant_type", "password");
        param.put("password", "000000");
        long t1 = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            HttpRequest.post("http://localhost:9999/oauth/token")
                    .header("Authorization", "Basic emhhbmdzYW46MTIz")
                    .form(param)
                    .execute()
                    .body();
        }
        long t2 = System.nanoTime();
        System.out.println("耗时:" + (t2 - t1));
    }

}
