package com.example.bilabonnement.repository;

import com.example.bilabonnement.exceptions.CarLeasingException;
import com.example.bilabonnement.exceptions.carExceptionEnum;
import com.example.bilabonnement.models.cars.Damage;
import com.example.bilabonnement.service.util.ConnectionManager;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static com.example.bilabonnement.controllers.LoginController.exceptionEnums;
import static com.example.bilabonnement.service.util.ConnectionManager.conn;

@Repository
public class MechanicRepository {

    public MechanicRepository() {
        if (conn == null) {
            ConnectionManager.createConnection(System.getenv("JDBCUrl"), System.getenv("JDBCUsername"),
                    System.getenv("JDBCPassword"));
        }

    }

    public void createDamageReport(int price, String placement, String description, int carId, int rentalAgreementId)
            throws CarLeasingException{
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

        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }

    public List<Damage> fetchAllDamagesForRentalId(int rentalId) throws CarLeasingException {

        List<Damage> damageList = new LinkedList<>();

        try {
            String queryCreate = ("SELECT * from damages WHERE rentalAgreement_id = ?");
            PreparedStatement psts = conn.prepareStatement(queryCreate);
            psts.setInt(1, rentalId);
            ResultSet resultSet = psts.executeQuery();


            while (resultSet.next()) {
                int damageId = resultSet.getInt(1);
                int price = resultSet.getInt(2);
                String placement = resultSet.getString(3);
                String description = resultSet.getString(4);
                int carId = resultSet.getInt(5);


                damageList.add(new Damage(damageId, price, placement, description, carId, rentalId));
            }
        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
        return damageList;
    }


    public void deleteDamageId(int damageId) throws CarLeasingException {

        try {
            String deleteQuery = ("DELETE FROM damages WHERE damage_id =?");
            PreparedStatement psts = conn.prepareStatement(deleteQuery);

            psts.setInt(1, damageId);
            psts.executeUpdate();


        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }

    //Jonathan
    public double fetchTotalSumDamages(int rentalId) {
        double sum = 0;
        try{
            String query = "SELECT * FROM damages WHERE rentalAgreement_id=?";
            PreparedStatement psts = conn.prepareStatement(query);
            psts.setInt(1, rentalId);
            ResultSet resultSet = psts.executeQuery();

            while (resultSet.next()){
                sum += resultSet.getInt(2);
            }

        } catch (CarLeasingException | SQLException e) {
            System.out.println(e.getMessage());
        }
        return sum;
    }

    //Jonathan
    public int fetchTotalNumDamages(int rentalId) {
        int num = 0;
        try{
            String query = "SELECT * FROM damages WHERE rentalAgreement_id=?";
            PreparedStatement psts = conn.prepareStatement(query);
            psts.setInt(1, rentalId);
            ResultSet resultSet = psts.executeQuery();

            while (resultSet.next()){
                num ++;
            }

        } catch (CarLeasingException | SQLException e) {
            System.out.println(e.getMessage());
        }
        return num;
    }
}



