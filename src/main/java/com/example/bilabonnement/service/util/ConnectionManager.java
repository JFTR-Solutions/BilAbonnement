package com.example.bilabonnement.service.util;

import com.example.bilabonnement.exceptions.CarLeasingException;
import com.example.bilabonnement.exceptions.carExceptionEnum;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.example.bilabonnement.controllers.LoginController.exceptionEnums;

public class ConnectionManager {

    public static Connection conn;

    public static Connection createConnection(String db_url, String uid, String pwd) {

        if (conn != null) {
            return conn;
        }
        try {
            conn = DriverManager.getConnection(db_url, uid, pwd);

        } catch (CarLeasingException | SQLException e) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
        return null;
    }

}
