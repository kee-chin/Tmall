package com.chinkee.tmall.service.impl;

import com.chinkee.tmall.mapper.OrderMapper;
import com.chinkee.tmall.pojo.Order;
import com.chinkee.tmall.pojo.OrderExample;
import com.chinkee.tmall.pojo.User;
import com.chinkee.tmall.service.OrderService;
import com.chinkee.tmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;
    @Autowired
    UserService userService;

    @Override
    public List<Order> list() {
        OrderExample orderExample = new OrderExample();
        orderExample.setOrderByClause("id desc");
        List<Order> orderList = orderMapper.selectByExample(orderExample);
        setUser(orderList);
        return orderList;
    }

    public void setUser(List<Order> orders){
        for (Order order:orders){
            setUser(order);
        }
    }

    public void setUser(Order order){
        User user = userService.get(order.getUid());
        order.setUser(user);
    }


    @Override
    public Order get(int id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(Order order) {
        orderMapper.insert(order);
    }

    @Override
    public void delete(int id) {
        orderMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Order order) {
        orderMapper.updateByPrimaryKeySelective(order);
    }

}
