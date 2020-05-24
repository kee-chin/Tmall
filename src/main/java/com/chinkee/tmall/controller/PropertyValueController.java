package com.chinkee.tmall.controller;

import com.chinkee.tmall.pojo.Product;
import com.chinkee.tmall.pojo.PropertyValue;
import com.chinkee.tmall.service.ProductService;
import com.chinkee.tmall.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("")
public class PropertyValueController {

    @Autowired
    PropertyValueService propertyValueService;

    @Autowired
    ProductService productService;

    // 通过产品管理界面的设置属性，跳到编辑页面
    @RequestMapping("admin_propertyValue_edit")
    public String edit(Model model, int pid){
        // 根据pid获取product对象，因为面包屑导航里需要显示产品的名称和分类的连接。
        Product product = productService.get(pid);
        // 初始化属性值： propertyValueService.init(p)。
        // 因为在第一次访问的时候，这些属性值是不存在的，需要进行初始化。
        propertyValueService.init(product);
        // 根据产品，获取其对应的属性值集合
        List<PropertyValue> propertyValues = propertyValueService.list(product.getId());

        model.addAttribute("p", product);
        model.addAttribute("pvs", propertyValues);
        return "admin/editPropertyValue";
    }

    /*
     admin_propertyValue_update导致PropertyValueController的update方法被调用
     参数 PropertyValue 获取浏览器Ajax方式提交的参数
     通过 propertyValueService.update(propertyValue) 更新到数据库
     结合方法update上的注解@ResponseBody和return "success" 就会向浏览器返回字符串 "success"
     propertyValueService调用的是propertValueMapper.updateByPrimaryKeySelective(pv);
     这个方法只会更新propertyValue存在的字段，而参数PropertyValue只有id和value有值，
     所以即便这个PropertyValue对象没有pid和ptid值，修改的时候也不会影响该PropertyValue的pid和ptid。
     */
    @RequestMapping("admin_propertyValue_update")
    @ResponseBody
    public String update(PropertyValue propertyValue){
        propertyValueService.update(propertyValue);
        return "success";
    }
}
