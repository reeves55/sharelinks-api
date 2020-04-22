package com.xiaolee.sharelinksApi.websocket.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

// 开始执行websocket握手
@Component
public class SLPrincipalHandshakeHandler extends DefaultHandshakeHandler {
    /**
     * 唯一标志一个用户，使用在@SendToUser中
     *
     * @param request
     * @param wsHandler
     * @param attributes
     * @return
     */
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        System.out.println("determineUser...");
        String username = ((ServletServerHttpRequest)request).getServletRequest().getParameter("username");

        // 设置新的用户标志
        return new SLPrincipal(username);
    }
}
