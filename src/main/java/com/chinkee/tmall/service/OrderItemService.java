package com.chinkee.tmall.service;

import com.chinkee.tmall.pojo.Order;
import com.chinkee.tmall.pojo.OrderItem;

import java.util.List;

public interface OrderItemService {
    List list();
    OrderItem get(int id);
    void add(OrderItem orderItem);
    void delete(int id);
    void update(OrderItem orderItem);

    void fill(List<Order> orders);

    void fill(Order order);
}
