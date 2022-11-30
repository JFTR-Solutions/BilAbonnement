package com.example.bilabonnement.service;

import com.example.bilabonnement.models.cars.Car;
import com.example.bilabonnement.models.cars.Model;
import com.example.bilabonnement.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

  CarRepository carRepository;

  public CarService(CarRepository carRepository) {
    this.carRepository = carRepository;
  }

  public List<Car> fetchAllCars() {
    return carRepository.findAll();
  }

  public void updateCar(Car car) {
    carRepository.updateCar(car);
  }

  public Car findCarById(int id) { return carRepository.findCarById(id); }

  public void deleteCar(int id) {
    carRepository.deleteCar(id);
  }

  public List<Model> getAllModels() {
    return carRepository.getAllModels();
  }

  public void addCarModel(String modelName, String manufacturer, String co2, String fuelType, double range){
    carRepository.addModel(modelName, manufacturer, co2, fuelType, range);
  }
}
