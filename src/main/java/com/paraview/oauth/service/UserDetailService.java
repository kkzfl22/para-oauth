package com.paraview.oauth.service;

import com.paraview.oauth.bean.User;
import com.paraview.oauth.context.CacheContext;
import org.springframework.stereotype.Service;


@Service
public class UserDetailService {

    public User laodUserByUsername(String username){
        if(username == null){
            return null;
        }
        return CacheContext.getUserCache().get(username);
    }

}
