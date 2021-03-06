/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab5.UI;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import lab5.Module.*;
/**
 *
 * @author rootpack
 */
public class frmTest extends javax.swing.JFrame {
    //
    
    public frmTest() {
        initComponents();
    }
    
    private String getArgs() {
        return taArgs.getText().toString();
    }
    private class MyCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

            //Cells are by default rendered as a JLabel.
            JLabel lb = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

            //Get the status for the current row.
            DefaultTableModel dtm = (DefaultTableModel)table.getModel();
            try {
                switch(Integer.parseInt(dtm.getValueAt(row, col).toString())) {
                    case 0: lb.setBackground(Color.GRAY); break;
                    case 1: lb.setBackground(Color.RED); break;
                    case 2: lb.setBackground(Color.GREEN); break;
                }
            } catch(IllegalArgumentException|NullPointerException e) {
                System.err.println("[W]MyCellRdr: "+e.toString());
            }

            //Return the JLabel which renders the cell.
            return lb;
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton4 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbSeat = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        taArgs = new javax.swing.JTextArea();
        btnSetRC = new javax.swing.JButton();
        btnSetCC = new javax.swing.JButton();
        btnFillNumber = new javax.swing.JButton();
        btnGetPos = new javax.swing.JButton();
        btnSetColor = new javax.swing.JButton();
        btnSetDefRdr = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("null");
        jTree1.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree1.setRootVisible(false);
        jScrollPane1.setViewportView(jTree1);

        jButton1.setText("add");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField1.setText("jTextField1");
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jTextField2.setText("jTextField2");

