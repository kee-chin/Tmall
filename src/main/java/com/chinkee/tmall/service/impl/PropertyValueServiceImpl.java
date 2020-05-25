package com.chinkee.tmall.service.impl;

import com.chinkee.tmall.mapper.PropertyValueMapper;
import com.chinkee.tmall.pojo.Product;
import com.chinkee.tmall.pojo.Property;
import com.chinkee.tmall.pojo.PropertyValue;
import com.chinkee.tmall.pojo.PropertyValueExample;
import com.chinkee.tmall.service.PropertyService;
import com.chinkee.tmall.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyValueServiceImpl implements PropertyValueService {

    @Autowired
    PropertyValueMapper propertyValueMapper;
    @Autowired
    PropertyService propertyService;

    // 产品属性值内嵌在产品中，获取某种属性需通过属性管理
    @Override // 根据产品id获取所有的属性值
    public List<PropertyValue> list(int pid) {
        PropertyValueExample valueExample = new PropertyValueExample();
        valueExample.createCriteria().andPidEqualTo(pid);
        List<PropertyValue> propertyValues
                = propertyValueMapper.selectByExample(valueExample);
        for(PropertyValue propertyValue:propertyValues){
            Property property = propertyService.get(propertyValue.getPtid());
            propertyValue.setProperty(property);
        }
        return propertyValues; // 列出某种产品的所有属性值
    }

    @Override // 根据属性id和产品id获取PropertyValue对象
    public PropertyValue get(int ptid, int pid) { // get方法只有init方法调用
        PropertyValueExample valueExample = new PropertyValueExample();
        valueExample.createCriteria().andPtidEqualTo(ptid).andPidEqualTo(pid);
        List<PropertyValue> propertyValues
                = propertyValueMapper.selectByExample(valueExample);
        // isEmpty()分配了内存空间，值为空，是绝对的空，是一种有值（值 = 空）
        if(propertyValues.isEmpty()){
            return null; // 如果是没有PropertyValue对象，在init方法则生成一组属性值
        }
        // 获得某种产品的PropertyValue对象，get(0)最先的一组属性值，
        return propertyValues.get(0);
    }

    /* 这个方法的作用是初始化PropertyValue。 为什么要初始化呢？
    因为对于PropertyValue的管理，没有增加，只有修改。
    所以需要通过初始化来进行自动地增加，以便于后面的修改。
     */
    @Override
    public void init(Product product) {

        // 根据产品获取分类，然后获取这个分类下的所有属性集合
        List<Property> properties = propertyService.list(product.getCid());

        // 根据属性id和产品id去查询，看看这个属性和这个产品，是否已经存在属性值了。
        for(Property property:properties){
                                        // 调用本继承类的get方法
            PropertyValue propertyValue = get(property.getId(), product.getId());
            // 如果不存在，那么就创建一个属性值，并设置其属性id和产品id，接着插入到数据库中
            if(propertyValue == null){
                propertyValue = new PropertyValue();
                propertyValue.setPid(product.getId());
                propertyValue.setPtid(property.getId());
                propertyValueMapper.insert(propertyValue);
            }
        }
    }

    @Override
    public void update(PropertyValue propertyValue) {
        propertyValueMapper.updateByPrimaryKeySelective(propertyValue);
    }
}
