package com.example.bilabonnement.controllers;

import com.example.bilabonnement.exceptions.CarLeasingException;
import com.example.bilabonnement.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class MechanicController {

    private final String role = "mechanic";

    public String getRole() {
        return role;
    }

    LoginController loginController;
    UserService userService;

    public MechanicController(LoginController loginController, UserService userService) {
        this.loginController = loginController;
        this.userService = userService;
    }

    @GetMapping("/mechanic")
    public String frontdeskPage(Model model, HttpSession httpSession) throws CarLeasingException {
        model.addAttribute("roles", loginController.validateRoles(httpSession));
        if (!loginController.validateLogin(httpSession,role)) {
            return "redirect:/";
        }
        model.addAttribute("userList", userService.getAll());
        return "mechanic";
    }
}
