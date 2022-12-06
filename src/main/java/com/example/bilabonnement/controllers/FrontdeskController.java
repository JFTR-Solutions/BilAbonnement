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

    private final String role = "front_desk";

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
        model.addAttribute("carlist", carService.fetchAllCars());
        return "frontdesk";
    }

    //Jonathan
    @GetMapping("/update-car/{id}")
    public String updateCar(@PathVariable("id") int id, Model model, HttpSession httpSession) throws CarLeasingException {
        model.addAttribute("roles", loginController.validateRoles(httpSession));
        if (!loginController.validateLogin(httpSession, role)) {
            return "redirect:/";
        }
        model.addAttribute("id", id);
        model.addAttribute("car", carService.findCarById(id));
        model.addAttribute("modellist", carService.getAllModels());
        return "updatecar";
    }

    //Jonathan
    @PostMapping("/update-car")
    public String saveCar(@ModelAttribute Car car) {
        carService.updateCar(car);
        return "redirect:/reception";
    }

    //Jonathan
    @GetMapping("/delete-car/{id}")
    public String deleteCar(@PathVariable("id") int id, Model model, HttpSession httpSession) throws CarLeasingException {
        model.addAttribute("roles", loginController.validateRoles(httpSession));
        if (!loginController.validateLogin(httpSession, role)) {
            return "redirect:/";
        }
        carService.deleteCar(id);
        return "redirect:/reception";
    }

    //Thomas
    @PostMapping("/create-carmodel")
    public String createCarModel(@RequestParam("modelName") String modelName,
                                 @RequestParam("manufacturer") String manufacturer, @RequestParam("co2") String co2,
                                 @RequestParam("fuelType") String fuelType, @RequestParam("range") double range) {
        carService.addCarModel(modelName, manufacturer, co2, fuelType, range);
        return "redirect:/create-car";
    }

    //Thomas
    @GetMapping("/create-carmodel")
    public String showCreateCarModel(HttpSession httpSession) throws CarLeasingException {
        if (!loginController.validateLogin(httpSession, role)) {
            return "redirect:/";
        }
        return "createmodel";
    }

    //Rami
    @PostMapping("/create-car")
    public String createCar(@RequestParam("modelId") int modelId, @RequestParam("available") byte available,
                            @RequestParam("colour") String colour, @RequestParam("vin") String vin,
                            @RequestParam("regNumber") String regNumber, @RequestParam("steelPrice") double steelprice,
                            @RequestParam("mthPrice") double mthPrice, @RequestParam("transmission") String transmission) {

        carService.addCar(modelId, available, colour, vin, regNumber, steelprice, mthPrice, transmission);

        return "redirect:/reception";
    }

    //Rami
    @GetMapping("/create-car")
    public String showCreateCar(HttpSession httpSession, Model model) throws CarLeasingException {
        if (!loginController.validateLogin(httpSession, role)) {
            return "redirect:/";
        }
        model.addAttribute("modellist", carService.getAllModels());
        return "createcar";
    }

    //Thomas
    @GetMapping("/create-rental-agreement")
    public String showCreateRentalAgreement(HttpSession httpSession, Model model) throws CarLeasingException {
        if (!loginController.validateLogin(httpSession, role)) {
            return "redirect:/";
        }
        model.addAttribute("kmpricelist", rentalService.fetchAllMthKm());
        model.addAttribute("carlist", carService.fetchAllAvailableCars());
        model.addAttribute("userlist", userService.getAllByRole(5));
        return "createrentalagreement";
    }

    //Thomas
    @PostMapping("/create-rental-agreement")
    public String createRentalAgreement(@RequestParam("carId") int carId, @RequestParam("userId") int userId,
                                        @RequestParam("mthKmId") int mthKmId, @RequestParam("months") int months,
                                        @RequestParam("startDate") Date startDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.MONTH, months);
        Date endDate = new Date(cal.getTimeInMillis());
        double mthPrice = carService.findCarById(carId).getMthPrice() + rentalService.findmthKmById(mthKmId).getPrice();
        rentalService.addRentalAgreement(carId, userId, mthKmId, endDate, startDate, mthPrice);
        carService.updateCarAvailability(carId, (byte) 0);

        return "redirect:/reception";
    }

    //Thomas
    @GetMapping("/show-rental-agreement")
    public String showRentalAgreement(HttpSession httpSession, Model model) throws CarLeasingException {
        if (!loginController.validateLogin(httpSession, role)) {
            return "redirect:/";
        }
        model.addAttribute("agreements", rentalService.fetchAllRentalAgreements());
        model.addAttribute("carlist", carService.fetchAllCars());
        model.addAttribute("userlist", userService.getAllEmployees());

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
                             @RequestParam("address") String address, @RequestParam("phonenr") String phonenr) throws CarLeasingException {
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
