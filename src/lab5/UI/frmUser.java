/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab5.UI;

import java.io.*;
import java.sql.*;
import java.util.*;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import lab5.Module.*;

/* [4]票务查询 */
public class frmUser extends javax.swing.JFrame {
    private DBAccess db;
    class DBItem {
        public String ticketID, movieID, movieName, status;
        public Timestamp scheduleTime;
        public int row, col;
        public String theaterName;

        public static final int MAX_INDEX = 7;

        public void setValueByIndex(int index, Object value) throws IndexOutOfBoundsException {
            switch(index) {
                case 0: ticketID = (String)value; break;
                case 1: movieID = (String)value; break;
                case 2: movieName = (String)value; break;
                case 3: status = (String)value; break;
                case 4: scheduleTime = (Timestamp)value; break;
                case 5: row = (Integer)value; break;
                case 6: col = (Integer)value; break;
                case 7: theaterName = (String)value; break;
                default: throw new IndexOutOfBoundsException("DBItem无效索引: "+index);
            }
        }
    }
    ArrayList<DBItem> dataList;

    public frmUser() {
        initComponents();
        // 初始化
        dataList = new ArrayList<DBItem>();
        try {
            db = new DBAccess();
            Thread t = new LoadMovieThread();
            t.start();
            t.join();
        } catch (ClassNotFoundException | SQLException e) {
            //JOptionPane.showMessageDialog(this, ex.toString(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1); // 退出
        } catch(InterruptedException e) {}
    }

