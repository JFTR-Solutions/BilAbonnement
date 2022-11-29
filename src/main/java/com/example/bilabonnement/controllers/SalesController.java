package com.example.bilabonnement.controllers;

import com.example.bilabonnement.Exceptions.UserNotFoundException;
import com.example.bilabonnement.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class SalesController {


    LoginController loginController;
    UserService userService;

    public SalesController(LoginController loginController, UserService userService) {
        this.loginController = loginController;
        this.userService = userService;
    }

    public Boolean validateLogin(HttpSession httpSession) {
        boolean isLoggedIn = true;
        if (!loginController.validateUser(httpSession).equals("validated")) {
            return !isLoggedIn;
        } if (!loginController.validateRoles(httpSession).contains("sales")) {
            return !isLoggedIn;
        }
        else {
            return isLoggedIn;
        }
    }

    @GetMapping("/salg")
    public String salesPage(Model model, HttpSession httpSession)  {
        model.addAttribute("roles", loginController.validateRoles(httpSession));
        if (!validateLogin(httpSession)){
            return "redirect:/";
        }
            model.addAttribute("userList", userService.getAll());
            return "sales";
        }
    }

