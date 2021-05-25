package com.paraview.oauth.client;

import com.paraview.oauth.context.ClientContext;

public class ClientApp {

    /**
     * 客户端应用ID
     */
    private String clientId;
    /**
     * 客户端密钥
     */
    private String clientSecret;

    /**
     * 认证模式
     */
    private String authorizeType;

    /**
     * 过期时间
     */
    private long expireTime = 60 * 60 * 1000;

    private ClientContext context;

    public ClientApp(ClientContext context) {
        this.context = context;
    }

    public String getClientId() {
        return clientId;
    }

    public ClientApp setClientId(String clientId) {
        this.clientId = clientId;
        context.putContext(clientId, this);
        return this;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public ClientApp setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public String getAuthorizeType() {
        return authorizeType;
    }

    public ClientApp setAuthorizeType(String authorizeType) {
        this.authorizeType = authorizeType;
        this.context.checkType(authorizeType);
        return this;
    }

    public ClientContext and() {
        return this.context;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public ClientApp setExpireTime(long expireTime) {
        this.expireTime = expireTime;
        return this;
    }

}
