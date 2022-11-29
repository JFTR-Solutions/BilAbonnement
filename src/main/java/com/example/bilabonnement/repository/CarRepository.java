package com.example.bilabonnement.repository;

import com.example.bilabonnement.models.cars.Car;
import com.example.bilabonnement.models.cars.Model;
import com.example.bilabonnement.service.util.ConnectionManager;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.LinkedList;
import java.util.List;

import static com.example.bilabonnement.service.util.ConnectionManager.conn;

@Repository
public class CarRepository {

  public CarRepository() {
    if (conn == null) {
      ConnectionManager.createConnection(System.getenv("JDBCUrl"), System.getenv("JDBCUsername"), System.getenv("JDBCPassword"));
    }
  }

  public List<Car> findAll() {

    List<Car> carList = new LinkedList<>();

    try {
      String queryCreate = "SELECT * from cars INNER JOIN model ON model.model_id=cars.model_id";
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
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return carList;
  }

  public void updateCar(Car car) {
    try {
      String queryCreate = ("UPDATE cars SET available=?, colour=?, vin=?,reg_number=?,steel_price=?,mth_price=?,transmission=?,model_id=? WHERE car_id=?");
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

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

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
        System.out.println(available);
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
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void deleteCar(int id) {
    try {
      String deleteCar = ("DELETE FROM cars where car_id=?");
      PreparedStatement psts = conn.prepareStatement(deleteCar);

      psts.setInt(1,id);
      psts.executeUpdate();


    } catch (SQLException exception){
      System.out.println(exception);
    }
  }

  public List<Model> getAllModels() {

    List<Model> modelsList = new LinkedList<>();

    try {
      String queryCreate = "SELECT * from model ORDER BY manufacturer";
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
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return modelsList;
  }
}
