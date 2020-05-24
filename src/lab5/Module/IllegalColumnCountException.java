/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lab5.Module;

public class IllegalColumnCountException extends IllegalArgumentException {
    private int jtc, rsc;
    public IllegalColumnCountException(int jtColumn, int rsColumn) {
        jtc = jtColumn; rsc = rsColumn;
    }
    @Override
    public String toString() {
        return "不合法的表列数:\nJTable = "+jtc+", ResultSet = "+rsc;
    }
}