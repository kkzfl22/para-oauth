package com.paraview.oauth.context;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.paraview.oauth.bean.Token;
import com.paraview.oauth.client.ClientApp;
import com.paraview.oauth.enums.AuthType;
import com.paraview.oauth.enums.AuthorizeType;
import com.paraview.oauth.exception.AuthException;

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
        if (ObjectUtil.isEmpty(auth)) {
            throw new AuthException("client校验失败，没有header头信息");
        }
        if (!auth.toLowerCase().startsWith(AuthType.BASIC.value().toLowerCase())) {
            throw new AuthException("认证模式不对，需要使用:" + AuthType.BASIC.value());
        }
        String[] data = auth.split(String.valueOf(StrUtil.C_SPACE));
        if (data.length < 2) {
            throw new AuthException("client校验失败，没有client信息");
        }
        String clientStr = Base64.decodeStr(data[1]);
        if (!clientStr.contains(StrUtil.COLON)) {
            throw new AuthException("client格式不正确!");
        }
        String[] clientInfo = clientStr.split(StrUtil.COLON);
        ClientApp clientApp = clients.get(clientInfo[0]);
        if (clientApp == null) {
            throw new AuthException("客户端不存在,请联系管理员!");
        }
        if (!clientInfo[1].equals(clientApp.getClientSecret())) {
            throw new AuthException("认证失败,密码错误!");
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
        if (ObjectUtil.isEmpty(authorizeType)) {
            throw new AuthException("授权类型不能为空");
        }
        for (String type : authorizeType.split(String.valueOf(StrUtil.C_COMMA))) {
            if (!ALL_SUPPORT_TYPE.contains(type)) {
                throw new AuthException(String.format("不支持[%s]这种类型", type));
            }
        }
        return true;
    }

    public ClientApp getClient(String clientId) {
        if(ObjectUtil.isEmpty(clientId))
            return null;
        return clients.get(clientId);
    }

}
