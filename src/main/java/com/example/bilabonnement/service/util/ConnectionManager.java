package com.example.bilabonnement.service.util;

import com.example.bilabonnement.exceptions.CarLeasingException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    public static Connection conn;

    public static Connection createConnection(String db_url, String uid, String pwd) {

        if (conn != null) {
            return conn;
        }
        try {
            conn = DriverManager.getConnection(db_url, uid, pwd);

        } catch (NullPointerException | SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
