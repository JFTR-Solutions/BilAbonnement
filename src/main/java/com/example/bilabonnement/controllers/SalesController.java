package com.example.bilabonnement.controllers;

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

    @GetMapping("/salg")
    public String frontdeskPage(Model model, HttpSession httpSession) {
        if (!loginController.validateUser(httpSession).equals("validated")) {
            return loginController.validateUser(httpSession);
        } else {
            model.addAttribute("userList", userService.getAll());
            return "sales";
        }
    }
}
