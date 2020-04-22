package com.xiaolee.sharelinksApi.service;

import com.xiaolee.sharelinksApi.domain.Category;
import com.xiaolee.sharelinksApi.domain.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }
}
