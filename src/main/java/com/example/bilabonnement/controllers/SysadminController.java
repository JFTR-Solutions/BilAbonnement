package com.example.bilabonnement.controllers;

import com.example.bilabonnement.exceptions.UserNotFoundException;
import com.example.bilabonnement.encryption.Encrypter;
import com.example.bilabonnement.models.users.User;
import com.example.bilabonnement.repository.UserRepository;
import com.example.bilabonnement.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SysadminController {

    LoginController loginController;
    UserService userService;
    UserRepository userRepository;

    //Frederik
    public Boolean validateLogin(HttpSession httpSession) {
        boolean isLoggedIn = true;
        if (!loginController.validateUser(httpSession).equals("validated")) {
            return !isLoggedIn;
        }
        if (!loginController.validateRoles(httpSession).contains("sysadmin")) {
            return !isLoggedIn;
        } else {
            return isLoggedIn;
        }
    }

    public SysadminController(UserService userService, LoginController loginController, UserRepository userRepository) {
        this.userService = userService;
        this.loginController = loginController;
        this.userRepository = userRepository;
    }

    //Frederik
    @GetMapping("/sysadmin")
    public String sysadminPage(Model model, HttpSession httpSession) {
        if (!validateLogin(httpSession)) {
            return "redirect:/";
        }
        model.addAttribute("roleList", roleList());
        model.addAttribute("userList", userService.getAll());
        return "sysadmin";
    }

    public List<String> roleList() {
        List<String> rolelist = new ArrayList<>();
        for (int i = 0; i < userService.getAll().size(); i++) {
            rolelist.add(userService.getRoles(userService.getAll().get(i).getUserId()).toString().substring(1, userService.getRoles(userService.getAll().get(i).getUserId()).toString().length() - 1));
        }
        return rolelist;
    }
    //Frederik
    @GetMapping("/opret-bruger")
    public String createUserPage(HttpSession httpSession) {
        if (!validateLogin(httpSession)) {
            return "redirect:/";
        }
        return "createuser";
    }
    //Frederik
    @PostMapping("/opret-bruger")
    public String createUser(@RequestParam("email") String email, @RequestParam("password") String password,
                             @RequestParam("username") String username, @RequestParam("firstname") String firstname,
                             @RequestParam("lastname") String lastname, @RequestParam("birthdate") Date birthdate,
                             @RequestParam("address") String address, @RequestParam("phonenr") String phonenr) throws UserNotFoundException {
        Encrypter encrypter = new Encrypter();
        String encryptedPassword = encrypter.encrypt(password);
        if (userService.getEmail(email, encryptedPassword) == null) {
            userService.createUser(email.toLowerCase(), encryptedPassword, username, firstname, lastname, birthdate, address, phonenr);
            return "redirect:/sysadmin";
        }
        return ("redirect:/error");
    }
    //Frederik
    @GetMapping("/opdater-bruger/{id}")
    public String updateUser(@PathVariable("id") int id, Model model, HttpSession httpSession) {
        if (!validateLogin(httpSession)) {
            return "redirect:/";
        }
        model.addAttribute("id", id);
        model.addAttribute("roles", userService.getRoles(id));
        model.addAttribute("user", userService.findUserByID(id));
        return "updateuser";
    }
    //Frederik
    @PostMapping("/opdater-bruger")
    public String saveUser(@ModelAttribute User user, @RequestParam(defaultValue = "false") boolean sysadmin,
                           @RequestParam(defaultValue = "false") boolean sales, @RequestParam(defaultValue = "false") boolean front_desk,
                           @RequestParam(defaultValue = "false") boolean mechanic) {
        userService.updateUser(user);
        userService.updateRoles(user, sysadmin, sales, front_desk, mechanic);
        return "redirect:/sysadmin";
    }
    //Frederik
    @GetMapping("/slet-bruger/{id}")
    public String DeleteUser(@PathVariable("id") int id, HttpSession httpSession) {
        if (!validateLogin(httpSession)) {
            return "redirect:/";
        }
        userService.deleteUser(id);
        return "redirect:/sysadmin";
    }


}
