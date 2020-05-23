package com.chinkee.tmall.controller;

import com.chinkee.tmall.pojo.Category;
import com.chinkee.tmall.pojo.Property;
import com.chinkee.tmall.service.CategoryService;
import com.chinkee.tmall.service.PropertyService;
import com.chinkee.tmall.util.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("")
public class PropertyController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    PropertyService propertyService;


    @RequestMapping("admin_property_list")
    public String list(int cid, Model model, Page page){
        // Category category = new Category(); // cid，这样并没有抓取cid数据
        Category category = categoryService.get(cid); // 抓取cid数据

        // 获取分页对象page，设置分页参数
        PageHelper.offsetPage(page.getStart(), page.getCount());
        // 基于cid，获取当前分类下的属性集合
        List<Property> ps = propertyService.list(cid);
        // 通过PageInfo获取属性总数
        int total = (int) new PageInfo<>(ps).getTotal();
        // 把总数设置给分页page对象
        page.setTotal(total);
        // 拼接字符串"&cid="+c.getId()，设置给page对象的Param值。
        // 因为属性分页都是基于当前分类下的分页，所以分页的时候需要传递这个cid
        page.setParam("&cid=" + category.getId());// 地址栏显示

        // 把属性集合设置到request的 "ps" 属性上
        model.addAttribute("ps", ps); // model增加属性方法，将后台实体类对象与前端数据属性对接
        model.addAttribute("c", category);// 把分类对象设置到request的 "c" 属性上，用于显示分类名称
        model.addAttribute("page", page); // 把分页对象设置到 request的 "page" 对象上

        // 服务端跳转到admin/listProperty.jsp页面
        return "admin/listProperty";

    }

    @RequestMapping("admin_property_add")
    public String add(Property property){ // 通过参数Property接受注入
        propertyService.add(property);// 通过propertyService保存到数据库
        // 客户端 跳转到admin_property_list,并带上参数cid
        return "redirect:admin_property_list?cid=" + property.getCid();
    }

    @RequestMapping("admin_property_edit")
    public String edit(Model model, int id){
        Property property = propertyService.get(id); // 根据属性id获取属性
        Category category = categoryService.get(property.getCid());// 根据属性cid获取Category对象
        property.setCategory(category);// Category对象设置在Property对象的category属性上
        model.addAttribute("p", property); // 把Property对象放在request的 "p" 属性中
        return "admin/editProperty"; // 服务端跳转到admin/editProperty.jsp
    }

    @RequestMapping("admin_property_update")
    public String update(Property property){ // 获取Property对象
        propertyService.update(property); // 通过propertyService更新这个对象到数据库
        // 客户端跳转到admin_property_list，并带上参数cid
        return "redirect:admin_property_list?cid=" + property.getCid();//Property对象作用
    }

    @RequestMapping("admin_property_delete")
    public String delete(int id){ // 获取id
        Property property = propertyService.get(id); // 根据id获取Property对象
        propertyService.delete(id); // 借助propertyService删除这个对象对应的数据
        // 客户端跳转到admin_property_list，并带上参数cid
        return "redirect:admin_property_list?cid=" + property.getCid();// Property对象作用
    }
}
