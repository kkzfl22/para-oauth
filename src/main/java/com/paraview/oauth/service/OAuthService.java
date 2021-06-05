package com.paraview.oauth.service;

import cn.hutool.core.util.ObjectUtil;
import com.paraview.oauth.bean.OAuthReq;
import com.paraview.oauth.bean.Token;
import com.paraview.oauth.bean.User;
import com.paraview.oauth.client.ClientApp;
import com.paraview.oauth.context.CacheContext;
import com.paraview.oauth.context.ClientContext;
import com.paraview.oauth.exception.AuthException;
import com.paraview.oauth.service.invoker.AuthInvoker;
import org.apache.commons.lang3.RandomStringUtils;
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
            throw new AuthException("grant_type error.");
        }
        if (!authInvoker.checkAuth(clientApp)) {
            throw new AuthException("client no permission.");
        }
        return authInvoker.doInvoker(req, clientApp);
    }

    public String makeCode(OAuthReq req) {
        if (ObjectUtil.isEmpty(req.getToken())) {
            throw new AuthException("not login.");
        }
        if (!"code".equalsIgnoreCase(req.getResponse_type())) {
            throw new AuthException("response_type must be code");
        }
        ClientApp clientApp = context.getClient(req.getClient_id());
        if(clientApp == null){
            throw new AuthException("client not exists");
        }
        User user = CacheContext.getTokenCache().get(req.getToken());
        String code = generateCode();
        CacheContext.getCodeCache().put(code, user);
        return code;
    }

    private String generateCode() {
        String code = RandomStringUtils.randomAlphabetic(8);
        User user = CacheContext.getCodeCache().get(code);
        if (user == null) {
            return code;
        }
        return generateCode();
    }

}
