package com.xiaolee.sharelinksApi.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    public List<Report> findByFuserAndFpostId(String fuser, Long fpostId);
}
