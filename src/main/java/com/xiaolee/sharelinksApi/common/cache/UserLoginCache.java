package com.xiaolee.sharelinksApi.common.cache;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Scope(value = "singleton")
public class UserLoginCache {
    private Map<String, CacheObject> cacheMap;

    public UserLoginCache() {
        this.cacheMap = new HashMap<>();
    }

    /**
     * 获取缓存项，如果缓存项失效则返回null并删除缓存项
     *
     * @param key
     * @return
     */
    public synchronized CacheObject get(String key) {
        CacheObject cacheObject = cacheMap.get(key);
        if (cacheObject == null) {
            return null;
        }

        Long now = new Date().getTime();
        if (now >= cacheObject.getTimestamp()) {
            // 缓存失效
            cacheMap.remove(key);
            return null;
        }

        return cacheMap.get(key);
    }

    /**
     * 删除缓存项
     *
     * @param key
     * @return
     */
    public synchronized boolean remove(String key) {
        if (cacheMap.get(key)==null) {
            return true;
        }

        cacheMap.remove(key);
        return true;
    }

    /**
     * 增加缓存项
     *
     * @param key
     * @param cacheObject
     * @return
     */
    public synchronized boolean push(String key, CacheObject cacheObject) {
        cacheMap.put(key, cacheObject);
        return true;
    }

    /**
     * 增加缓存项
     *
     * @param key
     * @param timestamp
     * @param data
     * @return
     */
    public synchronized boolean push(String key, Long timestamp, Object data) {
        CacheObject cacheObject = new CacheObject(timestamp, data);
        cacheMap.put(key, cacheObject);
        return true;
    }
}
