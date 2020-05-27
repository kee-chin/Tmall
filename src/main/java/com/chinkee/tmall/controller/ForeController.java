package com.chinkee.tmall.controller;

import com.chinkee.tmall.pojo.Category;
import com.chinkee.tmall.pojo.User;
import com.chinkee.tmall.service.CategoryService;
import com.chinkee.tmall.service.ProductService;
import com.chinkee.tmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

}

