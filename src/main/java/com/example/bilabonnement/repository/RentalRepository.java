package com.example.bilabonnement.repository;

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

        } catch (SQLException e) {
            throw new RuntimeException(e);
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<RentalAgreement> fetchAllRentalAgreements() {
        List<RentalAgreement> rentalAgreementList = new LinkedList<>();
        try {
            String queryCreate = "SELECT DISTINCT rental_agreements.rental_id, rental_agreements.start_date, " +
                    "                rental_agreements.end_date, rental_agreements.mth_price, models.model_name,\n" +
                    "                models.manufacturer, models.model_id, cars.car_id, cars.vin, cars.reg_number, " +
                    "                users.first_name, users.last_name, users.user_id FROM rental_agreements\n" +
                    "                INNER JOIN cars ON rental_agreements.car_id = cars.car_id\n" +
                    "                INNER JOIN models ON cars.model_id = models.model_id\n" +
                    "                INNER JOIN users ON rental_agreements.user_id = users.user_id\n" +
                    "                ORDER BY rental_agreements.start_date;";
            PreparedStatement psts = conn.prepareStatement(queryCreate);
            ResultSet resultSet = psts.executeQuery();

            while (resultSet.next()) {
                int rentalId = resultSet.getInt(1);
                Date startDate = resultSet.getDate(2);
                Date endDate = resultSet.getDate(3);
                double mthPrice = resultSet.getDouble(4);
                String modelName = resultSet.getString(5);
                String manufacturer = resultSet.getString(6);
                int modelId = resultSet.getInt(7);
                int carId = resultSet.getInt(8);
                String vin = resultSet.getString(9);
                String regNumber = resultSet.getString(10);
                String firstName = resultSet.getString(11);
                String lastName = resultSet.getString(12);
                int userId = resultSet.getInt(13);

                rentalAgreementList.add(new RentalAgreement(rentalId, startDate, endDate, mthPrice, carId,
                        new Car(carId, vin, regNumber), userId, new User(userId, firstName, lastName), modelId,
                        new Model(modelId, modelName, manufacturer)));
            }
            return rentalAgreementList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
