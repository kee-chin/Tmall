package com.chinkee.tmall.service.impl;

import com.chinkee.tmall.mapper.ProductImageMapper;
import com.chinkee.tmall.pojo.ProductImage;
import com.chinkee.tmall.pojo.ProductImageExample;
import com.chinkee.tmall.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImageServiceImpl implements ProductImageService {

    @Autowired
    ProductImageMapper productImageMapper;

    @Override
    public List list(int pid, String type) {
        ProductImageExample productImageExample = new ProductImageExample();
        productImageExample.createCriteria()
                .andPidEqualTo(pid).andTypeEqualTo(type);

        // 不用分页
        productImageExample.setOrderByClause("id asc");
        return productImageMapper.selectByExample(productImageExample);
    }

    @Override
    public void add(ProductImage productImage) {
        productImageMapper.insert(productImage);
    }

    @Override
    public void delete(int id) {
        productImageMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(ProductImage productImage) {
        productImageMapper.updateByPrimaryKeySelective(productImage);
    }

    @Override
    public ProductImage get(int id) {
        return productImageMapper.selectByPrimaryKey(id);
    }
}
