package com.chinkee.tmall.service.impl;

import com.chinkee.tmall.mapper.CategoryMapper;
import com.chinkee.tmall.pojo.Category;
import com.chinkee.tmall.pojo.CategoryExample;
import com.chinkee.tmall.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//CategoryService接口的实现类CategoryServiceImpl

//注解@Service声明当前类是一个Service类
@Service
public class CategoryServiceImpl implements CategoryService {

    // 通过自动装配@Autowired引入CategoryMapper接口，
    // 在list方法中调用CategoryMapper的list方法.
    @Autowired(required = false) // 调试注入bean失败bug，解决方法@Autowired加上（required = false)
    CategoryMapper categoryMapper; // 相当于new对象

    /*
    @Override
    public List<Category> list(Page page) {
        return categoryMapper.list(page);
    } // list方法的实现
    改用分页插件，去掉这两方法
    @Override
    public int total(){return categoryMapper.total();} // total方法的实现
    */
    @Override
    public List<Category> list(){
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("id desc"); // 设置降序列出方法
        return categoryMapper.selectByExample(categoryExample);
    }

    @Override
    public void add(Category category){
        categoryMapper.insert(category); // add方法的实现。注意void方法不返回任何值
    }

    @Override
    public void delete(int id){
        categoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Category get(int id) {
        return categoryMapper.selectByPrimaryKey(id); // get方法，需返回Category类型值
    }

    @Override
    public void update(Category category){
        categoryMapper.updateByPrimaryKey(category);
    }
}
