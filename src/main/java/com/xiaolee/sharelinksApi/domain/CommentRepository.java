package com.xiaolee.sharelinksApi.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    public List<Comment> findByFpostIdAndFenableOrderByFupdateTimeDesc(Long fpostId,String fenable);
}
