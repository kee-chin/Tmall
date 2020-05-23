package com.chinkee.tmall.service.impl;

import com.chinkee.tmall.mapper.ProductMapper;
import com.chinkee.tmall.pojo.Category;
import com.chinkee.tmall.pojo.Product;
import com.chinkee.tmall.pojo.ProductExample;
import com.chinkee.tmall.pojo.ProductImage;
import com.chinkee.tmall.service.CategoryService;
import com.chinkee.tmall.service.ProductImageService;
import com.chinkee.tmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductMapper productMapper;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductImageService productImageService;

    @Override
    public List list(int cid) {
        ProductExample productExample = new ProductExample();
        productExample.createCriteria().andCidEqualTo(cid);
        productExample.setOrderByClause("id desc");
        // 取出来的Product对象设置上对应的category
        List result = productMapper.selectByExample(productExample); // 获取全部并列出
        setCategory(result); // 层层调用该方法
        // 调用setFirstProductImage(List<Product> products)为多个产品设置图片
        setFirstProductImage(result);
        return result;
    }

    private void setCategory(List<Product> products) {
        for(Product product:products){
            setCategory(product);
        }
    }

    private void setCategory(Product product) {
        int cid = product.getCid();
        Category category = categoryService.get(cid);// 注入categoryService
        product.setCategory(category);
    }

    @Override
    public void add(Product product) {
        productMapper.insert(product);
    }

    @Override
    public void delete(int id) {
        productMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Product product) {
        productMapper.updateByPrimaryKeySelective(product);// sql语句有选择更新
    }

    @Override
    public Product get(int id) {
        Product product = productMapper.selectByPrimaryKey(id); // 获取单个，及其全部属性（编辑更改）
        setCategory(product); // 取出来的Product对象设置上对应的category
        setFirstProductImage(product); // 调用setFirstProductImage(Product product)为单个产品设置图片
        return product;
    }

    @Override           // get方法使用
    public void setFirstProductImage(Product product) {
        // 根据pid和图片类型查询出所有的单个图片
        List<ProductImage> productImages
                = productImageService.list(product.getId(),
                ProductImageService.type_single);

        // 把第一个取出来放在firstProductImage上
        if(!productImages.isEmpty()){
            ProductImage productImage = productImages.get(0);
            product.setFirstProductImage(productImage);
        }
    }

    // 给多个产品设置图片，list方法中使用
    public void setFirstProductImage(List<Product> products){
        for(Product product:products){
            setFirstProductImage(product);
        }
    }

}
