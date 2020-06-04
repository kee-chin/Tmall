package com.chinkee.tmall.controller;

import com.chinkee.tmall.comparator.*;
import com.chinkee.tmall.pojo.*;
import com.chinkee.tmall.service.*;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    OrderService orderService;

    @RequestMapping("forehome")
    public String home(Model model){
        List<Category> categories = categoryService.list(); // 查询所有分类

        productService.fill(categories); // 先给分类填充产品:为这些分类填充产品集合

        productService.fillByRow(categories); // 后从分类拿取产品:为这些分类填充推荐产品集合

        model.addAttribute("cs", categories);

        return "fore/home";
    }

    // 实际上这业务只是判断用户名是否已注册，与数据库交互，
    // 并不是注册页面，而是在该页面上显示"用户名已存在，不可使用"，
    // 所以需要先访问注册页面，再实现这功能。由于register.jsp 是放在WEB-INF目录下的，
    // 是无法通过浏览器直接访问的。为了访问这些放在WEB-INF下的jsp，
    // 准备一个专门的PageController类，专门做服务端跳转。
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

    @RequestMapping("foreproduct") // 访问地址foreproduct？pid=xx
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
        if(null == user)  // IE浏览器Session不会存用户，以致无法识别是否登录
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

    @RequestMapping("forecategory")
    public String category(int cid, String sort, Model model){
        Category category = categoryService.get(cid);
        productService.fill(category); // 为category填充产品
        // 为产品填充销量和评价数据
        productService.setSaleAndReviewNumber(category.getProducts());

        if(null != sort){
            switch (sort){
                case "all":
                    Collections.sort(category.getProducts(), new ProductAllComparator());
                    break;

                case "date":
                    Collections.sort(category.getProducts(), new ProductDateComparator());
                    break;

                case "price":
                    Collections.sort(category.getProducts(), new ProductPriceComparator());
                    break;

                case "review":
                    Collections.sort(category.getProducts(), new ProductReviewComparator());
                    break;

                case "sale":
                    Collections.sort(category.getProducts(), new ProductSaleComparator());
                    break;
            }
        }

        model.addAttribute("c", category);
        return "fore/category";
    }

    @RequestMapping("foresearch")
    public String search(String keyword, String sort, Model model){
        // 根据keyword进行模糊查询，获取满足条件的前20个产品
        PageHelper.offsetPage(0, 20);
        List<Product> products = productService.search(keyword);
        // 为这些产品设置销量和评价数量
        productService.setSaleAndReviewNumber(products);

        // 搜出来的产品排序，未完善
        if(null != sort){
            switch (sort){
                case "all":
                    Collections.sort(products, new ProductAllComparator());
                    break;

                case "date":
                    Collections.sort(products, new ProductDateComparator());
                    break;

                case "price":
                    Collections.sort(products, new ProductPriceComparator());
                    break;

                case "review":
                    Collections.sort(products, new ProductReviewComparator());
                    break;

                case "sale":
                    Collections.sort(products, new ProductSaleComparator());
                    break;
            }
        }

        model.addAttribute("ps", products);
        return "fore/searchResult";
    }

    @RequestMapping("forebuyone")
    public String buyOne(int pid, int num, HttpSession session){

        int oiid = 0;

        // 从session中获取用户对象user
        User user = (User) session.getAttribute("user");
        List<OrderItem> orderItems = orderItemService.listByUser(user.getId());

        boolean found = false;
        //如果已经存在这个产品对应的OrderItem，并且还没有生成订单，即还在购物车中。
        //那么就应该在对应的OrderItem基础上，调整数量
        for(OrderItem orderItem:orderItems){
            if(orderItem.getProduct().getId().intValue() == pid){ // 这都是int类型，可以比较
                orderItem.setNumber(orderItem.getNumber() + num);
                orderItemService.update(orderItem);
                found = true;
                oiid = orderItem.getId();
                break;
            }
        }

        // 如果不存在对应的OrderItem,那么就新增一个订单项OrderItem
        if(!found){
            OrderItem orderItem = new OrderItem();
            orderItem.setUid(user.getId());
            orderItem.setPid(pid);
            orderItem.setNumber(num);
            orderItemService.add(orderItem);
            oiid = orderItem.getId();
        }

        return "redirect:forebuy?oiid=" + oiid;
    }

    @RequestMapping("forebuy") // 用字符串数组获取多个oiid
    public String buy(Model model, String[] oiid, HttpSession httpSession){
        List<OrderItem> orderItems = new ArrayList<>(); // 准备一个泛型是OrderItem的集合
        float total = 0;

        for(String strid:oiid){
            int id = Integer.parseInt(strid); // 通过Integer解析String为int
            OrderItem orderItem = orderItemService.get(id);
            total += orderItem.getProduct().getPromotePrice()*orderItem.getNumber();
            orderItems.add(orderItem); // ArrayList增加元素方法，放入orderItems集合中
        }

        // 把订单项集合放在session的属性 "ois" 上
        // 购物是一个过程，在这个过程把订单项属性放在session，等添加购物可以更快响应
        httpSession.setAttribute("ois", orderItems);
        // 把总价格放在 model的属性 "total" 上
        model.addAttribute("total", total);
        return "fore/buy";
    }

    // 与立即购买一样，只不过是返回值不同，加入后购物车按钮变灰，对接imgAndInfo.jsp
    @RequestMapping("foreaddCart")
    @ResponseBody
    public String addCart(int pid, int num, HttpSession session){
        User user = (User) session.getAttribute("user");
        List<OrderItem> orderItems = orderItemService.listByUser(user.getId());

        boolean found = false;
        for(OrderItem orderItem:orderItems){
            if(orderItem.getProduct().getId().intValue() == pid){
                orderItem.setNumber(orderItem.getNumber() + num);
                orderItemService.update(orderItem);
                found = true;
                break;
            }
        }

        if(!found){
            OrderItem orderItem = new OrderItem();
            orderItem.setUid(user.getId());
            orderItem.setPid(pid);
            orderItem.setNumber(num);
            orderItemService.add(orderItem);
        }
        return "success";
    }

    @RequestMapping("forecart")
    public String cart(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        List<OrderItem> orderItems = orderItemService.listByUser(user.getId());
        model.addAttribute("ois", orderItems);
        return "fore/cart";
    }

    @RequestMapping("forechangeOrderItem")
    @ResponseBody
    public String changeOrderItem(HttpSession session, int pid, int num){
        User user = (User) session.getAttribute("user");
        if(null == user)
            return "fail"; //  判断用户是否登录，对接cartPage.jsp

        List<OrderItem> orderItems = orderItemService.listByUser(user.getId());
        for(OrderItem orderItem:orderItems){
            if(orderItem.getProduct().getId().intValue() == pid){
                orderItem.setNumber(num);
                orderItemService.update(orderItem);
                break;
            }
        }
        return "success";
    }

    @RequestMapping("foredeleteOrderItem")
    @ResponseBody
    public String deleteOrderItem(int oiid, HttpSession session){
        User user = (User) session.getAttribute("user");
        if(null == user){
            return "fail";
        }

        orderItemService.delete(oiid); // 后台工作，处理数据库业务
        return "success"; // 前端工作，处理页面显示
    }

    // order参数从浏览器forebuy页面获取，对接buyPage.jsp
    @RequestMapping("forecreateOrder")
    public String createOrder(HttpSession session, Order order){
        User  user = (User) session.getAttribute("user");
        // 根据当前时间加上一个4位随机数生成订单号(0-9999)
        String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS")
                .format(new Date()) + RandomUtils.nextInt(10000);

        TimeZone time = TimeZone.getTimeZone("GMT+8");
        TimeZone.setDefault(time);
        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        // 根据上述参数，创建订单对象
        order.setOrderCode(orderCode);
        order.setCreateDate(date);
        order.setUid(user.getId());
        order.setStatus(OrderService.waitPay);
        // 从session中获取订单项集合 ( 在结算功能的buy()，订单项集合被放到了session中 )
        List<OrderItem> orderItems = (List<OrderItem>) session.getAttribute("ois");

        // 把订单加入到数据库，并且遍历订单项集合，设置每个订单项的order，更新到数据库
        // 统计本次订单的总金额
        float total = orderService.add(order, orderItems);
        return "redirect:forealipay?oid=" + order.getId() + "&total=" + total;
    }

    @RequestMapping("forepayed") // 访问地址forepayed?oid=
    public String payed(Model model, int oid, HttpSession session){
        Order order = orderService.get(oid);

        /** 从session中获取订单项集合 ( 在结算功能的buy()，订单项集合被放到了session中 )
        List<OrderItem> orderItems = (List<OrderItem>) session.getAttribute("ois");
        float total = orderService.add(order, orderItems);
        if(order.getStatus() != OrderService.waitPay){
            return "redirect:forealipay?oid=" + order.getId() + "&total=" + total;
        }
         **/

        order.setStatus(OrderService.waitDelivery);
        order.setPayDate(new Date());
        orderService.update(order);
        model.addAttribute("o", order); // 跳转页面
        return "fore/payed";
    }

    @RequestMapping("forebought")
    public String bought(Model model, HttpSession httpSession){
        User user = (User) httpSession.getAttribute("user");
        // 查询user所有的状态不是"delete" 的订单集合os
        List<Order> orders = orderService.list(user.getId(), OrderService.delete);
        // 为这些订单填充订单项，列出每项订单的细则
        orderItemService.fill(orders);

        model.addAttribute("os", orders);

        return "fore/bought";
    }

    @RequestMapping("foreconfirmPay")
    public String confirmPay(Model model, int oid){
        Order order = orderService.get(oid);

        /**
        if(order.getStatus() != OrderService.waitDelivery){
            return "fore/home";
        }**/

        orderItemService.fill(order); // 为订单对象填充订单项
        model.addAttribute("o", order);
        return "fore/confirmPay";
    }

    @RequestMapping("foreorderConfirmed")
    public String orderConfirmed(int oid){
        Order order = orderService.get(oid);

        // 修改对象o的状态为等待评价，修改其确认支付时间
        order.setStatus(OrderService.waitReview);
        order.setConfirmDate(new Date());
        orderService.update(order);

        return "fore/orderConfirmed";
    }

    @RequestMapping("foredeleteOrder")
    @ResponseBody // 不加这个就会去找对应的 jsp 文件
    public String deleteOrder(int oid){
        Order order = orderService.get(oid);
        order.setStatus(OrderService.delete);
        orderService.update(order); // 更新到数据库

        return "success";
    }

    @RequestMapping("forereview")
    public String review(Model model, int oid){
        Order order = orderService.get(oid);
        orderItemService.fill(order);

        // 获取第一个订单项对应的产品
        Product product = order.getOrderItems().get(0).getProduct();
        // 获取这个产品的评价集合
        List<Review> reviews = reviewService.list(product.getId());
        // 为产品设置评价数量和销量
        productService.setSaleAndReviewNumber(product);

        // 把产品，订单和评价集合放在request上
        model.addAttribute("p", product);
        model.addAttribute("o", order);
        model.addAttribute("reviews", reviews);
        return "fore/review";
    }


    @RequestMapping("foredoreview")
    public String doreview(HttpSession session, String content,
                           @RequestParam("pid")int pid, @RequestParam("oid")int oid){
        Order order = orderService.get(oid);

        order.setStatus(OrderService.finish);
        orderService.update(order);

        // 获取产品
        // Product product = productService.get(pid);
        // 获取评价内容 对评价信息进行转义
        content = HtmlUtils.htmlEscape(content);

        // 为评价对象review设置 评价信息，产品，时间，用户
        User user = (User) session.getAttribute("user");
        Review review = new Review();
        review.setUid(user.getId());
        review.setCreateDate(new Date());
        review.setPid(pid);
        review.setContent(content);
        reviewService.add(review);

        // 客户端跳转到/forereview：评价产品页面，并带上参数showonly=true
        return "redirect:forereview?oid=" + oid + "&showonly=true";
    }
}

