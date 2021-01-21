package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcUtil {
    private static Connection conn = null;
    public static void getConn(){
        String driver = Config.getDriver();
        String url = Config.getURL();
        String username = Config.getUsername();
        String passwd = Config.getPassword();
        try{
            Class.forName(driver);
            conn = DriverManager.getConnection(url,username,passwd);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static void close(){
        try{
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    static PreparedStatement stmt;
    public static void add1(String path, String classname, String methodName,String commend){
        String sql = "insert into featureenvy values(?,?,?,?)";
        try{

            stmt=(PreparedStatement)conn.prepareStatement(sql);
            stmt.setString(1,path);
            stmt.setString(2,classname);
            stmt.setString(3,methodName);
            stmt.setString(4,commend);
            stmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void add2(String path, String className, String probabilities){
        String sql = "insert into godclass values(?,?,?)";
        try{
            stmt=(PreparedStatement)conn.prepareStatement(sql);
            stmt.setString(1,path);
            stmt.setString(2,className);
            stmt.setString(3,probabilities);
            stmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
