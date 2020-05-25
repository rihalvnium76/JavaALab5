/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab5.Module;

import java.sql.*;

/* 数据库访问类 */
public class DBAccess {
    private Connection ct;  // 连接对象
    private Statement st; // 临时Statement
    private ResultSet rs; // 临时ResultSet对象
    
    public static final String DEFAULT_DB_URL = "127.0.0.1:1433"; // 默认数据库地址
    public static final String DEFAULT_DB_NAME = "MovieDB"; // 默认数据库名
    public static final String DEFAULT_DB_USER = "sa"; // 默认登录用户名
    public static final String DEFAULT_DB_PASSWORD = "123456"; // 默认登陆密码
    
    // 创建数据库Connection对象
    // 参数：数据库地址（IP:端口），数据库名，用户名，密码
    public DBAccess(String dbUrl, String dbName, String usr, String pwd) throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String url = "jdbc:sqlserver://" + dbUrl + ";DatabaseName=" + dbName;
        ct = DriverManager.getConnection(url,usr,pwd);
    }
    // 创建数据库Connection对象
    public DBAccess() throws ClassNotFoundException, SQLException {
        this(DEFAULT_DB_URL, DEFAULT_DB_NAME, DEFAULT_DB_USER, DEFAULT_DB_PASSWORD);
    }
    // 获取Connection对象
    public Connection getConnection() {
        return ct;
    }
    // 关闭数据库连接
    public void close() throws SQLException {
        if(ct!=null) {
            ct.close();
            ct = null;
        }
    }

    // 执行sql查询语句
    // 返回值：ResultSet（查询结果）
    // 注意：查询结果解析往后必须调用releaseQuery()函数释放Statement和ResultSet对象
    public ResultSet queryDB(String sql) throws SQLException {
        st = ct.createStatement();
        rs = st.executeQuery(sql);
        return rs;
    }
    // 释放临时的Statement和ResultSet对象
    public void releaseQuery() throws SQLException {
        if(rs!=null) { rs.close(); rs = null; }
        if(st!=null) { st.close(); st = null; }
    }
    // 执行sql修改语句，返回值为受影响的行数
    public int modifyDB(String sql) throws SQLException {
        Statement st = ct.createStatement();
        int rt = st.executeUpdate(sql);
        st.close();
        return rt;
    }

    // TODO 登录校验
    // 参数：用户名，密码
    // 返回值：错误0，密码正确且为普通用户1，密码正确且为管理员1
    public int verifyLogin(String usr, String pwd) {
        // TODO 查询数据请用上述queryDB()和releaseQuery()函数
        return 0;
    }
    // TODO 注册用户
    // 参数：用户名，密码，用户类型(0 普通用户 1 管理员，传Module.WinCtrl.registerUserType的值进来即可)
    // 返回值：正确true，错误false
    public boolean registerUser(String usr, String pwd, int userType) {
        // TODO 查询数据请用上述modifyDB()函数
        return false;
    }
}
