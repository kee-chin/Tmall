package com.chinkee.tmall.service;

import com.chinkee.tmall.pojo.Order;

import java.util.List;

public interface OrderService {
    List list();
    Order get(int id);
    void add(Order order);
    void delete(int id);
    void update(Order order);

    String waitPay = "waitPay";
    String waitDelivery = "waitDelivery";
    String waitConfirm = "waitConfirm";
    String waitReview = "waitReview";
    String finish = "finish";
    String delete = "delete";
}
