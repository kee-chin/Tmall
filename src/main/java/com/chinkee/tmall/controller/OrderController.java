package com.chinkee.tmall.controller;

import com.chinkee.tmall.pojo.Order;
import com.chinkee.tmall.service.OrderItemService;
import com.chinkee.tmall.service.OrderService;
import com.chinkee.tmall.util.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired // 列出订单下的订单项
    OrderItemService orderItemService;

    @RequestMapping("admin_order_list")
    public String list(Model model, Page page){
        // 获取分页对象
        PageHelper.offsetPage(page.getStart(), page.getCount());
        // 查询订单集合
        List<Order> orderList = orderService.list();
        int total = (int) new PageInfo<>(orderList).getTotal();
        page.setTotal(total);

        // 借助orderItemService.fill()方法为这些订单填充上orderItems信息
        orderItemService.fill(orderList);

        model.addAttribute("os", orderList);
        model.addAttribute("page", page);

        return "admin/listOrder";
    }

    @RequestMapping("admin_order_delivery") // 注入订单对象
    public String delivery(Order order)throws IOException{
        order.setDeliveryDate(new Date()); // 修改发货时间，设置发货状态
        order.setStatus(OrderService.waitConfirm);
        orderService.update(order); // 更新到数据库
        return "redirect:admin_order_list";
    }

}
