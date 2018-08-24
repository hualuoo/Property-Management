package util;

import application.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQL_Connect {

    static Connection con;
    static Statement stmt;
    String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public int getConnection(String serverIP,String serverPort,String SQLTableName,String SQLUserName,String SQLPassword) {
        //测试连接    返回1连接成功    返回0连接失败
        try{
            Class.forName(driver);
            con = DriverManager.getConnection("jdbc:sqlserver://" + serverIP + ":" + serverPort + ";DatabaseName=" + SQLTableName,SQLUserName,SQLPassword);
            stmt = con.createStatement();
            return 1;
        }
        catch (Exception e) {
            return 0;
        }
    }
    public int login(String username,String password){
        //1为用户名、密码登录成功  2为密码错误  3为无该用户  0为数据库异常
        if(stmt==null) getConnection(Main.serverIP,Main.serverPort,Main.SQLTableName,Main.SQLUserName,Main.SQLPassword);
        String query="SELECT Username,Password FROM user_table";
        try{
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                if(rs.getString("Username").trim().equals(username)==true){
                    if(rs.getString("Password").trim().equals(password)==true) {
                        return 1;
                    }
                    return 2;
                }
            }
            return 3;
        }
        catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }
    public ResultSet sql_Query (String query){
        if(stmt==null) getConnection(Main.serverIP,Main.serverPort,Main.SQLTableName,Main.SQLUserName,Main.SQLPassword);
        try{
            return stmt.executeQuery(query);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public int sql_Update(String query){
        if(stmt==null) getConnection(Main.serverIP,Main.serverPort,Main.SQLTableName,Main.SQLUserName,Main.SQLPassword);
        try{
            int result = stmt.executeUpdate(query);
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}