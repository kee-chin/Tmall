package com.chinkee.tmall.service.impl;

import com.chinkee.tmall.mapper.UserMapper;
import com.chinkee.tmall.pojo.User;
import com.chinkee.tmall.pojo.UserExample;
import com.chinkee.tmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public List<User> list() {
        UserExample userExample = new UserExample();
        userExample.setOrderByClause("id desc");
        return userMapper.selectByExample(userExample);
    }

    @Override
    public User get(int id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(User user) {
        userMapper.insert(user);
    }

    @Override
    public void delete(int id) {
        userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public boolean isExist(String name) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNameEqualTo(name);
        List<User> result = userMapper.selectByExample(userExample);
        if(!result.isEmpty())
            return true;
        return false;
    }

    @Override
    public User get(String name, String password) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andNameEqualTo(name).andPasswordEqualTo(password);
        // mapper中的类型就是List<User>
        List<User> users = userMapper.selectByExample(userExample);
        if(users.isEmpty())
            return null;
        return users.get(0);
    }
}
