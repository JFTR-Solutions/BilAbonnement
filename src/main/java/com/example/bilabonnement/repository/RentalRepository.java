package com.example.bilabonnement.repository;

import com.example.bilabonnement.models.rentalagreements.MthKm;
import com.example.bilabonnement.models.rentalagreements.RentalAgreement;
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
            String queryCreate = "SELECT * from rental_agreements";
            PreparedStatement psts = conn.prepareStatement(queryCreate);
            ResultSet resultSet = psts.executeQuery();

            while (resultSet.next()) {
                int rentalId = resultSet.getInt(1);
                int userId = resultSet.getInt(2);
                int mthKmId = resultSet.getInt(3);
                Date endDate = resultSet.getDate(4);
                Date startDate = resultSet.getDate(5);
                int carId = resultSet.getInt(6);
                double mthPrice = resultSet.getDouble(7);
                rentalAgreementList.add(new RentalAgreement(rentalId, endDate, startDate, mthPrice, carId, mthKmId, userId));
            }
            return rentalAgreementList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
