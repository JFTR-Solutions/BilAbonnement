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


//    The loginController constructor is used to initialize the userService and put exception messages into the static HashMap created above.
    //FREDERIK
    public LoginController(UserService userService) {
        exceptionEnums.put(carExceptionEnum.NO_LOGIN, "Du skal være logget på for at få vist denne side");
        exceptionEnums.put(carExceptionEnum.WRONG_LOGIN, "Ugyldigt brugernavn eller adgangskode");
        exceptionEnums.put(carExceptionEnum.NO_PERMISSION, "Du har ikke rettigheder til at få vist denne side");
        exceptionEnums.put(carExceptionEnum.DATABASE_ERROR, "Kunne ikke oprette forbindelse til databasen, prøv igen senere");
        exceptionEnums.put(carExceptionEnum.ROLE_ERROR, "Kunne ikke opdatere rollen");
        exceptionEnums.put(carExceptionEnum.USER_EXIST, "En bruger med denne email eksisterer allerede");
        this.userService = userService;
    }

    /*validateLogin is called upon in every getMapping to validate that the user is logged in and assigned a role that grants access to the requested page.
    * If validateUser returns false, a carLeasingException will be thrown with a message that the user needs to login.
     * The validateRoles method is checking if the userrole contains the role attribute that has been defined in each controller or if the they have the sysadmin role.
     * validateRoles will also throw a carLeasingException if false but will instead tell the user that they do not have permission to view the page.
    * */
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


    /*loginPage is the first page the user will see when opening https://jftr-solution.azurewebsites.net/ . This method
    * will catch exceptions thrown from validateuser and display the errormessage if thrown. By using Thymeleaf (th:if),
    *  we can tell the html page to only display the error if the attribute has been created.
    * The method is also checking if the user is already logged in, if so they will be sent directly to the welcome page instead of displaying the loginpage */

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

    /* The login postMapping method will check the input with the database. If no exception is thrown,
    the httpsession attributes "email" & "password" will be set and user is redirected to the welcome page.
    Else the error attribute will be set instead and the user will be sent back to the loginpage.
   */
    //Frederik + Thomas
    @PostMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password,
                        HttpSession httpSession) {
        try {
            userService.getEmailAndPassword(email, e.encrypt(password));
            httpSession.setAttribute("email", email);
            httpSession.setAttribute("password", e.encrypt(password));
        } catch (CarLeasingException e) {
            httpSession.setAttribute("error", e.getMessage());
            return "redirect:/";
        }
        httpSession.removeAttribute("error");
        return "redirect:/welcome";
    }


    /*validateUser is checking whether the user is logged in by first checking if the HttpSession attribute "email" & "password" has been set.
    * Afterwards it will check if they match the email and password in the database. */
    //Frederik
    public boolean validateUser(HttpSession httpSession) throws CarLeasingException {

        if (httpSession.getAttribute("email") != null && httpSession.getAttribute("password") != null) {
            User user = userService.getEmailAndPassword((String) httpSession.getAttribute("email"), (String) httpSession.getAttribute("password"));
            return user.getEmail().equals(httpSession.getAttribute("email")) && user.getPassword().equals((httpSession.getAttribute("password")));
        }
        throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.NO_LOGIN)) ;
    }

    /* validateRoles will first find the user based on the httpsession attributes. As these were already validated in the previous method,
    we therefore don't have to validate them again. A rolelist will then be created for the user */
    //Frederik
    public List<String> validateRoles(HttpSession httpSession) throws CarLeasingException {
        User user = userService.getEmailAndPassword((String) httpSession.getAttribute("email"), (String) httpSession.getAttribute("password"));
        List<String> roleList = userService.getRoles(user.getUserId());
        return roleList;
    }

    /*welcomeUser is firstly validating the login. Afterwards modelattributes will be set to display an overview of all functions that the user has access to.
    * This method also catches exceptions fx. a user tries to visit a page they do not have access to. Then they will be returned to the welcome page with an error message.
    * */
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
        if (httpSession.getAttribute("error") != null) {
            model.addAttribute("errorMessage", httpSession.getAttribute("error"));
        }
        httpSession.removeAttribute("error");
        model.addAttribute("roles", validateRoles(httpSession));
//        httpSession.setAttribute("roller", validateRoles(httpSession));
        model.addAttribute("name", userService.getEmailAndPassword((String) httpSession.getAttribute("email"), (String) httpSession.getAttribute("password")).getFirstName());
        return "welcome";
    }

    /*Lastly the LoginController has a page called invalidate-cookie, which will invalidate the session, meaning that all objects assigned to it will be removed.
    This functions as a logout button since when pressed, the user is returned back to the login page due the session now being invalid*/
    //Frederik
    @GetMapping("/invalidate-cookie")
    public String logOut(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }


}
