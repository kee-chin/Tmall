package com.chinkee.tmall.service.impl;

import com.chinkee.tmall.mapper.ReviewMapper;
import com.chinkee.tmall.pojo.Review;
import com.chinkee.tmall.pojo.ReviewExample;
import com.chinkee.tmall.pojo.User;
import com.chinkee.tmall.service.ReviewService;
import com.chinkee.tmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewMapper reviewMapper;
    @Autowired
    UserService userService;

    @Override
    public Review get(int id) {
        return reviewMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(Review review) {
        reviewMapper.insert(review);
    }

    @Override
    public void delete(int id) {
        reviewMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Review review) {
        reviewMapper.updateByPrimaryKeySelective(review);
    }

    @Override
    public List<Review> list(int pid) {
        ReviewExample reviewExample = new ReviewExample();
        reviewExample.createCriteria().andPidEqualTo(pid);
        reviewExample.setOrderByClause("id desc");

        List<Review> reviews = reviewMapper.selectByExample(reviewExample);
        setUser(reviews);
        return reviews;
    }

    private void setUser(List<Review> reviews) {
        for (Review review:reviews){
            setUser(review);
        }
    }

    private void setUser(Review review) {
        int uid = review.getUid();
        User user = userService.get(uid); // 获取user对象
        review.setUser(user); // review设置user属性
    }

    @Override
    public int getCount(int pid) {
        return list(pid).size();
    }
}
