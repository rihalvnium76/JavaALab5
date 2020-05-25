/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab5.Module;

import java.io.*;
import java.util.Enumeration;

import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

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

    // 当前登录用户名
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
    
    /* JTree操作函数 */
    // 添加结点
    public static DefaultMutableTreeNode addTreeNode(JTree tree, DefaultMutableTreeNode selNode, String nodeName) {
        if(selNode==null) return null;
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(nodeName);
        model.insertNodeInto(newNode, selNode, selNode.getChildCount());
        return newNode;
    }
    // 获取选中结点
    public static DefaultMutableTreeNode getSelectedTreeNode(JTree tree) {
        return (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
    }
    // 修改结点
    public static void setTreeNodeText(DefaultMutableTreeNode node, String str) {
        node.setUserObject(str);
    }
    // 删除结点
    public static void deleteTreeNode(JTree tree, DefaultMutableTreeNode selNode) {
        ((DefaultTreeModel)tree.getModel()).removeNodeFromParent(selNode);
    }
    // 查找结点
    public static DefaultMutableTreeNode searchTreeNode(JTree tree, String nodeStr) {
        DefaultMutableTreeNode ret;
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        // 获取根节点
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)model.getRoot();
        // 枚举
        Enumeration e = rootNode.breadthFirstEnumeration();
        while(e.hasMoreElements()) {
            ret = (DefaultMutableTreeNode) e.nextElement();
            if(nodeStr.equals(ret.getUserObject().toString()))
                return ret;
        }
        return null;
    }
    // 展开父节点
    public static void unfoldTreeNode(JTree tree, DefaultMutableTreeNode node) {
        // 获取从根节点导到指定结点的所有结点
        TreeNode[] nodes = ((DefaultTreeModel)tree.getModel()).getPathToRoot(node);
        // 使用指定的结点数组创建TreePath
        TreePath path = new TreePath(nodes);
        // 显示指定的TreePath
        tree.scrollPathToVisible(path);
    }
}
