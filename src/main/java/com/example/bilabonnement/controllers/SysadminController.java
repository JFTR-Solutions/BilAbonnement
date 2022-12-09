package com.example.bilabonnement.controllers;

import com.example.bilabonnement.exceptions.CarLeasingException;
import com.example.bilabonnement.exceptions.carExceptionEnum;
import com.example.bilabonnement.security.Encrypter;
import com.example.bilabonnement.models.users.User;
import com.example.bilabonnement.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static com.example.bilabonnement.controllers.LoginController.exceptionEnums;

@Controller
public class SysadminController {

    LoginController loginController;
    UserService userService;

    private final String role = "System adminstrator";


    public SysadminController(UserService userService, LoginController loginController) {
        this.userService = userService;
        this.loginController = loginController;
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

        System.out.println(userService.getEmployeeWithoutRole());
        model.addAttribute("roleList", roleList());
        model.addAttribute("userList", userService.getAllEmployees());
        model.addAttribute("usersNoRole", userService.getEmployeeWithoutRole());
        return "sysadmin";
    }

    public List<String> roleList() throws CarLeasingException {
        List<String> rolelist = new ArrayList<>();
        for (int i = 0; i < userService.getAllEmployees().size(); i++) {
            rolelist.add(userService.getRoles(userService.getAllEmployees().get(i).getUserId()).toString().substring(1,
                    userService.getRoles(userService.getAllEmployees().get(i).getUserId()).toString().length() - 1));
        }
        return rolelist;
    }

    //Frederik
    @GetMapping("/create-user")
    public String createUserPage(HttpSession httpSession, Model model) throws CarLeasingException {
        if (!loginController.validateLogin(httpSession, role)) {
            return "redirect:/";
        }
        model.addAttribute("errorMessage", httpSession.getAttribute("error"));
        return "createuser";
    }

    //Frederik
    @PostMapping("/create-user")
    public String createUser(@RequestParam("email") String email, @RequestParam("password") String password,
                             @RequestParam("username") String username, @RequestParam("firstname") String firstname,
                             @RequestParam("lastname") String lastname, @RequestParam("birthdate") Date birthdate,
                             @RequestParam("address") String address, @RequestParam("phonenr") String phonenr, HttpSession httpSession)
            throws CarLeasingException {
        Encrypter encrypter = new Encrypter();
        String encryptedPassword = encrypter.encrypt(password);
        try {
            userService.getEmail(email);
        } catch (CarLeasingException e) {
            httpSession.setAttribute("error", e.getMessage());
            return "redirect:/create-user";
        }
        httpSession.setAttribute("error", "");
        userService.createUser(email.toLowerCase(), encryptedPassword, username, firstname, lastname, birthdate,
                address, phonenr);
        return "redirect:/sysadmin";
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
    public String saveUser(@ModelAttribute User user, @RequestParam(defaultValue = "false", value = "sysadmin") boolean sysadmin,
                           @RequestParam(defaultValue = "false", value = "sales") boolean sales,
                           @RequestParam(defaultValue = "false", value = "front_desk") boolean front_desk,
                           @RequestParam(defaultValue = "false", value = "mechanic") boolean mechanic) throws CarLeasingException {
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
