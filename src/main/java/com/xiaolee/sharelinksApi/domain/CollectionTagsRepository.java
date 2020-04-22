package com.xiaolee.sharelinksApi.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollectionTagsRepository extends JpaRepository<CollectionTags, Long> {
    public List<CollectionTags> findByFuserAndFenableOrderByFupdateTimeDesc(String fuser, String fenable);
    public List<CollectionTags> findByFuserAndFtagAndFenable(String fuser, String ftag, String fenable);
}
