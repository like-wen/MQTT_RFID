package org.lkw;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DBHelper {
    //连接数据库
    public static final String url="jdbc:mysql://localhost:3306/rfid?characterEncoding=utf-8";

    public static final String name="com.mysql.cj.jdbc.Driver";
    public static final String user="root";
    public static final String password="148963";
    public Connection connection=null;
    public PreparedStatement preparedStatement=null;
    public DBHelper(String sql){
        try {
            Class.forName(name);
            connection= DriverManager.getConnection(url,user,password);
            preparedStatement=connection.prepareStatement(sql);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public void close(){
        try{
            this.connection.close();
            this.preparedStatement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
