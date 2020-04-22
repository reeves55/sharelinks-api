package com.xiaolee.sharelinksApi.common.cache;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Scope(value = "singleton")
public class NotifyStatusCache {
    private Map<String,Long> cacheMap;

    public NotifyStatusCache() {
        cacheMap = new HashMap<>();
    }

    public synchronized void update(String key, Long value) {
        cacheMap.put(key,value);
    }

    public synchronized Long get(String key){
        if (cacheMap.get(key)==null) {
            return 0L;
        }

        return cacheMap.get(key);
    }
}
