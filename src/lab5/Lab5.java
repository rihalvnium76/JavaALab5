package lab5;

import lab5.UI.*;
public class Lab5 {
    public static void setUIStyle(String frmName) {
        // 窗体显示分隔使用当前系统风格
        try {
            String lookAndFeel = javax.swing.UIManager.getSystemLookAndFeelClassName();
            javax.swing.UIManager.setLookAndFeel(lookAndFeel);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmName).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        frmLogin.main(null);
    }
    
}
