package com.example.bilabonnement.controllers;

import com.example.bilabonnement.exceptions.CarLeasingException;
import com.example.bilabonnement.models.cars.Car;
import com.example.bilabonnement.service.CarService;
import com.example.bilabonnement.service.RentalService;
import com.example.bilabonnement.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class CarController {

    LoginController loginController;
    UserService userService;
    CarService carService;

    RentalService rentalService;

    private final String role = "Dataregistrering";

    public CarController(LoginController loginController, UserService userService, CarService carService, RentalService rentalService) {
        this.loginController = loginController;
        this.userService = userService;
        this.carService = carService;
        this.rentalService = rentalService;
    }

    @GetMapping("/cars")
    public String showCars(Model model, HttpSession httpSession){
            try {
                model.addAttribute("roles", loginController.validateRoles(httpSession));
                if (!loginController.validateLogin(httpSession, role)) {
                    return "redirect:/";
                }
            } catch (CarLeasingException e) {
                httpSession.setAttribute("error", e.getMessage());
                return "redirect:/welcome";
            }
            model.addAttribute("carlist", carService.fetchAllCars());
            return "car-list";
        }

    //Jonathan
    @GetMapping("/update-car/{id}")
    public String updateCar(@PathVariable("id") int id, Model model, HttpSession httpSession) throws CarLeasingException {
        model.addAttribute("roles", loginController.validateRoles(httpSession));
        if (!loginController.validateLogin(httpSession, role)) {
            return "redirect:/";
        }
        model.addAttribute("id", id);
        model.addAttribute("car", carService.findCarById(id));
        model.addAttribute("modellist", carService.getAllModels());
        return "updatecar";
    }

    //Jonathan
    @PostMapping("/update-car")
    public String saveCar(@ModelAttribute Car car) {
        carService.updateCar(car);
        return "redirect:/reception";
    }

    //Jonathan
    @GetMapping("/delete-car/{id}")
    public String deleteCar(@PathVariable("id") int id, Model model, HttpSession httpSession) throws CarLeasingException {
        model.addAttribute("roles", loginController.validateRoles(httpSession));
        if (!loginController.validateLogin(httpSession, role)) {
            return "redirect:/";
        }
        carService.deleteCar(id);
        return "redirect:/reception";
    }

    //Thomas
    @PostMapping("/create-carmodel")
    public String createCarModel(@RequestParam("modelName") String modelName,
                                 @RequestParam("manufacturer") String manufacturer, @RequestParam("co2") String co2,
                                 @RequestParam("fuelType") String fuelType, @RequestParam("range") double range) {
        carService.addCarModel(modelName, manufacturer, co2, fuelType, range);
        return "redirect:/create-car";
    }

    //Thomas
    @GetMapping("/create-carmodel")
    public String showCreateCarModel(HttpSession httpSession) throws CarLeasingException {
        if (!loginController.validateLogin(httpSession, role)) {
            return "redirect:/";
        }
        return "createmodel";
    }

    //Rami
    @PostMapping("/create-car")
    public String createCar(@RequestParam("modelId") int modelId, @RequestParam("available") byte available,
                            @RequestParam("colour") String colour, @RequestParam("vin") String vin,
                            @RequestParam("regNumber") String regNumber, @RequestParam("steelPrice") double steelprice,
                            @RequestParam("mthPrice") double mthPrice, @RequestParam("transmission") String transmission, HttpSession httpSession) {

        carService.addCar(modelId, available, colour, vin, regNumber, steelprice, mthPrice, transmission);

        return "redirect:/reception";
    }

    //Rami
    @GetMapping("/create-car")
    public String showCreateCar(HttpSession httpSession, Model model) throws CarLeasingException {
        if (!loginController.validateLogin(httpSession, role)) {
            return "redirect:/";
        }
        model.addAttribute("modellist", carService.getAllModels());
        return "createcar";
    }

    @GetMapping("/show-all-models")
    public String showModel(Model model, HttpSession httpSession){
        try {
            model.addAttribute("roles", loginController.validateRoles(httpSession));
            if (!loginController.validateLogin(httpSession, role)) {
                return "redirect:/";
            }
        } catch (CarLeasingException e) {
            httpSession.setAttribute("error", e.getMessage());
            return "redirect:/welcome";
        }
        model.addAttribute("modellist", carService.getAllModels());
        return "showallmodels";
    }


 @GetMapping("/update-model/{id}")
    public String updateModel(@PathVariable("id") int id, Model model, HttpSession httpSession) throws CarLeasingException {
        model.addAttribute("roles", loginController.validateRoles(httpSession));
        if (!loginController.validateLogin(httpSession, role)) {
            return "redirect:/";
        }
        model.addAttribute("id", id);
        model.addAttribute("model", carService.findModelById(id));
        return "updatemodel";
    }

    @PostMapping("/update-model")
    public String saveModel(@ModelAttribute com.example.bilabonnement.models.cars.Model model) {
        carService.updateModel(model);
        return "redirect:/show-all-models";
    }
}
