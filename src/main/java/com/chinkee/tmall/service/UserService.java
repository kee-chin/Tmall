package com.chinkee.tmall.service;

import com.chinkee.tmall.pojo.User;

import java.util.List;

public interface UserService {
    List list();
    User get(int id);
    void add(User user);
    void delete(int id);
    void update(User user);

    boolean isExist(String name);

    User get(String name, String password);
}
