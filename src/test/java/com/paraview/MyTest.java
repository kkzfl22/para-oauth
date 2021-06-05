package com.paraview;

import cn.hutool.core.util.StrUtil;
import com.paraview.oauth.context.CacheContext;
import com.paraview.oauth.enums.AuthType;
import com.paraview.oauth.exception.AuthException;

public class MyTest {

    public static void main(String[] args) {

//        CacheContext.init();
//        long s1 = System.nanoTime();
//        CacheContext.getUserCache().get("user1000");
//        long s2 = System.nanoTime();
//        System.out.println("耗时:" + (s2 - s1));
        String auth = "Basic emhhbmdzYW46MTIz";
        long s1 = System.nanoTime();
        if (!auth.startsWith(AuthType.BASIC.value())) {
            throw new AuthException("please use :" + AuthType.BASIC.value());
        }
        long s2 = System.nanoTime();
        System.out.println("耗时:" + (s2 - s1));

        long s3 = System.nanoTime();
        String[] data = auth.split(String.valueOf(StrUtil.C_SPACE));
        if (data.length < 2) {
            throw new AuthException("client check error,no client");
        }
        long s4 = System.nanoTime();
        System.out.println("耗时:" + (s4 - s3));

    }

}
