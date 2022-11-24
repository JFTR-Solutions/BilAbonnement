package com.example.bilabonnement.service;

import com.example.bilabonnement.models.cars.Car;
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
}
