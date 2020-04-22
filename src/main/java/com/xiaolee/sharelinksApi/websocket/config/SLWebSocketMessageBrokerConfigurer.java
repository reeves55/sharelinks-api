package com.xiaolee.sharelinksApi.websocket.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

// websocket的整体配置
@Configuration
@EnableWebSocketMessageBroker
public class SLWebSocketMessageBrokerConfigurer implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private AuthHandshakeInterceptor authHandshakeInterceptor;
    @Autowired
    private SLPrincipalHandshakeHandler slPrincipalHandshakeHandler;

    /**
     *
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        System.out.println("registerStompEndpoints...");
        registry.addEndpoint("/ws/notify") // 客户端连接地址
                .addInterceptors(authHandshakeInterceptor)
                .setHandshakeHandler(slPrincipalHandshakeHandler)
                .setAllowedOrigins("*")
                .withSockJS(); // 指定端点使用SockJS协议
    }

    /**
     *
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        System.out.println("configureMessageBroker...");
        registry.setApplicationDestinationPrefixes("/ws");

    }
}
