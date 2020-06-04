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
        public String thCapacity;
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
                case 10: thCapacity = (String)value; break;  // NOTICE!!!
                case 11: price = (Float)value; break;
                case 12: scheduleTime = (Timestamp)value; break;
                case 13: status = (String)value; break;
                default: throw new IndexOutOfBoundsException("DBItem无效索引: "+index);
            }
        }
    }

    private ArrayList<DBItem> dataList;

    /* 初始化与控件绑定 */
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
                    if(item.userID == null)
                        cmbCustomer.setSelectedItem("无"); // NOTCIE
                    else
                        cmbCustomer.setSelectedItem(item.userID + "|" + item.userName);
                    cmbScheduleID.setSelectedItem(item.scheduleID);
                    cmbMovie.setSelectedItem(item.movieID + "|" + item.movieName);
                    cmbTheater.setSelectedItem(item.theaterID + "|" + item.theaterName);
                    // 载入Text
                    tfRow.setText(String.valueOf(item.row));
                    tfCol.setText(String.valueOf(item.col));
                    tfScheduleTime.setText(item.scheduleTime.toString());
                    tfThCapacity.setText(item.thCapacity);
                    tfPrice.setText(String.valueOf(item.price));
                }
            }
        });
    }
    // 载入Schedule对应数据
    public void loadSchedule() throws SQLException {
        PreparedStatement pst = db.getConnection().prepareStatement("select scheduletime,movieid,theaterid from Schedule where scheduleid=?");
        // avoid NullPointerException
        pst.setString(1, WinCtrl.avoidNullString(cmbScheduleID.getSelectedItem()).toString());
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
        // avoid NullPointerException
        pst.setString(1, WinCtrl.avoidNullString(cmbTheater.getSelectedItem()).toString().split("\\|", 2)[0]);
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
        // avoid NullPointerException
        pst.setString(1, WinCtrl.avoidNullString(cmbMovie.getSelectedItem()).toString().split("\\|", 2)[0]);
        ResultSet rs = pst.executeQuery();
        if(rs.next())
            tfPrice.setText(String.valueOf(rs.getFloat(1)));
        rs.close();
        pst.close();
    }

    /*
        ---LOAD CODE---
    */
    // 写入ArrayList
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
    // 写入JTable
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
    // 写入JComboBox
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
    // 删除电影票
    public void deleteTicket() {
        int[] rl = tbTicketList.getSelectedRows();
        if(rl.length==0) {
            JOptionPane.showMessageDialog(null, "请选择要删除的项", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DefaultTableModel dtm = (DefaultTableModel)tbTicketList.getModel();
        try {
            PreparedStatement pst = db.getConnection().prepareStatement("delete from Ticket where ticketID=?");
            for(int r : rl) {
                pst.clearParameters();
                pst.setString(1, dataList.get(r).ticketID);
                pst.executeUpdate();

                deleteUselessSchedule();
            }
            pst.close();
        } catch(SQLException e) {
            //JOptionPane.showMessageDialog(null, e.toString(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }
        loadData(); // 刷新
    }
    // 删除无用计划
    public void deleteUselessSchedule() throws SQLException {
        db.modifyDB("delete from Schedule where scheduleid not in (select scheduleid from ticket)");
    }

    /*
        ---SAVE CODE---
    */
    private void processTicket(int row, int col, String ticketID) throws SQLException, IllegalArgumentException {
        PreparedStatement pst;
        int t;
        switch(cmbTkIDOperation.getSelectedIndex()) {
            case 0: // update
                pst = db.getConnection().prepareStatement("update Ticket set UserID=?,row=?,col=?,scheduleID=?,status=? where ticketID=?");
                t = cmbCustomer.getSelectedIndex();
                pst.setString(1, t<1? null: cmbCustomer.getSelectedItem().toString().split("\\|", 2)[0]);
                pst.setInt(2, row);
                pst.setInt(3, col);
                pst.setString(4, cmbScheduleID.getSelectedItem().toString());
                pst.setString(5,  t<1? "未售": "已售");
                pst.setString(6, ticketID);
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
                pst.setString(1, ticketID);
                t = cmbCustomer.getSelectedIndex();
                pst.setString(2, t<1? null: cmbCustomer.getSelectedItem().toString().split("\\|", 2)[0]);
                pst.setInt(3, row);
                pst.setInt(4, col);
                pst.setString(5, cmbScheduleID.getSelectedItem().toString());
                pst.setString(6, t<1? "未售": "已售");
                pst.executeUpdate();
                pst.close();
                pst = db.getConnection().prepareStatement("update Movie set price=? where movieid=?");
                pst.setFloat(1, Float.valueOf(tfPrice.getText()));
                pst.setString(2, cmbMovie.getSelectedItem().toString().split("\\|", 2)[0]);
                pst.executeUpdate();
                pst.close();
        }
    }
    private void processTicket() throws SQLException, IllegalArgumentException {
        processTicket(Integer.valueOf(tfRow.getText()), Integer.valueOf(tfCol.getText()), cmbTicketID.getSelectedItem().toString());
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
                pst.setString(1, tfThCapacity.getText());
                pst.setString(2, t[0]);
                pst.executeUpdate();
                pst.close();
                break;
            case 2: // insert
                pst = db.getConnection().prepareStatement("insert into Theater values(?,?,?)");
                pst.setString(3, tfThCapacity.getText());
                pst.setString(1, t[0]);
                pst.setString(2, t[1]);
                pst.executeUpdate();
                pst.close();
                break;
        }
    }
    private boolean isTheaterFull() throws SQLException, IllegalArgumentException {
        PreparedStatement pst = db.getConnection().prepareStatement("select ticketID from ticket where scheduleid=?");
        pst.setString(1, cmbScheduleID.getSelectedItem().toString().split("\\|", 2)[0]);
        ResultSet rs = pst.executeQuery();
        int n = 0;
        while(rs.next()) ++n;
        rs.close();
        pst.close();
        // maxRow x maxCol
        String[] maxSeat = tfThCapacity.getText().toLowerCase().split("x", 2);
        if(maxSeat.length!=2) throw new IllegalArgumentException();
        if(n+1 > Integer.valueOf(maxSeat[0])*Integer.valueOf(maxSeat[1]))
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
            JOptionPane.showMessageDialog(null, "场次/放映厅ID+名字/放映厅规格输入格式不正确", "错误", JOptionPane.ERROR_MESSAGE);
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "电影票/计划/放映厅ID已存在，无法新建\n" + e.toString(), "错误", JOptionPane.ERROR_MESSAGE);
            //e.printStackTrace();
        }
        loadData(); // 刷新
    }

    /*
        ---BATCH CODE---
    */
    // 获取未使用的ticketID [code v2]
    // 返回值：未使用的纯数字ticketID
    private int getAvailableTicketID() throws SQLException {
        ResultSet rs = db.queryDB("select ticketID from ticket order by ticketid asc");
        int ret = 0; // start-id is 1
        try {
            while(rs.next() && ++ret==Integer.parseInt(rs.getString(1)));
        } catch(IllegalArgumentException e) {
            // 非数字ID跳过
        }
        db.releaseQuery();
        return ret;
    }
    // 检查当前座位的电影票是否存在
    // 返回值：0不存在 1存在，未售 2存在已售
    private int ticketExist(int row, int col, String scheduleID) throws SQLException {
        int ret = 0;
        PreparedStatement pst = db.getConnection().prepareStatement("select status from ticket where row=? and col=? and scheduleid=?");
        pst.setInt(1, row);
        pst.setInt(2, col);
        pst.setString(3, scheduleID);
        ResultSet rs = pst.executeQuery();
        if(rs.next())
            if(rs.getString(1).equals("已售"))
                ret = 2;
            else // 未售
                ret = 1;
        rs.close();
        pst.close();
        return ret;
    }
    /*
        ---BATCH CODE END---
    */
    // 批量填充电影票
    public void batchFillTicket() {
        try {
            processTheater();
            processSchedule();
            String[] maxSeat = tfThCapacity.getText().toLowerCase().split("x", 2);
            if(maxSeat.length!=2) throw new IllegalArgumentException();
            int maxRow = Integer.valueOf(maxSeat[0]), maxCol = Integer.valueOf(maxSeat[1]);
            // 购票人全部为null
            cmbCustomer.setSelectedIndex(0);
            // 新建电影票模式
            cmbTkIDOperation.setSelectedIndex(1);
            // 批量添加
            for(int i=1; i<=maxRow; ++i)
                for(int j=1; j<=maxCol; ++j)
                    if(ticketExist(i, j, cmbScheduleID.getSelectedItem().toString())==0) {
                        String ticketID = String.valueOf(getAvailableTicketID());
                        if(ticketID.length()==1) ticketID = "0" + ticketID;
                        processTicket(i, j, ticketID);
                    }
        } catch(IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "场次/放映厅ID+名字/放映厅规格输入格式不正确", "错误", JOptionPane.ERROR_MESSAGE);
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "电影票/计划/放映厅ID已存在，无法新建\n\n" + e.toString(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        loadData(); // 刷新
    }

}
