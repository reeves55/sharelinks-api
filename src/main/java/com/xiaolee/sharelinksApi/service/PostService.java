package com.xiaolee.sharelinksApi.service;

import com.xiaolee.sharelinksApi.domain.*;
import com.xiaolee.sharelinksApi.common.wrapper.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CollectionTagsRepository collectionTagsRepository;


    /**
     * 获取所有的帖子
     *
     * @return add by loyalli
     */
    public List<Post> getAll() {
        return postRepository.findByFshare("Y");
    }

    /**
     * 根据帖子id获取帖子详情
     *
     * @param fid
     * @return
     */
    public Msg fetch(Long fid) {
        List<Comment> comments = commentRepository.findByFpostIdAndFenableOrderByFupdateTimeDesc(fid, "Y");
        Post post = postRepository.findById(fid).get();
        Map<String, Object> result = new HashMap<>();
        result.put("post", post);
        result.put("comments", comments);

        return new Msg(1, result, "success");
    }

    /**
     * 获取分页帖子
     *
     * @param category
     * @param pageable
     * @return
     */
    public Page<Post> getPage(String category, Pageable pageable) {
        if (!"".equals(category)) {
            return postRepository.findByFcategoryAndFenableAndFshare(category, "Y", "Y", pageable);
        } else {
            return postRepository.findByFenableAndFshare("Y", "Y", pageable);
        }
    }

    /**
     * 添加到收藏夹
     *
     * @param params
     * @return
     */
    @CacheEvict(value = {"categoryPostPageCache", "postPageCache"}, allEntries = true)
    public Msg addPost(Map<String, Object> params) {
        List<Post> postsFinded = postRepository.findByFurlAndFenable((String) params.get("url"), "Y");

        if (postsFinded.size() > 1) {
            return new Msg(-100, postsFinded.get(0).getFid(), "repetitive");
        } else if (postsFinded.size() == 1) {
            // 已经有人上传过链接到链接库，则判断用户是否已经收藏，如果未收藏则添加到收藏中
            List<Like> likes = likeRepository.findByFuserAndFpostIdAndFenable((String) params.get("username"), postsFinded.get(0).getFid(), "Y");
            if (likes.size() > 0) {
                return new Msg(-110, "", "你已收藏过该链接");
            }

            Like like = new Like();
            like.setFuser((String) params.get("username"));
            like.setFtag((String) params.get("tag"));
            like.setFpostId(postsFinded.get(0).getFid());
            like.setFenable("Y");
            likeRepository.save(like);


            Post post = postRepository.findById(postsFinded.get(0).getFid()).get();
            if (null != post) {
                post.setFlike(post.getFlike() + 1);
            }
            postRepository.save(post);

            Msg msg = new Msg(100, postsFinded.get(0).getFid(), "success");
            return msg;
        } else {
            // 没有人上传过这个链接，则1. 将链接加入到链接库并同时(未分享) 2. 添加到用户收藏中
            Post post = new Post();
            post.setFtitle((String) params.get("title"));
            post.setFurl((String) params.get("url"));
            post.setFcategory("");
            post.setFauthor((String) params.get("username"));
            post.setFenable("Y");
            post.setFshare("N");
            post.setFlike(1);
            postRepository.save(post);

            Like like = new Like();
            like.setFuser((String) params.get("username"));
            like.setFtag((String) params.get("tag"));
            like.setFpostId(post.getFid());
            like.setFenable("Y");
            likeRepository.save(like);

            // 添加链接到链接库之后，同时添加到用户收藏中
            return new Msg(101, post.getFid(), "success");
        }
    }

    /**
     * 分享链接
     *
     * @param params
     * @return
     */
    @CacheEvict(value = {"categoryPostPageCache", "postPageCache"}, allEntries = true)
    public Msg share(Map<String, String> params) {
        String username = params.get("username");
        long postId = Long.parseLong(params.get("fpostId"));
        String category = params.get("category");

        Post post = postRepository.findById(postId).get();
        if (post != null && username.equals(post.getFauthor())) {
            post.setFshare("Y");
            post.setFcategory(category);
            postRepository.save(post);
            return new Msg(100, "", "success");
        } else {
            return new Msg(-100, "", "分享失败，无此链接或无分享权限");
        }
    }

    /**
     * 分享标签组
     *
     * @param params
     * @return
     */
    @CacheEvict(value = {"categoryPostPageCache", "postPageCache"}, allEntries = true)
    public Msg shareTag(Map<String, String> params) {
        String username = params.get("username");
        String tag = params.get("tag");
        String category = params.get("category");
        String title = params.get("title");

        // 在链接库当中存储此标签组
        Post post = new Post();
        post.setFtitle(title);
        post.setFcategory(category);
        post.setFtag(tag);
        post.setFurl("");
        post.setFshare("Y");
        post.setFenable("Y");
        post.setFauthor(username);
        postRepository.save(post);

        // 设置标签状态为已分享
        List<CollectionTags> records = collectionTagsRepository.findByFuserAndFtagAndFenable(username, tag, "Y");
        if (records.size() > 0) {
            CollectionTags item = records.get(0);
            item.setFshare("Y");
            item.setFpostId(post.getFid());
            collectionTagsRepository.save(item);
        }

        return new Msg(100, post.getFid(), "success");
    }

    /**
     * 取消分享链接
     *
     * @param params
     * @return
     */
    @CacheEvict(value = {"categoryPostPageCache", "postPageCache"}, allEntries = true)
    public Msg cancelshare(Map<String, String> params) {
        String username = params.get("username");
        long postId = Long.parseLong(params.get("fpostId"));
        Post post = postRepository.findById(postId).get();
        if (post != null && username.equals(post.getFauthor())) {
            post.setFshare("N");
            postRepository.save(post);
            return new Msg(100, "", "success");
        } else {
            return new Msg(-100, "", "取消分享，无此链接或无分享权限");
        }

    }

    /**
     * 删除链接
     *
     * @param params
     * @return
     */
    @CacheEvict(value = {"categoryPostPageCache", "postPageCache"}, allEntries = true)
    public Msg delete(Map<String, String> params) {
        String username = params.get("username");
        long postId = Long.parseLong(params.get("fpostId"));

        Post post = postRepository.findById(postId).get();
        if (post != null && username.equals(post.getFauthor())) {
            post.setFenable("N");
            postRepository.save(post);

            // 删除用户收藏记录(fenable=>false)
            List<Like> likes = likeRepository.findByFuserAndFpostIdAndFenable(username, postId, "Y");
            if (likes.size() == 1) {
                likes.get(0).setFenable("N");
                likeRepository.save(likes.get(0));
                return new Msg(1, "", "success");
            } else {
                return new Msg(-1, -1, "参数错误");
            }
        } else {
            return new Msg(-1, "", "删除失败");
        }
    }

    /**
     * 修改链接
     *
     * @param params
     * @return
     */
    @CacheEvict(value = {"categoryPostPageCache", "postPageCache"}, allEntries = true)
    public Msg edit(Map<String, String> params) {
        String username = params.get("username");
        long postId = Long.parseLong(params.get("fpostId"));
        String title = params.get("ftitle");
        String url = params.get("furl");
        String category = params.get("fcategory");

        Post post = postRepository.findById(postId).get();
        if (post != null && username.equals(post.getFauthor())) {
            post.setFtitle(title);
            post.setFurl(url);
            post.setFcategory(category);
            post.setFupdateTime(new Date());
            postRepository.save(post);
            return new Msg(1, "", "success");
        } else {
            return new Msg(-1, "", "修改失败");
        }
    }

    /**
     * 收藏帖子
     *
     * @return
     */
    @CacheEvict(value = {"categoryPostPageCache", "postPageCache"}, allEntries = true)
    public Msg likePost(Map<String, Object> params) {
        Integer fid = (Integer) params.get("fid");
        String tag = (String) params.get("tag");
        List<Like> likes = likeRepository.findByFuserAndFpostIdAndFenable((String) params.get("username"), Long.parseLong(fid.toString()), "Y");
        if (likes.size() > 0) {
            return new Msg(-110, "", "你已收藏过该链接");
        }

        Like like = new Like();
        like.setFuser((String) params.get("username"));
        like.setFtag(tag);
        like.setFpostId(Long.parseLong(fid.toString()));
        like.setFenable("Y");
        likeRepository.save(like);

        Post post = postRepository.findById(Long.parseLong(fid.toString())).get();
        if (null != post) {
            post.setFlike(post.getFlike() + 1);
        }
        postRepository.save(post);

        Msg msg = new Msg(1, like.getFid(), "");
        return msg;
    }

    /**
     * 举报帖子
     *
     * @param params
     * @return
     */
    public Msg reportPost(Map<String, Object> params) {
        Integer fid = (Integer) params.get("fid");
        List<Report> reports = reportRepository.findByFuserAndFpostId((String) params.get("username"), Long.parseLong(fid.toString()));
        if (reports.size() > 0) {
            return new Msg(-110, "", "你已举报过该链接");
        }

        Report report = new Report();
        report.setFuser((String) params.get("username"));
        report.setFpostId(Long.parseLong(fid.toString()));
        report.setFcontent("举报内容示例");
        report.setFenable("Y");
        reportRepository.save(report);

        Post post = postRepository.findById(Long.parseLong(fid.toString())).get();
        if (null != post) {
            post.setFreported(post.getFreported() + 1);
        }
        postRepository.save(post);

        return new Msg(1, report.getFid(), "举报成功");
    }

    /**
     * 搜索帖子
     *
     * @param params
     * @return
     */
    public Msg searchPosts(Map<String, String> params) {
        Sort sort = new Sort(Sort.Direction.DESC, "fadded_time"); // 注意这里因为下面使用的查询是native的，因此这里的排序属性应该是数据库当中的原属性名
        Pageable pageable = PageRequest.of(0, 50, sort);

        Page<Post> posts = postRepository.findByFenableAndFshareAndFtitleMatch("Y", "Y" , params.get("ftitle"), pageable);

        if (null != posts) {
            return new Msg(1, posts.getContent(), "success");
        } else {
            return new Msg(-1, "", "服务器错误");
        }
    }

    /**
     * 获取用户颜色标记收藏
     *
     * @param params
     * @return
     */
    public Msg getUserCollectionsByTagColor(Map<String, String> params) {
        String username = params.get("username");
        int colortag = Integer.parseInt(params.get("colortag"));
        int page = Integer.parseInt(params.get("page"));

        List<Long> likePostIds = new ArrayList<>();
        if (colortag == 0) {
            // 如果是获取所有收藏
            Sort sort = new Sort(Sort.Direction.DESC, "faddedTime");
            Pageable pageable = PageRequest.of(page, 30, sort);
            List<Like> likes = likeRepository.findByFuserAndFenable(username, "Y", pageable); // 根据用户名来分页查询
            for (Like like : likes
                    ) {
                likePostIds.add(like.getFpostId());
            }
            likes = null;
        } else {
            // 如果是获取指定标记收藏
            Sort sort = new Sort(Sort.Direction.DESC, "faddedTime");
            Pageable pageable = PageRequest.of(page, 30, sort);
            List<Like> likes = likeRepository.findByFuserAndFenableAndFcolortag(username, "Y", colortag, pageable); // 根据用户名、标签来分页查询
            for (Like like : likes
                    ) {
                likePostIds.add(like.getFpostId());
            }
            likes = null;
        }

        // 根据likePostsIds获取所有收藏帖子信息
        List<Post> posts = postRepository.findByFenableAndFidIn("Y", likePostIds);
        Map<Long, Post> idToPostMap = new HashMap<>();
        for (Post item : posts
                ) {
            idToPostMap.put(item.getFid(), item);
        }
        posts = null;

        List<Post> result = new ArrayList<>();
        for (Long postId : likePostIds
                ) {
            if (idToPostMap.get(postId) != null) {
                result.add(idToPostMap.get(postId));
            }
        }

        return new Msg(1, result, "success");
    }

    /**
     * 获取用户标签收藏
     *
     * @param params
     * @return
     */
    public Msg getUserCollectionsByTag(Map<String, String> params) {
        String username = params.get("username");
        String tag = params.get("tag");
        int page = Integer.parseInt(params.get("page"));

        List<Long> likePostIds = new ArrayList<>();
        if ("summary".equals(tag)) {
            // 获取标签收藏预览条目(标签以及标签内收藏的链接数)
            Map<String, Object> collCountMap = new HashMap<>(); // ftag => collection number
            List<Map<String, Object>> userTagCollectionInfo = likeRepository.findTagCollectionInfo();
            for (Map<String, Object> value : userTagCollectionInfo) {
                collCountMap.put((String) value.get("ftag"), value.get("count"));
            }

            List<CollectionTags> collectionTagsList = collectionTagsRepository.findByFuserAndFenableOrderByFupdateTimeDesc(username, "Y");

            List<Map<String, Object>> result = new ArrayList<>();
            for (CollectionTags value : collectionTagsList) {
                Map<String, Object> item = new HashMap<>();

                if ("Y".equals(value.getFshare())) {
                    // 获取每个标签被收藏的次数
                    Long starCount = likeRepository.countByFpostIdAndFenable(value.getFpostId(), "Y");
                    item.put("star", starCount);
                }

                item.put("ftag", value.getFtag());
                item.put("count", collCountMap.get(value.getFtag()));
                item.put("fadded_time", value.getFaddedTime());
                item.put("fupdate_time", value.getFupdateTime());
                item.put("fshare", value.getFshare());
                result.add(item);
            }

            return new Msg(100, result, "success");

        } else {
            // 如果是获取指定标记收藏
            Sort sort = new Sort(Sort.Direction.DESC, "faddedTime");
            Pageable pageable = PageRequest.of(page, 50, sort);
            List<Like> likes = likeRepository.findByFuserAndFenableAndFtag(username, "Y", tag, pageable); // 根据用户名、标签来分页查询
            for (Like like : likes
                    ) {
                likePostIds.add(like.getFpostId());
            }
            likes = null;
        }

        // 根据likePostsIds获取所有收藏帖子信息
        List<Post> posts = postRepository.findByFenableAndFidIn("Y", likePostIds);
        Map<Long, Post> idToPostMap = new HashMap<>();
        for (Post item : posts
                ) {
            idToPostMap.put(item.getFid(), item);
        }
        posts = null;

        List<Post> result = new ArrayList<>();
        for (Long postId : likePostIds
                ) {
            if (idToPostMap.get(postId) != null) {
                result.add(idToPostMap.get(postId));
            }
        }

        return new Msg(1, result, "success");
    }

    /**
     * 标记用户收藏颜色
     *
     * @param params
     * @return
     */
    public Msg tagUserCollection(Map<String, String> params) {
        String username = params.get("username");
        long postId = Long.parseLong(params.get("fpostId"));
        int colortag = Integer.parseInt(params.get("colortag"));

        List<Like> likes = likeRepository.findByFuserAndFpostIdAndFenable(username, postId, "Y");
        if (likes.size() == 1) {
            likes.get(0).setFcolortag(colortag);
            likeRepository.save(likes.get(0));
        } else {
            return new Msg(-1, colortag, "参数错误");
        }

        return new Msg(1, colortag, "success");
    }

    /**
     * 删除用户收藏
     *
     * @param params
     * @return
     */
    public Msg deleteUserCollection(Map<String, String> params) {
        String username = params.get("username");
        long postId = Long.parseLong(params.get("fpostId"));

        List<Like> likes = likeRepository.findByFuserAndFpostIdAndFenable(username, postId, "Y");
        if (likes.size() == 1) {
            likes.get(0).setFenable("N");
            likeRepository.save(likes.get(0));
        } else {
            return new Msg(-1, -1, "参数错误");
        }

        return new Msg(1, 1, "success");
    }

    /**
     * 添加链接评论
     *
     * @param params
     * @return
     */
    public Msg addComment(Map<String, String> params) {
        String username = params.get("username");
        long postId = Long.parseLong(params.get("fpostId"));
        String content = params.get("content");

        // 防止评论过长
        if (content.length() >= 500) {
            content = content.substring(0, 490) + "...";
        }

        Comment comment = new Comment();
        comment.setFuserName(username);
        comment.setFpostId(postId);
        comment.setFcontent(content);
        comment.setFenable("Y");

        commentRepository.save(comment);
        return new Msg(1, "", "success");
    }
}
