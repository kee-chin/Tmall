package com.chinkee.tmall.pojo;

public class CategoryBackup {

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
        //三元符号；trim方法去掉字符串头尾空白符
    }
}
