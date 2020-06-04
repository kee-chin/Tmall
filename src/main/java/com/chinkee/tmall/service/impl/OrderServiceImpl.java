package com.chinkee.tmall.service.impl;

import com.chinkee.tmall.mapper.OrderMapper;
import com.chinkee.tmall.pojo.Order;
import com.chinkee.tmall.pojo.OrderExample;
import com.chinkee.tmall.pojo.OrderItem;
import com.chinkee.tmall.pojo.User;
import com.chinkee.tmall.service.OrderItemService;
import com.chinkee.tmall.service.OrderService;
import com.chinkee.tmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;
    @Autowired
    UserService userService;
    @Autowired
    OrderItemService orderItemService;

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


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "Exception")
    // 通过注解进行事务管理，出现问题回滚到异常
    public float add(Order order, List<OrderItem> orderItems) {
        float total = 0;
        add(order);

        // 模拟当增加订单后出现异常，观察事务管理是否预期发生。
        if(false)
            throw  new RuntimeException();

        for (OrderItem orderItem:orderItems){
            orderItem.setOid(order.getId());
            orderItemService.update(orderItem);
            total += orderItem.getProduct().getPromotePrice()*orderItem.getNumber();
        }
        return total;
    }

    @Override
    public List<Order> list(int uid, String excludedStatus) {
        OrderExample orderExample = new OrderExample();
        orderExample.createCriteria().andUidEqualTo(uid).andStatusNotEqualTo(excludedStatus);
        orderExample.setOrderByClause("id desc");
        return orderMapper.selectByExample(orderExample);
    }

}
