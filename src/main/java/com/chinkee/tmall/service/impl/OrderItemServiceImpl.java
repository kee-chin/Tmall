package com.chinkee.tmall.service.impl;

import com.chinkee.tmall.mapper.OrderItemMapper;
import com.chinkee.tmall.pojo.Order;
import com.chinkee.tmall.pojo.OrderItem;
import com.chinkee.tmall.pojo.OrderItemExample;
import com.chinkee.tmall.pojo.Product;
import com.chinkee.tmall.service.OrderItemService;
import com.chinkee.tmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service //三个一对多，User，Product和Order；订单项内嵌在订单中
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    OrderItemMapper orderItemMapper;
    @Autowired
    ProductService productService;

    @Override // 获取订单项列表
    public List<OrderItem> list() {
        OrderItemExample itemExample = new OrderItemExample();
        itemExample.setOrderByClause("id desc");
        return orderItemMapper.selectByExample(itemExample);
    }

    @Override
    public OrderItem get(int id) {
        OrderItem orderItem = orderItemMapper.selectByPrimaryKey(id);
        setProduct(orderItem);
        return orderItem;
    }

    @Override
    public void add(OrderItem orderItem) {
        orderItemMapper.insert(orderItem);
    }

    @Override
    public void delete(int id) {
        orderItemMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(OrderItem orderItem) {
        orderItemMapper.updateByPrimaryKeySelective(orderItem);
    }


    @Override // 遍历每个订单，然后挨个调用fill(Order order)
    public void fill(List<Order> orders) {
        for (Order order:orders){
            fill(order);
        }
    }
    // 包含与被包含
    @Override
    public void fill(Order order) {
        // 根据订单id查询出其对应的所有订单项
        OrderItemExample itemExample = new OrderItemExample();
        itemExample.createCriteria().andOidEqualTo(order.getId());
        itemExample.setOrderByClause("id asc");
        List<OrderItem> orderItems
                = orderItemMapper.selectByExample(itemExample);
        // 通过setProduct为所有的订单项设置Product属性。 设置
        setProduct(orderItems);
        // 遍历所有的订单项，然后计算出该订单的总金额和总数量
        float totalMoney = 0;
        int totalNumber = 0;
        for (OrderItem orderItem:orderItems){
            totalMoney += orderItem.getNumber()
                    *orderItem.getProduct().getPromotePrice();
            totalNumber += orderItem.getNumber();
        }
        order.setTotalMoney(totalMoney);
        order.setTotalNumber(totalNumber);
        // 最后再把订单项设置在订单的orderItems属性上。 设置在
        order.setOrderItems(orderItems);
    }


    public void setProduct(List<OrderItem> orderItems){
        for (OrderItem orderItem:orderItems){
            setProduct(orderItem);
        }

    }

    public void setProduct(OrderItem orderItem){
        Product product = productService.get(orderItem.getPid());
        orderItem.setProduct(product);
    }
}
