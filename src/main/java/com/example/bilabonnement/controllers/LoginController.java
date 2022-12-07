package com.example.bilabonnement.controllers;

import com.example.bilabonnement.security.Encrypter;
import com.example.bilabonnement.exceptions.CarLeasingException;
import com.example.bilabonnement.exceptions.carExceptionEnum;
import com.example.bilabonnement.models.users.User;
import com.example.bilabonnement.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@Controller
public class LoginController {

    public static HashMap<carExceptionEnum, String> exceptionEnums = new HashMap<>();
    UserService userService;
    Encrypter e = new Encrypter();

    //FREDERIK
    public LoginController(UserService userService) {
        exceptionEnums.put(carExceptionEnum.NO_LOGIN, "Du skal være logget på for at få vist denne side");
        exceptionEnums.put(carExceptionEnum.WRONG_LOGIN, "Ugyldigt brugernavn eller adgangskode");
        exceptionEnums.put(carExceptionEnum.NO_PERMISSION, "Du har ikke rettigheder til at få vist denne side");
        exceptionEnums.put(carExceptionEnum.DATABASE_ERROR, "Kunne ikke oprette forbindelse til databasen, prøv igen senere");
        exceptionEnums.put(carExceptionEnum.ROLE_ERROR, "Kunne ikke opdatere rollen");
        this.userService = userService;
    }
    //FREDERIK
    public Boolean validateLogin(HttpSession httpSession, String role) throws CarLeasingException {
        if (!validateUser(httpSession)) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.NO_LOGIN));
        } else if (!validateRoles(httpSession).contains(role) && (!validateRoles(httpSession).contains("System adminstrator"))) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.NO_PERMISSION));
        } else {
            return true;
        }
    }
    //FREDERIK
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
        throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.NO_LOGIN));
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
        if (httpSession.getAttribute("error") != null && !httpSession.getAttribute("error").equals("") &&
                (!httpSession.getAttribute("error").equals(exceptionEnums.get(carExceptionEnum.WRONG_LOGIN))) &&
                (!httpSession.getAttribute("error").equals(exceptionEnums.get(carExceptionEnum.NO_LOGIN)))) {
            model.addAttribute("errorMessage", httpSession.getAttribute("error"));
        }
        httpSession.setAttribute("error","");
        model.addAttribute("roles", validateRoles(httpSession));
        httpSession.setAttribute("roller", validateRoles(httpSession));
        model.addAttribute("name", userService.getEmail((String) httpSession.getAttribute("email"), (String) httpSession.getAttribute("password")).getFirstName());
        return "welcome";
    }

    //Frederik
    @GetMapping("/invalidate-cookie")
    public String invalidateCookie(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }


}
