package com.example.bilabonnement.controllers;

import com.example.bilabonnement.Exceptions.UserNotFoundException;
import com.example.bilabonnement.models.cars.Car;
import com.example.bilabonnement.service.CarService;
import com.example.bilabonnement.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class FrontdeskController {

    LoginController loginController;
    UserService userService;
    CarService carService;

    public FrontdeskController(LoginController loginController, UserService userService, CarService carService) {
        this.loginController = loginController;
        this.userService = userService;
        this.carService = carService;
    }

    public Boolean validateLogin(HttpSession httpSession) {
        boolean isLoggedIn = false;
        if (loginController.validateUser(httpSession).equals("validated")){
            return !isLoggedIn;
        } else if(loginController.validateRoles(httpSession).contains("front_desk") ||
                loginController.validateRoles(httpSession).contains("sysadmin")) {
            return !isLoggedIn;
        } else {
            return isLoggedIn;
        }
    }

    //Jonathan
    @GetMapping("/reception")
    public String frontdeskPage(Model model, HttpSession httpSession) {
        model.addAttribute("roles", loginController.validateRoles(httpSession));
        if (!validateLogin(httpSession)) {
            return "redirect:/";
        }
        model.addAttribute("carlist", carService.fetchAllCars());
        return "frontdesk";
    }

    @GetMapping("/opdater-bil/{id}")
    public String updateCar(@PathVariable("id") int id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("car", carService.findCarById(id));
        model.addAttribute("modellist", carService.getAllModels());
        return "updatecar";
    }

    @PostMapping("/opdater-bil")
    public String saveCar(@ModelAttribute Car car) {
        carService.updateCar(car);
        return "redirect:/reception";
    }

    @GetMapping("/slet-bil/{id}")
    public String deleteCar(@PathVariable("id") int id) {
        carService.deleteCar(id);
        return "redirect:/reception";
    }
}
