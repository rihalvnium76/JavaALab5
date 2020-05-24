/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab5.Module;

import java.io.*;
import javax.swing.JTable;

/* 窗体控制类 */
public class WinCtrl {
    // 界面1所选择的按钮
    // 值：0 管理员登录 1 注册
    public static int loginFormSelectedButton;
    // 要注册的用户类型
    // 值：0 普通用户 1 管理员
    public static int registerUserType;
    // 是否为重设新密码
    public static boolean isResetPassword;

    // 当前登录用户
    public static String currentLoginUser;

    // TODO 从数据库获取指定用户usr所有的订单并写入到JTable中
    /*
        电影票ID(Ticket.TicketID)
        电影院(Ticket.ScheduleID->TheaterID->TheaterName)
        场次(Ticket.Schedule->ScheduleTime)
        座位(第Ticket.Row行 第Ticket.Columns列)
        价格(Ticket.ScheduleID->MovieID->price)
        状态(Ticket.Status)
    */
    public static void getUserOrder(String usr, JTable jt) {}
    // TODO 从数据库删除指定用户usr的ID为ticketID的指定订单
    public static void removeUserOrder(String usr, String ticketID) {}
    
    // 返回res目录路径
    public static String getResDirPath() throws IOException {
        // File.separator为路径分隔符
        String ret = new File("").getCanonicalPath() + File.separator + "res" + File.separator;
        return ret;
    }
    // 返回res/image目录路径
    public static String getImageDirPath() throws IOException {
        String ret = new File("").getCanonicalPath() + File.separator + "res" + File.separator + "image" + File.separator;
        return ret;
    }

    // 传递给[5]电影详细信息窗口的选择的电影的ID
    public static String selectedMovie = null;
    
}
