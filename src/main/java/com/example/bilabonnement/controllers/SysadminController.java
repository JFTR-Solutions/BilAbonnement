package com.example.bilabonnement.controllers;

import com.example.bilabonnement.exceptions.CarLeasingException;
import com.example.bilabonnement.repository.MechanicRepository;
import com.example.bilabonnement.security.Encrypter;
import com.example.bilabonnement.models.users.User;
import com.example.bilabonnement.repository.UserRepository;
import com.example.bilabonnement.service.MechanicService;
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

    private final String role = "sysadmin";


    public SysadminController(UserService userService, LoginController loginController, UserRepository userRepository) {
        this.userService = userService;
        this.loginController = loginController;
        this.userRepository = userRepository;

    }

    //Frederik
    @GetMapping("/sysadmin")
    public String sysadminPage(Model model, HttpSession httpSession) throws CarLeasingException {
        try {
            model.addAttribute("roles", loginController.validateRoles(httpSession));
            if (!loginController.validateLogin(httpSession, role)) {
                return "redirect:/";
            }
        } catch (CarLeasingException e) {
            httpSession.setAttribute("error", e.getMessage());
            return "redirect:/welcome";
        }
        model.addAttribute("roleList", roleList());
        model.addAttribute("userList", userService.getAll());
        return "sysadmin";
    }

    public List<String> roleList() throws CarLeasingException {
        List<String> rolelist = new ArrayList<>();
        for (int i = 0; i < userService.getAll().size(); i++) {
            rolelist.add(userService.getRoles(userService.getAll().get(i).getUserId()).toString().substring(1, userService.getRoles(userService.getAll().get(i).getUserId()).toString().length() - 1));
        }
        return rolelist;
    }

    //Frederik
    @GetMapping("/create-user")
    public String createUserPage(HttpSession httpSession) throws CarLeasingException {
        if (!loginController.validateLogin(httpSession, role)) {
            return "redirect:/";
        }
        return "createuser";
    }

    //Frederik
    @PostMapping("/create-user")
    public String createUser(@RequestParam("email") String email, @RequestParam("password") String password,
                             @RequestParam("username") String username, @RequestParam("firstname") String firstname,
                             @RequestParam("lastname") String lastname, @RequestParam("birthdate") Date birthdate,
                             @RequestParam("address") String address, @RequestParam("phonenr") String phonenr) throws CarLeasingException {
        Encrypter encrypter = new Encrypter();
        String encryptedPassword = encrypter.encrypt(password);
        if (userService.getEmail(email, encryptedPassword) == null) {
            userService.createUser(email.toLowerCase(), encryptedPassword, username, firstname, lastname, birthdate, address, phonenr);
            return "redirect:/sysadmin";
        }
        return ("redirect:/error");
    }

    //Frederik
    @GetMapping("/update-user/{id}")
    public String updateUser(@PathVariable("id") int id, Model model, HttpSession httpSession) throws CarLeasingException {
        if (!loginController.validateLogin(httpSession, role)) {
            return "redirect:/";
        }
        model.addAttribute("id", id);
        model.addAttribute("roles", userService.getRoles(id));
        model.addAttribute("user", userService.findUserByID(id));
        return "updateuser";
    }

    //Frederik
    @PostMapping("/update-user")
    public String saveUser(@ModelAttribute User user, @RequestParam(defaultValue = "false") boolean sysadmin,
                           @RequestParam(defaultValue = "false") boolean sales, @RequestParam(defaultValue = "false") boolean front_desk,
                           @RequestParam(defaultValue = "false") boolean mechanic) throws CarLeasingException {
        userService.updateUser(user);
        userService.updateRoles(user, sysadmin, sales, front_desk, mechanic);
        userService.removeRoles(user, sysadmin, sales, front_desk, mechanic);
        return "redirect:/sysadmin";
    }

    //Frederik
    @GetMapping("/delete-user/{id}")
    public String DeleteUser(@PathVariable("id") int id, HttpSession httpSession) throws CarLeasingException {
        if (!loginController.validateLogin(httpSession, role)) {
            return "redirect:/";
        }
        userService.deleteUser(id);
        return "redirect:/sysadmin";
    }


}
