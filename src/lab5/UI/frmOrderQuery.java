/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab5.UI;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import lab5.Module.DBAccess;
import lab5.Module.WinCtrl;

/* [6]个人订单查询 */
public class frmOrderQuery extends javax.swing.JFrame {
    private DBAccess db; // 数据库访问对象
    /**
     * Creates new form frmOrderQuery
     */
    public frmOrderQuery() {
        initComponents();
        this.setTitle("个人订单 - 用户：" + WinCtrl.currentLoginUser);
        try {
            db = new DBAccess();
        } catch(SQLException|ClassNotFoundException e) {
            e.printStackTrace();
        }
        loadData();
    }
    void loadData() {
        //显示当前登录用户的订单
        DefaultTableModel dtm = (DefaultTableModel) TicketList.getModel();
        try {
            PreparedStatement pstQue = db.getConnection().prepareStatement("select Ticket.TicketID,Movie.MovieName,Theater.TheaterName,ScheduleTime,Ticket.Row,Ticket.Col,price,Ticket.Status from Ticket,Schedule,Movie,Users,Theater where Theater.TheaterID=Schedule.TheaterID and Ticket.ScheduleID=Schedule.ScheduleID and Schedule.MovieID=Movie.MovieID and Ticket.UserID=Users.UserID and Users.LoginName=?");
            pstQue.setString(1, WinCtrl.currentLoginUser);
            //执行查询 SQL 语句，返回查询的结果集         
            ResultSet rs = pstQue.executeQuery( ); 
            while(rs.next()){
                Vector<String> v=new Vector<String>();
                v.add(rs.getString(1));
                v.add(rs.getString(2));
                v.add(rs.getString(3));
                v.add(rs.getString(4));
                v.add(rs.getInt(5)+ "-" +rs.getInt(6));
                v.add(String.valueOf(rs.getFloat(7)));
                v.add(rs.getString(8));
                dtm.addRow(v);
            }
            rs.close();
            pstQue.close();
        }catch(SQLException e) {
            //JOptionPane.showMessageDialog(null, e.toString(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        TicketList = new javax.swing.JTable();
        Unsubscribe = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        TicketList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "电影票ID", "电影名", "放映厅", "场次", "座位", "价格", "状态"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TicketList.getTableHeader().setReorderingAllowed(false);
        TicketList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TicketListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TicketList);

        Unsubscribe.setText("退订");
        Unsubscribe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UnsubscribeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Unsubscribe)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Unsubscribe)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TicketListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TicketListMouseClicked

    }//GEN-LAST:event_TicketListMouseClicked

    private void UnsubscribeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UnsubscribeActionPerformed
        //退订按钮
        DefaultTableModel dtm = (DefaultTableModel) TicketList.getModel();
        int r = TicketList.getSelectedRow();
        if(JOptionPane.showConfirmDialog(null, "是否删除该订单？", "警告", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
            try {
                PreparedStatement pstDel = db.getConnection().prepareStatement("delete from Ticket where TicketID=?");
                pstDel.setObject(1, TicketList.getValueAt(r, 0)); 
                pstDel.executeUpdate();
                pstDel.close();
            }catch(SQLException e) {
                //JOptionPane.showMessageDialog(null, e.toString(), "错误", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }
        }
        else{
            return;
        }
        dtm.removeRow(r);
    }//GEN-LAST:event_UnsubscribeActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // 窗体关闭
        try { 
            db.close();
        } catch(SQLException e) {
            e.printStackTrace(); 
        }
        this.dispose();
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        lab5.Lab5.setUIStyle(frmOrderQuery.class.getName());
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmOrderQuery().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TicketList;
    private javax.swing.JButton Unsubscribe;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
