package com.xiaolee.sharelinksApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SharelinksApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(SharelinksApiApplication.class, args);
	}
}
