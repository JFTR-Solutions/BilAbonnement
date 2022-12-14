package com.example.bilabonnement.repository;

import com.example.bilabonnement.exceptions.CarLeasingException;
import com.example.bilabonnement.exceptions.carExceptionEnum;
import com.example.bilabonnement.models.cars.Car;
import com.example.bilabonnement.models.cars.Model;
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
public class CarRepository {

    public CarRepository() {
        if (conn == null) {
            ConnectionManager.createConnection(System.getenv("JDBCUrl"), System.getenv("JDBCUsername"),
                    System.getenv("JDBCPassword"));
        }
    }


    //Thomas
    public List<Car> fetchAllAvailableCars() throws CarLeasingException {
        List<Car> carList = new LinkedList<>();
        try {
            String queryCreate = "SELECT * from cars INNER JOIN models ON models.model_id=cars.model_id " +
                    "WHERE available = 1 ORDER BY manufacturer, model_name";
            PreparedStatement psts = conn.prepareStatement(queryCreate);
            ResultSet resultSet = psts.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                byte isAvailable = resultSet.getByte(2);
                String colour = resultSet.getString(3);
                String vin = resultSet.getString(4);
                String regNumber = resultSet.getString(5);
                double steelPrice = resultSet.getDouble(6);
                double mthPrice = resultSet.getDouble(7);
                String transmission = resultSet.getString(8);
                int modelId = resultSet.getInt(9);
                String modelName = resultSet.getString(11);
                String manufacturer = resultSet.getString(12);
                double co2Emission = resultSet.getDouble(13);
                String fuelType = resultSet.getString(14);
                double range = resultSet.getDouble(15);
                carList.add(new Car(id, isAvailable, colour, vin, regNumber, steelPrice, mthPrice, transmission, modelId, new Model(modelId, modelName, manufacturer, co2Emission, fuelType, range)));
            }
            return carList;
        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }

    //Jonathan - Frederik
    public List<Car> findAll() {

        List<Car> carList = new LinkedList<>();

        try {
            String queryCreate = "SELECT * from cars INNER JOIN models ON models.model_id=cars.model_id";
            PreparedStatement psts = conn.prepareStatement(queryCreate);
            ResultSet resultSet = psts.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                byte isAvailable = resultSet.getByte(2);
                String colour = resultSet.getString(3);
                String vin = resultSet.getString(4);
                String regNumber = resultSet.getString(5);
                double steelPrice = resultSet.getDouble(6);
                double mthPrice = resultSet.getDouble(7);
                String transmission = resultSet.getString(8);
                int modelId = resultSet.getInt(9);
                String modelName = resultSet.getString(11);
                String manufacturer = resultSet.getString(12);
                double co2Emission = resultSet.getDouble(13);
                String fuelType = resultSet.getString(14);
                double range = resultSet.getDouble(15);
                carList.add(new Car(id, isAvailable, colour, vin, regNumber, steelPrice, mthPrice, transmission,
                        modelId, new Model(modelId, modelName, manufacturer, co2Emission, fuelType, range)));
            }
        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
        return carList;
    }

