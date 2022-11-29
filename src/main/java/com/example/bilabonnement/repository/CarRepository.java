package com.example.bilabonnement.repository;

import com.example.bilabonnement.models.cars.Car;
import com.example.bilabonnement.models.cars.Model;
import com.example.bilabonnement.service.util.ConnectionManager;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

            PreparedStatement psts = conn.prepareStatement("SELECT * from cars INNER JOIN model");
            ResultSet resultSet = psts.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                boolean isAvailable = resultSet.getBoolean(2);
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
                carList.add(new Car(id, isAvailable, colour, vin, regNumber, steelPrice, mthPrice, transmission, modelId, new Model(modelId, modelName, manufacturer, co2Emission, fuelType)));
            }
        } catch (SQLException e) {
            System.out.println("Can't connect to database");
            e.printStackTrace();
        }
        return carList;
    }
}
