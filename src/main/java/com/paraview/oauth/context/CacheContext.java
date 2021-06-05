package com.paraview.oauth.context;

import com.paraview.oauth.bean.User;
import com.paraview.oauth.infrastructure.collection.CacheKeyValue;

public class CacheContext {

    private static CacheKeyValue<String, User> userCache;

    private static CacheKeyValue<String, User> tokenCache;

    private static CacheKeyValue<String, User> codeCache;

    public static void init() {
        userCache = new CacheKeyValue(".user.buffMap");
        tokenCache = new CacheKeyValue(".token.buffMap");
        codeCache = new CacheKeyValue(".code.buffMap");
    }

    public static CacheKeyValue<String, User> getUserCache() {
        return userCache;
    }

    public static CacheKeyValue<String, User> getTokenCache() {
        return tokenCache;
    }

    public static CacheKeyValue<String,User> getCodeCache(){
        return codeCache;
    }

    public static void save() {
        if (userCache != null){
            userCache.defaultSave();
        }
        if (tokenCache != null) {
            tokenCache.defaultSave();
        }
        if (codeCache != null) {
            codeCache.defaultSave();
        }
    }

}
