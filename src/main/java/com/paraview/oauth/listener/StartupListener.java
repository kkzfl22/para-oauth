package com.paraview.oauth.listener;

import com.paraview.oauth.context.CacheContext;
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //缓存初始化加载
        CacheContext.init();
        //注册优雅关机钩子
        AnnotationConfigReactiveWebServerApplicationContext context = (AnnotationConfigReactiveWebServerApplicationContext) contextRefreshedEvent.getApplicationContext();
        context.registerShutdownHook();
    }

    @PreDestroy
    public void destroy() {
        //保存文件
        CacheContext.save();
    }

}
