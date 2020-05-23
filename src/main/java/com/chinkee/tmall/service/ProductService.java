package com.chinkee.tmall.service;

import com.chinkee.tmall.pojo.Product;

import java.util.List;

public interface ProductService {

    List list(int cid); // 服务端，为什么返回值，内嵌在category页面

    void add(Product product); // 客户端 为什么不返回值，待解

    void delete(int id); // 客户端

    void update(Product product); // 客户端

    Product get(int id); // 服务端

    void setFirstProductImage(Product product);

}