    //Jonathan - Frederik
    public void updateCar(Car car) {
        try {
            String queryCreate = ("UPDATE cars SET available=?, colour=?, vin=?,reg_number=?,steel_price=?,mth_price=?," +
                    "transmission=?,model_id=? WHERE car_id=?");
            PreparedStatement psts = conn.prepareStatement(queryCreate);

            psts.setByte(1, car.isAvailable());
            psts.setString(2, car.getColour());
            psts.setString(3, car.getVIN());
            psts.setString(4, car.getRegNumber());
            psts.setDouble(5, car.getSteelPrice());
            psts.setDouble(6, car.getMthPrice());
            psts.setString(7, car.getTransmission());
            psts.setInt(8, car.getModelId());
            psts.setInt(9, car.getCarId());

            psts.executeUpdate();

        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }

    //Jonathan - Frederik
    public Car findCarById(int carId) {
        Car car = new Car();
        try {
            String queryCreate = "SELECT * FROM cars WHERE car_id=?";
            PreparedStatement psts = conn.prepareStatement(queryCreate);

            psts.setInt(1, carId);

            ResultSet resultSet = psts.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                byte available = resultSet.getByte(2);
                String colour = resultSet.getString(3);
                String vin = resultSet.getString(4);
                String regNumber = resultSet.getString(5);
                double steelPrice = resultSet.getDouble(6);
                double mthPrice = resultSet.getDouble(7);
                String transmission = resultSet.getString(8);
                int modelId = resultSet.getInt(9);

                car.setCarId(id);
                car.setAvailable(available);
                car.setColour(colour);
                car.setVIN(vin);
                car.setRegNumber(regNumber);
                car.setSteelPrice(steelPrice);
                car.setMthPrice(mthPrice);
                car.setTransmission(transmission);
                car.setModelId(modelId);
            }
            return car;
        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }

    //Jonathan
    public void deleteCar(int id) {
        try {
            String deleteCar = ("DELETE FROM cars where car_id=?");
            PreparedStatement psts = conn.prepareStatement(deleteCar);

            psts.setInt(1, id);
            psts.executeUpdate();


        } catch (SQLException exception) {
            System.out.println(exception);
        }
    }

    //Jonathan - Frederik
    public List<Model> getAllModels() {

        List<Model> modelsList = new LinkedList<>();

        try {
            String queryCreate = "SELECT * from models ORDER BY manufacturer";
            PreparedStatement psts = conn.prepareStatement(queryCreate);
            ResultSet resultSet = psts.executeQuery();

            while (resultSet.next()) {
                int modelId = resultSet.getInt(1);
                String modelName = resultSet.getString(2);
                String manufacturer = resultSet.getString(3);
                double co2Emission = resultSet.getDouble(4);
                String fuelType = resultSet.getString(5);
                double range = resultSet.getDouble(6);
                modelsList.add(new Model(modelId, modelName, manufacturer, co2Emission, fuelType, range));
            }
        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
        return modelsList;
    }


    //Thomas - Frederik
    public void addModel(String modelName, String manufacturer, String co2, String fuelType, double range)
            throws CarLeasingException{

        try {
            String addModel = "INSERT INTO models (model_id, model_name, manufacturer, co2_emission, fuel_type, car_range)" +
                    " VALUES (DEFAULT,?,?,?,?,?)";

            PreparedStatement psts = conn.prepareStatement(addModel);

            psts.setString(1, modelName);
            psts.setString(2, manufacturer);
            psts.setString(3, co2);
            psts.setString(4, fuelType);
            psts.setDouble(5, range);

            psts.executeUpdate();

        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }

    //Rami
    public void addCar(int modelId, byte available, String colour, String vin, String regNumber, double steelPrice,
                       double mthPrice, String transmission) throws CarLeasingException{
        try {
            String queryCreate = "INSERT INTO cars (car_id, available, colour, vin,reg_number,steel_price,mth_price," +
                    "transmission,model_id)" +
                    "VALUES (DEFAULT,?,?,?,?,?,?,?,?)";
            PreparedStatement psts = conn.prepareStatement(queryCreate);

            psts.setByte(1, available);
            psts.setString(2, colour);
            psts.setString(3, vin);
            psts.setString(4, regNumber);
            psts.setDouble(5, steelPrice);
            psts.setDouble(6, mthPrice);
            psts.setString(7, transmission);
            psts.setInt(8, modelId);


            psts.executeUpdate();

        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }


    }

    //Thomas
    public void markCarAvailable(int carId) throws CarLeasingException {
        try {
            String queryCreate = "UPDATE cars SET available=1 WHERE car_id=?";
            PreparedStatement psts = conn.prepareStatement(queryCreate);

            psts.setInt(1, carId);

            psts.executeUpdate();

        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }

    //Thomas
    public void updateCarAvailability(int carId, byte b) throws CarLeasingException{
        try {
            String queryCreate = ("UPDATE cars SET available=? WHERE car_id=?");
            PreparedStatement psts = conn.prepareStatement(queryCreate);

            psts.setByte(1, b);
            psts.setInt(2, carId);

            psts.executeUpdate();

        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }

    //Rami
    public Model findModelById(int id) throws CarLeasingException{
        Model model = new Model();

        try {
            String queryFindModelId = ("SELECT * FROM models WHERE model_id=?");
            PreparedStatement psts = conn.prepareStatement(queryFindModelId);

            psts.setInt(1, id);

            ResultSet resultSet = psts.executeQuery();

            while (resultSet.next()) {
                int modelId = resultSet.getInt(1);
                String modelName = resultSet.getString(2);
                String manufacturer = resultSet.getString(3);
                double co2Emission = resultSet.getDouble(4);
                String fuelType = resultSet.getString(5);
                double range = resultSet.getDouble(6);

                model.setModelId(modelId);
                model.setModelName(modelName);
                model.setManufacturer(manufacturer);
                model.setCo2Emission(co2Emission);
                model.setFuelType(fuelType);
                model.setRange(range);

            }
            System.out.println(model);
            return model;


        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));

        }
    }

    //Rami
    public void updateModel(Model model) throws CarLeasingException{
        try {
            String queryUpdate = ("UPDATE models SET model_name=?, manufacturer=?, co2_emission=?, fuel_type=?, " +
                    "car_range=? WHERE model_id=?");
            PreparedStatement psts = conn.prepareStatement(queryUpdate);

            psts.setString(1, model.getModelName());
            psts.setString(2, model.getManufacturer());
            psts.setDouble(3, model.getCo2Emission());
            psts.setString(4, model.getFuelType());
            psts.setDouble(5, model.getRange());
            psts.setInt(6, model.getModelId());

            psts.executeUpdate();

        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }
}
