package com.xiaolee.sharelinksApi.common.cache;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Scope(value = "singleton")
public class OnlineStatusCache {
    // 存储所有在线用户状态
    private Map<String,Status> statusMap;

    public OnlineStatusCache() {
        statusMap = new ConcurrentHashMap<>();
    }

    // 获取所有在线用户名
    public List<String> onlineUsers() {
        List<String> onlineUserList = new ArrayList<>();
        for (String key: statusMap.keySet()) {
                onlineUserList.add(key);
        }

        return onlineUserList;
    }

    // 添加在线用户状态
    public void add(String key,String userAgent) {
        statusMap.put(key,new Status(new Date().getTime(),userAgent));
    }

    // 获取在线用户状态
    public Status get(String key) {
        return statusMap.get(key);
    }

    // 删除用户在线状态
    public void delete(String key) {
        statusMap.remove(key);
    }
}

class Status {
    private Long timestamp;
    private String userAgent;

    public Status(Long timestamp, String userAgent) {
        this.timestamp = timestamp;
        this.userAgent = userAgent;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
