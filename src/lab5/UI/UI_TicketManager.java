package lab5.UI;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import lab5.Module.*;
import java.sql.*;
import java.util.ArrayList;

// 电影票管理
public class UI_TicketManager {
    // frmManager控件
    private JComboBox<String> cmbTicketID, cmbCustomer, cmbScheduleID, cmbMovie, cmbTheater;
    private JCheckBox ckbNewTicket, ckbStatus, ckbNewSchedule, ckbNewTheater;
    private JTextField tfRow, tfCol, tfThCapacity, tfPrice, tfScheduleTime;
    //private JButton btnAddBatch, btnSave, btnDelete, btnReload;
    private JTable tbTicketList;
    // 数据库访问
    private DBAccess db;

    // 完整数据库信息表
    private class DBItem {
        public String ticketID, userID, userName;
        public int row, col;
        public String scheduleID, movieID, movieName, theaterID, theaterName;
        public int thCapacity;
        public double price;
        public Timestamp scheduleTime;
        public String status;

        public void setValueByIndex(int index, Object value) throws IndexOutOfBoundsException {
            switch(index) {
                case 0: ticketID = (String)value; break;
                case 1: userID = (String)value; break;
                case 2: userName = (String)value; break;
                case 3: row = (Integer)value; break;
                case 4: col = (Integer)value; break;
                case 5: scheduleID = (String)value; break;
                case 6: movieID = (String)value; break;
                case 7: movieName = (String)value; break;
                case 8: theaterID = (String)value; break;
                case 9: theaterName = (String)value; break;
                case 10: thCapacity = (Integer)value; break;
                case 11: price = (Double)value; break;
                case 12: scheduleTime = (Timestamp)value; break;
                case 13: status = (String)value; break;
                default: throw new IndexOutOfBoundsException("DBItem无效索引: "+index);
            }
        }
    }

    private ArrayList<DBItem> dataList;

    /* 初始化 */
    public UI_TicketManager(DBAccess db) {
        this.db = db;
    }

    public void bindCombox(JComboBox<String> cmbTicketID, JComboBox<String> cmbCustomer,
            JComboBox<String> cmbScheduleID, JComboBox<String> cmbMovie, JComboBox<String> cmbTheater) {
        this.cmbTicketID = cmbTicketID;
        this.cmbCustomer = cmbCustomer;
        this.cmbScheduleID = cmbScheduleID;
        this.cmbMovie = cmbMovie;
        this.cmbTheater = cmbTheater;
    }

    public void bindCheckBox(JCheckBox ckbNewTicket, JCheckBox ckbStatus, JCheckBox ckbNewSchedule, JCheckBox ckbNewTheater) {
        this.ckbNewTicket = ckbNewTicket;
        this.ckbStatus = ckbStatus;
        this.ckbNewSchedule = ckbNewSchedule;
        this.ckbNewTheater = ckbNewTheater;
    }

    public void bindTextField(JTextField tfRow, JTextField tfCol, JTextField tfThCapacity, JTextField tfPrice,
            JTextField tfScheduleTime) {
        this.tfRow = tfRow;
        this.tfCol = tfCol;
        this.tfThCapacity = tfThCapacity;
        this.tfPrice = tfPrice;
        this.tfScheduleTime = tfScheduleTime;
    }

    /*public void bindButton(JButton btnAddBatch, JButton btnSave, JButton btnDelete, JButton btnReload) {
        this.btnAddBatch = btnAddBatch;
        this.btnSave = btnSave;
        this.btnDelete = btnDelete;
        this.btnReload = btnReload;
    }*/

    public void bindTable(JTable tbTicketList) {
        this.tbTicketList = tbTicketList;
    }

