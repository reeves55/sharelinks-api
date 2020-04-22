package com.xiaolee.sharelinksApi.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long>, PagingAndSortingRepository<Like, Long> {
    public List<Like> findByFuserAndFpostIdAndFenable(String fuser, Long fpostId, String fenable);

    public List<Like> findByFuserAndFenable(String fuser, String fenable, Pageable pageable);

    public List<Like> findByFuserAndFenableAndFtag(String fuser, String fenable, String ftag, Pageable pageable);

    public List<Like> findByFuserAndFenableAndFcolortag(String fuser, String fenable, int fcolortag, Pageable pageable);

    @Query(value = "SELECT COUNT(fid) AS count,ftag FROM t_like WHERE fenable='Y' GROUP BY ftag", nativeQuery = true)
    public List<Map<String, Object>> findTagCollectionInfo();

    public Long countByFpostIdAndFenable(Long postId, String fenable);
}
