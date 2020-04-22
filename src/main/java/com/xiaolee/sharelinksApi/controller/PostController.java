package com.xiaolee.sharelinksApi.controller;

import com.xiaolee.sharelinksApi.common.annotation.Login;
import com.xiaolee.sharelinksApi.domain.Post;
import com.xiaolee.sharelinksApi.service.PostService;
import com.xiaolee.sharelinksApi.common.wrapper.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * 获取所有链接
     *
     * @return
     * add by loyalli
     */
    @RequestMapping(value = "/posts/all", method = RequestMethod.GET)
    @CrossOrigin(value = "*")
    public List<Post> getAll() {
        return postService.getAll();
    }

    /**
     * 获取链接详情
     *
     * @param fid
     * @return
     */
    @RequestMapping(value = "/posts/fetch/{fid}")
    @CrossOrigin(value = "*")
    public Msg getPost(@PathVariable(name = "fid") Long fid) {
        return postService.fetch(fid);
    }

    /**
     * 获取分页链接
     *
     * @return
     * add by loyalli
     */
    @RequestMapping(value = "/posts/page", method = RequestMethod.GET)
    @CrossOrigin(value = "*")
    public Page<Post> getPage(@RequestParam(value = "category", defaultValue = "") String category,
                              @RequestParam(value = "page", defaultValue = "0") Integer page,
                              @RequestParam(value = "size", defaultValue = "50") Integer size,
                              @RequestParam(value = "order", defaultValue = "fupdateTime") String order) {
        Sort sort = new Sort(Sort.Direction.DESC, order);
        Pageable pageable = PageRequest.of(page, size, sort);
        return postService.getPage(category, pageable);
    }

    /**
     * 增加链接
     *
     * @return
     */
    @RequestMapping(value = "/posts/add", method = RequestMethod.POST)
    @CrossOrigin(value = "*")
    @Login
    public Msg submitPost(@RequestBody Map<String, Object> params) {
        return postService.addPost(params);
    }

    /**
     * 分享链接
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/posts/share", method = RequestMethod.POST)
    @CrossOrigin(value = "*")
    @Login
    public Msg share(@RequestBody Map<String, String> params) {
        return postService.share(params);
    }

    /**
     * 分享标签组
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/posts/tag/share", method = RequestMethod.POST)
    @CrossOrigin(value = "*")
    @Login
    public Msg shareTag(@RequestBody Map<String, String> params) {
        return postService.shareTag(params);
    }

    /**
     * 取消分享链接
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/posts/cancelshare", method = RequestMethod.POST)
    @CrossOrigin(value = "*")
    @Login
    public Msg cancelshare(@RequestBody Map<String, String> params) {
        return postService.cancelshare(params);
    }

    /**
     * 收藏链接
     *
     * @return
     */
    @RequestMapping(value = "/posts/like",method = RequestMethod.POST)
    @CrossOrigin(value = "*")
    @Login
    public Msg likePost(@RequestBody Map<String,Object> params) {
        return postService.likePost(params);
    }

    /**
     * 举报链接
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/posts/report",method = RequestMethod.POST)
    @CrossOrigin(value = "*")
    @Login
    public Msg reportPost(@RequestBody Map<String,Object> params) {
        return postService.reportPost(params);
    }

    /**
     * 搜索链接
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/posts/search",method = RequestMethod.POST)
    @CrossOrigin(value = "*")
    public Msg searchPosts(@RequestBody Map<String,String> params) {
        return postService.searchPosts(params);
    }

    /**
     * 删除链接
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/post/delete", method = RequestMethod.POST)
    @CrossOrigin(value = "*")
    @Login
    public Msg delete(@RequestBody Map<String, String> params) {
        return postService.delete(params);
    }

    /**
     * 修改链接
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/post/edit", method = RequestMethod.POST)
    @CrossOrigin(value = "*")
    @Login
    public Msg edit(@RequestBody Map<String, String> params) {
        return postService.edit(params);
    }

    /**
     * 获取特定颜色标记的用户收藏
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/collection/color",method = RequestMethod.POST)
    @CrossOrigin(value = "*")
    @Login
    public Msg userColorCollections(@RequestBody Map<String,String> params) {
        return postService.getUserCollectionsByTagColor(params);
    }

    /**
     * 获取特定标签的用户收藏
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/collection/tag",method = RequestMethod.POST)
    @CrossOrigin(value = "*")
    @Login
    public Msg userTagCollections(@RequestBody Map<String,String> params) {
        return postService.getUserCollectionsByTag(params);
    }

    /**
     * 标记用户收藏颜色
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/collection/color/mark", method = RequestMethod.POST)
    @CrossOrigin(value = "*")
    @Login
    public Msg tagUserCollection(@RequestBody Map<String, String> params) {
        return postService.tagUserCollection(params);
    }

    /**
     * 删除用户收藏
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/collection/delete", method = RequestMethod.POST)
    @CrossOrigin(value = "*")
    @Login
    public Msg deleteUserCollection(@RequestBody Map<String, String> params) {
        return postService.deleteUserCollection(params);
    }

    /**
     * 添加链接评论
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/comment/add", method = RequestMethod.POST)
    @CrossOrigin(value = "*")
    @Login
    public Msg addComment(@RequestBody Map<String, String> params) {
        return postService.addComment(params);
    }
}
