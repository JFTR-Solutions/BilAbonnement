package com.example.bilabonnement.controllers;

import com.example.bilabonnement.repository.CarRepository;
import com.example.bilabonnement.service.CarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class CarController {

  CarService carService;

  public CarController(CarService carService) {
    this.carService = carService;
  }

  @GetMapping("/carlist")
  public String showCarList(Model model) {
    model.addAttribute("carlist", carService.fetchAllCars());
    return "carlist";
  }
}
