/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab5.UI;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import java.awt.Image;

import lab5.Module.*;

/* [4]票务查询 */
public class frmUser extends javax.swing.JFrame {
    private DBAccess db;
    class DBItem {
        public String ticketID, movieID, movieName, status;
        public Timestamp scheduleTime;
        public int row, col;
        public String theaterName, userID, userName;

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
                case 8: userID = (String)value; break;
                case 9: userName = (String)value; break;
                default: throw new IndexOutOfBoundsException("DBItem无效索引: "+index);
            }
        }
    }
    ArrayList<DBItem> dataList;

    public frmUser() {
        initComponents();
        // 初始化
        try {
            db = new DBAccess();
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.toString(), "错误", JOptionPane.ERROR_MESSAGE);
            System.exit(1); // 退出
        }
        dataList = new ArrayList<DBItem>();
    }

    // 加载数据到JTree，参数为可选
    void loadDataToTree(String searchMovie) {
        class MovieItem {
            public String movieName, movieID;
            public MovieItem(String name, String id) {
                movieName = name; movieID = id;
            }
        }
        Queue<MovieItem> movieList = new LinkedList<MovieItem>();
        Connection ct = db.getConnection();
        try {
            /* 获取电影名列表 */
            PreparedStatement pst;
            if(searchMovie==null)
                pst = ct.prepareStatement("select movieName, movieID from Movie");
            else {
                pst = ct.prepareStatement("select movieName, movieID from Movie where movieName like ?");
                pst.setString(1, "%" + searchMovie + "%"); // 模糊查找
            }
            ResultSet rs = pst.executeQuery();
            while(rs.next())
                movieList.offer(new MovieItem(rs.getString(1), rs.getString(2)));
            rs.close();
            pst.close();

            /* 获取放映厅与场次 */
            MovieItem curMovie;
            DefaultTreeModel model = (DefaultTreeModel)treeNavi.getModel();
            // 获取树根节点
            DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)model.getRoot();

            pst = ct.prepareStatement("select Theater.TheaterName, Schedule.ScheduleTime from Theater, Schedule where Schedule.MovieID=? and Theater.TheaterID=Schedule.TheaterID");
            while((curMovie = movieList.poll())!=null) {
                // 写入Tree
                DefaultMutableTreeNode curMovieNode = WinCtrl.addTreeNode(treeNavi, rootNode, curMovie.movieName);
                // 查询放映厅+场次 写入Tree
                pst.clearParameters(); // 清除参数记录
                pst.setString(1, curMovie.movieID);
                rs = pst.executeQuery();
                while(rs.next())
                    WinCtrl.addTreeNode(treeNavi, curMovieNode, rs.getString(1) + "|" + rs.getTimestamp(2).toString());
                rs.close();
            }
            pst.close();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(this, e.toString(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 加载数据到JTable+ArrayList
    void loadDataToTable(String movieName, String theaterName, String scheduleTime) {
        Connection ct = db.getConnection();
        try {
            PreparedStatement pst = ct.prepareStatement(
                "select Ticket.TicketID, Movie.MovieID, Movie.MovieName, Ticket.Status, Schedule.ScheduleTime, Ticket.Row, Ticket.Col, Theater.TheaterName, Users.UserID, Users.UserName "+
                "from Ticket, Schedule, Movie, Theater, Users "+
                "where Ticket.ScheduleID=Schedule.ScheduleID and Schedule.MovieID=Movie.MovieID and Schedule.TheaterID=Theater.TheaterID and Ticket.UserID=Users.UserID and "+
                "Movie.MovieName=?" + (theaterName==null? "": " and Theater.TheaterName=?") + (scheduleTime==null? "": " and Schedule.ScheduleTime=?"));
            pst.setString(1, movieName);
            if(theaterName!=null) pst.setString(2, theaterName);
            if(scheduleTime!=null) pst.setTimestamp(3, Timestamp.valueOf(scheduleTime));
            ResultSet rs = pst.executeQuery();
            DefaultTableModel dtm = (DefaultTableModel)tbMovieList.getModel();
            dtm.setRowCount(0); // 清空表
            dataList.clear(); // 清空表
            while(rs.next()) {
                // ArrayList
                DBItem r = new DBItem();
                for(int i=0, cc=rs.getMetaData().getColumnCount(); i<cc; ++i)
                    r.setValueByIndex(i, rs.getObject(i+1));
                dataList.add(r);

                // JTable
                dtm.addRow(new String[] {
                    r.movieName,
                    r.theaterName,
                    r.scheduleTime.toString(),
                    r.row + "-" + r.col,
                    r.status
                });
            }
            rs.close();
            pst.close();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(this, e.toString(), "错误", JOptionPane.ERROR_MESSAGE);
        } catch(IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "场次格式错误，应为yyyy-mm-dd hh:mm:ss", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 加载电影基本信息
    // movieName 按电影名查询
    // index 表索引->dataList->movieID
    void loadIntroduction(String movieName, Integer index) {
        if(movieName==null && index==null) return;
        String[] intro = new String[6]; // 电影名 导演 主演 类型 价格 图片
        String val = movieName!=null? movieName: dataList.get(index).movieID;

        Connection ct = db.getConnection();
        try {
            PreparedStatement pst = ct.prepareStatement("select movieName, director, mainActors, movieType, price, moviePoster from movie where " + (movieName!=null? "movieName": "movieID") + "=?");
            pst.setString(1, val);
            ResultSet rs = pst.executeQuery();
            if(rs.next())
                for(int i=0; i<6; ++i)
                    if(i==4) // 价格
                        intro[i] = String.valueOf(rs.getDouble(i));
                    else
                        intro[i] = rs.getString(i);
            rs.close();
            pst.close();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString(), "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 写入文本框
        taMovieInfo.setText(
            "电影名：" + intro[0] +
            "\n导演：" + intro[1] +
            "\n主要演员：" + intro[2] +
            "\n类型：" + intro[3] +
            "\n价格：" + intro[4]);
        // 设置图片
        try {
            ImageIcon image = new ImageIcon(WinCtrl.getImageDirPath() + File.separator + "ctOSx.jpg");
            image.setImage(image.getImage().getScaledInstance(lbPoster.getWidth(), lbPoster.getHeight(), Image.SCALE_DEFAULT));
            lbPoster.setIcon(image);
        } catch(IOException e) {
            lbPoster.setText("无图片");
        }
    }

    // 电影票订票
    void bookTicket() {
        int[] rows = tbMovieList.getSelectedRows();
        if(rows.length==0) return;
        Connection ct = db.getConnection();

        // index(rows)->ArrayList->ticketID->Status
        try {
            PreparedStatement pst = ct.prepareStatement("update Ticket set userid=?, status=\'已购\' where ticketid=?");
            for(int index : rows) {
                DBItem item = dataList.get(index);
                if(item.status.equals("已购")) continue;
                pst.clearParameters();
                pst.setString(1, item.userID);
                pst.setString(2, item.ticketID);
                pst.executeUpdate();
            }
            pst.close();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString(), "错误", JOptionPane.ERROR_MESSAGE);
        }
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
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

        lbPoster.setText("[封面]");

        btnDetail.setText("详细信息");
        btnDetail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDetailMouseClicked(evt);
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
        tbMovieList.setColumnSelectionAllowed(true);
        tbMovieList.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tbMovieList);
        tbMovieList.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

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
        jMenuItem1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem1MouseClicked(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("个人中心");

        jMenuItem2.setText("订单查询");
        jMenuItem2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem2MouseClicked(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem3.setText("修改密码");
        jMenuItem3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem3MouseClicked(evt);
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

    private void jMenuItem1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem1MouseClicked
         // 退出
        try {
            db.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.toString(), "错误", JOptionPane.ERROR_MESSAGE);
        }
        this.dispose();
    }//GEN-LAST:event_jMenuItem1MouseClicked

    private void jMenuItem3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem3MouseClicked
        // 修改密码
        WinCtrl.isResetPassword = false;
        frmChangePwd.main(null);
    }//GEN-LAST:event_jMenuItem3MouseClicked

    private void jMenuItem2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem2MouseClicked
        // 订单查询
        frmOrderQuery.main(null);
    }//GEN-LAST:event_jMenuItem2MouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // 窗体关闭
        try {
            db.close();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(this, e.toString(), "错误", JOptionPane.ERROR_MESSAGE);
        }
        this.dispose();
    }//GEN-LAST:event_formWindowClosing

    private void btnQueryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnQueryMouseClicked
        // 电影名查询
        loadDataToTree(tfQueryMovie.getText().trim());
    }//GEN-LAST:event_btnQueryMouseClicked

    private void btnClearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClearMouseClicked
        // 清空
        tfQueryMovie.setText(null);
        loadDataToTree(null);
    }//GEN-LAST:event_btnClearMouseClicked

    private void btnDetailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDetailMouseClicked
        // 详细信息
        WinCtrl.currentSelectedMovieID = dataList.get(tbMovieList.getSelectedRow()).movieID;
        // 显示frmMovieInfo
        frmMovieInfo.main(null);
    }//GEN-LAST:event_btnDetailMouseClicked

    private void btnBookMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBookMouseClicked
        // 订票
        bookTicket();
        DBItem r = dataList.get(tbMovieList.getSelectedRow());
        loadDataToTable(r.movieName, r.theaterName, r.scheduleTime.toString());
    }//GEN-LAST:event_btnBookMouseClicked

    private void btnCancelSelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelSelMouseClicked
        // 取消选择
        tbMovieList.clearSelection();
    }//GEN-LAST:event_btnCancelSelMouseClicked

    private void treeNaviValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_treeNaviValueChanged
        // 树选择改变
        String movieName = null, schedule = null;
        String[] schInfo = new String[2];
        DefaultMutableTreeNode node = WinCtrl.getSelectedTreeNode(treeNavi);
        switch(node.getLevel()) {
            case 1:
                movieName = (String)node.getUserObject(); break;
            case 2:
                schedule = (String)node.getUserObject();
                movieName = (String)((DefaultMutableTreeNode)node.getParent()).getUserObject();
                schInfo = schedule.split("|", 2);
                break;
            default: return;
        }
        if(movieName==null) return;
        loadDataToTable(movieName, schInfo[0], schInfo[1]);
    }//GEN-LAST:event_treeNaviValueChanged

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
