package com.example.bilabonnement.controllers;

import com.example.bilabonnement.Exceptions.UserNotFoundException;
import com.example.bilabonnement.encryption.Encrypter;
import com.example.bilabonnement.models.users.User;
import com.example.bilabonnement.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class LoginController {


    UserService userService;
    Encrypter e = new Encrypter();

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String loginPage(HttpSession httpSession) {
        if (!validateUser(httpSession).equals("validated")) {
            return "index";
        } else {
            return "redirect:/velkommen";
        }
    }

    //Frederik + Thomas
    @PostMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password,
                        HttpSession httpSession)  {
        User user = userService.getEmail(email, e.encrypt(password));
        if (user == null) {
            return "redirect:/error";
        }
        httpSession.setAttribute("email", email);
        httpSession.setAttribute("password", e.encrypt(password));

        return "redirect:/velkommen";
    }

    //Frederik
    public String validateUser(HttpSession httpSession) {
        User user = userService.getEmail((String) httpSession.getAttribute("email"), (String) httpSession.getAttribute("password"));
        if (httpSession.getAttribute("email") == null || httpSession.getAttribute("password") == null) {
            return "redirect:/";
        } else if (user.getEmail().equals(httpSession.getAttribute("email")) && user.getPassword().equals((httpSession.getAttribute("password")))) {
            return "validated";
        } else return "redirect:/error";
    }

    //Frederik
    public List<String> validateRoles(HttpSession httpSession) {
        User user = userService.getEmail((String) httpSession.getAttribute("email"), (String) httpSession.getAttribute("password"));
        List<String> roleList = userService.getRoles(user.getUserId());
        return roleList;
    }

    //Frederik
    @GetMapping("/velkommen")
    public String welcomeUser(HttpSession httpSession, Model model)  {
        if (!validateUser(httpSession).equals("validated")) {
            return validateUser(httpSession);
        } else {
            model.addAttribute("roles", validateRoles(httpSession));
            httpSession.setAttribute("roller",validateRoles(httpSession));
            model.addAttribute("name", userService.getEmail((String) httpSession.getAttribute("email"), (String) httpSession.getAttribute("password")).getFirstName());
            return "welcome";
        }
    }

    //Frederik
    @GetMapping("/invalider-cookie")
    public String invalidateCookie(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }


}
