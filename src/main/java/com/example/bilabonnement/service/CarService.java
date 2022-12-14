package com.example.bilabonnement.service;

import com.example.bilabonnement.exceptions.CarLeasingException;
import com.example.bilabonnement.models.cars.Car;
import com.example.bilabonnement.models.cars.Model;
import com.example.bilabonnement.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

  CarRepository carRepository;

  public CarService(CarRepository carRepository) throws CarLeasingException {
    this.carRepository = carRepository;
  }

  public List<Car> fetchAllCars() throws CarLeasingException {
    return carRepository.findAll();
  }


  public List<Car> fetchAllAvailableCars() throws CarLeasingException {
    return carRepository.fetchAllAvailableCars();
  }

  public void updateCar(Car car) throws CarLeasingException {
    carRepository.updateCar(car);
  }

  public Car findCarById(int id) throws CarLeasingException {
    return carRepository.findCarById(id);
  }

  public void deleteCar(int id) throws CarLeasingException {
    carRepository.deleteCar(id);
  }

  public List<Model> getAllModels() throws CarLeasingException {
    return carRepository.getAllModels();
  }

  public void addCarModel(String modelName, String manufacturer, String co2, String fuelType, double range)
          throws CarLeasingException{
    carRepository.addModel(modelName, manufacturer, co2, fuelType, range);

  }

  public void addCar(int modelId, byte available, String colour, String vin, String regNumber, double steelprice,
                     double mthPrice, String transmission) throws CarLeasingException{
    carRepository.addCar(modelId, available, colour, vin, regNumber, steelprice, mthPrice, transmission);
  }

  public void updateCarAvailability(int carId, byte b) throws CarLeasingException {
    carRepository.updateCarAvailability(carId, b);
  }

  public void markCarAvailable(int carId) throws CarLeasingException {
    carRepository.markCarAvailable(carId);
  }

  public Model findModelById(int id) throws CarLeasingException {
    return carRepository.findModelById(id);
  }


  public void updateModel(Model model) throws CarLeasingException {
    carRepository.updateModel(model);
  }
}



