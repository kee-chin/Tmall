package com.chinkee.tmall.service;

import com.chinkee.tmall.pojo.Product;
import com.chinkee.tmall.pojo.PropertyValue;

import java.util.List;

public interface PropertyValueService {

    List<PropertyValue> list(int pid);

    PropertyValue get(int ptid, int pid); // pid产品id ptid属性id

    void init(Product product);

    void update(PropertyValue propertyValue); //
}