    // 控件初始化
    public void initComponents() {
        tbTicketList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // 列表值选择改变事件
                if (!e.getValueIsAdjusting()) {
                    int row = tbTicketList.getSelectedRow();
                    if (row == -1)
                        return; // 无选择

                    /* 载入当前值到控件中 */
                    DBItem item = dataList.get(row);
                    // 载入ComboBox
                    cmbTicketID.setSelectedItem(item.ticketID);
                    cmbCustomer.setSelectedItem(item.userID + "|" + item.userName);
                    cmbScheduleID.setSelectedItem(item.scheduleID);
                    cmbMovie.setSelectedItem(item.movieID + "|" + item.movieName);
                    cmbTheater.setSelectedItem(item.theaterID + "|" + item.theaterName);
                    // 载入Text
                    tfRow.setText(String.valueOf(item.row));
                    tfCol.setText(String.valueOf(item.col));
                    tfScheduleTime.setText(item.scheduleTime.toString());
                    tfThCapacity.setText(String.valueOf(item.thCapacity));
                    tfPrice.setText(String.valueOf(item.price));
                    // 设置CheckBox
                    ckbNewTicket.setSelected(false);
                    ckbStatus.setSelected(true);
                    ckbNewSchedule.setSelected(false);
                    ckbNewTheater.setSelected(false);
                }
            }
        });
    }

    // 载入数据
    public void loadData() {
        /* Write ArrayList */

        Connection ct = db.getConnection();
        ResultSet rs;
        try {
            PreparedStatement pstQuery = ct.prepareStatement(
                "select Ticket.TicketID, Ticket.UserID, Users.LoginName, Ticket.Row, Ticket.Col, Ticket.ScheduleID, Movie.MovieID, Movie.MovieName, Theater.TheaterID, Theater.TheaterName, Theater.Capacity, Movie.Price, Schedule.ScheduleTime, Ticket.Status "+
                "from Ticket, Theater, Movie, Users, Schedule "+
                "where Ticket.ScheduleID=Schedule.ScheduleID and Schedule.MovieID=Movie.MovieID and Schedule.TheaterID=Theater.TheaterID and Ticket.UserID=Users.UserID");
            ;
            rs = pstQuery.executeQuery();
            dataList.clear(); // 清空旧数据
            while(rs.next()) {
                DBItem item = new DBItem();
                for(int i=0, cc=rs.getMetaData().getColumnCount(); i<cc; ++i)
                    item.setValueByIndex(i, rs.getObject(i+1));
                dataList.add(item);
            }

            rs.close();
            pstQuery.close();
            rs = null;
        } catch (SQLException | IndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, e.toString(), "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        /* ArrayList -> JTable */

        DefaultTableModel dtm = (DefaultTableModel)tbTicketList.getModel();
        dtm.setRowCount(0); // 清空列表
        for(DBItem r : dataList)
            dtm.addRow(new Object[] {
                r.ticketID,
                r.movieName,
                r.theaterName,
                r.scheduleTime.toString(),
                r.row + "-" + r.col,
                r.price,
                r.status
            });

        /* Write ComboBox */

        String[] sqls = new String[] {
            "select TicketID from Ticket",
            "select Ticket.UserID, Users.LoginName from Ticket, Users where Ticket.UserID=Users.UserID",
            "select ScheduleID from Schedule",
            "select movieID, movieName from Movie",
            "select TheaterID, TheaterName from Theater"
        };
        // 清空ComboBox
        JComboBox<String>[] cmbs = new JComboBox[] {
            cmbTicketID,
            cmbCustomer,
            cmbScheduleID,
            cmbMovie,
            cmbTheater
        };
        for(JComboBox<String> cmb : cmbs)
            cmb.removeAllItems();
        cmbCustomer.addItem("无");
        try {
            for(int i=0; i<sqls.length; ++i) {
                rs = db.queryDB(sqls[i]);
                int cc = rs.getMetaData().getColumnCount();
                while(rs.next())
                    if(cc==2)
                        cmbs[i].addItem(rs.getString(0) + "|" + rs.getString(1));
                    else if(cc==1)
                        cmbs[i].addItem(rs.getString(0));
                    else {
                        db.releaseQuery();
                        throw new IllegalArgumentException("数据库查询结果总列数不合法: " + cc);
                    }
                db.releaseQuery();
            }
        } catch(SQLException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.toString(), "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    /* 事件处理 */
    // 删除一项
    public void deleteItem() {
        int r = tbTicketList.getSelectedRow();
        if(r<0) return;
        DefaultTableModel dtm = (DefaultTableModel)tbTicketList.getModel();
        try {
            PreparedStatement pstDel = db.getConnection().prepareStatement("delete from Ticket where ticketID=?");
            pstDel.setString(1, dataList.get(r).ticketID);
            pstDel.executeUpdate();
            pstDel.close();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString(), "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        dtm.removeRow(r);
        dataList.remove(r);
    }
    // 保存一项
    public void saveItem() {
        // 预处理新建表需求 //
        String newTicketID = cmbTicketID.getSelectedItem().toString().trim(),
            newScheduleID = cmbScheduleID.getSelectedItem().toString().trim();
        String[] newTheater = cmbTheater.getSelectedItem().toString().trim().split("|", 2);
        Connection ct = db.getConnection();
        try {
            // 新放映厅
            PreparedStatement pstCrTh;
            if(ckbNewTheater.isSelected())
                pstCrTh = ct.prepareStatement("insert into Theater values(?,?,?)");
            else {
                pstCrTh = ct.prepareStatement("update Theater set TheaterID=?, TheaterName=?, Capacity=? where TheaterID=?");
                pstCrTh.setString(4, dataList.get(tbTicketList.getSelectedRow()).theaterID); // 原剧院ID
            }
            pstCrTh.setString(1, newTheater[0]);
            pstCrTh.setString(2, newTheater[1]);
            pstCrTh.setInt(3, Integer.valueOf(tfThCapacity.getText())); // 容量
            pstCrTh.executeUpdate();
            pstCrTh.close();

            // 新计划
            PreparedStatement pstCrSch;
            if(ckbNewSchedule.isSelected())
                pstCrSch = ct.prepareStatement("insert into Schedule values(?,?,?,?)");
            else {
                pstCrSch = ct.prepareStatement("update Schedule set ScheduleID=?, ScheduleTime=?, MovieID=?, TheaterID=? where TheaterID=?");
                pstCrSch.setString(5, dataList.get(tbTicketList.getSelectedRow()).scheduleID); // 原计划IDＩＤ
            }
            pstCrSch.setString(1, newScheduleID);
            pstCrSch.setTimestamp(2, Timestamp.valueOf(tfScheduleTime.getText().trim()));
            pstCrSch.setString(3, cmbMovie.getSelectedItem().toString().trim().split("|", 2)[0]); // 电影ID
            pstCrSch.setString(4, newTheater[0]);
            pstCrSch.executeUpdate();
            pstCrSch.close();

            // 放映厅容量校验
            PreparedStatement pstChkTk = ct.prepareStatement("select Ticket.TicketID from Ticket, Schedule where Ticket.ShceduleID=Schedule.ShceduleID and Ticket.ShceduleID=? and Schedule.TheaterID=?");
            pstChkTk.setString(1, newTicketID);
            pstChkTk.setString(2, newTheater[0]);
            ResultSet rs = pstChkTk.executeQuery();
            rs.last();
            int rc = rs.getRow();
            rs.close();
            pstChkTk.close();
            if(rs.getRow()+1>Integer.valueOf(tfThCapacity.getText().toString()))
                throw new IndexOutOfBoundsException("放映厅的该场次的安排已满，添加失败");

            // 新电影票
            PreparedStatement pstCrTk;
            if(ckbNewTicket.isSelected())
                pstCrTk = ct.prepareStatement("insert into Ticket values(?,?,?,?,?,?)");
            else {
                pstCrTk = ct.prepareStatement("update Ticket set TicketID=?, UserID=?, Row=?, Col=?, ScheduleID=?, Status=? where TicketID=?");
                pstCrTk.setString(7,  dataList.get(tbTicketList.getSelectedRow()).ticketID);
            }
            pstCrTk.setString(1, newTicketID);
            pstCrTk.setString(2, cmbCustomer.getSelectedItem().toString().trim().split("|", 2)[0]); // UserID
            pstCrTk.setInt(3, Integer.parseInt(tfRow.getText().toString().trim()));
            pstCrTk.setInt(4, Integer.parseInt(tfCol.getText().toString().trim()));
            pstCrTk.setString(5, newScheduleID);
            pstCrTk.setString(6, (ckbStatus.isSelected()? "已购": "未购"));
            pstCrTk.executeUpdate();
            pstCrTk.close();
        } catch(SQLException | IndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, e.toString(), "错误", JOptionPane.ERROR_MESSAGE);
            return;
        } catch(IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "场次输入格式不正确", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
    // TODO 批量添加电影票
    public void addBatch() {
        // 预处理新建表需求 //
        String newTicketID = cmbTicketID.getSelectedItem().toString().trim(),
            newScheduleID = cmbScheduleID.getSelectedItem().toString().trim();
        String[] newTheater = cmbTheater.getSelectedItem().toString().trim().split("|", 2);
        Connection ct = db.getConnection();
        try {
            // 新放映厅
            PreparedStatement pstCrTh;
            if(ckbNewTheater.isSelected())
                pstCrTh = ct.prepareStatement("insert into Theater values(?,?,?)");
            else {
                pstCrTh = ct.prepareStatement("update Theater set TheaterID=?, TheaterName=?, Capacity=? where TheaterID=?");
                pstCrTh.setString(4, dataList.get(tbTicketList.getSelectedRow()).theaterID); // 原剧院ID
            }
            pstCrTh.setString(1, newTheater[0]);
            pstCrTh.setString(2, newTheater[1]);
            pstCrTh.setInt(3, Integer.valueOf(tfThCapacity.getText())); // 容量
            pstCrTh.executeUpdate();
            pstCrTh.close();

            // 新计划
            PreparedStatement pstCrSch;
            if(ckbNewSchedule.isSelected())
                pstCrSch = ct.prepareStatement("insert into Schedule values(?,?,?,?)");
            else {
                pstCrSch = ct.prepareStatement("update Schedule set ScheduleID=?, ScheduleTime=?, MovieID=?, TheaterID=? where TheaterID=?");
                pstCrSch.setString(5, dataList.get(tbTicketList.getSelectedRow()).scheduleID); // 原计划IDＩＤ
            }
            pstCrSch.setString(1, newScheduleID);
            pstCrSch.setTimestamp(2, Timestamp.valueOf(tfScheduleTime.getText().trim()));
            pstCrSch.setString(3, cmbMovie.getSelectedItem().toString().trim().split("|", 2)[0]); // 电影ID
            pstCrSch.setString(4, newTheater[0]);
            pstCrSch.executeUpdate();
            pstCrSch.close();

            int rmax = Integer.parseInt(tfRow.getText().toString().trim()), cmax = Integer.parseInt(tfCol.getText().toString().trim());
            int iTkID = Integer.parseInt(newTicketID); // 未使用过的电影票ID起始值
            endfnc:
            for(int i=1; i<=rmax; ++i) {
                for(int j=1; j<=cmax; ++j) {
                    // 放映厅容量校验
                    PreparedStatement pstChkTk = ct.prepareStatement("select Ticket.TicketID from Ticket, Schedule where Ticket.ShceduleID=Schedule.ShceduleID and Ticket.ShceduleID=? and Schedule.TheaterID=?");
                    pstChkTk.setString(1, newTicketID);
                    pstChkTk.setString(2, newTheater[0]);
                    ResultSet rs = pstChkTk.executeQuery();
                    rs.last();
                    int rc = rs.getRow(); // 库存数量
                    rs.close();
                    pstChkTk.close();
                    if(rs.getRow()+1>Integer.valueOf(tfThCapacity.getText().toString()))
                        break endfnc;

                    // 新电影票
                    PreparedStatement pstCrTk;
                    pstCrTk = ct.prepareStatement("insert into Ticket values(?,?,?,?,?,?)");
                    pstCrTk.setString(1, String.valueOf(iTkID++));
                    pstCrTk.setString(2, cmbCustomer.getSelectedItem().toString().trim().split("|", 2)[0]); // UserID
                    pstCrTk.setInt(3, i);
                    pstCrTk.setInt(4, j);
                    pstCrTk.setString(5, newScheduleID);
                    pstCrTk.setString(6, (ckbStatus.isSelected()? "已购": "未购"));
                    pstCrTk.executeUpdate();
                    pstCrTk.close();
                }
            }
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString(), "错误", JOptionPane.ERROR_MESSAGE);
            return;
        } catch(IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "场次输入格式不正确", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

}
