package com.example.bilabonnement.controllers;

import com.example.bilabonnement.security.Encrypter;
import com.example.bilabonnement.exceptions.CarLeasingException;
import com.example.bilabonnement.models.cars.Car;
import com.example.bilabonnement.security.PasswordGenerator;
import com.example.bilabonnement.security.UsernameMaker;
import com.example.bilabonnement.service.CarService;
import com.example.bilabonnement.service.RentalService;
import com.example.bilabonnement.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.util.Calendar;

@Controller
public class FrontdeskController {

    LoginController loginController;
    UserService userService;
    CarService carService;

    RentalService rentalService;

    private final String role = "Reception";

    public FrontdeskController(LoginController loginController, UserService userService, CarService carService, RentalService rentalService) {
        this.loginController = loginController;
        this.userService = userService;
        this.carService = carService;
        this.rentalService = rentalService;
    }

    //Jonathan
    @GetMapping("/reception")
    public String frontdeskPage(Model model, HttpSession httpSession) throws CarLeasingException {
        try {
            model.addAttribute("roles", loginController.validateRoles(httpSession));
            if (!loginController.validateLogin(httpSession, role)) {
                return "redirect:/";
            }
        } catch (CarLeasingException e) {
            httpSession.setAttribute("error", e.getMessage());
            return "redirect:/welcome";
        }
        model.addAttribute("carlist", carService.fetchAllAvailableCars());
        return "frontdesk";
    }

    //Thomas
    @GetMapping("/create-rental-agreement/{carId}")
    public String showCreateRentalAgreement(@PathVariable("carId")int carId, HttpSession httpSession, Model model)
            throws CarLeasingException {
        if (!loginController.validateLogin(httpSession, role)) {
            return "redirect:/";
        }
        model.addAttribute("kmpricelist", rentalService.fetchAllMthKm());
        model.addAttribute("car", carService.findCarById(carId));
        model.addAttribute("userlist", userService.getAllByRole(5));
        return "createrentalagreement";
    }

    //Thomas
    @PostMapping("/create-rental-agreement")
    public String createRentalAgreement(@RequestParam("carId") int carId, @RequestParam("userId") int userId,
                                        @RequestParam("mthKmId") int mthKmId, @RequestParam("months") int months,
                                        @RequestParam("startDate") Date startDate,
                                        @RequestParam(defaultValue = "false", value="deliveryInsurance") boolean deliveryInsurance,
                                        @RequestParam(defaultValue = "false", value="selfInsurance") boolean selfInsurance,
                                        @RequestParam(defaultValue = "false", value="winterTires") boolean winterTires,
                                        @RequestParam(defaultValue = "false", value="viking") boolean viking,
                                        @RequestParam(defaultValue = "false", value="cleverNetwork") boolean cleverNetwork,
                                        @RequestParam(defaultValue = "false", value="clever") boolean clever) throws CarLeasingException {

        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.MONTH, months);
        Date endDate = new Date(cal.getTimeInMillis());
        double mthPrice = carService.findCarById(carId).getMthPrice() + rentalService.findmthKmById(mthKmId).getPrice();
        rentalService.addRentalAgreement(carId, userId, mthKmId, endDate, startDate, mthPrice);
        carService.updateCarAvailability(carId, (byte) 0);

        int rentalId = rentalService.findRentalAgreementIdByCarId(carId);
        if(deliveryInsurance){
            rentalService.addCarAddon(rentalId, 1);
        } if(selfInsurance){
            rentalService.addCarAddon(rentalId, 2);
        } if(winterTires){
            rentalService.addCarAddon(rentalId, 3);
        } if(viking){
            rentalService.addCarAddon(rentalId, 4);
        } if(cleverNetwork){
            rentalService.addCarAddon(rentalId, 5);
        } if(clever){
            rentalService.addCarAddon(rentalId, 6);
        }


        return "redirect:/reception";
    }

    //Thomas
    @GetMapping("/show-rental-agreement")
    public String showRentalAgreementList(HttpSession httpSession, Model model) throws CarLeasingException {
        if (!loginController.validateLogin(httpSession, role)) {
            return "redirect:/";
        }
        model.addAttribute("agreements", rentalService.fetchAllRentalAgreements());

        return "showrentalagreementlist";
    }

    @GetMapping("/show-rental-agreement/{rentalId}")
    public String showRentalAgreementByID(@PathVariable("rentalId")int rentalId, HttpSession httpSession,
                                          Model model) throws CarLeasingException {
        if (!loginController.validateLogin(httpSession, role)) {
            return "redirect:/";
        }
        model.addAttribute("agreement", rentalService.findRentalAgreementById(rentalId));
        model.addAttribute("addons", rentalService.findCarAddonsByRentalId(rentalId));

        return "showrentalagreement";
    }

    //Thomas
    @GetMapping("/create-customer")
    public String createUserPage(HttpSession httpSession) throws CarLeasingException {
        if (!loginController.validateLogin(httpSession, role)) {
            return "redirect:/";
        }
        return "createcustomer";
    }

    //Thomas
    @PostMapping("/create-customer")
    public String createUser(@RequestParam("email") String email, @RequestParam("firstname") String firstname,
                             @RequestParam("lastname") String lastname, @RequestParam("birthdate") Date birthdate,
                             @RequestParam("address") String address, @RequestParam("phonenr") String phonenr)
            throws CarLeasingException {
        PasswordGenerator pw = new PasswordGenerator();
        UsernameMaker um = new UsernameMaker(userService);
        String username = um.makeUsername(firstname, lastname, birthdate);
        Encrypter encrypter = new Encrypter();
        String encryptedPassword = encrypter.encrypt(pw.generateRandomPassword());
        userService.createCustomer(email.toLowerCase(), encryptedPassword, username, firstname, lastname, birthdate, address, phonenr);
        userService.giveCustomerRole(userService.findUserByUsername(username));

        return ("redirect:/reception");
    }

}
