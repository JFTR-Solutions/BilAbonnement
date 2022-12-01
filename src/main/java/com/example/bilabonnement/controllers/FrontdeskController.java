package com.example.bilabonnement.controllers;

import com.example.bilabonnement.exceptions.CarLeasingException;
import com.example.bilabonnement.models.cars.Car;
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

    public FrontdeskController(LoginController loginController, UserService userService, CarService carService, RentalService rentalService) {
        this.loginController = loginController;
        this.userService = userService;
        this.carService = carService;
        this.rentalService = rentalService;
    }

    public Boolean validateLogin(HttpSession httpSession) throws CarLeasingException {
        boolean isLoggedIn = false;
        if (loginController.validateUser(httpSession).equals("validated")){
            return !isLoggedIn;
        } else if(loginController.validateRoles(httpSession).contains("front_desk") ||
                loginController.validateRoles(httpSession).contains("sysadmin")) {
            return !isLoggedIn;
        } else {
            return isLoggedIn;
        }
    }

    //Jonathan
    @GetMapping("/reception")
    public String frontdeskPage(Model model, HttpSession httpSession) throws CarLeasingException {
        model.addAttribute("roles", loginController.validateRoles(httpSession));
        if (!validateLogin(httpSession)) {
            return "redirect:/";
        }
        model.addAttribute("carlist", carService.fetchAllCars());
        return "frontdesk";
    }

    //Jonathan
    @GetMapping("/update-car/{id}")
    public String updateCar(@PathVariable("id") int id, Model model, HttpSession httpSession) throws CarLeasingException {
        model.addAttribute("roles", loginController.validateRoles(httpSession));
        if (!validateLogin(httpSession)) {
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
    public String deleteCar(@PathVariable("id") int id,Model model, HttpSession httpSession) throws CarLeasingException {
        model.addAttribute("roles", loginController.validateRoles(httpSession));
        if (!validateLogin(httpSession)) {
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
        if (!validateLogin(httpSession)) {
            return "redirect:/";
        }
        return "createmodel";
    }

    //Rami
    @PostMapping("/create-car")
    public String createCar(@RequestParam("modelId") int modelId, @RequestParam("available") byte available,
                            @RequestParam("colour") String colour, @RequestParam("vin") String vin,
                            @RequestParam("regNumber") String regNumber, @RequestParam("steelPrice") double steelprice,
                            @RequestParam("mthPrice") double mthPrice, @RequestParam("transmission") String transmission){

        carService.addCar(modelId, available, colour, vin, regNumber, steelprice, mthPrice, transmission);

        return "redirect:/reception";
    }

    //Rami
    @GetMapping("/create-car")
    public String showCreateCar(HttpSession httpSession, Model model) throws CarLeasingException {
        if (!validateLogin(httpSession)) {
            return "redirect:/";
        }
        model.addAttribute("modellist", carService.getAllModels());
        return "createcar";
    }

    @GetMapping("/create-rentalAgreement")
    public String showCreateRentalAgreement(HttpSession httpSession, Model model) throws CarLeasingException {
        if (!validateLogin(httpSession)) {
            return "redirect:/";
        }
        model.addAttribute("kmpricelist", rentalService.fetchAllMthKm());
        model.addAttribute("carlist", carService.fetchAllAvailableCars());
        model.addAttribute("userlist", userService.getAll());
        return "createrentalagreement";
    }

    @PostMapping("/create-rentalAgreement")
    public String createRentalAgreement(@RequestParam("carId") int carId, @RequestParam("userId") int userId,
                                        @RequestParam("mthKmId") int mthKmId, @RequestParam("months") int months,
                                        @RequestParam("startDate") Date startDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.MONTH, months);
        Date endDate = new Date(cal.getTimeInMillis());
        double mthPrice = carService.findCarById(carId).getMthPrice() + rentalService.findmthKmById(mthKmId).getPrice();
        rentalService.addRentalAgreement(carId, userId, mthKmId, endDate, startDate, mthPrice);
        carService.updateCarAvailability(carId, (byte) 0);

        return "redirect:/reception";
    }

    @GetMapping("/show-rentalAgreement")
    public String showRentalAgreement(HttpSession httpSession, Model model) throws CarLeasingException {
        if (!validateLogin(httpSession)) {
            return "redirect:/";
        }
        model.addAttribute("agreements", rentalService.fetchAllRentalAgreements());
        model.addAttribute("carlist", carService.fetchAllCars());
        model.addAttribute("userlist", userService.getAll());
        model.addAttribute("kmpricelist", rentalService.fetchAllMthKm());

        return "showrentalagreement";
    }


   /*public static void main(String[] args) {
        int months = 12;
        //parse SQL date into Calendar
        Calendar cal = Calendar.getInstance();
        cal.setTime(Date.valueOf("2020-01-01"));
        //add months to Calendar
        cal.add(Calendar.MONTH, months);
        //parse Calendar back to SQL date
        Date endDate = new Date(cal.getTimeInMillis());
        System.out.println(endDate);
    }*/
}
