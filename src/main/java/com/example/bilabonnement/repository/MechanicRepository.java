package com.example.bilabonnement.repository;

import com.example.bilabonnement.exceptions.CarLeasingException;
import com.example.bilabonnement.exceptions.carExceptionEnum;
import com.example.bilabonnement.service.util.ConnectionManager;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.example.bilabonnement.controllers.LoginController.exceptionEnums;
import static com.example.bilabonnement.service.util.ConnectionManager.conn;

@Repository
public class MechanicRepository {

    public MechanicRepository() {
        if (conn == null) {
            ConnectionManager.createConnection(System.getenv("JDBCUrl"), System.getenv("JDBCUsername"), System.getenv("JDBCPassword"));
        }

    }
    public void createDamageReport(int price, String placement, String description, int carId, int rentalAgreementId) {
        try {
            String queryCreate = "INSERT INTO damages (damage_id, price, placement, description, carscar_id, rentalAgreement_id)" +
                    "VALUES (DEFAULT,?,?,?,?,?)";

            PreparedStatement psts = conn.prepareStatement(queryCreate);

            psts.setInt(1, price);
            psts.setString(2, placement);
            psts.setString(3, description);
            psts.setInt(4, carId);
            psts.setInt(5, rentalAgreementId);


            psts.executeUpdate();

        } catch (NullPointerException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }
}