        jButton2.setText("remove");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("info");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        jButton4.setText("Close");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("无封面");
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButton5.setText("picture");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("MultiPst_SQL");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("MultiThread");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("invalid_picture");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText("ByRef_Failed");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setText("showInputBox");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addGap(18, 18, 18)
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton6))
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton7))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton8)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton10)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton1)
                                    .addComponent(jButton2)
                                    .addComponent(jButton6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton3)
                                    .addComponent(jButton4)
                                    .addComponent(jButton7))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton5)
                                    .addComponent(jButton8)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton9)
                            .addComponent(jButton10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("tab1", jPanel1);

        tbSeat.setFont(new java.awt.Font("宋体", 0, 18)); // NOI18N
        tbSeat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbSeat.setColumnSelectionAllowed(true);
        tbSeat.setRowHeight(20);
        jScrollPane4.setViewportView(tbSeat);
        tbSeat.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        jLabel2.setText("Instruction:");

        jLabel3.setText("Arguments:");

        taArgs.setColumns(20);
        taArgs.setRows(5);
        jScrollPane3.setViewportView(taArgs);

        btnSetRC.setText("setRowCount");
        btnSetRC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetRCActionPerformed(evt);
            }
        });

        btnSetCC.setText("setColCount");
        btnSetCC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetCCActionPerformed(evt);
            }
        });

        btnFillNumber.setText("fill 0/1/2");
        btnFillNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFillNumberActionPerformed(evt);
            }
        });

        btnGetPos.setText("getPos");
        btnGetPos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGetPosActionPerformed(evt);
            }
        });

        btnSetColor.setText("color");
        btnSetColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetColorActionPerformed(evt);
            }
        });

        btnSetDefRdr.setText("setDefaultRenderer");
        btnSetDefRdr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetDefRdrActionPerformed(evt);
            }
        });

        jTextArea2.setEditable(false);
        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jTextArea2.setText("NOTICE:\nsetCount -> setRdr -> loadData\nRowHeight\nfont\n");
        jScrollPane5.setViewportView(jTextArea2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(btnSetRC)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnSetCC))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(btnFillNumber)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnSetColor))
                                    .addComponent(btnGetPos)
                                    .addComponent(btnSetDefRdr))
                                .addGap(0, 151, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSetRC)
                    .addComponent(btnSetCC))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFillNumber)
                    .addComponent(btnSetColor))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGetPos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSetDefRdr)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(5, 5, 5)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("tab2", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        /*DefaultMutableTreeNode node = new DefaultMutableTreeNode(System.currentTimeMillis());
        TreePath selectionPath = jTree1.getSelectionPath();
        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)selectionPath.getLastPathComponent();
        ((DefaultTreeModel)jTree1.getModel()).insertNodeInto(node, parentNode, parentNode.getChildCount());
        TreePath path = selectionPath.pathByAddingChild(node);
        if(!jTree1.isVisible(path))
            jTree1.makeVisible(path);*/
        JTree tree = jTree1;
        String nodeName = String.valueOf(System.currentTimeMillis());
        DefaultMutableTreeNode selNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        
        // if(selNode==null) return;
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        if(selNode==null) selNode = (DefaultMutableTreeNode)model.getRoot();

        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(nodeName);
        model.insertNodeInto(newNode, selNode, selNode.getChildCount());
        
        // 获取从根节点导到指定结点的所有结点
        TreeNode[] nodes = ((DefaultTreeModel)tree.getModel()).getPathToRoot(newNode);
        // 使用指定的结点数组创建TreePath
        TreePath path = new TreePath(nodes);
        // 显示指定的TreePath
        tree.scrollPathToVisible(path);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        jTextField2.setText(jTextField1.getText());
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        WinCtrl.deleteTreeNode(jTree1, WinCtrl.getSelectedTreeNode(jTree1));
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)jTree1.getLastSelectedPathComponent();
        String val = (String)node.getUserObject();
        jTextArea1.setText("val= "+val+"\nlevel= "+node.getLevel()+"\nChildCount= "+node.getChildCount());
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
            JOptionPane.showMessageDialog(this, "Exception!", "错误", JOptionPane.ERROR_MESSAGE);
            System.exit(0); // 退出
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        /*try {
            ImageIcon image = new ImageIcon(WinCtrl.getImageDirPath() + File.separator + "ctOSx.jpg");
            int w = jLabel1.getWidth(), h = jLabel1.getHeight();
            image.setImage(image.getImage().getScaledInstance(w, h, Image.SCALE_DEFAULT));
            jLabel1.setIcon(image);
        } catch (IOException ex) {
        }*/
        try {
            WinCtrl.setLabelImage(jLabel1, WinCtrl.getImageDirPath() + File.separator + "ctOS.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {
            DBAccess db = new DBAccess(DBAccess.DEFAULT_DB_URL, "BookDB", "sa", "123456");
            PreparedStatement pst = db.getConnection().prepareStatement("select readerName from Reader where readerNo=?");
            pst.setString(1, "R2005001");
            ResultSet rs = pst.executeQuery();
            rs.next();
            jTextArea1.setText(rs.getString(1)+"\n");
            rs.close();

            pst.clearParameters();
            pst.setString(1, "R2006001");

            // pst.close(); // 【不能中途关闭】

            rs = pst.executeQuery();
            rs.next();
            jTextArea1.append(rs.getString(1)+"\n");
            rs.close();

            pst.close();

            db.close();
        } catch(SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        class ThreadTest extends Thread {
            @Override
            public void run() {
                jTextArea1.setText("Starting Thread...\n");
                for(int i=0; i<10000; ++i) jTextArea1.append(i+" ");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                jTextArea1.append("\nThread Finished");
            }
        }
        ThreadTest t = new ThreadTest();
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        try {
            WinCtrl.setLabelImage(jLabel1, WinCtrl.getImageDirPath() + File.separator + "ctOSx.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        Integer rt = 0;
        class MyOnClickListener {
            private Object rt;
            private int choiceValue;
            public MyOnClickListener(Object rtObj, int choiceValue) {
                rt = rtObj; this.choiceValue = choiceValue;
            }

            public void ax() {
                rt = choiceValue;
            }
        }
        new MyOnClickListener(rt, 999).ax();
        jTextArea1.setText(rt.toString());
    }//GEN-LAST:event_jButton9ActionPerformed

    private void btnSetRCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetRCActionPerformed
        ((DefaultTableModel)tbSeat.getModel()).setRowCount(Integer.parseInt(getArgs()));
    }//GEN-LAST:event_btnSetRCActionPerformed

    private void btnSetCCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetCCActionPerformed
        ((DefaultTableModel)tbSeat.getModel()).setColumnCount(Integer.parseInt(getArgs()));
    }//GEN-LAST:event_btnSetCCActionPerformed

    private void btnFillNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFillNumberActionPerformed
        DefaultTableModel dtm = (DefaultTableModel)tbSeat.getModel();
        int rc = dtm.getRowCount(), cc = dtm.getColumnCount();
        Random rnd = new Random();
        for(int i=0; i<rc; ++i)
            for(int j=0; j<cc; ++j)
                dtm.setValueAt(rnd.nextInt(3), i, j);
    }//GEN-LAST:event_btnFillNumberActionPerformed

    private void btnSetColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetColorActionPerformed
        /*DefaultTableCellRenderer dtcr;
        DefaultTableModel dtm = (DefaultTableModel)tbSeat.getModel();
        int rc = dtm.getRowCount(), cc = dtm.getColumnCount();
        for(int i=0; i<rc; ++i)
            for(int j=0; j<cc; ++j) {
                dtcr = (DefaultTableCellRenderer)tbSeat.getCellRenderer(i, j);
                switch(Integer.parseInt(dtm.getValueAt(i, j).toString())) {
                    case 0: dtcr.setForeground(Color.BLACK); break;
                    case 1: dtcr.setForeground(Color.RED); break;
                    case 2: dtcr.setForeground(Color.GREEN); break;
                }
                dtcr.updateUI();
            }*/

    }//GEN-LAST:event_btnSetColorActionPerformed

    private void btnGetPosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGetPosActionPerformed
        System.out.println("x= "+tbSeat.getSelectedRow()+", y= "+tbSeat.getSelectedColumn());
    }//GEN-LAST:event_btnGetPosActionPerformed

    private void btnSetDefRdrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetDefRdrActionPerformed
        // 设置表模型
        Enumeration<TableColumn> e = tbSeat.getColumnModel().getColumns();
        while(e.hasMoreElements())
            e.nextElement().setCellRenderer(new MyCellRenderer());
        // 去掉表头
        tbSeat.getTableHeader().setVisible(false);
        DefaultTableCellRenderer tbHeaderRdr = new DefaultTableCellRenderer();
        tbHeaderRdr.setPreferredSize(new Dimension(0, 0));
        tbSeat.getTableHeader().setDefaultRenderer(tbHeaderRdr);
    }//GEN-LAST:event_btnSetDefRdrActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // 输入对话框
        jTextArea1.setText(JOptionPane.showInputDialog(this, "请输入新电影的ID（新ID与已有ID不能重复）：", "新建电影", JOptionPane.PLAIN_MESSAGE)==null? "NULL": "STR");
    }//GEN-LAST:event_jButton10ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        lab5.Lab5.setUIStyle(frmTest.class.getName());
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmTest().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFillNumber;
    private javax.swing.JButton btnGetPos;
    private javax.swing.JButton btnSetCC;
    private javax.swing.JButton btnSetColor;
    private javax.swing.JButton btnSetDefRdr;
    private javax.swing.JButton btnSetRC;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTree jTree1;
    private javax.swing.JTextArea taArgs;
    private javax.swing.JTable tbSeat;
    // End of variables declaration//GEN-END:variables
}
