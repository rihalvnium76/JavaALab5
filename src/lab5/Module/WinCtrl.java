/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab5.Module;

import java.io.*;
import java.util.Enumeration;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
//import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/* 窗体控制类 */
public class WinCtrl {

    /*
        状态公共变量区
    */

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

    // 当前选择的要查看详细信息的电影的ID
    public static String currentSelectedMovieID;

    /*
        功能函数区
    */
    
    // 返回res目录路径
    public static String getResDirPath() throws IOException {
        String ret = new File("").getCanonicalPath() + File.separator + "res" + File.separator;
        return ret;
    }
    // 返回res/image目录路径
    public static String getImageDirPath() throws IOException {
        String ret = new File("").getCanonicalPath() + File.separator + "res" + File.separator + "image" + File.separator;
        return ret;
    }
    // 给JLabel设置图片并适配控件大小
    // 参数：lb JLabel控件
    //   imgPath 图片路径，使用例：getImageDirPath() + File.separator + 文件名
    // 说明：File.separator为路径分隔符
    public static void setLabelImage(JLabel lb, String imgPath) {
        ImageIcon image = new ImageIcon(imgPath);
        image.setImage(image.getImage().getScaledInstance(lb.getWidth(), lb.getHeight(), Image.SCALE_DEFAULT));
        lb.setIcon(image);
    }
    // 对JLabel设置电影海报
    // 参数：lb JLabel控件
    //   imgName 放在/res/image下的海报图像名
    public static void setLabelMoviePoster(JLabel lb, String imgName) throws IOException {
        setLabelImage(lb, getImageDirPath() + File.separator + imgName);
    }

    /* JTree操作函数 */

    // 添加结点
    public static DefaultMutableTreeNode addTreeNode(JTree tree, DefaultMutableTreeNode selNode, String nodeName) {
        if(selNode==null) return null;
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(nodeName);
        model.insertNodeInto(newNode, selNode, selNode.getChildCount());
        
        // 展开结点
        TreeNode[] nodes = model.getPathToRoot(newNode);
        TreePath path = new TreePath(nodes);
        tree.scrollPathToVisible(path);
        return newNode;
    }
    // 获取选中结点
    public static DefaultMutableTreeNode getSelectedTreeNode(JTree tree) {
        return (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
    }
    // 修改结点
    public static void setTreeNodeText(DefaultMutableTreeNode node, String str) {
        if(node==null) return;
        node.setUserObject(str);
    }
    // 删除结点
    public static void deleteTreeNode(JTree tree, DefaultMutableTreeNode selNode) {
        if(selNode==null) return;
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
    // 获取根节点
    public static DefaultMutableTreeNode getTreeRootNode(JTree tree) {
        return (DefaultMutableTreeNode)((DefaultTreeModel)tree.getModel()).getRoot();
    }
    // 清空树
    public static void clearTree(JTree tree) {
        getTreeRootNode(tree).removeAllChildren();
        tree.updateUI();
    }
}
