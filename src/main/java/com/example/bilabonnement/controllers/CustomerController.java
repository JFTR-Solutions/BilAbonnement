package com.example.bilabonnement.controllers;

import com.example.bilabonnement.exceptions.CarLeasingException;
import com.example.bilabonnement.models.users.User;
import com.example.bilabonnement.service.CarService;
import com.example.bilabonnement.service.RentalService;
import com.example.bilabonnement.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class CustomerController {

    private LoginController loginController;
    private UserService userService;
    private CarService carService;
    private SysadminController sysadminController;
    private RentalService rentalService;

    private final String ROLE = "Dataregistrering";


    public CustomerController(LoginController loginController, UserService userService, CarService carService,
                              RentalService rentalService, SysadminController sysadminController) {
        this.loginController = loginController;
        this.userService = userService;
        this.carService = carService;
        this.rentalService = rentalService;
        this.sysadminController = sysadminController;
    }

    @GetMapping("/customers")
    public String frontdeskPage(Model model, HttpSession httpSession) throws CarLeasingException {
        try {
            model.addAttribute("roles", loginController.validateRoles(httpSession));
            if (!loginController.validateLogin(httpSession, ROLE)) {
                return "redirect:/";
            }
        } catch (CarLeasingException e) {
            httpSession.setAttribute("error", e.getMessage());
            return "redirect:/";
        }
        model.addAttribute("userList", userService.getAllCustomers());
        return "customer";
    }

    @GetMapping("/update-customer/{id}")
    public String updateUser(@PathVariable("id") int id, Model model, HttpSession httpSession)
        throws CarLeasingException {
        try {
            model.addAttribute("roles", loginController.validateRoles(httpSession));
            if (!loginController.validateLogin(httpSession, ROLE)) {
                return "redirect:/";
            }
        } catch (CarLeasingException e) {
            httpSession.setAttribute("error", e.getMessage());
            return "redirect:/";
        }
        model.addAttribute("id", id);
        model.addAttribute("customer", userService.findUserByID(id));
        return "updatecustomer";
    }

    @PostMapping("/update-customer")
    public String saveUser(@ModelAttribute User user, Model model, HttpSession httpSession)
        throws CarLeasingException {
        try {
            model.addAttribute("roles", loginController.validateRoles(httpSession));
            if (!loginController.validateLogin(httpSession, ROLE)) {
                return "redirect:/";
            }
        } catch (CarLeasingException e) {
            httpSession.setAttribute("error", e.getMessage());
            return "redirect:/";
        }
        userService.updateUser(user);
        return "redirect:/customers";
    }

}
