package com.xiaolee.sharelinksApi.service;

import com.xiaolee.sharelinksApi.common.cache.CacheObject;
import com.xiaolee.sharelinksApi.common.cache.OnlineStatusCache;
import com.xiaolee.sharelinksApi.common.cache.UserLoginCache;
import com.xiaolee.sharelinksApi.common.util.security.JWTokenUtil;
import com.xiaolee.sharelinksApi.common.util.security.SHA1;
import com.xiaolee.sharelinksApi.common.wrapper.Msg;
import com.xiaolee.sharelinksApi.domain.CollectionTags;
import com.xiaolee.sharelinksApi.domain.CollectionTagsRepository;
import com.xiaolee.sharelinksApi.domain.User;
import com.xiaolee.sharelinksApi.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserLoginCache userLoginCache;
    @Autowired
    private OnlineStatusCache onlineStatusCache;
    @Autowired
    private CollectionTagsRepository collectionTagsRepository;

    /**
     * 用户登陆
     *
     * @param params
     * @return
     */
    public Msg login(Map<String, String> params) {
        // 如果含有用户名和token，则使用token进行验证
        if (params.get("username") != null && params.get("token") != null) {
            String username = params.get("username");
            String token = params.get("token");

            CacheObject cacheObject = userLoginCache.get(username);
            // 如果缓存中存在用户对应token，并且用户提供的token和缓存中的一致，则登陆成功
            if (cacheObject != null && token.equals((String) cacheObject.getData())) {
                return new Msg(110, "", "success");
            }
        }

        // 如果使用用户名密码验证
        if (null == params.get("username") || null == params.get("password") || null == params.get("verifyCode")) {
            return new Msg(-120, "", "参数错误");
        }

        String username = params.get("username");
        String password = params.get("password").toUpperCase();
        String verifyCode = params.get("verifyCode");

        List<User> users = userRepository.findByFname(username);
        if (users.size() != 1) {
            return new Msg(-121, "", "参数错误");
        }

        User user = users.get(0);
        // 验证用户提供的密码和数据库中是否一致
        if (!password.equals(SHA1.sha1(user.getFpassword() + verifyCode))) {
            return new Msg(-122, "", "用户名或密码错误");
        }

        JWTokenUtil jwTokenUtil = new JWTokenUtil(username, 7 * 24 * 60 * 60);
        String token = jwTokenUtil.getToken();
        Msg msg = new Msg(111, token, "success");
        userLoginCache.push(username, new Date().getTime() + 7 * 24 * 60 * 60 * 1000, token);

        return msg;
    }

    /**
     * 用户注册
     *
     * @param params
     * @return
     */
    public Msg register(Map<String, String> params) {
        if (null == params.get("username") || null == params.get("password")) {
            return new Msg(-110, "", "参数错误");
        }

        String username = params.get("username");
        String password = params.get("password");

        List<User> users = userRepository.findByFname(username);
        if (users.size() > 1) {
            return new Msg(-123, "", "用户已存在");
        }

        User user = new User();
        user.setFname(username);
        user.setFpassword(password);
        user.setFenable("Y");
        user.setFtype(0); // 普通用户
        // 新建用户
        userRepository.save(user);

        // 新建用户收藏标签
        CollectionTags collectionTags = new CollectionTags();
        collectionTags.setFuser(username);
        collectionTagsRepository.save(collectionTags);

        JWTokenUtil jwTokenUtil = new JWTokenUtil(username, 7 * 24 * 60 * 60);
        String token = jwTokenUtil.getToken();
        Msg msg = new Msg(111, token, "success");
        userLoginCache.push(username, new Date().getTime() + 7 * 24 * 60 * 60, token);

        return msg;
    }

    /**
     * 注销登陆
     *
     * @param params
     * @return
     */
    public Msg logout(Map<String, String> params) {
        String username = params.get("username");

        userLoginCache.remove(username);
        return new Msg(112, "", "success");
    }

    /**
     * 检查用户名是否存在
     *
     * @param params
     * @return
     */
    public Msg check(Map<String, Object> params) {
        if (params.get("username") == null) {
            return new Msg(-121, "", "参数错误");
        }

        String username = (String) params.get("username");
        List<User> users = userRepository.findByFname(username);
        if (users.size() > 0) {
            return new Msg(-122, "", "用户已存在");
        } else {
            return new Msg(130, "", "success");
        }
    }

    /**
     * 获取所有在线用户的用户名列表
     *
     * @return
     */
    public Msg onlineUsers() {
        return new Msg(100, onlineStatusCache.onlineUsers(), "success");
    }

    /**
     * 获取所有用户
     *
     * @return
     */
    public Msg allUsers() {
        List<User> users = userRepository.findAll();
        List<Map<String, String>> result = new ArrayList<>();

        for (User user : users) {
            Map<String, String> item = new HashMap<>();

            item.put("name", user.getFname());
            item.put("status", userLoginCache.get(user.getFname()) == null ? "" : "(在线)");

            result.add(item);
        }

        return new Msg(100, result, "success");
    }

    /**
     * 获取用户收藏标签
     *
     * @param params
     * @return
     */
    public Msg getcoltags(Map<String, String> params) {
        List<CollectionTags> collectionTagsList = collectionTagsRepository.findByFuserAndFenableOrderByFupdateTimeDesc(params.get("username"), "Y");

        if (collectionTagsList.size() < 1) {
            return new Msg(100, Arrays.asList(), "success");
        } else {
            List<String> tags = new ArrayList<>();
            for (CollectionTags collectionTags : collectionTagsList) {
                tags.add(collectionTags.getFtag());
            }

            return new Msg(100, tags, "success");
        }
    }

    /**
     * 更新用户收藏标签
     *
     * @param params
     * @return
     */
    public Msg updatecoltags(Map<String, String> params) {
        List<String> tagList = Arrays.asList(params.get("tags").split(";"));
        List<CollectionTags> collectionTagsList = collectionTagsRepository.findByFuserAndFenableOrderByFupdateTimeDesc(params.get("username"), "Y");

        if (collectionTagsList.size() < 1) {
            return new Msg(-100, "", "record not found");
        } else {
            List<String> dbTags = new ArrayList<>();
            // 删除已有标签
            for (CollectionTags item : collectionTagsList) {
                dbTags.add(item.getFtag());
                if (!tagList.contains(item.getFtag())) {
                    item.setFenable("N");
                    collectionTagsRepository.save(item);
                }
            }

            // 增添新的标签
            for (String newTag : tagList) {
                if (!dbTags.contains(newTag)) {
                    CollectionTags newRecord = new CollectionTags();
                    newRecord.setFuser(params.get("username"));
                    newRecord.setFtag(newTag);
                    newRecord.setFenable("Y");
                    newRecord.setFshare("N");
                    newRecord.setFpostId(0L);
                    collectionTagsRepository.save(newRecord);
                }
            }

            return new Msg(100, "", "success");
        }
    }
}
