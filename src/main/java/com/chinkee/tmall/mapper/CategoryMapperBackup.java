package com.chinkee.tmall.mapper;

// 接口。官方解释：Java接口是一系列方法的声明，是一些方法特征的集合，一个接口
// 只有方法的特征没有方法的实现，因此这些方法可以在不同的地方被不同的类实现，
// 而这些实现可以具有不同的行为（功能）。

import com.chinkee.tmall.pojo.CategoryBackup;

import java.util.List;

public interface CategoryMapperBackup {

    // List<Category> list(Page page); // 支持分页的查询方法list(Page page)
    // 改用分页插件，去掉这两方法
    // int total(); // 获取总数的方法total
    // 改成以下list方法
    List<CategoryBackup> list();

    void add(CategoryBackup categoryBackup); // 原页面上，增加方法

    void delete(int id); // 原页面上，删除方法

    CategoryBackup get(int id); // 增加个编辑页面，获取Category对象，该对象有get方法，直接调用

    void update(CategoryBackup categoryBackup); // 原页面上，修改方法
}
