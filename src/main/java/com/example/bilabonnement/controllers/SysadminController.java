package com.example.bilabonnement.controllers;

import com.example.bilabonnement.encryption.Encrypter;
import com.example.bilabonnement.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Date;

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

    @GetMapping("/createuser")
    public String createUserPage(){
        return "createuser";
    }

    @PostMapping("/createuser")
    public String createUser(@RequestParam("email") String email, @RequestParam("password") String password,
                             @RequestParam("username") String username, @RequestParam("firstname") String firstname,
                             @RequestParam("lastname") String lastname, @RequestParam("birthdate") String birthdate,
                             @RequestParam("address") String address, @RequestParam("phonenr") String phonenr){
        Encrypter encrypter = new Encrypter();
        String encryptedPassword = encrypter.encrypt(password);
        if (userService.getEmail(email,encryptedPassword)==null) {
            userService.createUser(email, encryptedPassword, username, firstname, lastname, birthdate, address, phonenr);
            return "redirect:/sysadmin";
        }
        return ("redirect:/error");
    }
}
