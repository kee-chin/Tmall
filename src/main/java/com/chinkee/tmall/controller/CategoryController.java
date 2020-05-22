package com.chinkee.tmall.controller;

import com.chinkee.tmall.pojo.Category;
import com.chinkee.tmall.service.CategoryService;
import com.chinkee.tmall.util.ImageUtil;
import com.chinkee.tmall.util.Page;
import com.chinkee.tmall.util.UploadedImageFile;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller // 注解@Controller声明当前类是一个控制器，页面控制，返回值是页面路径，所以方法返回类型是字符串
@RequestMapping("") // 注解@RequestMapping("")表示访问的时候无需额外的地址
public class CategoryController {

    @Autowired(required = false) //注解@Autowired自动装配-实现了CategoryService接口的实例（CategoryServiceImpl）
    CategoryService categoryService;

    // @RequestMapping("admin_category_list")
    // public String list(Model model, Page page){
    /*
    在list方法中，通过categoryService.list()获取所有的Category对象，然后放在"cs"中，并
    服务端跳转到 “admin/listCategory” 视图。“admin/listCategory” 会根据后续的
    springMVC.xml 配置文件，跳转到 WEB-INF/jsp/‘admin/listCategory’.jsp 文件
     */
    /*
        List<Category> cs = categoryService.list(page);
        int total = categoryService.total();
        page.setTotal(total);

        model.addAttribute("cs", cs); // model是指springMVC中的M
        model.addAttribute("page", page);
        // page那个参数怎么传进来的，配置文件中并没有交给spring管理？
        // 作为 springMVC 模型，只要参数有这个对象，就会自动实例化它。

        return "admin/listCategory"; // 前后端对接接口
    }
     */

    @RequestMapping("admin_category_list") //映射admin_category_list路径的访问
    // 使用分页插件 use PageHelper
    public String list(Model model, Page page){
        // 通过分页插件指定分页参数
        PageHelper.offsetPage(page.getStart(), page.getCount());
        // 调用list() 获取对应分页的数据
        List<Category> cs = categoryService.list();
        // 通过PageInfo<>(cs).getTotal()获取总数
        int total = (int) new PageInfo<>(cs).getTotal();

        page.setTotal(total);
        model.addAttribute("cs", cs);
        model.addAttribute("page", page);

        return "admin/listCategory"; // 前后端对接接口
    }

    @RequestMapping("admin_category_add") // add方法映射路径admin_category_add的访问
    public String add(Category category, HttpSession httpSession, UploadedImageFile uploadedImageFile)
            throws IOException {
        // System.out.println("Test");
        categoryService.add(category); // 通过categoryService保存category对象
        // System.out.println(category.getId());

        // 保存图片
        // 通过httpSession获取ServletContext,再通过getRealPath定位存放分类图片的路径。
        // 图片就会存放在:D:\project\tmall\target\tmall_chen\img\category
        File imageFolder = new File(httpSession.getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder, category.getId() + ".jpg"); // 文件命名
        //  如果/img/category目录不存在，则创建该目录
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs(); // mkdirs()是创建多级目录。
        }
        /* System.out.println(uploadedImageFile);
        System.out.println(uploadedImageFile.getImage());
        System.out.println(file);
         */

        // 通过 UploadedImageFile 把浏览器传递过来的图片保存在上述指定的位置
        uploadedImageFile.getImage().transferTo(file);
        // 通过ImageUtil.change2jpg(file); 确保图片格式一定是jpg，而不仅仅是后缀名是jpg
        BufferedImage bufferedImage = ImageUtil.change2jpg(file);
        ImageIO.write(bufferedImage, "jpg", file);

        // 客户端跳转到admin_category_list
        return "redirect:/admin_category_list";
    }

    @RequestMapping("admin_category_delete")
    public String delete(int id, HttpSession httpSession) throws IOException{
        categoryService.delete(id);

        File imageFolder = new File(httpSession.getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder, id + ".jpg");
        file.delete();

        return "redirect:/admin_category_list";
    }

    @RequestMapping("admin_category_edit")
    public String edit(int id, Model model)throws IOException{
        Category category = categoryService.get(id);
        model.addAttribute("category", category); // 把对象加载到Model

        return "admin/editCategory";
    }

    @RequestMapping("admin_category_update")
    public String update(Category category, HttpSession httpSession, UploadedImageFile uploadedImageFile)throws IOException{
        categoryService.update(category);
        MultipartFile image = uploadedImageFile.getImage();
        if(null != image && !image.isEmpty()){
            File imageFolder = new File(httpSession.getServletContext().getRealPath("image/category"));
            File file = new File(imageFolder, category.getId() + ".jpg");
            // save image
            image.transferTo(file);
            BufferedImage bufferedImage = ImageUtil.change2jpg(file);
            ImageIO.write(bufferedImage, "jpg", file);
        }
        return "redirect:/admin_category_list";
    }
}
