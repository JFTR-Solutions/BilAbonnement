package com.example.bilabonnement.repository;

import com.example.bilabonnement.exceptions.CarLeasingException;
import com.example.bilabonnement.exceptions.carExceptionEnum;
import com.example.bilabonnement.models.cars.Car;
import com.example.bilabonnement.models.cars.Model;
import com.example.bilabonnement.models.rentalagreements.Addon;
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
            ConnectionManager.createConnection(System.getenv("JDBCUrl"), System.getenv("JDBCUsername"),
                    System.getenv("JDBCPassword"));
        }
    }

    public void addRentalAgreement(int carId, int userId, int mthKmId, Date endDate, Date startDate, double mthPrice)
            throws CarLeasingException {
        try{
            String queryCreate = "INSERT INTO rental_agreements (rental_id, user_id, mth_km_id, end_date, start_date, " +
                    "car_id, mth_price)"+
                    "VALUES (DEFAULT,?,?,?,?,?,?)";
            PreparedStatement psts = conn.prepareStatement(queryCreate);

            psts.setInt(1, userId);
            psts.setInt(2, mthKmId);
            psts.setDate(3, endDate);
            psts.setDate(4, startDate);
            psts.setInt(5, carId);
            psts.setDouble(6, mthPrice);



            psts.executeUpdate();

        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }

    public void addCarAddon(int rentalId, int addonId) throws CarLeasingException {
        try{
            String queryCreate = "INSERT INTO car_addons (rental_agreement_id, addon_id)"+
                    "VALUES (?,?)";
            PreparedStatement psts = conn.prepareStatement(queryCreate);

            psts.setInt(1, rentalId);
            psts.setInt(2, addonId);

            psts.executeUpdate();

        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }


    public int findRentalAgreementIdByCarId(int carId) throws CarLeasingException{
        int rentalId = 0;
        try{
            String queryCreate = "SELECT rental_id FROM rental_agreements WHERE car_id = ?";
            PreparedStatement psts = conn.prepareStatement(queryCreate);

            psts.setInt(1, carId);

            ResultSet rs = psts.executeQuery();

            while (rs.next()) {
                rentalId = rs.getInt("rental_id");
            }

        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        } return rentalId;
    }

    public List<Addon> fetchAllAddons() throws CarLeasingException{
        List<Addon> addons = new LinkedList<>();
        try {
            String query = "SELECT * FROM addons";
            PreparedStatement psts = conn.prepareStatement(query);
            ResultSet rs = psts.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("addon_id");
                String name = rs.getString("addon_name");
                String shortDesc = rs.getString("addon_short_desc");
                String desc = rs.getString("addon_desc");
                double price = rs.getDouble("price");
                Addon addon = new Addon(id, name, shortDesc,desc, price);
                addons.add(addon);
            }
        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
        return addons;
    }

    public MthKm findMthKmById(int id) throws CarLeasingException{
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
        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }

    public List<MthKm> fetchAllMthKm() throws CarLeasingException{
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
        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }

    public RentalAgreement findRentalAgreementById(int id) throws CarLeasingException{
        RentalAgreement rentalAgreement = new RentalAgreement();
        try {
            String queryCreate = "SELECT * FROM rental_agreements INNER JOIN cars ON  cars.car_id = rental_agreements.car_id" +
                    " INNER JOIN models ON cars.model_id = models.model_id INNER JOIN users ON rental_agreements.user_id" +
                    "= users.user_id INNER JOIN mth_km mk on rental_agreements.mth_km_id = mk.km_id WHERE rental_id=?";
            PreparedStatement psts = conn.prepareStatement(queryCreate);
            psts.setInt(1, id);
            ResultSet resultSet = psts.executeQuery();

            while (resultSet.next()) {
                int rentalId = resultSet.getInt(1);
                Date endDate = resultSet.getDate(2);
                Date startDate = resultSet.getDate(3);
                double rentalMthPrice = resultSet.getDouble(4);
                int carId = resultSet.getInt(5);
                int mthKmId = resultSet.getInt(6);
                int userId = resultSet.getInt(7);
                byte active = resultSet.getByte(8);
                String colour = resultSet.getString(11);
                String vin = resultSet.getString(12);
                String regNumber = resultSet.getString(13);
                double steelPrice = resultSet.getDouble(14);
                double carMthPrice = resultSet.getDouble(15);
                String transmission = resultSet.getString(16);
                int modelId = resultSet.getInt(17);
                String modelName = resultSet.getString(19);
                String manufacturer = resultSet.getString(20);
                double c02Emission = resultSet.getDouble(21);
                String fuelType = resultSet.getString(22);
                double carRange = resultSet.getDouble(23);
                String email = resultSet.getString(25);
                String username = resultSet.getString(26);
                String firstName = resultSet.getString(28);
                String lastName = resultSet.getString(29);
                Date birthDate = resultSet.getDate(30);
                String address = resultSet.getString(31);
                String phoneNumber = resultSet.getString(32);
                int mthKm = resultSet.getInt(34);
                int mthKmPrice = resultSet.getInt(35);

                Model carModel = new Model(modelId, modelName, manufacturer, c02Emission, fuelType, carRange);
                rentalAgreement = new RentalAgreement(rentalId, endDate, startDate, rentalMthPrice, carId, mthKmId, userId,
                        modelId, new MthKm(mthKmId, mthKm, mthKmPrice), new Car(carId, colour, vin, regNumber,
                        steelPrice, carMthPrice, transmission, modelId, carModel), carModel, new User(userId, email,
                        username, firstName, lastName, birthDate, address, phoneNumber), active);
            }
            return rentalAgreement;
        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }

    public List<RentalAgreement> fetchAllRentalAgreements() throws CarLeasingException{
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
                byte active = resultSet.getByte(8);
                String colour = resultSet.getString(11);
                String vin = resultSet.getString(12);
                String regNumber = resultSet.getString(13);
                double steelPrice = resultSet.getDouble(14);
                double carMthPrice = resultSet.getDouble(15);
                String transmission = resultSet.getString(16);
                int modelId = resultSet.getInt(17);
                String modelName = resultSet.getString(19);
                String manufacturer = resultSet.getString(20);
                double c02Emission = resultSet.getDouble(21);
                String fuelType = resultSet.getString(22);
                double carRange = resultSet.getDouble(23);
                String email = resultSet.getString(25);
                String username = resultSet.getString(26);
                String firstName = resultSet.getString(28);
                String lastName = resultSet.getString(29);
                Date birthDate = resultSet.getDate(30);
                String address = resultSet.getString(31);
                String phoneNumber = resultSet.getString(32);
                int mthKm = resultSet.getInt(34);
                int mthKmPrice = resultSet.getInt(35);

                Model carModel = new Model(modelId, modelName, manufacturer, c02Emission, fuelType, carRange);
                rentalAgreementList.add(new RentalAgreement(rentalId, endDate, startDate, rentalMthPrice, carId, mthKmId, userId,
                        modelId, new MthKm(mthKmId, mthKm, mthKmPrice), new Car(carId, colour, vin, regNumber,
                        steelPrice, carMthPrice, transmission, modelId, carModel), carModel, new User(userId, email,
                        username, firstName, lastName, birthDate, address, phoneNumber), active));

            }
            return rentalAgreementList;
        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }

    public List<Addon> findCarAddonsByRentalId(int rentalId) throws CarLeasingException{
        List <Addon> addonList = new LinkedList<>();
        try {
            String queryCreate = "SELECT * FROM addons INNER JOIN car_addons ca on addons.addon_id = ca.addon_id " +
                    "INNER JOIN rental_agreements ra on ca.rental_agreement_id = ra.rental_id WHERE rental_id=?;";
            PreparedStatement psts = conn.prepareStatement(queryCreate);
            psts.setInt(1, rentalId);
            ResultSet resultSet = psts.executeQuery();

            while (resultSet.next()) {
                int addonId = resultSet.getInt(1);
                String  addonName = resultSet.getString(2);
                String addonShortDesc = resultSet.getString(4);
                String addonDesc = resultSet.getString(5);
                double price = resultSet.getDouble(5);
                addonList.add(new Addon(addonId, addonName, addonShortDesc, addonDesc, price));
            }
            return addonList;
        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }

    public List<Integer> findCarAddonsIdByRentalId(int rentalId) throws CarLeasingException{
        List <Integer> addonList = new LinkedList<>();
        try {
            String queryCreate = "SELECT * FROM addons INNER JOIN car_addons ca on addons.addon_id = ca.addon_id " +
                    "INNER JOIN rental_agreements ra on ca.rental_agreement_id = ra.rental_id WHERE rental_id=?;";
            PreparedStatement psts = conn.prepareStatement(queryCreate);
            psts.setInt(1, rentalId);
            ResultSet resultSet = psts.executeQuery();

            while (resultSet.next()) {
                int addonId = resultSet.getInt(1);
                addonList.add(addonId);
            }
            return addonList;
        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }

    public void endRental(int rentalAgreementId) throws CarLeasingException {
        try {
            String queryCreate = "UPDATE rental_agreements SET active=0 WHERE rental_id=?;";
            PreparedStatement psts = conn.prepareStatement(queryCreate);
            psts.setInt(1, rentalAgreementId);
            psts.executeUpdate();
        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }

    //Jonathan
    public void reopenRentalAgreement(int rentalAgreementId) {
        try {
            String queryCreate = "UPDATE rental_agreements SET active=1 WHERE rental_id=?;";
            PreparedStatement psts = conn.prepareStatement(queryCreate);
            psts.setInt(1, rentalAgreementId);
            psts.executeUpdate();
        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }
}
