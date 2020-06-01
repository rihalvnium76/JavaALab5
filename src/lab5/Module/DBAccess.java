/*
Ticket
    TicketID(Primary Key)：票名：ID均为数字编号字符串
    UserID：用户，未购票该值为0
    Row(int)：行数
    Col(int)：列数
    ScheduleID：计划ID
    Status: 购买状态：值可为“已购”或“未购”
    Ticket表要加上 UserID varchar(10) foreign key references Users(UserID)
Theater
    TheaterID(Primary Key)：放映厅ID
    TheaterName：放映厅名
    Capacity(int类型)：容量
Movie
    MovieID(Primary Key)：电影ID
    MovieName：电影名
    Director: 导演
    MainActors:主演
    MoviePoster(varchar(255)类型)：电影海报，图片文件名：
    值例：xxxx.jpg
    MovieType：电影类型
    price(float(2)类型)：价格
    MovieInfo(text类型): 电影简介
Users
    UserID(Primary Key)：用户ID
    LoginName：登录名
    Password：密码
    UserType：用户类型：值可为“普通用户”或“管理员”
Schedule
    ScheduleID(Primary Key)：计划ID
    ScheduleTime(datetime类型)：场次
    MovieID：电影ID
    TheaterID：放映厅ID
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
}
