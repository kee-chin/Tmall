package com.chinkee.tmall.service;

import com.chinkee.tmall.pojo.ProductImage;

import java.util.List;

public interface ProductImageService {

    String type_single = "type_single"; // ProductImage常量
    String type_detail = "type_detail";

    List list(int pid, String type); // 内嵌在产品(product)页面
    void add(ProductImage productImage);
    void delete(int id);
    void update(ProductImage productImage);
    ProductImage get(int id);

}
