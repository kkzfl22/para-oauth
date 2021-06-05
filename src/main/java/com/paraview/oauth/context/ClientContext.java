package com.paraview.oauth.context;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.paraview.oauth.client.ClientApp;
import com.paraview.oauth.enums.AuthType;
import com.paraview.oauth.enums.AuthorizeType;
import com.paraview.oauth.exception.AuthException;
import com.paraview.oauth.utils.StringUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClientContext {

    private static Map<String, ClientApp> clients = new HashMap<>();

    private static Set<String> ALL_SUPPORT_TYPE = new HashSet<>();

    static {
        ALL_SUPPORT_TYPE.add(AuthorizeType.PASSWORD.value());
        ALL_SUPPORT_TYPE.add(AuthorizeType.AUTHORIZATION_CODE.value());
    }

    public ClientApp create() {
        return new ClientApp(this);
    }

    public void putContext(String clientId, ClientApp clientApp) {
        clients.put(clientId, clientApp);
    }

    public ClientApp checkHeader(String auth) {
        if (auth == null) {
            throw new AuthException("client check error,no header");
        }
        if (!StringUtil.startWith(auth,AuthType.BASIC.value())) {
            throw new AuthException("please use :" + AuthType.BASIC.value());
        }
        String[] data = auth.split(String.valueOf(StrUtil.C_SPACE));
        if (data.length < 2) {
            throw new AuthException("client check error,no client");
        }
        String clientStr = Base64.decodeStr(data[1]);
        if (!clientStr.contains(StrUtil.COLON)) {
            throw new AuthException("client format error!");
        }
        String[] clientInfo = clientStr.split(StrUtil.COLON);
        ClientApp clientApp = clients.get(clientInfo[0]);
        if (clientApp == null) {
            throw new AuthException("not exists client!");
        }
        if (!clientInfo[1].equals(clientApp.getClientSecret())) {
            throw new AuthException("client password error!");
        }
        return clientApp;
    }

    public boolean checkAuth(ClientApp clientApp, String authType) {
        if (clientApp == null || authType == null || clientApp.getAuthorizeType() == null) {
            return false;
        }
        for (String authorizeType : clientApp.getAuthorizeType().split(String.valueOf(StrUtil.C_COMMA))) {
            if (authorizeType.equalsIgnoreCase(authType)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkType(String authorizeType) {
        if (authorizeType == null) {
            throw new AuthException("authorizeType can not be null");
        }
        for (String type : authorizeType.split(String.valueOf(StrUtil.C_COMMA))) {
            if (!ALL_SUPPORT_TYPE.contains(type)) {
                throw new AuthException(String.format("no support [%s]", type));
            }
        }
        return true;
    }

    public ClientApp getClient(String clientId) {
        if (clientId == null) {
            return null;
        }
        return clients.get(clientId);
    }

}
