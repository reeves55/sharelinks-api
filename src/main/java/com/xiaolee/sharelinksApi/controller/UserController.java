package com.xiaolee.sharelinksApi.controller;

import com.xiaolee.sharelinksApi.common.annotation.Login;
import com.xiaolee.sharelinksApi.common.wrapper.Msg;
import com.xiaolee.sharelinksApi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 检测登陆信息是否失效
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/user/detect",method = RequestMethod.POST)
    @CrossOrigin(value = "*")
    @Login
    public Msg detect(@RequestBody Map<String,String> params) {
        return new Msg(100,"","success");
    }

    /**
     * 登陆
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/user/login",method = RequestMethod.POST)
    @CrossOrigin(value = "*")
    public Msg login(@RequestBody Map<String,String> params) {
        Msg msg = userService.login(params);
        return msg;
    }

    /**
     * 注册
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/user/register",method = RequestMethod.POST)
    @CrossOrigin(value = "*")
    public Msg register(@RequestBody Map<String,String> params) {
        Msg msg =  userService.register(params);
        return msg;
    }

    /**
     * 注销登陆
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/user/logout")
    @CrossOrigin(value = "*")
    @Login
    public Msg logout(@RequestBody Map<String,String> params) {
        return userService.logout(params);
    }

    /**
     * 检查用户名是否重复
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/user/check",method = RequestMethod.POST)
    @CrossOrigin(value = "*")
    public Msg check(@RequestBody Map<String,Object> params) {
        return userService.check(params);
    }

    /**
     * 查看所有当前在线用户
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/user/online",method = RequestMethod.POST)
    @CrossOrigin(value = "*")
    @Login(type = 1)
    public Msg onlineUsers(@RequestBody Map<String,Object> params) {
        return userService.onlineUsers();
    }

    /**
     * 获取所有用户
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/user/all")
    @CrossOrigin(value = "*")
    @Login(type = 1)
    public Msg allUsersWithStatus(@RequestBody Map<String,Object> params) {
        return  userService.allUsers();
    }

    /**
     * 验证管理员登陆
     *
     * @return
     */
    @RequestMapping(value = "/admin/login")
    @CrossOrigin(value = "*")
    @Login(type = 1)
    public Msg adminLogin(@RequestBody Map<String,Object> params) {
        return new Msg(100,"","success");
    }

    /**
     * 获取用户收藏标签
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/user/getcoltags", method = RequestMethod.POST)
    @CrossOrigin(value = "*")
    @Login
    public Msg getcoltags(@RequestBody Map<String,String> params) {
        return userService.getcoltags(params);
    }

    /**
     * 更新用户收藏标签
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/user/updatecoltags", method = RequestMethod.POST)
    @CrossOrigin(value = "*")
    @Login
    public Msg updatecoltags(@RequestBody Map<String,String> params){
        return userService.updatecoltags(params);
    }
}
