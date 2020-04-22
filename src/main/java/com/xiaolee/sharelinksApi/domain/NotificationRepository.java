package com.xiaolee.sharelinksApi.domain;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Cacheable(value = "notifyCache",key = "#p0")
    public List<Notification> findByFtoInAndFenableOrderByFaddedTimeDesc(List<String> ftos,String fenable);
    public Notification findFirstByFtoAndFenableOrderByFaddedTimeDesc(String fto, String fenable);
    public List<Notification> findByFtypeAndFenableOrderByFaddedTimeDesc(String ftype,String fenable);
}
