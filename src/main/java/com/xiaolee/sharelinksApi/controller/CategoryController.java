package com.xiaolee.sharelinksApi.controller;

import com.xiaolee.sharelinksApi.domain.Category;
import com.xiaolee.sharelinksApi.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 获取所有分类信息
     *
     * @return
     * add by loyalli
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Category> getAll() {
        return categoryService.getAll();
    }
}
