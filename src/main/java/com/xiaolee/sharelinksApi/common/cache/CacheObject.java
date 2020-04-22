package com.xiaolee.sharelinksApi.common.cache;

public class CacheObject {
    private Long timestamp;
    private Object data;

    public CacheObject(Long timestamp,Object data) {
        this.timestamp = timestamp;
        this.data = data;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
