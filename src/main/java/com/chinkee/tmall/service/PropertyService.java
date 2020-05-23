package com.chinkee.tmall.service;

import com.chinkee.tmall.pojo.Property;

import java.util.List;

public interface PropertyService {

    // C:P为一对多，在业务上需要查询某个分类下的属性，所以list方法会带上对应分类的id。
    List<Property> list(int cid); // 获取列表，返回一列表对象，需要返回值，内嵌在分类页面

    void add(Property property); // 增，增加整个对象

    void delete(int id); // 删，通过id删除

    void update(Property property); // 改，更新整个对象

    Property get(int id); // 查，通过id查询，返回整个对象
}
