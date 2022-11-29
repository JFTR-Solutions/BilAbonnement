package com.example.bilabonnement.controllers;

import com.example.bilabonnement.encryption.Encrypter;
import com.example.bilabonnement.models.users.Role;
import com.example.bilabonnement.models.users.User;
import com.example.bilabonnement.repository.UserRepository;
import com.example.bilabonnement.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SysadminController {

    LoginController loginController;
    UserService userService;
    UserRepository userRepository;

    public SysadminController(UserService userService, LoginController loginController, UserRepository userRepository) {
        this.userService = userService;
        this.loginController = loginController;
        this.userRepository = userRepository;
    }

    //Frederik
    @GetMapping("/sysadmin")
    public String sysadminPage(Model model, HttpSession httpSession) {
        if (!loginController.validateRoles(httpSession).contains("sysadmin")){
            return "redirect:/";
        }
        if (!loginController.validateUser(httpSession).equals("validated")) {
            return loginController.validateUser(httpSession);
        } else {
            model.addAttribute("roleList",roleList());
            model.addAttribute("userList", userService.getAll());
            return "sysadmin";
        }
    }

    public List<String> roleList(){
        List<String> rolelist = new ArrayList<>();
        for (int i = 0; i < userService.getAll().size(); i++) {
            rolelist.add(userService.getRoles(userService.getAll().get(i).getUserId()).toString().substring(1,userService.getRoles(userService.getAll().get(i).getUserId()).toString().length()-1));
        }
        return rolelist;
    }

    @GetMapping("/createuser")
    public String createUserPage(){
        return "createuser";
    }

    @PostMapping("/createuser")
    public String createUser(@RequestParam("email") String email, @RequestParam("password") String password,
                             @RequestParam("username") String username, @RequestParam("firstname") String firstname,
                             @RequestParam("lastname") String lastname, @RequestParam("birthdate") String birthdate,
                             @RequestParam("address") String address, @RequestParam("phonenr") String phonenr){
        Encrypter encrypter = new Encrypter();
        String encryptedPassword = encrypter.encrypt(password);
        if (userService.getEmail(email,encryptedPassword)==null) {
            userService.createUser(email, encryptedPassword, username, firstname, lastname, birthdate, address, phonenr);
            return "redirect:/sysadmin";
        }
        return ("redirect:/error");
    }

    @GetMapping("/updateuser/{id}")
    public String updateWishlist(@PathVariable("id") int id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("roles",userService.getRoles(id));
        model.addAttribute("user", userService.findUserByID(id));
        return "updateuser";
    }

    @PostMapping("/updateuser")
    public String saveWishlist(@ModelAttribute User user,@RequestParam(defaultValue = "false") boolean sysadmin,
                               @RequestParam(defaultValue = "false") boolean sales, @RequestParam(defaultValue = "false") boolean front_desk,
                               @RequestParam(defaultValue = "false") boolean mechanic) {
        System.out.println("" + sysadmin + sales + front_desk + mechanic);
        System.out.println(user);
        System.out.println(user.getUserId());
        userService.updateUser(user);

        userService.updateRoles(user, sysadmin, sales, front_desk, mechanic);
        return "redirect:/sysadmin";
    }

    //TODO Delete user
}
