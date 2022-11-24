package com.example.bilabonnement.service.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    public static Connection conn;

    public static void createConnection(String db_url, String uid, String pwd) {

        if (conn != null) {
            try {
                conn = DriverManager.getConnection(db_url, uid, pwd);

            } catch (SQLException e) {
                System.out.printf("Couldn't connect to db");
                e.printStackTrace();
            }
        }
    }
}
