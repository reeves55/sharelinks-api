package com.xiaolee.sharelinksApi.controller;

import com.xiaolee.sharelinksApi.common.annotation.Login;
import com.xiaolee.sharelinksApi.common.wrapper.Msg;
import com.xiaolee.sharelinksApi.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/ws")
public class SLWebsocketController {
    @Autowired
    private WebSocketService webSocketService;

    /**
     * 向指定用户发送请求
     */
    @RequestMapping(value = "/send2user",method = RequestMethod.POST)
    @CrossOrigin(value = "*")
    @Login
    public Msg sendToUser(@RequestBody Map<String ,String> params){
        return webSocketService.sendToUser(params);
    }

    /**
     * 向所有订阅主题的用户发送消息(管理员权限功能)
     *
     * @return
     */
    @RequestMapping(value = "/send2all",method = RequestMethod.POST)
    @CrossOrigin(value = "*")
    //@Login(type = 1) // 要求必须是管理员登陆
    public Msg sendToAll(@RequestBody Map<String ,String> params){
        return webSocketService.sendToAll(params);
    }
}
