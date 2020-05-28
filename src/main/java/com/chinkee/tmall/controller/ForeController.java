package com.chinkee.tmall.controller;

import com.chinkee.tmall.pojo.*;
import com.chinkee.tmall.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("")
public class ForeController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @Autowired
    ProductImageService productImageService;
    @Autowired
    PropertyValueService propertyValueService;
    @Autowired
    ReviewService reviewService;

    @RequestMapping("forehome")
    public String home(Model model){
        List<Category> categories = categoryService.list(); // 查询所有分类

        productService.fill(categories); // 先给分类填充产品:为这些分类填充产品集合

        productService.fillByRow(categories); // 后从分类拿取产品:为这些分类填充推荐产品集合

        model.addAttribute("cs", categories);

        return "fore/home";
    }

    // registerPage.jsp form方法调用
    @RequestMapping("foreregister")
    public String register(Model model, User user){
        // 通过参数User获取浏览器提交的账号密码
        String name = user.getName();

        // 通过HtmlUtils.htmlEscape(name);把账号里的特殊符号进行转义
        name = HtmlUtils.htmlEscape(name);
        user.setName(name);

        boolean exist = userService.isExist(name);
        // 存在，就服务端跳转到reigster.jsp，并且带上错误提示信息
        if(exist){
            String message = "用户名已存在，不可使用";
            model.addAttribute("msg", message);
            // 当用户存在，服务端跳转到register.jsp的时候不带上参数user,
            // 否则当注册失败的时候，会在原本是“请登录”的超链位置显示刚才注册的名称
            model.addAttribute("user", null);
            return "fore/register";
        }
        // 不存在，则加入到数据库中，并服务端跳转到registerSuccess.jsp页面
        userService.add(user);
        return "redirect:registerSuccessPage";
    }

    @RequestMapping("forelogin")
    public String login(Model model, @RequestParam("name")String name,
                        @RequestParam("password") String password, HttpSession httpSession){
        name = HtmlUtils.htmlEscape(name); // 获取账号密码
        User user = userService.get(name, password); // 根据账号和密码获取User对象

        // 如果对象为空，则服务端跳转回login.jsp，也带上错误信息，
        // 并且使用 loginPage.jsp 中的方法显示错误信息
        if(null == user){
            model.addAttribute("msg", "账号密码错误");
            return "fore/login";
        }

        // 如果对象存在，则把对象保存在session中，并客户端跳转到首页"forehome"
        httpSession.setAttribute("user", user);
        return "redirect:forehome";
    }

    @RequestMapping("forelogout")
    public String logout(HttpSession httpSession){
        httpSession.removeAttribute("user"); // 在session中去掉"user"
        return "redirect:forehome";
    }

    @RequestMapping("foreproduct")
    public String product(int pid, Model model){
        // int与integer的区别从大的方面来说就是基本数据类型与其包装类的区别
        // int初始值为0，Integer初始值为null
        // 根据pid获取Product对象
        Product product = productService.get(pid); // Integer product.getId() and int pid

        // 根据对象，获取这个产品对应的单个图片集合
        List<ProductImage> productSingleImages
                = productImageService.list(product.getId(), ProductImageService.type_single);
        // 根据对象，获取这个产品对应的详细图片集合
        List<ProductImage> productDetailImages
                = productImageService.list(product.getId(), ProductImageService.type_detail);
        product.setProductSingleImages(productSingleImages); // 产品设置图片属性
        product.setProductDetailImages(productDetailImages);

        // 获取产品的所有属性值
        List<PropertyValue> propertyValues = propertyValueService.list(product.getId());
        // 获取产品对应的所有的评价
        List<Review> reviews = reviewService.list(product.getId());
        // 设置产品的销量和评价数量
        productService.setSaleAndReviewNumber(product);

        model.addAttribute("reviews", reviews);
        model.addAttribute("p", product);
        model.addAttribute("pvs", propertyValues);
        return "fore/product";

    }

    @RequestMapping("forecheckLogin")
    @ResponseBody // 异步ajax的方式访问
    public String checkLogin(HttpSession httpSession){
        User user = (User) httpSession.getAttribute("user");
        if(null == user)
            return "fail";
        return "success";
    }

    @RequestMapping("foreloginAjax")
    @ResponseBody // 异步ajax的方式访问
    public String loginAjax(@RequestParam("name") String name,
                            @RequestParam("password") String password,
                            HttpSession httpSession){
        name = HtmlUtils.htmlEscape(name);

        User user = userService.get(name, password);
        if(user == null)
            return "fail";

        httpSession.setAttribute("user", user);
        return "success";
    }

}

