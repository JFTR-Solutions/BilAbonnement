package com.example.bilabonnement.controllers;

import com.example.bilabonnement.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class SysadminController {

    LoginController loginController;
    UserService userService;

    public SysadminController(UserService userService, LoginController loginController) {
        this.userService = userService;
        this.loginController = loginController;
    }

    //Frederik
    @GetMapping("/sysadmin")
    public String sysadminPage(Model model, HttpSession httpSession) {
        if (!loginController.validateUser(httpSession).equals("validated")) {
            return loginController.validateUser(httpSession);
        } else {

            model.addAttribute("userList", userService.getAll());
            return "sysadmin";
        }
    }
}
