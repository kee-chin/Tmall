package com.chinkee.tmall.service;

import com.chinkee.tmall.pojo.Review;

import java.util.List;

public interface ReviewService {

    Review get(int id);
    void add(Review review);
    void delete(int id);
    void update(Review review);

    List list(int pid); // 通过产品获取评价方法：
    int getCount(int pid);
}