    private class LoadMovieThread extends Thread {
        @Override
        public void run() {
            try {
                loadMovie();
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /*
        ---LOAD CODE---
    */

    // load movie data to tree

    void loadMovie() throws SQLException {
        loadMovie(""); // 模糊搜索 全部
    }
    void loadMovie(String searchMovie) throws SQLException {
        WinCtrl.clearTree(treeNavi); // 清空树

        PreparedStatement pst = db.getConnection().prepareStatement("select movieid,moviename from movie where moviename like ?");
        pst.setString(1, "%" + searchMovie + "%"); // 模糊搜索
        ResultSet rs = pst.executeQuery();
        DefaultMutableTreeNode rootNode = WinCtrl.getTreeRootNode(treeNavi);
        while(rs.next()) {
            DefaultMutableTreeNode movieNode = WinCtrl.addTreeNode(treeNavi, rootNode, rs.getString(2)); // movieName
            loadSchedule(movieNode, rs.getString(1)); // by movieID
        }
        rs.close();
        pst.close();
    }
    void loadSchedule(DefaultMutableTreeNode movieNode, String movieID) throws SQLException {
        PreparedStatement pst = db.getConnection().prepareStatement("select theater.theatername,schedule.scheduletime from schedule,theater,ticket where schedule.theaterid=theater.theaterid and schedule.movieid=? and ticket.scheduleid=schedule.scheduleid");
        // ticket.scheduleid=schedule.scheduleid : 过滤没有实际安排的计划
        pst.setString(1, movieID);
        ResultSet rs = pst.executeQuery();
        while(rs.next())
            WinCtrl.addTreeNode(treeNavi, movieNode, rs.getString(1) + "|" + rs.getTimestamp(2).toString()); // theaterName | scheduleTime
        rs.close();
        pst.close();
    }

    // load data to arraylist and table

    class LoadDataToTable {
        private String movieName, theaterName, scheduleTime;

        public LoadDataToTable(String movieName) throws SQLException {
            this(movieName, null, null);
        }
        public LoadDataToTable(String movieName, String theaterName, String scheduleTime) throws SQLException {
            this.movieName = movieName;
            this.theaterName = theaterName;
            this.scheduleTime = scheduleTime;

            writeArrayList();
            writeTableFromArray();
        }

        private void writeArrayList() throws SQLException {
            boolean scheduleExist = theaterName!=null && scheduleTime!=null;
            PreparedStatement pst = db.getConnection().prepareStatement("select ticket.ticketid,movie.movieid,movie.moviename,ticket.status,schedule.scheduletime,ticket.row,ticket.col,theater.theaterName from ticket,schedule,movie,theater where ticket.scheduleid=schedule.scheduleid and schedule.movieid=movie.movieid and schedule.theaterid=theater.theaterid and movie.moviename like ?" + (scheduleExist? " and theater.theatername like ? and schedule.scheduletime=?": ""));
            pst.setString(1, "%" + movieName + "%");
            if(scheduleExist) {
                pst.setString(2, theaterName);
                pst.setTimestamp(3, Timestamp.valueOf(scheduleTime));
            }
            ResultSet rs = pst.executeQuery();
            dataList.clear(); // 清空ArrayList
            while(rs.next()) {
                DBItem d = new DBItem();
                for(int i=0; i<=DBItem.MAX_INDEX; ++i)
                    d.setValueByIndex(i, rs.getObject(i+1));
                dataList.add(d);
            }
            pst.close();
        }
        private void writeTableFromArray() throws SQLException {
            DefaultTableModel model = (DefaultTableModel)tbMovieList.getModel();
            model.setRowCount(0); // 清空table
            for(DBItem d : dataList)
                model.addRow(new Object[] {
                    d.movieName,
                    d.theaterName,
                    d.scheduleTime.toString(),
                    d.row + "-" + d.col,
                    d.status
                });
        }
    }
    // 根据Tree选择添加数据到Table
    // 返回值：电影名
    String loadDataToTable() throws SQLException {
        LoadDataToTable loadData;
        DefaultMutableTreeNode selNode = WinCtrl.getSelectedTreeNode(treeNavi);
        String nodeStr = (String)selNode.getUserObject();
        if(selNode!=null && nodeStr!=null)
            switch(selNode.getLevel()) {
                case 1: // movie only
                    loadData = new LoadDataToTable(nodeStr);
                    return nodeStr;
                case 2: // movie+schedule
                    String[] sch = nodeStr.split("\\|", 2);
                    String movName = (String)((DefaultMutableTreeNode)selNode.getParent()).getUserObject();
                    loadData = new LoadDataToTable(movName, sch[0], sch[1]);
                    return movName;
            }
        return null;
    }
    /*
        ---LOAD CODE END---
    */

    void writeIntroduction(String movieName) throws SQLException, IOException {
        PreparedStatement pst = db.getConnection().prepareStatement("select director,mainactors,movietype,price,movieposter from movie where moviename like ?");
        pst.setString(1, movieName);
        ResultSet rs = pst.executeQuery();
        if(rs.next()) {
            taMovieInfo.setText(
                "电影名：" + movieName +
                "\n导演：" + rs.getString(1) +
                "\n主要演员：" + rs.getString(2) +
                "\n类型：" + rs.getString(3) +
                "\n价格：" + String.valueOf(rs.getFloat(4)));
            WinCtrl.setLabelMoviePoster(lbPoster, rs.getString(5)); // movie poster
        }
        pst.close();
    }

    String findUserIDByName(String userName) throws SQLException {
        String ret = null;
        if(userName!=null) {
            PreparedStatement pst = db.getConnection().prepareStatement("select userid from users where loginname=?");
            pst.setString(1, userName);
            ResultSet rs = pst.executeQuery();
            if(rs.next()) ret = rs.getString(1); // userid
            rs.close();
            pst.close();
        }
        return ret;
    }

    // 电影票订票
    void bookTicket() throws SQLException {
        int[] rows = tbMovieList.getSelectedRows();
        if(rows.length==0) return;

        // index(rows)->ArrayList->ticketID->Status
        PreparedStatement pst = db.getConnection().prepareStatement("update Ticket set userid=?, status=? where ticketid=?");
        for(int index : rows) {
            DBItem item = dataList.get(index);
            if(item.status.equals("未售")) {
                pst.clearParameters();
                pst.setString(1, findUserIDByName(WinCtrl.currentLoginUser)); // userid
                pst.setString(2, "已售");
                pst.setString(3, item.ticketID);
                pst.executeUpdate();
            }
        }
        pst.close();
    }

    // =========== CUSTOM CODE END =========== //

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnClear = new javax.swing.JButton();
        btnQuery = new javax.swing.JButton();
        tfQueryMovie = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lbPoster = new javax.swing.JLabel();
        btnDetail = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        taMovieInfo = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        treeNavi = new javax.swing.JTree();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbMovieList = new javax.swing.JTable();
        btnBook = new javax.swing.JButton();
        btnCancelSel = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("查询"));

        btnClear.setText("清空");
        btnClear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnClearMouseClicked(evt);
            }
        });

        btnQuery.setText("查询");
        btnQuery.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnQueryMouseClicked(evt);
            }
        });

        jLabel2.setText("电影名");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnQuery)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnClear))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfQueryMovie, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tfQueryMovie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnQuery)
                    .addComponent(btnClear))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("电影简介"));

        lbPoster.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbPoster.setText("无封面");

        btnDetail.setText("详细信息");
        btnDetail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDetailMouseClicked(evt);
            }
        });
        btnDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetailActionPerformed(evt);
            }
        });

        taMovieInfo.setEditable(false);
        taMovieInfo.setBackground(new java.awt.Color(240, 240, 240));
        taMovieInfo.setColumns(20);
        taMovieInfo.setFont(new java.awt.Font("宋体", 0, 16)); // NOI18N
        taMovieInfo.setRows(5);
        taMovieInfo.setText("电影名：\n导演：\n主要演员：\n类型：\n价格：");
        taMovieInfo.setBorder(null);
        jScrollPane1.setViewportView(taMovieInfo);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(lbPoster, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnDetail))
                    .addComponent(jScrollPane1)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDetail))
            .addComponent(lbPoster, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("null");
        treeNavi.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        treeNavi.setRootVisible(false);
        treeNavi.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                treeNaviValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(treeNavi);

        tbMovieList.setFont(new java.awt.Font("宋体", 0, 18)); // NOI18N
        tbMovieList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "电影名", "放映厅", "场次", "座位", "状态"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbMovieList.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tbMovieList);

        btnBook.setText("订票");
        btnBook.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBookMouseClicked(evt);
            }
        });

        btnCancelSel.setText("取消选择");
        btnCancelSel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelSelMouseClicked(evt);
            }
        });

        jMenu1.setText("系统");

        jMenuItem1.setText("退出");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("个人中心");

        jMenuItem2.setText("订单查询");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem3.setText("修改密码");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnBook)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelSel))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBook)
                    .addComponent(btnCancelSel))
                .addGap(2, 2, 2))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // 窗体关闭
        try {
            db.close();
        } catch(SQLException e) {
            //JOptionPane.showMessageDialog(this, e.toString(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        this.dispose();
    }//GEN-LAST:event_formWindowClosing

    private void btnQueryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnQueryMouseClicked
        // 电影名查询
        try {
            loadMovie(tfQueryMovie.getText());
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnQueryMouseClicked

    private void btnClearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClearMouseClicked
        // 清空
        try {
            tfQueryMovie.setText(null);
            loadMovie();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnClearMouseClicked

    private void btnDetailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDetailMouseClicked
        // 详细信息
        int i = tbMovieList.getSelectedRow();
        if(i<0) return;
        WinCtrl.currentSelectedMovieID = dataList.get(i).movieID;
        // 显示frmMovieInfo
        frmMovieInfo.main(null);
    }//GEN-LAST:event_btnDetailMouseClicked

    private void btnBookMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBookMouseClicked
        // 订票
        try {
            bookTicket();
            loadDataToTable();
            JOptionPane.showMessageDialog(this, "订票完成");
        } catch(SQLException e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_btnBookMouseClicked

    private void btnCancelSelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelSelMouseClicked
        // 取消选择
        tbMovieList.clearSelection();
    }//GEN-LAST:event_btnCancelSelMouseClicked

    private void treeNaviValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_treeNaviValueChanged
        // 树选择改变
        try {
            String movName = loadDataToTable();
            if(movName!=null) writeIntroduction(movName);
        } catch(SQLException | IOException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_treeNaviValueChanged

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // 退出
        try {
            db.close();
        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(this, e.toString(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        this.dispose();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // 订单查询
        frmOrderQuery.main(null);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // 修改密码
        WinCtrl.isResetPassword = false;
        frmChangePwd.main(null);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void btnDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDetailActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        lab5.Lab5.setUIStyle(frmUser.class.getName());
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmUser().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBook;
    private javax.swing.JButton btnCancelSel;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDetail;
    private javax.swing.JButton btnQuery;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lbPoster;
    private javax.swing.JTextArea taMovieInfo;
    private javax.swing.JTable tbMovieList;
    private javax.swing.JTextField tfQueryMovie;
    private javax.swing.JTree treeNavi;
    // End of variables declaration//GEN-END:variables
}
