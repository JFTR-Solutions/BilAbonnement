package com.example.bilabonnement.repository;

import com.example.bilabonnement.exceptions.CarLeasingException;
import com.example.bilabonnement.exceptions.carExceptionEnum;
import com.example.bilabonnement.models.cars.Car;
import com.example.bilabonnement.models.cars.Model;
import com.example.bilabonnement.models.rentalagreements.MthKm;
import com.example.bilabonnement.models.rentalagreements.RentalAgreement;
import com.example.bilabonnement.models.users.User;
import com.example.bilabonnement.service.util.ConnectionManager;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static com.example.bilabonnement.controllers.LoginController.exceptionEnums;
import static com.example.bilabonnement.service.util.ConnectionManager.conn;

@Repository
public class RentalRepository {

    public RentalRepository() {
        if (conn == null) {
            ConnectionManager.createConnection(System.getenv("JDBCUrl"), System.getenv("JDBCUsername"), System.getenv("JDBCPassword"));
        }
    }

    public void addRentalAgreement(int carId, int userId, int mthKmId, Date endDate, Date startDate, double mthPrice) {
        try{
            String queryCreate = "INSERT INTO rental_agreements (rental_id, user_id, mth_km_id, end_date, start_date, car_id, mth_price)"+
                    "VALUES (DEFAULT,?,?,?,?,?,?)";
            PreparedStatement psts = conn.prepareStatement(queryCreate);

            psts.setInt(1, userId);
            psts.setInt(2, mthKmId);
            psts.setDate(3, endDate);
            psts.setDate(4, startDate);
            psts.setInt(5, carId);
            psts.setDouble(6, mthPrice);



            psts.executeUpdate();

        } catch (NullPointerException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }

    public MthKm findMthKmById(int id){
        MthKm mthKmObject = null;
        try {
            String queryCreate = "SELECT * from mth_km WHERE km_id=?";
            PreparedStatement psts = conn.prepareStatement(queryCreate);
            psts.setInt(1, id);
            ResultSet resultSet = psts.executeQuery();

            while (resultSet.next()) {
                int mthKmId = resultSet.getInt(1);
                int mthKm = resultSet.getInt(2);
                int price = resultSet.getInt(3);
                mthKmObject = new MthKm(mthKmId, mthKm, price);
            }
            return mthKmObject;
        } catch (NullPointerException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }

    public List<MthKm> fetchAllMthKm() {
        List<MthKm> mthKmList = new LinkedList<>();
        try {
            String queryCreate = "SELECT * from mth_km";
            PreparedStatement psts = conn.prepareStatement(queryCreate);
            ResultSet resultSet = psts.executeQuery();

            while (resultSet.next()) {
                int mthKmId = resultSet.getInt(1);
                int mthKm = resultSet.getInt(2);
                int price = resultSet.getInt(3);
                mthKmList.add(new MthKm(mthKmId, mthKm, price));
            }
            return mthKmList;
        } catch (NullPointerException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }

    public List<RentalAgreement> fetchAllRentalAgreements() {
        List<RentalAgreement> rentalAgreementList = new LinkedList<>();
        try {
            String queryCreate = "SELECT * FROM rental_agreements INNER JOIN cars ON  cars.car_id = rental_agreements.car_id " +
                    "INNER JOIN models ON cars.model_id = models.model_id INNER JOIN users ON rental_agreements.user_id " +
                    "= users.user_id INNER JOIN mth_km mk on rental_agreements.mth_km_id = mk.km_id " +
                    "ORDER BY rental_agreements.start_date;";
            PreparedStatement psts = conn.prepareStatement(queryCreate);
            ResultSet resultSet = psts.executeQuery();

            while (resultSet.next()) {
                int rentalId = resultSet.getInt(1);
                Date endDate = resultSet.getDate(2);
                Date startDate = resultSet.getDate(3);
                double rentalMthPrice = resultSet.getDouble(4);
                int carId = resultSet.getInt(5);
                int mthKmId = resultSet.getInt(6);
                int userId = resultSet.getInt(7);
                String colour = resultSet.getString(10);
                String vin = resultSet.getString(11);
                String regNumber = resultSet.getString(12);
                double steelPrice = resultSet.getDouble(13);
                double carMthPrice = resultSet.getDouble(14);
                String transmission = resultSet.getString(15);
                int modelId = resultSet.getInt(16);
                String modelName = resultSet.getString(18);
                String manufacturer = resultSet.getString(19);
                double c02Emission = resultSet.getDouble(20);
                String fuelType = resultSet.getString(21);
                double carRange = resultSet.getDouble(22);
                String email = resultSet.getString(24);
                String username = resultSet.getString(25);
                String firstName = resultSet.getString(27);
                String lastName = resultSet.getString(28);
                Date birthDate = resultSet.getDate(29);
                String address = resultSet.getString(30);
                String phoneNumber = resultSet.getString(31);
                int mthKm = resultSet.getInt(33);
                int mthKmPrice = resultSet.getInt(34);

                Model carModel = new Model(modelId, modelName, manufacturer, c02Emission, fuelType, carRange);
                rentalAgreementList.add(new RentalAgreement(rentalId, endDate, startDate, rentalMthPrice, carId, mthKmId, userId,
                        modelId, new MthKm(mthKmId, mthKm, mthKmPrice), new Car(carId, colour, vin, regNumber,
                        steelPrice, carMthPrice, transmission, modelId, carModel), carModel, new User(userId, email,
                        username, firstName, lastName, birthDate, address, phoneNumber)));

            }
            return rentalAgreementList;
        } catch (NullPointerException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }
}
