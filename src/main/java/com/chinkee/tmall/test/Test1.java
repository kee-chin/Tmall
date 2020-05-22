package com.chinkee.tmall.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// 数据库分类表插入数据，与项目无关

// 借助JDBC, 运行代码，创建10条分类测试数据。
// 注：现用JDBC创建 增加功能时用mybatis创建测试数据（SSM）。
public class Test1 {
    public static void main(String args[]){

        try{
            //驱动类com.mysql.jdbc.Driver
            //就在 mysql-connector-java-5.0.8-bin.jar中
            //如果没有导入包，就会抛出ClassNotFoundException
            Class.forName("com.mysql.cj.jdbc.Driver"); // Class.forname 获取类对象
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        try(
                // 建立与数据库的Connection连接
                // 这里需要提供：
                // 数据库所处于的ip:127.0.0.1 (本机)
                // 数据库的端口号： 3306 （mysql专用端口号）
                // 数据库名称 tmall_ssm
                // 编码方式 UTF-8
                // 账号 root
                // 密码 admin
                Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/tmall_ssm?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone = GMT",
                "root", "admin");
                Statement statement = connection.createStatement()
                )
        {
            for (int i=0; i<=12; i++){
                String sqlSentence = "insert into category values (null, '测试分类%d')";
                String sql = String.format(sqlSentence, i); // 字符串格式化
                statement.execute(sql);
            }

            System.out.println("成功创建10条测试数据");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
