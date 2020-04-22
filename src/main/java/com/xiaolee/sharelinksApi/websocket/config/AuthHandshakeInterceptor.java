package com.xiaolee.sharelinksApi.websocket.config;

import com.xiaolee.sharelinksApi.common.cache.OnlineStatusCache;
import com.xiaolee.sharelinksApi.common.cache.UserLoginCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class AuthHandshakeInterceptor implements HandshakeInterceptor{
    @Autowired
    private UserLoginCache userLoginCache;
    @Autowired
    private OnlineStatusCache onlineStatusCache;
    /**
     * 自定义握手拦截策略
     * 在握手前判断，判断当前用户是否已经登录。如果未登录，则不允许进行websocket连接
     *
     * @param request
     * @param response
     * @param wsHandler
     * @param attributes
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        System.out.println("beforeHandshake...");

        // 验证用户已经登陆
        if (request instanceof ServletServerHttpRequest) {
            String username = ((ServletServerHttpRequest)request).getServletRequest().getParameter("username");
            String token = ((ServletServerHttpRequest)request).getServletRequest().getParameter("token");

            if (username==null || token==null) {
                return false;
            }

            if (null != userLoginCache.get(username)) {
                if (!token.equals((String) userLoginCache.get(username).getData())) {
                    return false;
                }
            } else {
                return false;
            }
        }else {
            return false;
        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        String username = ((ServletServerHttpRequest)request).getServletRequest().getParameter("username");
        // 创建用户登陆缓存
        onlineStatusCache.add(username,((ServletServerHttpRequest)request).getServletRequest().getHeader("User-Agent"));
        System.out.println("afterHandshake...");
    }
}
