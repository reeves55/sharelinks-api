package com.xiaolee.sharelinksApi.websocket.config;

import com.xiaolee.sharelinksApi.common.cache.OnlineStatusCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {
    @Autowired
    private OnlineStatusCache onlineStatusCache;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        System.out.println(event.getUser().getName()+" connection established...");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String username = event.getUser().getName();
        onlineStatusCache.delete(username); // 删除缓存中用户登陆状态
        System.out.println(event.getUser().getName()+" disconnected...");
    }
}
