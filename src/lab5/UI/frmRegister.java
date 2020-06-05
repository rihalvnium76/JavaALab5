/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab5.UI;

import lab5.Module.*;
import java.sql.*;

import javax.swing.JOptionPane;

/* [3]注册窗口 */
public class frmRegister extends javax.swing.JFrame {
    private DBAccess db;
    private String usrTyp;

    public frmRegister() {
        initComponents();
        try {
            db = new DBAccess();
        } catch(SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        switch(WinCtrl.registerUserType) {
            case 0: usrTyp = "普通用户"; break;
            case 1: usrTyp = "管理员"; break;
            default: usrTyp = "未知类型"; break;
        }
        lbUserType.setText("注册用户类型：" + usrTyp);
    }

    // 注册用户
    // 返回值：false注册失败，true注册成功
    private boolean registerUser(String usr, String pwd) throws SQLException {
        PreparedStatement pst = db.getConnection().prepareStatement("insert into Users values(?,?,?,?)");
        pst.setString(1, getAvailableUserID()); // UserID
        pst.setString(2, usr);
        pst.setString(3, pwd);
        pst.setString(4, usrTyp); // userType
        int rs = pst.executeUpdate();
        pst.close();
        return rs>0? true: false;
    }
    // 获取未使用的userID
    // 要求：userID全部为数字
    private String getAvailableUserID() throws SQLException {
        ResultSet rs = db.queryDB("select userid from users order by userid desc");
        String lastID = null; // 当前表里最大的ID
        if(rs.next()) lastID = rs.getString(1);
        db.releaseQuery();
        int ret = -1;
        try {
            // ID转为整数+1
            ret = Integer.parseInt(lastID) + 1;
        } catch(IllegalArgumentException e) {
            // 表最大ID非整数，无法转化为整数再+1
            // 则使用当前系统时间取余
            boolean isLegal = false;
            while(!isLegal) {
                ret = (int)(System.currentTimeMillis() % 65536);
                PreparedStatement pst = db.getConnection().prepareStatement("select userid from users where userid=?");
                pst.setString(1, String.valueOf(ret));
                ResultSet rs1 = pst.executeQuery();
                if(!rs1.next()) isLegal = true; // 确定这个ID没被使用
                rs1.close();
                pst.close();
            }
        }
        return String.valueOf(ret);
    }

    // 销毁窗体
    void destroy() {
        try {
            db.close();
        }
        catch(SQLException e) {
            e.printStackTrace(); 
        }
        this.dispose();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnRegister = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        lbUserType = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        tfUser = new javax.swing.JTextField();
        pfPassword = new javax.swing.JPasswordField();
        pfConfirmPwd = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("注册");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        btnRegister.setText("注册");
        btnRegister.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRegisterMouseClicked(evt);
            }
        });

        btnBack.setText("返回");
        btnBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBackMouseClicked(evt);
            }
        });

        lbUserType.setText("注册用户类型：");

        jLabel2.setText("用户名：");

        jLabel3.setText("密码：");

        jLabel4.setText("确认密码：");

        pfPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pfPasswordActionPerformed(evt);
            }
        });

        pfConfirmPwd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pfConfirmPwdActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pfPassword)
                            .addComponent(tfUser)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pfConfirmPwd))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbUserType, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 112, Short.MAX_VALUE)
                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbUserType, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tfUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(pfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(pfConfirmPwd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // 窗体关闭
        destroy();
    }//GEN-LAST:event_formWindowClosing

    private void btnBackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBackMouseClicked
        // 返回
        frmLogin.main(null);
        destroy();
    }//GEN-LAST:event_btnBackMouseClicked

    private void btnRegisterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegisterMouseClicked
        // 注册
        String[] input = new String[] {
            tfUser.getText(), // 用户名
            new String(pfPassword.getPassword()), // 密码
            new String(pfConfirmPwd.getPassword()) // 确定密码
        };
        if(input[1].equals(input[2]) && input[1].length()>3 && input[1].length()<20) {
            try {
                boolean rt = registerUser(input[0], input[1]);
                if(rt) {
                    JOptionPane.showMessageDialog(this, "注册成功", "注册", JOptionPane.INFORMATION_MESSAGE);
                    frmLogin.main(null); // 返回
                    this.dispose();
                }
            } catch(SQLException e) {
                JOptionPane.showMessageDialog(this, "注册失败！该用户名已被注册", "注册", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else
            JOptionPane.showMessageDialog(this, "密码长度要大于3位而小于20位！", "错误", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_btnRegisterMouseClicked

    private void pfPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pfPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pfPasswordActionPerformed

    private void pfConfirmPwdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pfConfirmPwdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pfConfirmPwdActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        lab5.Lab5.setUIStyle(frmRegister.class.getName());
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmRegister().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnRegister;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel lbUserType;
    private javax.swing.JPasswordField pfConfirmPwd;
    private javax.swing.JPasswordField pfPassword;
    private javax.swing.JTextField tfUser;
    // End of variables declaration//GEN-END:variables
}
