package com.example.bilabonnement.controllers;

import com.example.bilabonnement.Exceptions.UserNotFoundException;
import com.example.bilabonnement.service.CarService;
import com.example.bilabonnement.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
        boolean isLoggedIn = true;
        if (!loginController.validateUser(httpSession).equals("validated")) {
            return !isLoggedIn;
        }
        if (!loginController.validateRoles(httpSession).contains("front_desk")) {
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
}
