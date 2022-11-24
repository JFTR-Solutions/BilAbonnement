package com.example.bilabonnement.controllers;

import com.example.bilabonnement.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SysadminController {

    UserService userService;

    public SysadminController(UserService userService) {
        this.userService = userService;
    }

    //Frederik
    @GetMapping("/sysadmin")
    public String sysadminPage(Model model){
        model.addAttribute("userList",userService.getAll());
        return "sysadmin";
    }
}
