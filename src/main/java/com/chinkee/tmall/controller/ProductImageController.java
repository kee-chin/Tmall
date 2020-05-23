package com.chinkee.tmall.controller;

import com.chinkee.tmall.pojo.Product;
import com.chinkee.tmall.pojo.ProductImage;
import com.chinkee.tmall.service.ProductImageService;
import com.chinkee.tmall.service.ProductService;
import com.chinkee.tmall.util.ImageUtil;
import com.chinkee.tmall.util.UploadedImageFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;

import java.util.List;

@Controller
@RequestMapping("")
public class ProductImageController {
    // 提供了list,add和delete方法。 edit和update没有相关业务，所以不提供

    @Autowired
    ProductService productService;
    @Autowired
    ProductImageService productImageService;

    @RequestMapping("admin_productImage_list")
    public String list(Model model, int pid){
        Product product = productService.get(pid); // 根据pid获取Product对象
        // 根据pid对象获取单个图片的集合pisSingle
        List<ProductImage> productImagesSingle                   // 调用常量方式
                = productImageService.list(pid, ProductImageService.type_single);
        // 根据pid对象获取详情图片的集合pisDetail
        List<ProductImage> productImagesDetail
                = productImageService.list(pid, ProductImageService.type_detail);

        // 把product对象，productImagesSingle ，productImagesDetail放在model上
        model.addAttribute("p", product);
        model.addAttribute("pisSingle", productImagesSingle);
        model.addAttribute("pisDetail", productImagesDetail);

        return "admin/listProductImage";
    }

    @RequestMapping("admin_productImage_add")
    public String add(ProductImage productImage, HttpSession httpSession,
                      UploadedImageFile uploadedImageFile){
        productImageService.add(productImage); // 增加产品，向数据库中插入数据

        // 文件命名以保存到数据库的产品图片对象的id+".jpg"的格式命名
        String fileName = productImage.getId() + ".jpg";
        String imageFolder; // 图片存放位置
        String imageFolder_small = null;
        String imageFolder_middle = null;
        if(ProductImageService.type_single.equals(productImage.getType())){
            /*
            根据session().getServletContext().getRealPath( "img/productSingle")，
            定位到存放单个产品图片的目录
            除了productSingle，还有productSingle_middle和productSingle_small。
            因为每上传一张图片，都会有对应的正常，中等和小的三种大小图片，并且放在3个不同的目录下
             */
            imageFolder = httpSession.getServletContext()
                    .getRealPath("img/productSingle");
            imageFolder_small = httpSession.getServletContext()
                    .getRealPath("img/productSingle_small");
            imageFolder_middle = httpSession.getServletContext()
                    .getRealPath("img/productSingle_middle");
        }
        else {
            imageFolder = httpSession.getServletContext()
                    .getRealPath("img/productDetail");
        }

        // 文件路径及文件名
        File file = new File(imageFolder, fileName);
        file.getParentFile().mkdirs(); // mkdirs()是创建多级目录。
        try {
            // 通过uploadedImageFile保存文件
            uploadedImageFile.getImage().transferTo(file);
            // 借助ImageUtil.change2jpg()方法把格式真正转化为jpg，而不仅仅是后缀名为.jpg
            BufferedImage image = ImageUtil.change2jpg(file);
            ImageIO.write(image, "jpg", file); // 写入图片

            // ImageUtil.resizeImage把正常大小的图片，改变大小之后，
            // 分别复制到productSingle_middle和productSingle_small目录下。
            if(ProductImageService.type_single.equals(productImage.getType())){
                File fileSmall = new File(imageFolder_small, fileName);
                File fileMiddle = new File(imageFolder_middle, fileName);

                // ImageUtil.resizeImage使用了swing自带的修改图片大小的API
                // 这方法包含写入图片文件方法
                ImageUtil.resizeImage(file, 56, 56, fileSmall);
                ImageUtil.resizeImage(file, 217, 190, fileMiddle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:admin_productImage_list?pid=" + productImage.getPid();
    }

    @RequestMapping("admin_productImage_delete")
    public String delete(int id, HttpSession httpSession){
        // 根据id获取ProductImage 对象productImage
        ProductImage productImage = productImageService.get(id);

        String fileName = productImage.getId() + ".jpg";
        String imageFolder; // 图片存放位置
        String imageFolder_small = null;
        String imageFolder_middle = null;

        // 如果是单个图片，那么删除3张正常，中等，小号图片
        // 如果是详情图片，那么删除一张图片
        if(ProductImageService.type_single.equals(productImage.getType())){
            imageFolder = httpSession.getServletContext()
                    .getRealPath("img/productSingle");
            imageFolder_small = httpSession.getServletContext()
                    .getRealPath("img/productSingle_small");
            imageFolder_middle = httpSession.getServletContext()
                    .getRealPath("img/productSingle_middle");

            File file = new File(imageFolder, fileName);
            File fileSmall = new File(imageFolder_small, fileName);
            File fileMiddle = new File(imageFolder_middle, fileName);

            file.delete();
            fileSmall.delete();
            fileMiddle.delete();
        }
        else {
            imageFolder = httpSession.getServletContext()
                    .getRealPath("img/productDetail");
            File file = new File(imageFolder, fileName);
            file.delete();
        }

        // 借助productImageService，删除数据
        productImageService.delete(id);

        return "redirect:admin_productImage_list?pid=" + productImage.getPid();
    }
}
