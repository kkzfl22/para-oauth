package com.paraview.oauth.service;

import cn.hutool.crypto.SecureUtil;
import com.paraview.oauth.bean.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserDetailService {

    private static final Map<String,User> users = new HashMap<>();

    static {
        users.put("zhangsan",new User("zhangsan", SecureUtil.md5("123")));
        users.put("lisi",new User("lisi", SecureUtil.md5("456")));
    }

    public User laodUserByUsername(String username){
        // 实现用户查询
        return users.get(username);
    }

}
