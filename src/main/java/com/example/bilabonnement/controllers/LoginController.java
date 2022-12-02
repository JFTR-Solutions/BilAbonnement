package com.example.bilabonnement.controllers;

import com.example.bilabonnement.encryption.Encrypter;
import com.example.bilabonnement.exceptions.CarException;
import com.example.bilabonnement.exceptions.CarLeasingException;
import com.example.bilabonnement.exceptions.carExceptionEnum;
import com.example.bilabonnement.models.users.User;
import com.example.bilabonnement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class LoginController {

    private final String roleAdmin = "sysadmin";
    UserService userService;
    Encrypter e = new Encrypter();

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    public Boolean validateLogin(HttpSession httpSession, String role) throws CarLeasingException {
        if (!validateUser(httpSession)) {
            throw new CarLeasingException("Du skal være logget på for at få vist denne side", carExceptionEnum.ERROR);
        } else if (!validateRoles(httpSession).contains(role) && (!validateRoles(httpSession).contains("sysadmin"))) {
            throw new CarLeasingException("Du har ikke rettigheder til at få vist denne side", carExceptionEnum.ERROR);
        } else {
            return true;
        }
    }

    @GetMapping("/")
    public String loginPage(HttpSession httpSession, Model model) {
        try {
            if (validateUser(httpSession)) {
                return "redirect:/welcome";
            }
        } catch (CarLeasingException e) {
            model.addAttribute("errorMessage", httpSession.getAttribute("error"));
        }
        return "index";
    }

    //Frederik + Thomas
    @PostMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password,
                        HttpSession httpSession) {
        try {
            userService.getEmail(email, e.encrypt(password));
            httpSession.setAttribute("email", email);
            httpSession.setAttribute("password", e.encrypt(password));
        } catch (CarLeasingException e) {
            httpSession.setAttribute("error", e.getMessage());
            return "redirect:/";
        }
        return "redirect:/welcome";
    }


    //Frederik
    public boolean validateUser(HttpSession httpSession) throws CarLeasingException {

        if (httpSession.getAttribute("email") != null && httpSession.getAttribute("password") != null) {
            User user = userService.getEmail((String) httpSession.getAttribute("email"), (String) httpSession.getAttribute("password"));
            return user.getEmail().equals(httpSession.getAttribute("email")) && user.getPassword().equals((httpSession.getAttribute("password")));
        }
        throw new CarLeasingException("Venligst log på for at få vist siden", carExceptionEnum.ERROR);
    }

    //Frederik
    public List<String> validateRoles(HttpSession httpSession) throws CarLeasingException {
        User user = userService.getEmail((String) httpSession.getAttribute("email"), (String) httpSession.getAttribute("password"));
        List<String> roleList = userService.getRoles(user.getUserId());
        return roleList;
    }

    //Frederik
    @GetMapping("/welcome")
    public String welcomeUser(HttpSession httpSession, Model model) throws CarLeasingException {
        try {
            if (!validateUser(httpSession)) {
                return "redirect:/";
            }
        } catch (CarLeasingException e) {
                httpSession.setAttribute("error", e.getMessage());
                return "redirect:/";
            }
        if (httpSession.getAttribute("error")!=null){
            model.addAttribute("errorMessage",httpSession.getAttribute("error"));
        }

        model.addAttribute("roles", validateRoles(httpSession));
        httpSession.setAttribute("roller", validateRoles(httpSession));
        model.addAttribute("name", userService.getEmail((String) httpSession.getAttribute("email"), (String) httpSession.getAttribute("password")).getFirstName());
        return "welcome";
    }

    //Frederik
    @GetMapping("/invalider-cookie")
    public String invalidateCookie(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }


}
