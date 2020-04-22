package com.xiaolee.sharelinksApi.domain;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PagingAndSortingRepository<Post, Long> {
    @Cacheable(value = "categoryPostPageCache")
    public Page<Post> findByFcategoryAndFenableAndFshare(String fcategory, String fenable, String fshare, Pageable pageable);
    @Cacheable(value = "postPageCache")
    public Page<Post> findByFenableAndFshare(String fenable, String fshare, Pageable pageable);
    public List<Post> findByFurlAndFenable(String furl, String fenable);
    @Query(nativeQuery = true, value = "SELECT * FROM t_posts WHERE fenable=?1 AND fshare=?2 AND ftitle REGEXP ?3")
    public Page<Post> findByFenableAndFshareAndFtitleMatch(String fenable,String fshare, String regex, Pageable pageable);
    public List<Post> findByFenableAndFidIn(String fenable, List<Long> fids);
    public List<Post> findByFshare(String fshare);
}