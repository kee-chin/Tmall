package com.chinkee.tmall.service;

import com.chinkee.tmall.pojo.Category;

import java.util.List;

public interface CategoryService {
    // List<Category> list(Page page); // 支持分页的查询方法list(Page page)
    // 改用分页插件，去掉这两方法
    // int total(); // 获取总数的方法total
    List<Category> list();

    void add(Category category); // 增加方法

    void delete(int id);

    Category get(int id);

    void update(Category category);
}
