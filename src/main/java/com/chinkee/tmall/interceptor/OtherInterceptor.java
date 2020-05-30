package com.chinkee.tmall.interceptor;

import com.chinkee.tmall.pojo.Category;
import com.chinkee.tmall.pojo.OrderItem;
import com.chinkee.tmall.pojo.User;
import com.chinkee.tmall.service.CategoryService;
import com.chinkee.tmall.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class OtherInterceptor implements HandlerInterceptor {

    @Autowired
    CategoryService categoryService;
    @Autowired
    OrderItemService orderItemService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        System.out.println("拦截器在业务处理器被请求之前");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

        // 获取分类集合信息，用于放在搜索栏下面
        List<Category> categories = categoryService.list();
        // 放在session，每次访问都会访问此信息
        // 获得属于本客户端的session对象httpServletRequest.getSession()
        // httpServletRequest.getSession().setAttribute("cs", categories);
        // 放在request，由于会优先访问拦截器，此类信息放在此也有效
        httpServletRequest.setAttribute("cs", categories);

        /**这里是获取当前的contextPath:tmall_ssm,用与放在左上角那个变形金刚，
        点击之后才能够跳转到首页，否则点击之后也仅仅停留在当前页面*/
        HttpSession session = httpServletRequest.getSession();
        String contextPath = session.getServletContext().getContextPath() + "/forehome";
        session.setAttribute("contextPath", contextPath);

        // System.out.println(contextPath);

        /**这里是获取购物车中一共有多少数量*/
        User user = (User) session.getAttribute("user");
        int cartTotalItemNumber = 0;
        if(null != user){
            List<OrderItem> orderItems = orderItemService.listByUser(user.getId());
            for (OrderItem orderItem:orderItems){
                cartTotalItemNumber += orderItem.getNumber();
            }
        }
        //
        httpServletRequest.setAttribute("cartTotalItemNumber", cartTotalItemNumber);

        System.out.println("在业务处理器处理请求执行完成后,生成视图之前");

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

        System.out.println("完全处理完请求后被调用，在访问视图之后被调用");
    }
}
