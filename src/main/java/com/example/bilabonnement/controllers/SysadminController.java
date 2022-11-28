package com.example.bilabonnement.controllers;

import com.example.bilabonnement.encryption.Encrypter;
import com.example.bilabonnement.models.users.Role;
import com.example.bilabonnement.models.users.User;
import com.example.bilabonnement.repository.UserRepository;
import com.example.bilabonnement.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SysadminController {

    LoginController loginController;
    UserService userService;
    UserRepository userRepository;

    public SysadminController(UserService userService, LoginController loginController, UserRepository userRepository) {
        this.userService = userService;
        this.loginController = loginController;
        this.userRepository = userRepository;
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

    //TODO fix update user

    @GetMapping("/updateuser/{id}")
    public String updateWishlist(@PathVariable("id") int id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("user", userService.findUserByID(id));
        return "updateuser";
    }

    @PostMapping("/updateuser")
    public String saveWishlist(@ModelAttribute User user) {
        userRepository.updateUser(user);
        return "redirect:/sysadmin";
    }

    //TODO Delete user
}
