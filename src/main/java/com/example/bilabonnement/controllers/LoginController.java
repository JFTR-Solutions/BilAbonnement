package com.example.bilabonnement.controllers;

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
//Frederik
    @PostMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password,
                        HttpSession httpSession) {
        User user = userService.getEmail(email, password);

        if (user == null) {
            return "redirect:/error";
        }
        httpSession.setAttribute("email", email);
        httpSession.setAttribute("password", password);

        return "redirect:/sign-in";
    }
    //Frederik
    public String validateUser(HttpSession httpSession) {
        User user = userService.getEmail((String) httpSession.getAttribute("email"), (String) httpSession.getAttribute("password"));
        if (httpSession.getAttribute("email") == null) {
            return "redirect:/";
        } else if (user.getEmail().equals(httpSession.getAttribute("email")) && user.getPassword().equals(httpSession.getAttribute("password"))) {
            return "validated";
        } else return "redirect:/error";
    }
    //Frederik
    public String validateRoleOnLogin(HttpSession httpSession) {
        User user = userService.getEmail((String) httpSession.getAttribute("email"), (String) httpSession.getAttribute("password"));
        List<String> roleList = userService.GetRoles(user.getUserId());
        for (String role: roleList) {
            if (role.equals("sysadmin")){
                httpSession.setAttribute("role",role);
                return "redirect:/sysadmin";
            }
        }
        return "redirect:/";
    }
    //Frederik
    @GetMapping("/sign-in")
    public String welcomeUser(HttpSession httpSession, Model model) {
        if (!validateUser(httpSession).equals("validated")) {
            return validateUser(httpSession);
        } else if (validateRoleOnLogin(httpSession).equals("redirect:/sysadmin")) {
            return "redirect:/sysadmin";

        } else {
            model.addAttribute("user", userService.getEmail((String) httpSession.getAttribute("username"), (String) httpSession.getAttribute("username")));
            httpSession.getAttribute("username");
            return "/welcome";
        }
    }

    //Frederik
    @GetMapping("/cookieinvalidate")
    public String invalidateCookie(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }


}
