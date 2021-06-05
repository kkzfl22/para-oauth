package com.paraview.oauth.web;

import com.paraview.oauth.bean.OAuthReq;
import com.paraview.oauth.bean.Token;
import com.paraview.oauth.service.OAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/oauth")
public class OAuthContorller {

    private OAuthService oAuthService;

    public OAuthContorller(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    /**
     * 密码模式、授权码模式认证接口
     */
    @PostMapping("/token")
    public Mono<Token> token(OAuthReq req, ServerWebExchange exchange) {
        return Mono.fromSupplier(() ->{
            req.setAuthorization(exchange.getRequest().getHeaders().getFirst("Authorization"));
            Token token = oAuthService.doAuth(req);
            if (token != null) {
                ResponseCookie responseCookie = ResponseCookie.from("oauth-token", token.getAccess_token()).build();
                exchange.getResponse().addCookie(responseCookie);
            }
            return token;
        });
    }

    /**
     * 授权码模式获取code接口
     * 授权码模式需要判断当前用户是否登入，如果没有登入需要跳转到登入页面，
     * 这块我们演示场景为：已经有用户登入了，登入信息放入了cookie，我们直观从cookie拿到当前用户的token就行
     */
    @GetMapping("/authorize")
    public Mono<String> authorize(OAuthReq req, ServerWebExchange exchange) {
        return Mono.fromSupplier(() -> {
            //从 cookie获取当前登入信息
            String token = exchange.getRequest().getCookies().getFirst("oauth-token").getValue();
            req.setToken(token);
            String code = oAuthService.makeCode(req);
            exchange.getResponse().setStatusCode(HttpStatus.FOUND);
            exchange.getResponse().getHeaders().setLocation(URI.create(req.getRedirect_uri() + "&code=" + code+"&state=" + req.getState()));
            return null;
        });
    }

    @GetMapping("/reviceCode")
    public void reviceCode(String code) {
        System.out.println("code:" + code);
    }

}
