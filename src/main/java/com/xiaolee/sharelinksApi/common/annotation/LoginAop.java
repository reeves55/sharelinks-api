package com.xiaolee.sharelinksApi.common.annotation;

import com.xiaolee.sharelinksApi.common.cache.UserLoginCache;
import com.xiaolee.sharelinksApi.common.wrapper.Msg;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Aspect
public class LoginAop {
    @Autowired
    private UserLoginCache userLoginCache;

    @Pointcut("@annotation(login)")
    public void loginMethodPointcut(Login login) {
    }

    /**
     * 检测登陆状态切面逻辑代码
     *
     * @param point
     * @return
     */
    @Around(value = "loginMethodPointcut(login)")
    public Object Interceptor(ProceedingJoinPoint point, Login login) {
        Object result = null;
        Object[] args = point.getArgs();
        int type = login.type();

        for (Object arg : args) {
            if (arg instanceof Map) {
                Map<String, Object> params = (Map<String, Object>) arg;
                if (!hasLogin(params)) {
                    // 用户尚未登陆
                    result = new Msg(-100, "nologin", "尚未登陆");
                } else {
                    // 用户已经登陆,则进一步判断需不需要验证是否是管理员
                    if (type == 1 && !"xiaobaba".equals((String) params.get("username"))) {
                        result = new Msg(-101, "you are not admin", "验证失败");
                    }
                }
            }
        }

        try {
            if (result == null) {
                result = point.proceed();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            result = new Msg(-101, "", "用户登陆状态检测失败");
        }

        return result;
    }

    /**
     * 检测是否已登陆
     *
     * @param params
     * @return
     */
    private boolean hasLogin(Map<String, Object> params) {
        String username = "";
        String token = "";

        if (params.get("username") == null || params.get("token") == null) {
            return false;
        }

        username = (String) params.get("username");
        token = (String) params.get("token");

        if ("".equals(username) || "".equals(token)) {
            return false;
        }

        if (null != userLoginCache.get(username)) {
            if (!token.equals((String) userLoginCache.get(username).getData())) {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }
}
