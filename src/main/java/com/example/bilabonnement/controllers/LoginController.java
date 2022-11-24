package com.example.bilabonnement.controllers;

import com.example.bilabonnement.models.users.User;
import com.example.bilabonnement.repository.UserRepository;
import com.example.bilabonnement.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

import static com.example.bilabonnement.encryption.Encryption.decrypt;
import static com.example.bilabonnement.encryption.Encryption.encrypt;

@Controller
public class LoginController {


    UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String loginPage(HttpSession httpSession) {
        if (!validateUser(httpSession).equals("validated")) {
            return "index";
        } else {
            return "welcome";
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password,
                        HttpSession httpSession) {
        User user = userService.getEmail(email, password);
//        System.out.println(user);

        if (user == null) {
            return "redirect:/error";
        }

        httpSession.setAttribute("email", email);
        httpSession.setAttribute("password", password);
        return "redirect:/welcome";
    }

    public String validateUser(HttpSession httpSession) {
        User user2 = userService.getEmail((String) httpSession.getAttribute("email"), (String) httpSession.getAttribute("password"));
        if (httpSession.getAttribute("email") == null) {
            return "redirect:/";
        } else if (user2.getEmail().equals(httpSession.getAttribute("email")) && user2.getPassword().equals(httpSession.getAttribute("password"))) {
            return "validated";
        } else return "redirect:/error";
    }

    @GetMapping("/welcome")
    public String welcomeUser(HttpSession httpSession, Model model) {
        if (!validateUser(httpSession).equals("validated")) {
            return validateUser(httpSession);
        } else if (httpSession.getAttribute("role") == "sysadmin") {
            return "redirect:/sysadmin";

        } else {
            model.addAttribute("user", userService.getEmail((String) httpSession.getAttribute("username"), (String) httpSession.getAttribute("username")));
            httpSession.getAttribute("username");
            return "/welcome";
        }
    }

    @GetMapping("/cookieinvalidate")
    public String invalidateCookie(HttpSession session) {
//        System.out.println(session.getAttribute("username"));
        session.invalidate();
        return "redirect:/";
    }


}
