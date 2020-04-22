package com.xiaolee.sharelinksApi.controller;

import com.xiaolee.sharelinksApi.common.annotation.Login;
import com.xiaolee.sharelinksApi.common.wrapper.Msg;
import com.xiaolee.sharelinksApi.service.NotifiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class NotifiController {
    @Autowired
    private NotifiService notifiService;

    /**
     * 拉取用户所有消息
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/notify/pull", method = RequestMethod.POST)
    @CrossOrigin(value = "*")
    @Login
    public Msg pull(@RequestBody Map<String, String> params) {
        return notifiService.pull(params);
    }

    /**
     * 检测用户是否有新的消息
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/notify/detect", method = RequestMethod.POST)
    @CrossOrigin(value = "*")
    @Login
    public Msg detect(@RequestBody Map<String, String> params) {
        return notifiService.detect(params);
    }

    /**
     * 获取所有系统发送的消息
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/notify/system", method = RequestMethod.POST)
    @CrossOrigin(value = "*")
    @Login(type = 1)
    public Msg system(@RequestBody Map<String, String> params) {
        return notifiService.system();
    }

    /**
     * 管理员推送消息
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/notify/push")
    @CrossOrigin(value = "*")
    @Login(type = 1)
    public Msg push(@RequestBody Map<String, String> params) {
        return notifiService.push(params);
    }
}
