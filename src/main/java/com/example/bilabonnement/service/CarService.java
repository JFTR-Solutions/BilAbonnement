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


  public List<Car> fetchAllAvailableCars() {
    return carRepository.fetchAllAvailableCars();
  }

  public void updateCar(Car car) {
    carRepository.updateCar(car);
  }

  public Car findCarById(int id) {
    return carRepository.findCarById(id);
  }

  public void deleteCar(int id) {
    carRepository.deleteCar(id);
  }

  public List<Model> getAllModels() {
    return carRepository.getAllModels();
  }

  public void addCarModel(String modelName, String manufacturer, String co2, String fuelType, double range) {
    carRepository.addModel(modelName, manufacturer, co2, fuelType, range);

  }

  public void addCar(int modelId, byte available, String colour, String vin, String regNumber, double steelprice, double mthPrice, String transmission) {
    carRepository.addCar(modelId, available, colour, vin, regNumber, steelprice, mthPrice, transmission);
  }

  public void updateCarAvailability(int carId, byte b) {
    carRepository.updateCarAvailability(carId, b);
  }

  public void markCarAvailable(int carId) {
    carRepository.markCarAvailable(carId);
  }

  public Model findModelById(int id) {
    return carRepository.findModelById(id);
  }


  public void updateModel(Model model) {
    carRepository.updateModel(model);
  }
}



