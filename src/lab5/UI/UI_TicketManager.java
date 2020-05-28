package lab5.UI;

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
    private JComboBox<String> cmbTicketID, cmbCustomer, cmbScheduleID, cmbMovie, cmbTheater, cmbTkIDOperation, cmbSchIDOperation, cmbThOperation;
    private JTextField tfRow, tfCol, tfThCapacity, tfPrice, tfScheduleTime;
    private JTable tbTicketList;
    // 数据库访问
    private DBAccess db;

    // 完整数据库信息表
    private class DBItem {
        public String ticketID, userID, userName;
        public int row, col;
        public String scheduleID, movieID, movieName, theaterID, theaterName;
        public int thCapacity;
        public float price;
        public Timestamp scheduleTime;
        public String status;

        public static final int MAX_INDEX = 13;
        
        public void setValueByIndex(int index, Object value) throws IndexOutOfBoundsException {
            switch(index) {
                case 0: userName = (String)value; break; // NOTICE!!!
                case 1: ticketID = (String)value; break;
                case 2: userID = (String)value; break;
                case 3: row = (Integer)value; break;
                case 4: col = (Integer)value; break;
                case 5: scheduleID = (String)value; break;
                case 6: movieID = (String)value; break;
                case 7: movieName = (String)value; break;
                case 8: theaterID = (String)value; break;
                case 9: theaterName = (String)value; break;
                case 10: thCapacity = (Integer)value; break;
                case 11: price = (Float)value; break;
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
        dataList = new ArrayList<DBItem>();
    }

    public void bindCombox(JComboBox<String> cmbTicketID, JComboBox<String> cmbCustomer,
            JComboBox<String> cmbScheduleID, JComboBox<String> cmbMovie, JComboBox<String> cmbTheater, JComboBox<String> cmbTkIDOperation, JComboBox<String> cmbSchIDOperation, JComboBox<String> cmbThOperation) {
        this.cmbTicketID = cmbTicketID;
        this.cmbCustomer = cmbCustomer;
        this.cmbScheduleID = cmbScheduleID;
        this.cmbMovie = cmbMovie;
        this.cmbTheater = cmbTheater;
        this.cmbTkIDOperation = cmbTkIDOperation;
        this.cmbSchIDOperation = cmbSchIDOperation;
        this.cmbThOperation = cmbThOperation;
    }

    public void bindTextField(JTextField tfRow, JTextField tfCol, JTextField tfThCapacity, JTextField tfPrice,
            JTextField tfScheduleTime) {
        this.tfRow = tfRow;
        this.tfCol = tfCol;
        this.tfThCapacity = tfThCapacity;
        this.tfPrice = tfPrice;
        this.tfScheduleTime = tfScheduleTime;
    }

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
                }
            }
        });
    }
    // 载入Schedule对应数据
    public void loadSchedule() throws SQLException {
        PreparedStatement pst = db.getConnection().prepareStatement("select scheduletime,movieid,theaterid from Schedule where scheduleid=?");
        Object t = cmbScheduleID.getSelectedItem();
        t = t==null?"":t; // avoid NullPointerException
        pst.setString(1, t.toString());
        ResultSet rs = pst.executeQuery();
        if(rs.next()) {
            tfScheduleTime.setText(rs.getTimestamp(1).toString());
            loadMovie(rs.getString(2));
            loadTheater(rs.getString(3));
        }
        rs.close();
        pst.close();
        loadTheater();
        loadMovie();
    }
    // 载入Theater对应数据
    private void loadTheater(String id) throws SQLException {
        PreparedStatement pst = db.getConnection().prepareStatement("select Theatername from theater where Theaterid=?");
        pst.setString(1, id);
        ResultSet rs = pst.executeQuery();
        if(rs.next())
            cmbTheater.setSelectedItem(id + "|" + rs.getString(1));
        rs.close();
        pst.close();
        loadTheater();
    }
    public void loadTheater() throws SQLException {
        PreparedStatement pst = db.getConnection().prepareStatement("select capacity from theater where theaterid=?");
        Object t = cmbTheater.getSelectedItem();
        t = t==null?"":t; // avoid NullPointerException
        pst.setString(1, t.toString().split("\\|", 2)[0]);
        ResultSet rs = pst.executeQuery();
        if(rs.next())
            tfThCapacity.setText(rs.getString(1));
        rs.close();
        pst.close();
    }
    // 载入Movie对应数据
    private void loadMovie(String id) throws SQLException {
        PreparedStatement pst = db.getConnection().prepareStatement("select moviename from movie where movieid=?");
        pst.setString(1, id);
        ResultSet rs = pst.executeQuery();
        if(rs.next())
            cmbMovie.setSelectedItem(id + "|" + rs.getString(1));
        rs.close();
        pst.close();
        loadMovie();
    }
    public void loadMovie() throws SQLException {
        PreparedStatement pst = db.getConnection().prepareStatement("select price from movie where movieid=?");
        Object t = cmbMovie.getSelectedItem();
        t = t==null?"":t; // avoid NullPointerException
        pst.setString(1, t.toString().split("\\|", 2)[0]);
        ResultSet rs = pst.executeQuery();
        if(rs.next())
            tfPrice.setText(String.valueOf(rs.getFloat(1)));
        rs.close();
        pst.close();
    }

    /*
        ---LOAD CODE---
    */
    private void writeArrayList() throws SQLException {
        ResultSet rs = db.queryDB("select Ticket.TicketID,Ticket.UserID,Ticket.row,ticket.col,ticket.scheduleid,schedule.movieid,movie.moviename,schedule.theaterid,theater.theatername,theater.capacity,movie.price,schedule.scheduletime,ticket.status from ticket,movie,schedule,theater where ticket.scheduleid=schedule.scheduleid and schedule.movieid=movie.movieid and schedule.theaterid=theater.theaterid");
        dataList.clear(); // 清空
        while(rs.next()) {
            DBItem item = new DBItem();
            for(int i=1; i<=DBItem.MAX_INDEX; ++i)
                item.setValueByIndex(i, rs.getObject(i)); // except username
            if(item.userID==null)
                item.setValueByIndex(0, null); // username
            else {
                PreparedStatement pst = db.getConnection().prepareStatement("select loginname from users where userid=?");
                pst.setString(1, item.userID);
                ResultSet rsu = pst.executeQuery();
                if(rsu.next()) item.setValueByIndex(0, rsu.getString(1)); // username
                rsu.close();
                pst.close();
            }
            dataList.add(item);
        }
        db.releaseQuery();
    }
    private void writeTableFromArray() {
        DefaultTableModel dtm = (DefaultTableModel)tbTicketList.getModel();
        dtm.setRowCount(0); // 清空
        for(DBItem d : dataList)
            dtm.addRow(new Object[] {
                d.ticketID,
                d.movieName,
                d.theaterName,
                d.scheduleTime.toString(),
                d.row + "-" + d.col,
                d.price,
                d.status
            });
    }
    private void writeComboBox() throws SQLException, IllegalArgumentException {
        // clear
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

        // write
        String[] sqls = new String[] {
            "select TicketID from Ticket",
            "select Users.UserID,Users.LoginName from Users",
            "select ScheduleID from Schedule",
            "select movieID, movieName from Movie",
            "select TheaterID, TheaterName from Theater"
        };
        for(int i=0; i<sqls.length; ++i) {
            ResultSet rs = db.queryDB(sqls[i]);
            int cc = rs.getMetaData().getColumnCount();
            while(rs.next())
                if(cc==2)
                    cmbs[i].addItem(rs.getString(1) + "|" + rs.getString(2));
                else if(cc==1)
                    cmbs[i].addItem(rs.getString(1));
                else {
                    db.releaseQuery();
                    throw new IllegalArgumentException("数据库查询结果总列数不合法: " + cc);
                }
            db.releaseQuery();
        }
    }
    /*
        ---LOAD CODE END---
    */
    // 载入数据
    public void loadData() {
        try {
            writeArrayList();
            writeTableFromArray();
            writeComboBox();
        } catch(SQLException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /* 事件处理 */
    // 删除一项
    public void deleteItem() {
        int r = tbTicketList.getSelectedRow();
        if(r<0) {
            JOptionPane.showMessageDialog(null, "请选择要删除的项", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DefaultTableModel dtm = (DefaultTableModel)tbTicketList.getModel();
        try {
            PreparedStatement pstDel = db.getConnection().prepareStatement("delete from Ticket where ticketID=?");
            pstDel.setString(1, dataList.get(r).ticketID);
            pstDel.executeUpdate();
            pstDel.close();
        } catch(SQLException e) {
            //JOptionPane.showMessageDialog(null, e.toString(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }
        dtm.removeRow(r);
        dataList.remove(r);
        loadData(); // 刷新
    }

    /*
        ---SAVE CODE---
    */
    private void processTicket() throws SQLException, IllegalArgumentException {
        PreparedStatement pst;
        int t;
        switch(cmbTkIDOperation.getSelectedIndex()) {
            case 0: // update
                pst = db.getConnection().prepareStatement("update Ticket set UserID=?,row=?,col=?,scheduleID=?,status=? where ticketID=?");
                t = cmbCustomer.getSelectedIndex();
                pst.setString(1, t<1? null: cmbCustomer.getSelectedItem().toString().split("\\|", 2)[0]);
                pst.setInt(2, Integer.valueOf(tfRow.getText()));
                pst.setInt(3, Integer.valueOf(tfCol.getText()));
                pst.setString(4, cmbScheduleID.getSelectedItem().toString());
                pst.setString(5,  t<1? "未购": "已购");
                pst.setString(6, cmbTicketID.getSelectedItem().toString());
                pst.executeUpdate();
                pst.close();
                pst = db.getConnection().prepareStatement("update Movie set price=? where movieid=?");
                pst.setFloat(1, Float.valueOf(tfPrice.getText()));
                pst.setString(2, cmbMovie.getSelectedItem().toString().split("\\|", 2)[0]);
                pst.executeUpdate();
                pst.close();
                break;
            case 1: // insert
                pst = db.getConnection().prepareStatement("insert into Ticket values(?,?,?,?,?,?)");
                pst.setString(1, cmbTicketID.getSelectedItem().toString());
                t = cmbCustomer.getSelectedIndex();
                pst.setString(2, t<1? null: cmbCustomer.getSelectedItem().toString().split("\\|", 2)[0]);
                pst.setInt(3, Integer.valueOf(tfRow.getText()));
                pst.setInt(4, Integer.valueOf(tfCol.getText()));
                pst.setString(5, cmbScheduleID.getSelectedItem().toString());
                pst.setString(6, t<1? "未购": "已购");
                pst.executeUpdate();
                pst.close();
                pst = db.getConnection().prepareStatement("update Movie set price=? where movieid=?");
                pst.setFloat(1, Float.valueOf(tfPrice.getText()));
                pst.setString(2, cmbMovie.getSelectedItem().toString().split("\\|", 2)[0]);
                pst.executeUpdate();
                pst.close();
        }
    }
    private void processSchedule() throws SQLException, IllegalArgumentException {
        PreparedStatement pst;
        switch(cmbSchIDOperation.getSelectedIndex()) {
            case 0: return; // select only
            case 1: // update
                pst = db.getConnection().prepareStatement("update Schedule set ScheduleTime=?,MovieID=?,TheaterID=? where ScheduleID=?");
                pst.setTimestamp(1, Timestamp.valueOf(tfScheduleTime.getText()));
                pst.setString(2, cmbMovie.getSelectedItem().toString().split("\\|", 2)[0]);
                pst.setString(3, cmbTheater.getSelectedItem().toString().split("\\|", 2)[0]);
                pst.setString(4, cmbScheduleID.getSelectedItem().toString());
                pst.executeUpdate();
                pst.close();
                break;
            case 2: // insert
                pst = db.getConnection().prepareStatement("insert into Schedule values(?,?,?,?)");
                pst.setTimestamp(2, Timestamp.valueOf(tfScheduleTime.getText()));
                pst.setString(3, cmbMovie.getSelectedItem().toString().split("\\|", 2)[0]);
                pst.setString(4, cmbTheater.getSelectedItem().toString().split("\\|", 2)[0]);
                pst.setString(1, cmbScheduleID.getSelectedItem().toString());
                pst.executeUpdate();
                pst.close();
        }
    }
    private void processTheater() throws SQLException, IllegalArgumentException {
        PreparedStatement pst;
        String[] t = cmbTheater.getSelectedItem().toString().split("\\|", 2);
        if(t.length!=2) throw new IllegalArgumentException();
        switch(cmbThOperation.getSelectedIndex()) {
            case 0: return; // select only
            case 1: // update
                // not support to change theater name
                pst = db.getConnection().prepareStatement("update Theater set Capacity=? where TheaterID=?");
                pst.setInt(1, Integer.valueOf(tfThCapacity.getText()));
                pst.setString(2, t[0]);
                pst.executeUpdate();
                pst.close();
                break;
            case 2: // insert
                pst = db.getConnection().prepareStatement("insert into Theater values(?,?,?)");
                pst.setInt(3, Integer.valueOf(tfThCapacity.getText()));
                pst.setString(1, t[0]);
                pst.setString(2, t[1]);
                pst.executeUpdate();
                pst.close();
                break;
        }
    }
    private boolean isTheaterFull() throws SQLException {
        PreparedStatement pst = db.getConnection().prepareStatement("select ticketID from ticket where scheduleid=?");
        pst.setString(1, cmbTheater.getSelectedItem().toString());
        ResultSet rs = pst.executeQuery();
        int n = 0;
        while(rs.next()) ++n;
        rs.close();
        pst.close();
        if(n+1>Integer.valueOf(tfThCapacity.getText()))
            return true;
        else
            return false;
    }
    /*
        ---SAVE CODE END---
    */

    // 保存一项
    public void saveItem() {
        try {
            processTheater();
            processSchedule();
            if(isTheaterFull())
                if(JOptionPane.showConfirmDialog(null, "该放映厅安排已满，是否强行添加？", "警告", JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION)
                    return;
            processTicket();
        } catch(IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "场次/放映厅ID+名字输入格式不正确", "错误", JOptionPane.ERROR_MESSAGE);
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "电影票/计划/放映厅ID已存在，无法新建\n" + e.toString(), "错误", JOptionPane.ERROR_MESSAGE);
            //e.printStackTrace();
        }
        loadData(); // 刷新
    }
    // 批量添加电影票
    /*public void addBatch() {
        // 预处理新建表需求 //
        String newTicketID = cmbTicketID.getSelectedItem().toString().trim(),
            newScheduleID = cmbScheduleID.getSelectedItem().toString().trim();
        String[] newTheater = cmbTheater.getSelectedItem().toString().trim().split("\\|", 2);
        Connection ct = db.getConnection();
        try {
            // 新放映厅
            PreparedStatement pstCrTh;
            if(ckbNewTheater.isSelected())
                pstCrTh = ct.prepareStatement("insert into Theater values(?,?,?)");
            else {
                pstCrTh = ct.prepareStatement("update Theater set TheaterID=?, TheaterName=?, Capacity=? where TheaterID=?");
                int i = tbTicketList.getSelectedRow();
                if(i==-1) throw new IndexOutOfBoundsException("请先在列表选择要修改的原项目");
                pstCrTh.setString(4, dataList.get(i).theaterID); // 原剧院ID
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
                int i = tbTicketList.getSelectedRow();
                if(i==-1) throw new IndexOutOfBoundsException("请先在列表选择要修改的原项目");
                pstCrSch.setString(5, dataList.get(i).scheduleID); // 原计划ID
            }
            pstCrSch.setString(1, newScheduleID);
            pstCrSch.setTimestamp(2, Timestamp.valueOf(tfScheduleTime.getText().trim()));
            pstCrSch.setString(3, cmbMovie.getSelectedItem().toString().trim().split("\\|", 2)[0]); // 电影ID
            pstCrSch.setString(4, newTheater[0]);
            pstCrSch.executeUpdate();
            pstCrSch.close();

            int rmax = Integer.parseInt(tfRow.getText().toString().trim()), cmax = Integer.parseInt(tfCol.getText().toString().trim());
            int iTkID = Integer.parseInt(newTicketID); // 未使用过的电影票ID起始值
            // 放映厅容量校验
            PreparedStatement pstChkTk = ct.prepareStatement("select Ticket.TicketID from Ticket, Schedule where Ticket.ScheduleID=Schedule.ScheduleID and Ticket.ScheduleID=? and Schedule.TheaterID=?");
            // 新电影票
            PreparedStatement pstCrTk = ct.prepareStatement("insert into Ticket values(?,?,?,?,?,?)");
            endfnc:
            for(int i=1; i<=rmax; ++i) {
                for(int j=1; j<=cmax; ++j) {
                    // 放映厅容量校验
                    pstChkTk.clearParameters();
                    pstChkTk.setString(1, newTicketID);
                    pstChkTk.setString(2, newTheater[0]);
                    ResultSet rs = pstChkTk.executeQuery();
                    int rc = 0;
                    while(rs.next()) ++rc;
                    rs.close();
                    pstChkTk.close();
                    if(rc+1>Integer.valueOf(tfThCapacity.getText().toString()))
                        break endfnc;

                    // 新电影票
                    pstCrTk.clearParameters();
                    pstCrTk.setString(1, String.valueOf(iTkID++));
                    String t = cmbCustomer.getSelectedItem().toString().trim().split("\\|", 2)[0];
                    pstCrTk.setString(2, t=="无"? null: t); // UserID
                    pstCrTk.setInt(3, i);
                    pstCrTk.setInt(4, j);
                    pstCrTk.setString(5, newScheduleID);
                    pstCrTk.setString(6, (ckbStatus.isSelected()? "已购": "未购"));
                    pstCrTk.executeUpdate();
                }
            }
            pstChkTk.close();
            pstCrTk.close();
        } catch(SQLException e) {
            // JOptionPane.showMessageDialog(null, e.toString(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch(IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "场次输入格式不正确", "错误", JOptionPane.ERROR_MESSAGE);
        } finally {}
    }*/

}
