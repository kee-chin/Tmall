package com.chinkee.tmall.controller;

import com.chinkee.tmall.pojo.Category;
import com.chinkee.tmall.pojo.Product;
import com.chinkee.tmall.service.CategoryService;
import com.chinkee.tmall.service.ProductService;
import com.chinkee.tmall.util.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("")
public class ProductController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;

    @RequestMapping("admin_product_list")
    public String list(int cid, Model model, Page page){
        Category category = categoryService.get(cid);

        PageHelper.offsetPage(page.getStart(), page.getCount());
        List<Product> products = productService.list(cid);
        int total = (int) new PageInfo<>(products).getTotal();
        page.setTotal(total);
        page.setParam("&cid=" + cid);

        model.addAttribute("ps", products);
        model.addAttribute("c", category);
        model.addAttribute("page", page);

        return "admin/listProduct";
    }

    @RequestMapping("admin_product_add")
    public String add(Model model, Product product){
        // 产品管理增加功能参数较多，另加一个页面，需服务端跳转过去，所以需要个model
        // 并没有使用，待解
        product.setCreateDate(new Date()); // 自动生成系统时间
        productService.add(product);
        return "redirect:admin_product_list?&cid=" + product.getCid();
    }

    @RequestMapping("admin_product_delete")
    public String delete(int id){
        Product product = productService.get(id);
        productService.delete(id);
        return "redirect:admin_product_list?&cid=" + product.getCid();
    }

    @RequestMapping("admin_product_update")
    public String update(Product product){
        productService.update(product);
        return "redirect:admin_product_list?&cid=" + product.getCid();
    }

    @RequestMapping("admin_product_edit")
    public String edit(Model model, int id){
        Product product = productService.get(id);
        Category category = categoryService.get(product.getCid());

        product.setCategory(category);
        model.addAttribute("p", product);
        return "admin/editProduct";
    }
}
