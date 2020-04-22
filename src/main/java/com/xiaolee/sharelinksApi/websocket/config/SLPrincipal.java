package com.xiaolee.sharelinksApi.websocket.config;

import java.security.Principal;

public class SLPrincipal implements Principal {
    private String username;

    public SLPrincipal(String username) {
        this.username = username;
    }

    @Override
    public String getName() {
        return username;
    }
}
