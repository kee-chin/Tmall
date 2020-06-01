package com.chinkee.tmall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class PageController {

    @RequestMapping("registerPage") // 浏览器可以直接访问registerPage
    public String registerPage(){
        return "fore/register"; // 浏览器不可以直接访问register
    }

    @RequestMapping("registerSuccessPage")
    public String registerSuccessPage(){
        return "fore/registerSuccess";
    }

    @RequestMapping("loginPage")
    public String loginPage(){
        return "fore/login";
    }

    @RequestMapping("forealipay")
    public String alipay(){return "fore/alipay";}

}
