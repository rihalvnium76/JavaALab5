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

//用户信息管理
public class UI_UsersInfoManager {
    // frmManager控件
    private JTable UsersList;
    // 数据库访问
    private DBAccess db;
    
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
    public UI_UsersInfoManager(DBAccess db) {
        this.db = db;
        dataList = new ArrayList<DBItem>();
    }
    
    public void bindTable(JTable tbTicketList) {
        this.UsersList = UsersList;
    }
    
    // 控件初始化
    public void initComponents() {
        UsersList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // 列表值选择改变事件
                if (!e.getValueIsAdjusting()) {
                    int row = UsersList.getSelectedRow();
                    if (row == -1)
                        return; // 无选择

                    /* 载入当前值到控件中 */
                    DBItem item = dataList.get(row);
                }
            }
        });
    }
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
        DefaultTableModel dtm = (DefaultTableModel)UsersList.getModel();
        dtm.setRowCount(0); // 清空
        for(DBItem d : dataList)
            dtm.addRow(new Object[] {
                d.userName
            });
    }
    /*
        ---LOAD CODE END---
    */
    // 载入数据
    public void loadData() {
        writeTableFromArray();
    }
    
    /* 事件处理 */
    // 删除一项
    public void deleteItem() {
        int r = UsersList.getSelectedRow();
        if(r<0) {
            JOptionPane.showMessageDialog(null, "请选择要删除的项", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DefaultTableModel dtm = (DefaultTableModel)UsersList.getModel();
        try {
            PreparedStatement pstDel = db.getConnection().prepareStatement("delete from Users where LoginName=?");
            pstDel.setString(1, dataList.get(r).userName);
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
}
