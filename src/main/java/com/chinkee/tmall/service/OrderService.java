package com.chinkee.tmall.service;

import com.chinkee.tmall.pojo.Order;
import com.chinkee.tmall.pojo.OrderItem;

import java.util.List;

public interface OrderService {
    List list();
    Order get(int id);
    void add(Order order);
    void delete(int id);
    void update(Order order);

    float add(Order order, List<OrderItem> orderItems);

    List<Order> list(int uid, String excludedStatus);

    String waitPay = "waitPay";
    String waitDelivery = "waitDelivery";
    String waitConfirm = "waitConfirm";
    String waitReview = "waitReview";
    String finish = "finish";
    String delete = "delete";
}
