/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab5.Module;

import java.sql.*;

public class DBAccess {
    private Connection ct;
    
    public static final String DEFAULT_DB_URL = "127.0.0.1:1433"; // 默认数据库地址
    public static final String DEFAULT_DB_NAME = "MovieDB"; // 默认数据库名
    public static final String DEFAULT_DB_USER = "sa"; // 默认登录用户名
    public static final String DEFAULT_DB_PASSWORD = "123456"; // 默认登陆密码
    
    // 创建数据库Connection对象
    // 参数：数据库地址（IP:端口），数据库名，用户名，密码
    public DBAccess(String dbUrl, String dbName, String usr, String pwd) {}
    // 创建数据库Connection对象
    public DBAccess() {}
    // 获取Connection对象
    public Connection getConnection() {
        return ct;
    }
    // 关闭数据库连接
    public void close() {}
}
