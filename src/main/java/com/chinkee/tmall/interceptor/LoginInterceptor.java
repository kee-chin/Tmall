package com.chinkee.tmall.interceptor;

import com.chinkee.tmall.pojo.User;
import org.apache.commons.lang.StringUtils;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Date;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    /**
     * 在业务处理器处理请求之前被调用
     * 如果返回false
     *     从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
     * 如果返回true
     *    执行下一个拦截器,直到所有的拦截器都执行完毕
     *    再执行被拦截的Controller
     *    然后进入拦截器链,
     *    从最后一个拦截器往回执行所有的postHandle()
     *    接着再从最后一个拦截器往回执行所有的afterCompletion()
     */

    // 在访问Controller之前被调用
    public boolean preHandle(HttpServletRequest servletRequest,
                              HttpServletResponse servletResponse,
                              Object handler) throws Exception{
        // 从请求获取session
        HttpSession session = servletRequest.getSession();

        // 获取访问路径：http://localhost:8080/tmall_ssm
        String contextPath = session.getServletContext().getContextPath();

        // 字符串数组 noNeedAuthPage，存放哪些不需要登录也能访问的路径
        String[] noNeedAuthPage = new String[]{
                "home",
                "register",
                "login",
                "product",
                "checkLogin",
                "loginAjax",
                "category",
                "search"};

        // 获取访问页面地址：http://localhost:8080/tmall_ssm/forehome
        String uri = servletRequest.getRequestURI();
        // 去掉uri中的contextPath字段
        uri = StringUtils.remove(uri, contextPath);
        //System.out.println(uri);

        // 获取/fore后面的方法名
        String method = StringUtils.substringAfterLast(uri,"/fore");
        // noNeedAuthPage不含有method
        if(!Arrays.asList(noNeedAuthPage).contains(method)){
            User user = (User) session.getAttribute("user");
            // 若user为空，即没有用户，响应导向去登录页面
            if (null == user){
                servletResponse.sendRedirect("loginPage");
                return false;
            }
        }

        return true;
    }


    /**
     * 在业务处理器处理请求执行完成后,生成视图之前执行的动作
     * 可在modelAndView中加入数据，比如当前时间
     */

    // 在访问Controller之后，访问视图之前被调用
    // 这里可以注入一个时间到modelAndView中，用于后续视图显示
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler, ModelAndView modelAndView)
            throws Exception {
            // 登录后，返回主页，地址栏出现这个时间
            modelAndView.addObject("time:" + new Date());
    }


    /**
     * 在DispatcherServlet完全处理完请求后被调用,可用于清理资源等
     *
     * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
     */

    // 在访问视图之后被调用
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object handler, Exception e) throws Exception{

    }
}
