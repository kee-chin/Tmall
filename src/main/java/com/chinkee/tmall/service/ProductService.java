package com.chinkee.tmall.service;

import com.chinkee.tmall.pojo.Category;
import com.chinkee.tmall.pojo.Product;

import java.util.List;

public interface ProductService {

    List list(int cid); // 服务端，为什么返回值，内嵌在category页面

    void add(Product product); // 客户端 为什么不返回值，待解

    void delete(int id); // 客户端

    void update(Product product); // 客户端

    Product get(int id); // 服务端

    void setFirstProductImage(Product product);

    void fill(List<Category> categories); // 为多个分类填充产品集合

    void fill(Category category); // 为分类填充产品集合

    // 为多个分类填充推荐产品集合，即把分类下的产品集合，
    // 按照8个为一行，拆成多行，以利于后续页面上进行显示
    void fillByRow(List<Category> categories);

}
