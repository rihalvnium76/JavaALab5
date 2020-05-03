/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab5.Module;

import javax.swing.JTable;

/* 窗体控制类 */
public class WinCtrl {
    // 管理员登录/注册用户类型选择
    public enum UserTypeChoice {
        COMMON_USER(0), ADMINISTRATOR(1); // 普通用户 管理员
        private int val;
        UserTypeChoice(int userType) {
            val = userType;
        }
    }
    // 当前登录用户
    public static String currentLoginUser;

    // TODO 从数据库获取指定用户usr所有的订单并写入到JTable中
    public static void getUserOrder(String usr, JTable jt) {}
    // TODO 从数据库删除指定用户usr的ID为ticketID的指定订单
    public static void removeUserOrder(String usr, String ticketID) {}
}
