/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab5.Module;

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
    public static void getUserOrder(String usr, JTable jt) {}
    // TODO 从数据库删除指定用户usr的ID为ticketID的指定订单
    public static void removeUserOrder(String usr, String ticketID) {}
}
