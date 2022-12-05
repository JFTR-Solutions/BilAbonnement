package com.example.bilabonnement.controllers;

import com.example.bilabonnement.exceptions.CarLeasingException;
import com.example.bilabonnement.repository.MechanicRepository;
import com.example.bilabonnement.service.MechanicService;
import com.example.bilabonnement.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.sql.Date;

@Controller
public class MechanicController {

    private final String role = "mechanic";

    LoginController loginController;
    UserService userService;
    MechanicService mechanicService;

    public MechanicController(LoginController loginController, UserService userService, MechanicService mechanicService) {
        this.loginController = loginController;
        this.userService = userService;
        this.mechanicService = mechanicService;
    }

    @GetMapping("/mechanic")
    public String frontdeskPage(Model model, HttpSession httpSession) throws CarLeasingException {
        try {
            model.addAttribute("roles", loginController.validateRoles(httpSession));
            if (!loginController.validateLogin(httpSession, role)) {
                return "redirect:/";
            }
        } catch (CarLeasingException e) {
            httpSession.setAttribute("error", e.getMessage());
            return "redirect:/welcome";
        }
        model.addAttribute("userList", userService.getAll());
        return "mechanic";
    }

    @GetMapping("/create-damagereport")
    public String showDamageReport(HttpSession httpSession, Model model) throws CarLeasingException {
        if (!loginController.validateLogin(httpSession, role)) {
            return "redirect:/";
        }
        return "createdamagereport";
    }

    @PostMapping("/create-damagereport")
    public String createDamageReport(@RequestParam("price") int price, @RequestParam("placement") String placement,
                                     @RequestParam("description") String description, @RequestParam("carid") int carId,
                                     @RequestParam("rentalagreement") int rentalAgreementId) throws CarLeasingException {
        mechanicService.createDamageReport(price, placement,description,carId, rentalAgreementId);

        return ("redirect:/reception");
    }


}
