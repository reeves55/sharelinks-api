package com.xiaolee.sharelinksApi.service;

import com.xiaolee.sharelinksApi.common.cache.NotifyStatusCache;
import com.xiaolee.sharelinksApi.common.wrapper.Msg;
import com.xiaolee.sharelinksApi.domain.Notification;
import com.xiaolee.sharelinksApi.domain.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NotifiService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotifyStatusCache notifyStatusCache;
    @Autowired
    private WebSocketService webSocketService;

    /**
     * 拉取用户通知
     *
     * @param params
     * @return
     */
    public Msg pull(Map<String, String> params) {
        String username = params.get("username");
        notifyStatusCache.update(username, new Date().getTime()); // 更新用户通知浏览时间缓存
        List<Notification> notificationList = notificationRepository.findByFtoInAndFenableOrderByFaddedTimeDesc(Arrays.asList("", username), "Y");
        return new Msg(100, notificationList, "success");
    }

    /**
     * 检测用户是否有新消息
     *
     * @param params
     * @return
     */
    public Msg detect(Map<String, String> params) {
        String username = params.get("username");
        Notification notification = notificationRepository.findFirstByFtoAndFenableOrderByFaddedTimeDesc(username, "Y");
        if (notification != null) {
            // 从缓存当中获取用户最后一次浏览通知的时间，并对比最新消息的时间
            if (notifyStatusCache.get(username) < notification.getFaddedTime().getTime()) {
                return new Msg(100, true, "success");
            }
        }

        return new Msg(100, false, "success");
    }

    /**
     * 获取所有系统发送的消息
     *
     * @return
     */
    public Msg system() {
        List<Notification> systemNotifi = notificationRepository.findByFtypeAndFenableOrderByFaddedTimeDesc("系统通知", "Y");
        return new Msg(100, systemNotifi, "success");
    }

    /**
     * 管理员推送消息
     *
     * @return
     */
    @CacheEvict(value = {"notifyCache"}, key = "#params?.get(to)", allEntries = true)
    public Msg push(Map<String, String> params) {
        String to = params.get("to");
        String content = params.get("content");

        Notification notification = new Notification();
        notification.setFtype("系统通知");
        notification.setFto(to);
        notification.setFcontent(content);
        notification.setForigin("system");
        notification.setFenable("Y");

        notificationRepository.save(notification);

        // 通过websocket实时向用户发送通知
        webSocketService.sendMessage(to, content);

        return new Msg(100, notification.getFid(), "success");
    }
}
