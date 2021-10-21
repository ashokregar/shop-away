package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConn {
    private Connection conn;
    final String jdbcUrl = "jdbc:postgresql://localhost:5432/shopaway";

    public PostgresConn(){

    }

    public Connection getConnection(){
        try {
            conn = DriverManager.getConnection(jdbcUrl);
            conn.setAutoCommit(false);
            System.out.println(conn);
            System.out.println("Opened database successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  conn;
    }

    public void closeConnection(){
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void commit(){
        if(conn != null) {
            try {
                conn.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
