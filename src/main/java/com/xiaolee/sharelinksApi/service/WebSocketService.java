package com.xiaolee.sharelinksApi.service;

import com.xiaolee.sharelinksApi.common.wrapper.Msg;
import com.xiaolee.sharelinksApi.domain.Notification;
import com.xiaolee.sharelinksApi.domain.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WebSocketService {
    private static String SINGLE_USER_TOPIC = "/topic/single";
    private static String ORDINARY_TOPIC = "/topic/all";

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private NotificationRepository notificationRepository;

    /**
     * 向指定用户发送websocket消息
     *
     * @param params
     * @return
     */
    @CacheEvict(value = {"notifyCache"}, key = "#params?.get(to)", allEntries = true)
    public Msg sendToUser(Map<String, String> params) {
        String origin = params.get("from");
        String name = params.get("to");
        String content = params.get("content");
        // 在数据库当中存储消息记录
        Notification notification = new Notification();
        notification.setFtype(origin == "system" ? "系统通知" : "回复");
        notification.setForigin(origin);
        notification.setFto(name);
        notification.setFcontent(content);
        notification.setFenable("Y");
        notificationRepository.save(notification);

        sendMessage(name, content);

        return new Msg(100, "", "success");
    }

    /**
     * 发送websocket通知消息
     *
     * @param user
     * @param payload
     */
    public void sendMessage(String user, Object payload) {
        // 使用websocket进行实时提醒
        simpMessagingTemplate.convertAndSendToUser(user, SINGLE_USER_TOPIC, payload);
    }

    /**
     * 向所有用户发送websocket消息
     *
     * @param params
     * @return
     */
    @CacheEvict(value = {"notifyCache"}, key = "#params?.get(to)", allEntries = true)
    public Msg sendToAll(Map<String, String> params) {
        String content = params.get("content");
        // 在数据库当中存储消息记录
        Notification notification = new Notification();
        notification.setForigin("system");
        notification.setFto("");
        notification.setFtype("系统通知");
        notification.setFcontent(content);
        notification.setFenable("Y");
        notificationRepository.save(notification);

        // 使用websocket进行实时提醒
        simpMessagingTemplate.convertAndSend(ORDINARY_TOPIC, content);
        return new Msg(100, "", "success");
    }
}
