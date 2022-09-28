package org.lkw;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Select {
    public static RfidLink selectById(String rfidlinkid) {
        String sql="select * from rfidlink WHERE id='"+rfidlinkid+"'";
        RfidLink rfidLink = new RfidLink();
        DBHelper dbHelper = new DBHelper(sql);
        try {
            ResultSet resultSet = dbHelper.preparedStatement.executeQuery();
            while (resultSet.next()){
                rfidLink.setId(resultSet.getString(1));
                    rfidLink.setValue(resultSet.getInt(2));
                    rfidLink.setName(resultSet.getString(3));
                    rfidLink.setOther(resultSet.getString(4));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rfidLink;
    }
}






