package com.paraview.oauth.config;

import com.paraview.oauth.client.ClientContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig implements InitializingBean{

    @Bean
    public ClientContext clientContext(){
        return new ClientContext();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 构建客户端应用
        clientContext()
                .create()
                    .setClientId("zhangsan")
                    .setClientSecret("123")
                    .setAuthorizeType("password,authorization_code")
                .and()
                .create()
                    .setClientId("lisi")
                    .setClientSecret("456")
                    .setAuthorizeType("password")
                .and();
    }

}
