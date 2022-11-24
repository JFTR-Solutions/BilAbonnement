package com.example.bilabonnement.logincontroller;

import com.example.bilabonnement.models.users.User;
import com.example.bilabonnement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public String loginPage() {
        return "index";
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password,
                        HttpSession httpSession) {
        User user = userRepository.findUserByEmail(email,password);
        System.out.println(user);

        if(user == null){
            return "redirect:/error";
        }

        httpSession.setAttribute("email",email);
        httpSession.setAttribute("password",password);
        return "redirect:/welcome";
    }

    public String validateUser(HttpSession httpSession){
        User user2 = userRepository.findUserByEmail((String) httpSession.getAttribute("email"), (String) httpSession.getAttribute("password"));
        if (httpSession.getAttribute("email") == null){
            return "redirect:/";
        }
        else if (user2.getEmail().equals(httpSession.getAttribute("email")) && user2.getPassword().equals(httpSession.getAttribute("password"))){
            return "validated";
        }
        else return "redirect:/error";
    }

    @GetMapping("/welcome")
    public String welcomeUser(HttpSession httpSession, Model model) {
        if (!validateUser(httpSession).equals("validated")) {
            return validateUser(httpSession);
        } else {
            model.addAttribute("user", userRepository.findUserByEmail((String) httpSession.getAttribute("username"), (String) httpSession.getAttribute("username")));
            httpSession.getAttribute("username");
            return "/welcome";
        }
    }



}
