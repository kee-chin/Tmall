package com.chinkee.tmall.service.impl;

import com.chinkee.tmall.mapper.PropertyMapper;
import com.chinkee.tmall.pojo.Property;
import com.chinkee.tmall.pojo.PropertyExample;
import com.chinkee.tmall.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyServiceImpl implements PropertyService {

    @Autowired
    PropertyMapper propertyMapper;

    // 辅助查询 example类
    @Override
    public List<Property> list(int cid) {
        PropertyExample propertyExample = new PropertyExample();
        // 建立准则：和cid是否相等，即查询cid字段
        propertyExample.createCriteria().andCidEqualTo(cid);
        // 设置排序方式
        propertyExample.setOrderByClause("id asc");
        return propertyMapper.selectByExample(propertyExample);
    }

    @Override
    public void add(Property property) {
        propertyMapper.insert(property);
    }

    @Override
    public void delete(int id) {
        propertyMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Property property) {
        propertyMapper.updateByPrimaryKeySelective(property);
    }

    @Override
    public Property get(int id) {
        return propertyMapper.selectByPrimaryKey(id);
    }
}
