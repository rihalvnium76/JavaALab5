/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab5.UI;

import java.awt.event.ItemEvent;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;
import lab5.Module.DBAccess;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.nio.channels.*;
import lab5.Module.*;

// [8]后台管理界面
public class frmManager extends javax.swing.JFrame {
    private DBAccess db; // 数据库访问对象
    private UI_TicketManager ticketManager;
    
    public frmManager() {
        initComponents();
        this.setTitle("后台管理系统 - 管理员：" + WinCtrl.currentLoginUser);
        try {
            db = new DBAccess();
        } catch (ClassNotFoundException | SQLException e) {
            // JOptionPane.showMessageDialog(this, e.toString(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1); // 退出
        }
        /* 电影票管理 */
        ticketManager = new UI_TicketManager(db);
        ticketManager.bindCombox(cmbTicketID, cmbCustomer, cmbScheduleID, cmbMovie, cmbTheater, cmbTkIDOperation, cmbSchIDOperation, cmbThOperation);
        ticketManager.bindTextField(tfRow, tfCol, tfThCapacity, tfPrice, tfScheduleTime);
        ticketManager.bindTable(tbTicketList);
        ticketManager.initComponents(); // 初始化控件
        ticketManager.loadData(); // 载入数据
        /* 电影票管理 */
        this.initTable();//表格初始化
        initCustomComponents(); // 初始化控件
        this.movielist();
    }

