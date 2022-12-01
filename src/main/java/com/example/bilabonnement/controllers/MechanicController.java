package com.example.bilabonnement.controllers;

import com.example.bilabonnement.exceptions.CarLeasingException;
import com.example.bilabonnement.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class MechanicController {


    LoginController loginController;
    UserService userService;

    public MechanicController(LoginController loginController, UserService userService) {
        this.loginController = loginController;
        this.userService = userService;
    }

    public Boolean validateLogin(HttpSession httpSession) throws CarLeasingException {
        boolean isLoggedIn = false;
        if (loginController.validateUser(httpSession).equals("validated")){
            return !isLoggedIn;
        } else if(loginController.validateRoles(httpSession).contains("mechanic") ||
                loginController.validateRoles(httpSession).contains("sysadmin")) {
            return !isLoggedIn;
        } else {
            return isLoggedIn;
        }
    }

    @GetMapping("/mekaniker")
    public String frontdeskPage(Model model, HttpSession httpSession) throws CarLeasingException {
        model.addAttribute("roles", loginController.validateRoles(httpSession));
        if (!validateLogin(httpSession)) {
            return "redirect:/";
        }
        model.addAttribute("userList", userService.getAll());
        return "mechanic";
    }
}
