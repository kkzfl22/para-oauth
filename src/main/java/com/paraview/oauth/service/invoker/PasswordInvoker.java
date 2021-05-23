package com.paraview.oauth.service.invoker;

import cn.hutool.crypto.SecureUtil;
import com.paraview.oauth.bean.OAuthReq;
import com.paraview.oauth.bean.Token;
import com.paraview.oauth.bean.User;
import com.paraview.oauth.client.ClientApp;
import com.paraview.oauth.client.ClientContext;
import com.paraview.oauth.enums.AuthType;
import com.paraview.oauth.enums.AuthorizeType;
import com.paraview.oauth.exception.AuthException;
import com.paraview.oauth.service.UserDetailService;
import org.springframework.stereotype.Component;

@Component
public class PasswordInvoker implements AuthInvoker {

    private UserDetailService userDetailService;

    private ClientContext clientContext;

    public PasswordInvoker(UserDetailService userDetailService, ClientContext context) {
        this.userDetailService = userDetailService;
        this.clientContext = context;
    }


    @Override
    public String grantType() {
        return AuthorizeType.PASSWORD.value();
    }

    @Override
    public Token doInvoker(OAuthReq req) {
        User user = userDetailService.laodUserByUsername(req.getUsername());
        if (user == null) {
            throw new AuthException("用户不存在.");
        }
        if (!SecureUtil.md5(req.getPassword()).equals(user.getPassword())) {
            throw new AuthException("密码错误.");
        }
        return new Token(AuthType.BEARER.value(), 3600);
    }

    @Override
    public boolean checkAuth(ClientApp clientApp) {
        return clientContext.checkAuth(clientApp, grantType());
    }

}