    private void initCustomComponents() {
        jmovielist.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            // 电影列表选择改变事件
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // 电影列表
                DefaultTableModel dtm=(DefaultTableModel)jmovielist.getModel();
                int row=jmovielist.getSelectedRow(); 
                if(row<0) return;
                jmovieID.setText(WinCtrl.avoidNullString(jmovielist.getValueAt(row, 0)).toString());//返回选中行的内容
                jmoviename.setText(WinCtrl.avoidNullString(jmovielist.getValueAt(row, 1)).toString());
                jdirector.setText(WinCtrl.avoidNullString(jmovielist.getValueAt(row, 2)).toString());
                jmainactor.setText(WinCtrl.avoidNullString(jmovielist.getValueAt(row, 3)).toString());
                jtype.setText(WinCtrl.avoidNullString(jmovielist.getValueAt(row, 4)).toString());
                jinfo.setText(WinCtrl.avoidNullString(jmovielist.getValueAt(row, 5)).toString());
                try {
                    Object mp = dtm.getValueAt(row, 6);
                    WinCtrl.setLabelMoviePoster(jposter, mp==null? null: mp.toString());
                }catch(IOException ex){
                    ex.printStackTrace();
                }
            }
        });
    }
    
    private void movielist(){ 
        //将数据库中的电影列表显示在表格中
        DefaultTableModel dtm=(DefaultTableModel)jmovielist.getModel();
        // 清空
        dtm.setRowCount(0);
        JTextField[] tflist = new JTextField[] {jmovieID, jmoviename, jdirector, jmainactor, jtype};
        for(JTextField t : tflist) t.setText(null);
        jinfo.setText(null);
        jposter.setIcon(null);
        try {
            Statement sta=db.getConnection().createStatement();
            String sql="select movieid,moviename,director,mainactors,movietype,movieinfo,movieposter from movie";
            ResultSet rs=sta.executeQuery(sql);
            while(rs.next()){
                Vector<String> v=new Vector<String>();
                v.add(rs.getString(1));
                v.add(rs.getString(2));
                v.add(rs.getString(3));
                v.add(rs.getString(4));
                v.add(rs.getString(5));
                v.add(rs.getString(6));
                v.add(rs.getString(7));
                dtm.addRow(v);
            } 
            rs.close();
            sta.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void initTable(){ 
        //将数据库中的用户显示在表格中
        DefaultTableModel dtm=(DefaultTableModel)UsersList.getModel();
            try {
                Statement sta=db.getConnection().createStatement();
                String sql="select * from Users";
                ResultSet rs=sta.executeQuery(sql);
                while(rs.next()){
                    Vector<String> v=new Vector<String>();
                    v.add(rs.getString(2));
                    dtm.addRow(v);
                } 
                rs.close();
                sta.close();
            } catch (Exception ex) {}
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jMenuItem1 = new javax.swing.JMenuItem();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jmovielist = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jmovieID = new javax.swing.JTextField();
        jmoviename = new javax.swing.JTextField();
        jdirector = new javax.swing.JTextField();
        jmainactor = new javax.swing.JTextField();
        jtype = new javax.swing.JTextField();
        jScrollPane6 = new javax.swing.JScrollPane();
        jinfo = new javax.swing.JTextArea();
        jposter = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        btnChangeMovieID = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cmbTicketID = new javax.swing.JComboBox<>();
        cmbCustomer = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        tfRow = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        tfCol = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cmbScheduleID = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        tfScheduleTime = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cmbTheater = new javax.swing.JComboBox<>();
        cmbMovie = new javax.swing.JComboBox<>();
        lbThCapacity = new javax.swing.JLabel();
        tfThCapacity = new javax.swing.JTextField();
        btnSave = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnReload = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel10 = new javax.swing.JLabel();
        tfPrice = new javax.swing.JTextField();
        cmbTkIDOperation = new javax.swing.JComboBox<>();
        cmbSchIDOperation = new javax.swing.JComboBox<>();
        cmbThOperation = new javax.swing.JComboBox<>();
        btnBatchFill = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbTicketList = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        UsersList = new javax.swing.JTable();
        DeleteAccount = new javax.swing.JButton();
        ResetPassword = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        TicketList = new javax.swing.JTable();
        DeleteTicket = new javax.swing.JButton();

        jTextField1.setText("jTextField1");

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jmovielist.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "电影ID", "电影名", "导演", "主演", "电影类型", "电影简介", "海报"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(jmovielist);

        jLabel11.setText("电影ID：");

        jLabel12.setText("电影名字：");

        jLabel13.setText("导演：");

        jLabel14.setText("主演：");

        jLabel15.setText("电影类型：");

        jLabel16.setText("电影简介：");

        jmovieID.setEditable(false);

        jinfo.setColumns(20);
        jinfo.setLineWrap(true);
        jinfo.setRows(5);
        jScrollPane6.setViewportView(jinfo);

        jposter.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jposter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jposterMouseClicked(evt);
            }
        });

        jButton1.setText("保 存 修 改");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("新 增 电 影");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("删 除 电 影");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("刷 新 列 表");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        btnChangeMovieID.setText("修改");
        btnChangeMovieID.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnChangeMovieIDMouseClicked(evt);
            }
        });

        jButton5.setText("修改电影价格");
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton5MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel15))
                                .addGap(28, 28, 28)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jtype, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton5)
                                        .addGap(6, 6, 6))
                                    .addComponent(jmainactor)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(28, 28, 28)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jmovieID)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnChangeMovieID))
                                    .addComponent(jmoviename, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jdirector, javax.swing.GroupLayout.Alignment.TRAILING))))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jposter, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane6)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jmovieID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(btnChangeMovieID))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jmoviename, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jdirector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jmainactor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jtype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton5)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(174, 174, 174))))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jposter, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("电影信息管理功能", jPanel1);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("修改电影票信息"));

        jLabel1.setText("电影票ID");

        jLabel2.setText("购票人");

        cmbCustomer.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "无" }));

        jLabel3.setText("座位");

        jLabel4.setText("行");

        jLabel5.setText("列");

        jLabel6.setText("计划ID");

        cmbScheduleID.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbScheduleIDItemStateChanged(evt);
            }
        });

        jLabel7.setText("场次");

        tfScheduleTime.setText("yyyy-mm-dd hh:mm:ss");
        tfScheduleTime.setToolTipText("格式：yyyy-mm-dd hh:mm:ss");
        tfScheduleTime.setEnabled(false);

        jLabel8.setText("电影");

        jLabel9.setText("放映厅");

        cmbTheater.setEnabled(false);
        cmbTheater.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTheaterItemStateChanged(evt);
            }
        });

        cmbMovie.setEnabled(false);
        cmbMovie.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMovieItemStateChanged(evt);
            }
        });

        lbThCapacity.setText("放映厅规格");

        tfThCapacity.setEditable(false);
        tfThCapacity.setToolTipText("格式：<最大行数>x<最大列数>");
        tfThCapacity.setEnabled(false);

        btnSave.setText("保存");
        btnSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSaveMouseClicked(evt);
            }
        });

        btnDelete.setText("删除");
        btnDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDeleteMouseClicked(evt);
            }
        });

        btnReload.setText("刷新列表");
        btnReload.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnReloadMouseClicked(evt);
            }
        });

        jTextArea1.setEditable(false);
        jTextArea1.setBackground(new java.awt.Color(240, 240, 240));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("宋体", 0, 16)); // NOI18N
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setText("说明：\n新建：勾上后将会在相应的数据库表中添加新的一项，ID需要是数据库没有的ID，否则会添加失败\n（特别说明：新建放映厅填写的信息的格式为：\n  放映厅ID|放映厅名 ）\n放映厅规格：格式：<最大行数>x<最大列数>\n批量填充：在指定放映计划下，会根据当前放映厅规格自动填充空票，已有票不受影响");
        jTextArea1.setBorder(null);
        jScrollPane2.setViewportView(jTextArea1);

        jLabel10.setText("价格");

        cmbTkIDOperation.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "修改", "新建" }));
        cmbTkIDOperation.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTkIDOperationItemStateChanged(evt);
            }
        });

        cmbSchIDOperation.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "仅选择", "修改", "新建" }));
        cmbSchIDOperation.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbSchIDOperationItemStateChanged(evt);
            }
        });

        cmbThOperation.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "仅选择", "修改", "新建" }));
        cmbThOperation.setEnabled(false);
        cmbThOperation.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbThOperationItemStateChanged(evt);
            }
        });

        btnBatchFill.setText("批量填充");
        btnBatchFill.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBatchFillMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(cmbTicketID, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbTkIDOperation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(cmbScheduleID, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                        .addComponent(tfRow, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tfCol, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(cmbSchIDOperation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(cmbCustomer, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cmbMovie, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(tfScheduleTime, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                                    .addComponent(cmbTheater, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbThOperation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(lbThCapacity)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfThCapacity, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReload)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBatchFill)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbTicketID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTkIDOperation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(tfRow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(tfCol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cmbScheduleID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbSchIDOperation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(tfScheduleTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cmbMovie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(cmbTheater, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbThOperation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbThCapacity)
                    .addComponent(tfThCapacity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(tfPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(btnDelete)
                    .addComponent(btnReload)
                    .addComponent(btnBatchFill))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("数据库电影表列表"));

        tbTicketList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "电影票ID", "电影名", "放映厅", "场次", "座位", "价格", "状态"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbTicketList.setColumnSelectionAllowed(true);
        tbTicketList.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tbTicketList.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbTicketList);
        tbTicketList.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 639, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("电影票管理功能", jPanel2);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("数据库用户列表"));

        UsersList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "用户名"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        UsersList.setColumnSelectionAllowed(true);
        UsersList.getTableHeader().setReorderingAllowed(false);
        UsersList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                UsersListMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(UsersList);
        UsersList.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        DeleteAccount.setText("删除账户");
        DeleteAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteAccountActionPerformed(evt);
            }
        });

        ResetPassword.setText("重置密码");
        ResetPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ResetPasswordActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(DeleteAccount)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ResetPassword)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ResetPassword)
                    .addComponent(DeleteAccount))
                .addContainerGap())
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("用户相关订单"));

        TicketList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "电影票ID", "电影名", "放映厅", "场次", "座位", "价格", "状态"
            }
        ));
        TicketList.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(TicketList);

        DeleteTicket.setText("删除订单");
        DeleteTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteTicketActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(DeleteTicket)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 799, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jScrollPane4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(DeleteTicket))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("用户信息管理", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 594, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveMouseClicked
        // 保存
        ticketManager.saveItem();
    }//GEN-LAST:event_btnSaveMouseClicked

    private void btnDeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteMouseClicked
        // 电影票管理界面的删除订单
        if(JOptionPane.showConfirmDialog(null, "确定删除所选记录？", "警告", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION) {
            ticketManager.deleteTicket();
            ticketManager.loadData();
        }
    }//GEN-LAST:event_btnDeleteMouseClicked

    private void btnReloadMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReloadMouseClicked
        // 刷新列表
        ticketManager.loadData();
    }//GEN-LAST:event_btnReloadMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // 窗体关闭
        try {
            db.close();
        } catch(SQLException e) {
            // JOptionPane.showMessageDialog(this, e.toString(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        this.dispose(); // 退出
    }//GEN-LAST:event_formWindowClosing

    private void cmbTkIDOperationItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTkIDOperationItemStateChanged
        // 电影票ID操作
        cmbTicketID.setEditable(cmbTkIDOperation.getSelectedIndex()==1);
    }//GEN-LAST:event_cmbTkIDOperationItemStateChanged

    private void cmbSchIDOperationItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbSchIDOperationItemStateChanged
        // 计划操作
        switch(cmbSchIDOperation.getSelectedIndex()) {
            case 0:
                cmbScheduleID.setEditable(false);
                tfScheduleTime.setEnabled(false);
                cmbMovie.setEnabled(false);
                cmbTheater.setEnabled(false);
                cmbThOperation.setEnabled(false);
                tfThCapacity.setEnabled(false);
                break;
            case 1:
                cmbScheduleID.setEditable(false);
                tfScheduleTime.setEnabled(true);
                cmbMovie.setEnabled(true);
                cmbTheater.setEnabled(true);
                cmbThOperation.setEnabled(true);
                tfThCapacity.setEnabled(true);
                break;
            case 2:
                cmbScheduleID.setEditable(true);
                tfScheduleTime.setEnabled(true);
                cmbMovie.setEnabled(true);
                cmbTheater.setEnabled(true);
                cmbThOperation.setEnabled(true);
                tfThCapacity.setEnabled(true);
                break;
        }
    }//GEN-LAST:event_cmbSchIDOperationItemStateChanged

    private void cmbThOperationItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbThOperationItemStateChanged
        // 放映厅操作
        switch(cmbThOperation.getSelectedIndex()) {
            case 0:
                cmbTheater.setEditable(false);
                tfThCapacity.setEditable(false);
                break;
            case 1:
                cmbTheater.setEditable(false);
                tfThCapacity.setEditable(true);
                break;
            case 2:
                cmbTheater.setEditable(true);
                tfThCapacity.setEditable(true);
                break;
        }
    }//GEN-LAST:event_cmbThOperationItemStateChanged

    private void cmbScheduleIDItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbScheduleIDItemStateChanged
        try {
            // 计划ID改变
            if(cmbScheduleID.getSelectedIndex()>=0)
                ticketManager.loadSchedule();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_cmbScheduleIDItemStateChanged

    private void cmbMovieItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMovieItemStateChanged
        try {
            // 电影改变
            if(cmbMovie.getSelectedIndex()>=0)
                ticketManager.loadMovie();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_cmbMovieItemStateChanged

    private void cmbTheaterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTheaterItemStateChanged
        try {
            // 放映厅改变
            if(cmbTheater.getSelectedIndex()>=0)
                ticketManager.loadTheater();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_cmbTheaterItemStateChanged

    private void DeleteTicketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteTicketActionPerformed
        //删除订单
        DefaultTableModel dtm = (DefaultTableModel) TicketList.getModel();
        int r = TicketList.getSelectedRow();
        if(JOptionPane.showConfirmDialog(null, "是否删除该订单？", "警告", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
            try {
                //PreparedStatement pstDel = db.getConnection().prepareStatement("delete from Ticket where TicketID=?");
                PreparedStatement pstDel = db.getConnection().prepareStatement("update ticket set userid=null,status=\'未售\' where ticketid=?");
                pstDel.setObject(1, TicketList.getValueAt(r, 0)); 
                pstDel.executeUpdate();
                pstDel.close();
            }catch(SQLException e) {
                //JOptionPane.showMessageDialog(null, e.toString(), "错误", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }
            dtm.removeRow(r);
        }
        else{
            return;
        }
    }//GEN-LAST:event_DeleteTicketActionPerformed

    private void DeleteAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteAccountActionPerformed
        //删除账户
        DefaultTableModel dtm = (DefaultTableModel) UsersList.getModel();
        DefaultTableModel dtm2 = (DefaultTableModel) TicketList.getModel();
        int r = UsersList.getSelectedRow();
        if(JOptionPane.showConfirmDialog(null, "是否删除该账户？", "警告", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
            try {
                //PreparedStatement pstDel1 = db.getConnection().prepareStatement("delete from Ticket where userid in (select userid from users where loginname=?)");
                PreparedStatement pstDel1 = db.getConnection().prepareStatement("update ticket set userid=null,status=\'未售\' where userid in (select userid from users where loginname=?)");
                pstDel1.setObject(1, UsersList.getValueAt(r, 0)); 
                pstDel1.executeUpdate();
                pstDel1.close();
                PreparedStatement pstDel2 = db.getConnection().prepareStatement("delete from Users where LoginName=?");
                pstDel2.setObject(1, UsersList.getValueAt(r, 0)); 
                pstDel2.executeUpdate();
                pstDel2.close();
                dtm2.setRowCount(0);
            }catch(SQLException e) {
                //JOptionPane.showMessageDialog(null, e.toString(), "错误", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }
            dtm.removeRow(r);
        }
        else{
            return;
        }
    }//GEN-LAST:event_DeleteAccountActionPerformed

    private void ResetPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ResetPasswordActionPerformed
        //重置密码
        if(UsersList.getSelectedRow()>-1) {
            WinCtrl.isResetPassword = true;
            WinCtrl.resetPwdUser = WinCtrl.avoidNullString(((DefaultTableModel)UsersList.getModel()).getValueAt(UsersList.getSelectedRow(), 0)).toString().trim();
            frmChangePwd.main(null);
        }
    }//GEN-LAST:event_ResetPasswordActionPerformed

    private void UsersListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_UsersListMouseClicked
        //点击用户名，获得该用户的订单信息
        DefaultTableModel dtm = (DefaultTableModel) TicketList.getModel();
        dtm.setRowCount(0); // 清空表
        int r = UsersList.getSelectedRow();
        try {
            PreparedStatement pstQue = db.getConnection().prepareStatement("select Ticket.TicketID,Movie.MovieName,Theater.TheaterName,ScheduleTime,Ticket.Row,Ticket.Col,price,Ticket.Status from Ticket,Schedule,Movie,Users,Theater where Theater.TheaterID=Schedule.TheaterID and Ticket.ScheduleID=Schedule.ScheduleID and Schedule.MovieID=Movie.MovieID and Ticket.UserID=Users.UserID and Users.LoginName=?");
            pstQue.setObject(1, UsersList.getValueAt(r, 0));
            //执行查询 SQL 语句，返回查询的结果集         
            ResultSet rs = pstQue.executeQuery( ); 
            while(rs.next()){
                Vector v=new Vector();
                v.add(rs.getObject(1));
                v.add(rs.getObject(2));
                v.add(rs.getObject(3));
                v.add(rs.getObject(4));
                v.add(rs.getObject(5)+ "-" +rs.getObject(6));
                v.add(rs.getObject(7));
                v.add(rs.getObject(8));
                dtm.addRow(v);
            }
            rs.close();
            pstQue.close();
        }catch(SQLException e) {
            //JOptionPane.showMessageDialog(null, e.toString(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }
    }//GEN-LAST:event_UsersListMouseClicked

    private void btnBatchFillMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBatchFillMouseClicked
        // 批量填充
        ticketManager.batchFillTicket();
    }//GEN-LAST:event_btnBatchFillMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // 添加电影
        try {
            PreparedStatement ps = db.getConnection().prepareStatement("Insert into movie values (?,?,?,?,?,?,?,?)");
            ps.setString(1, JOptionPane.showInputDialog(this, "请输入新电影的ID（新ID与已有ID不能重复）：", "新建电影", JOptionPane.PLAIN_MESSAGE));
            for(int i=2; i<=8; ++i) ps.setString(i, null);    
            ps.setFloat(7, Float.valueOf(JOptionPane.showInputDialog(this, "请输入新电影的电影票价格：", "新建电影", JOptionPane.PLAIN_MESSAGE)));
            ps.executeUpdate();
            ps.close();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "新电影ID与已有ID不能重复\n" + e.toString(), "错误", JOptionPane.ERROR_MESSAGE);
            //e.printStackTrace();
            return;
        } catch(IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "无效价格\n" + e.toString(), "错误", JOptionPane.ERROR_MESSAGE);
            //e.printStackTrace();
            return;
        }
        movielist(); // 刷新
        int r = jmovielist.getRowCount() - 1;
        if(r>-1)jmovielist.setRowSelectionInterval(r, r);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // 删除电影
        DefaultTableModel dtm = (DefaultTableModel)jmovielist.getModel();
        Object mp = dtm.getValueAt(jmovielist.getSelectedRow(), 6);
        try {
            // 删除图像文件
            if(mp!=null) {
                File f = new File(WinCtrl.getImageDirPath() + File.separator + mp.toString());
                if(f.exists()) f.delete();
            }
            // 删除数据库项
            PreparedStatement ps = db.getConnection().prepareStatement("delete from movie where movieid=?");
            ps.setObject(1, jmovieID.getText()); 
            ps.executeUpdate();
            ps.close();
        }catch(SQLException | IOException e) {
            e.printStackTrace();
            return;
        }
        movielist();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // 保存修改
        try {
            PreparedStatement ps = db.getConnection().prepareStatement("update movie set moviename=?,director=?,mainactors=?,movietype=?,movieinfo=? where movieid=?" );
            ps.setString(1, jmoviename.getText());
            ps.setString(2, jdirector.getText());
            ps.setString(3, jmainactor.getText());
            ps.setString(4, jtype.getText());
            ps.setString(5, jinfo.getText());
            ps.setString(6, jmovieID.getText());
            ps.executeUpdate();
            ps.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }
        movielist(); // 刷新
        int r = jmovielist.getRowCount() - 1;
        if(r>-1)jmovielist.setRowSelectionInterval(r, r);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jposterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jposterMouseClicked
        // 修改海报
        int r = jmovielist.getSelectedRow();
        try {
            File srcImg = WinCtrl.openImageFileChooser(true);
            if(srcImg!=null) {
                File destImg = new File(WinCtrl.getImageDirPath() + File.separator + srcImg.getName());
                if(destImg.exists())
                    if(JOptionPane.showConfirmDialog(null, "数据库内已存在同名海报文件，是否覆盖？", "警告", JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION)
                        return;
                WinCtrl.copyFile(srcImg, destImg, true);
            } else return;
            //写入数据库
            PreparedStatement pst = db.getConnection().prepareStatement("update movie set movieposter = ? where movieid=?");
            pst.setString(1, srcImg.getName());
            pst.setString(2, jmovieID.getText());
            pst.executeUpdate();
            pst.close();
        } catch(IOException | SQLException e) {
            e.printStackTrace();
        }
        movielist(); // 刷新
        if(r>-1) jmovielist.setRowSelectionInterval(r, r);
    }//GEN-LAST:event_jposterMouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // 刷新电影票列表
        this.movielist();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void btnChangeMovieIDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnChangeMovieIDMouseClicked
        // 电影ID修改
        String mvID = JOptionPane.showInputDialog(this, "请输入新电影的ID（新ID与已有ID不能重复）：", "新建电影", JOptionPane.PLAIN_MESSAGE);
        if(mvID!=null && !mvID.equals("")) {
            int r = jmovielist.getSelectedRow();
            try {
                PreparedStatement pst = db.getConnection().prepareStatement("update movie set movieid=? where movieid=?");
                pst.setString(1, mvID);
                pst.setString(2, jmovieID.getText());
                pst.executeUpdate();
                pst.close();
            } catch(SQLException e) {
                JOptionPane.showMessageDialog(this, "新电影ID与已有ID不能重复\n" + e.toString(), "错误", JOptionPane.ERROR_MESSAGE);
                //e.printStackTrace();
            }
            movielist();
            jmovielist.setRowSelectionInterval(r, r);
        }
    }//GEN-LAST:event_btnChangeMovieIDMouseClicked

    private void jButton5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseClicked
        // 修改电影价格
        try {
            int r = jmovielist.getSelectedRow();
            if(r<0) return;
            PreparedStatement pst = db.getConnection().prepareStatement("select price from movie where movieid=?");
            pst.setString(1, jmovieID.getText());
            ResultSet rs = pst.executeQuery();
            if(rs.next()) {
                Float originPrice = rs.getFloat(1);
                if(originPrice!=null) {
                    String newPrice = JOptionPane.showInputDialog(this, "电影：" + jmovieID.getText() + " - " + jmoviename.getText() + "\n旧电影票价格：" + originPrice + "\n\n请输入新电影票价格：", "修改价格", JOptionPane.PLAIN_MESSAGE);
                    if(newPrice!=null) {
                        PreparedStatement ps2 = db.getConnection().prepareStatement("update movie set price=? where movieid=?");
                        ps2.setFloat(1, Float.valueOf(newPrice));
                        ps2.setString(2, jmovieID.getText());
                        JOptionPane.showMessageDialog(this, "修改" + (ps2.executeUpdate()==0? "失败": "成功"), "修改价格", JOptionPane.INFORMATION_MESSAGE);
                        ps2.close();
                    }
                }
            }
            rs.close();
            pst.close();
        } catch(SQLException e) {
            e.printStackTrace();
            return;
        } catch(IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "无效价格\n" + e.toString(), "错误", JOptionPane.ERROR_MESSAGE);
            //e.printStackTrace();
            return;
        }
    }//GEN-LAST:event_jButton5MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        lab5.Lab5.setUIStyle(frmManager.class.getName());
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmManager().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton DeleteAccount;
    private javax.swing.JButton DeleteTicket;
    private javax.swing.JButton ResetPassword;
    private javax.swing.JTable TicketList;
    private javax.swing.JTable UsersList;
    private javax.swing.JButton btnBatchFill;
    private javax.swing.JButton btnChangeMovieID;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnReload;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cmbCustomer;
    private javax.swing.JComboBox<String> cmbMovie;
    private javax.swing.JComboBox<String> cmbSchIDOperation;
    private javax.swing.JComboBox<String> cmbScheduleID;
    private javax.swing.JComboBox<String> cmbThOperation;
    private javax.swing.JComboBox<String> cmbTheater;
    private javax.swing.JComboBox<String> cmbTicketID;
    private javax.swing.JComboBox<String> cmbTkIDOperation;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jdirector;
    private javax.swing.JTextArea jinfo;
    private javax.swing.JTextField jmainactor;
    private javax.swing.JTextField jmovieID;
    private javax.swing.JTable jmovielist;
    private javax.swing.JTextField jmoviename;
    private javax.swing.JLabel jposter;
    private javax.swing.JTextField jtype;
    private javax.swing.JLabel lbThCapacity;
    private javax.swing.JTable tbTicketList;
    private javax.swing.JTextField tfCol;
    private javax.swing.JTextField tfPrice;
    private javax.swing.JTextField tfRow;
    private javax.swing.JTextField tfScheduleTime;
    private javax.swing.JTextField tfThCapacity;
    // End of variables declaration//GEN-END:variables
}
