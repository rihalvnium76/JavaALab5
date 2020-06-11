/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab5.UI;

import java.io.*;
import java.sql.*;
import java.util.*;

import java.awt.*;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
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
        public float price;

        public static final int MAX_INDEX = 8;

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
                case 8: price = (Float)value; break;
                default: throw new IndexOutOfBoundsException("DBItem无效索引: "+index);
            }
        }
    }
    private ArrayList<DBItem> dataList;
    private int[][] seatStatus;
    private ColorCellRenderer colorCellRenderer;

    public frmUser() {
        initComponents();
        this.setTitle("个人购票大厅 - 用户：" + WinCtrl.currentLoginUser);
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

        colorCellRenderer = new ColorCellRenderer();
        // 去掉表头
        tbMovieList.getTableHeader().setVisible(false);
        DefaultTableCellRenderer tbHeadRdr = new DefaultTableCellRenderer();
        tbHeadRdr.setPreferredSize(new Dimension(0, 0));
        tbMovieList.getTableHeader().setDefaultRenderer(tbHeadRdr);
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
    private class ColorCellRenderer extends DefaultTableCellRenderer {
        public static final int
            TICKET_INVALID = 0,
            TICKET_SOLD = 1,
            TICKET_UNSOLD = 2;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            JLabel lb = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            try {
                // 设置单元格背景颜色
                switch(seatStatus[row][col]) {
                    case TICKET_INVALID: lb.setBackground(Color.GRAY); break; // 不存在的座位
                    case TICKET_SOLD: lb.setBackground(Color.RED); break; // 已售
                    case TICKET_UNSOLD: lb.setBackground(Color.GREEN); break; // 未售
                }
            } catch(IllegalArgumentException | NullPointerException e) {
                System.out.println("[W]ColorCellRdr: "+e.toString());
            }

            return lb;
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
        PreparedStatement pst = db.getConnection().prepareStatement("select theater.theatername,schedule.scheduletime from schedule,theater where schedule.theaterid=theater.theaterid and movieid=?");
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
        private boolean scheduleExist;

        public LoadDataToTable(String movieName) throws SQLException, IllegalArgumentException {
            this(movieName, null, null);
        }
        public LoadDataToTable(String movieName, String theaterName, String scheduleTime) throws SQLException, IllegalArgumentException {
            this.movieName = movieName;
            this.theaterName = theaterName;
            this.scheduleTime = scheduleTime;

            scheduleExist = theaterName!=null && scheduleTime!=null;

            writeArrayList();
            if(setTableSize())
                writeTableFromArray();
        }

        private void writeArrayList() throws SQLException {
            PreparedStatement pst = db.getConnection().prepareStatement("select ticket.ticketid,movie.movieid,movie.moviename,ticket.status,schedule.scheduletime,ticket.row,ticket.col,theater.theaterName,movie.price from ticket,schedule,movie,theater where ticket.scheduleid=schedule.scheduleid and schedule.movieid=movie.movieid and schedule.theaterid=theater.theaterid and movie.moviename like ?" + (scheduleExist? " and theater.theatername like ? and schedule.scheduletime=?": ""));
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
            int rc = model.getRowCount(), cc = model.getColumnCount();
            for(DBItem d : dataList) {
                int r = d.row - 1, c = d.col - 1; // 座位号从1起
                if(r>=0 && r<rc && c>=0 && c<cc)
                    seatStatus[r][c] = d.status.equals("已售")? ColorCellRenderer.TICKET_SOLD: ColorCellRenderer.TICKET_UNSOLD;
                //System.out.println("[D]wrTb: r= "+r+"; c= "+c+"; sS= "+seatStatus[r][c]+"; status= "+d.status);
            }
        }

        private boolean setTableSize() throws SQLException, IllegalArgumentException {
            DefaultTableModel dtm = (DefaultTableModel)tbMovieList.getModel();
            // 重置表
            dtm.setRowCount(0);
            dtm.setColumnCount(0);
            if(!scheduleExist) return false; // 未知放映厅则返回
            // 获取放映厅规格
            PreparedStatement pst  = db.getConnection().prepareStatement("select capacity from theater where theatername=?");
            pst.setString(1, theaterName);
            ResultSet rs = pst.executeQuery();
            int r, c; // 行 列
            if(rs.next()) {
                String[] s = rs.getString(1).split("x", 2);
                if(s.length!=2) throw new IllegalArgumentException();
                r = Integer.parseInt(s[0]);
                c = Integer.parseInt(s[1]);
                dtm.setRowCount(r);
                dtm.setColumnCount(c);
            } else return false; // 未知放映厅则返回
            // 设置单元格渲染器
            Enumeration<TableColumn> e = tbMovieList.getColumnModel().getColumns();
            while(e.hasMoreElements())
                e.nextElement().setCellRenderer(colorCellRenderer);
            // 填充默认数据
            seatStatus = new int[r][c];
            for(int i=0; i<r; ++i)
                for(int j=0; j<c; ++j) {
                    seatStatus[i][j] = ColorCellRenderer.TICKET_INVALID;
                    dtm.setValueAt((i+1) + "-" + (j+1), i, j);
                }
            return true;
        }
    }
    // 根据Tree选择添加数据到Table
    // 返回值：电影名
    String loadDataToTable() throws SQLException, IllegalArgumentException {
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
    // 返回值：成功购票数
    int bookTicket() throws SQLException, IllegalArgumentException {
        // 根据选中项的Index生成座位
        int r = tbMovieList.getSelectedRow() + 1, c = tbMovieList.getSelectedColumn() + 1;
        if(r==0 || c==0) return 0;
        DBItem item = null;
        for(DBItem d : dataList)
            if(d.row==r && d.col==c) // find seat
                item = d;
        if(item==null) throw new IllegalArgumentException("未知座位: " + r + "+" + c);

        // index->ArrayList->ticketID->Status
        int rt = 0;
        PreparedStatement pst = db.getConnection().prepareStatement("update Ticket set userid=?, status=? where ticketid=?");
        if(item.status.equals("未售")) {
            pst.setString(1, findUserIDByName(WinCtrl.currentLoginUser)); // userid
            pst.setString(2, "已售");
            pst.setString(3, item.ticketID);
            rt = pst.executeUpdate();
        }
        pst.close();
        return rt;
    }
    // 询问购票
    boolean askTicket() {
        if(dataList.isEmpty())
            return false;
        else {
            if(JOptionPane.showConfirmDialog(this, "确定购买电影票？", "购买", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
            //if(JOptionPane.showConfirmDialog(this, "确定购买电影票？\n总计：" + String.format("%.2f元", dataList.get(0).price), "购买", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
                return true;
            else
                return false;
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
        jLabel1 = new javax.swing.JLabel();
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

        tbMovieList.setFont(new java.awt.Font("宋体", 0, 20)); // NOI18N
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
        tbMovieList.setRowHeight(22);
        tbMovieList.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tbMovieList);
        tbMovieList.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

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

        jLabel1.setText("灰色:不可用 | 绿色:未售 | 红色:已售 | 单选");

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
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBook)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelSel))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3))
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
                    .addComponent(btnCancelSel)
                    .addComponent(jLabel1))
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
        //int i = tbMovieList.getSelectedRow();
        if(dataList.isEmpty()) return;
        WinCtrl.currentSelectedMovieID = dataList.get(0).movieID;
        // 显示frmMovieInfo
        frmMovieInfo.main(null);
    }//GEN-LAST:event_btnDetailMouseClicked

    private void btnBookMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBookMouseClicked
        // 订票
        try {
            if(askTicket()) {
                JOptionPane.showMessageDialog(this, "订票完成\n成功订票张数：" + bookTicket());
                loadDataToTable();
            }
        } catch(SQLException | IllegalArgumentException e) {
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
        } catch(SQLException | IOException | IllegalArgumentException e) {
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
        WinCtrl.resetPwdUser = WinCtrl.currentLoginUser;
        frmChangePwd.main(null);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

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
    private javax.swing.JLabel jLabel1;
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
