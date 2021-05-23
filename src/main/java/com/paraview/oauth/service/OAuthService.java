package com.paraview.oauth.service;

import com.paraview.oauth.bean.OAuthReq;
import com.paraview.oauth.bean.Token;
import com.paraview.oauth.client.ClientApp;
import com.paraview.oauth.client.ClientContext;
import com.paraview.oauth.exception.AuthException;
import com.paraview.oauth.service.invoker.AuthInvoker;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OAuthService {

    private List<AuthInvoker> invokers;

    private ClientContext context;

    public OAuthService(List<AuthInvoker> invokers, ClientContext context) {
        this.invokers = invokers;
        this.context = context;
    }

    private AuthInvoker getInvoker(String grantType) {
        for (AuthInvoker authInvoker : invokers) {
            if (authInvoker.grantType().equalsIgnoreCase(grantType)) {
                return authInvoker;
            }
        }
        return null;
    }

    public Token doAuth(OAuthReq req) {
        ClientApp clientApp = context.checkHeader(req.getAuthorization());
        AuthInvoker authInvoker = getInvoker(req.getGrant_type());
        if (authInvoker == null) {
            throw new AuthException("授权类型不正确，请检查grant_type.");
        }
        if (!authInvoker.checkAuth(clientApp)) {
            throw new AuthException("客户端应用无权限访问.");
        }
        return authInvoker.doInvoker(req);
    }

    public String makeCode(OAuthReq req){
        return "123";
    }

}
