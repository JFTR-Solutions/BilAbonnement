package com.example.bilabonnement.repository;

import com.example.bilabonnement.models.cars.Car;
import com.example.bilabonnement.service.util.ConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Repository
public class CarRepository {

  @Value("${spring.datasource.url}")
  private String db_url;

  @Value("${spring.datasource.username}")
  private String uid;

  @Value("${spring.datasource.password}")
  private String pwd;
  public List<Car> findAll() {

    List<Car> carList = new LinkedList<>();

    try {

      Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);

      PreparedStatement psts = conn.prepareStatement("SELECT * from cars");
      ResultSet resultSet = psts.executeQuery();

      while (resultSet.next()) {
        int id = resultSet.getInt(1);
        boolean isAvailable = resultSet.getBoolean(2);
        String colour = resultSet.getString(3);
        int vin = resultSet.getInt(4);
        String regNumber = resultSet.getString(5);
        double steelPrice = resultSet.getDouble(6);
        double mthPrice = resultSet.getDouble(7);
        String transmission = resultSet.getString(8);
        int modelId = resultSet.getInt(9);
        carList.add(new Car(id, isAvailable, colour,vin,regNumber,steelPrice,mthPrice,transmission,modelId));
        System.out.println("Id: " + id + ". Available: " + isAvailable + ". Vin: " + vin + ". RegNumber: " + regNumber + ". SteelPrice: "
        + steelPrice + ". Monthly Price: " + mthPrice + ". Transmission: " + transmission + ". Model ID: " + modelId);
      }
    } catch (SQLException e) {
      System.out.println("Can't connect to database");
      e.printStackTrace();
    }
    return carList;
  }
}
