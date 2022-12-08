package com.example.bilabonnement.controllers;

import com.example.bilabonnement.exceptions.CarLeasingException;
import com.example.bilabonnement.models.cars.Car;
import com.example.bilabonnement.models.rentalagreements.RentalAgreement;
import com.example.bilabonnement.service.CarService;
import com.example.bilabonnement.service.RentalService;
import com.example.bilabonnement.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SalesController {

    private final String role = "Salg";
    LoginController loginController;
    UserService userService;
    RentalService rentalService;
    CarService carService;

    public SalesController(LoginController loginController, UserService userService, RentalService rentalService, CarService carService) {
        this.loginController = loginController;
        this.userService = userService;
        this.rentalService = rentalService;
        this.carService = carService;
    }
/*Thomas & Frederik*/
    public List<Double> carRevenueEachMonth() {
        List<Double> carRevenueList = new ArrayList<>();
        List<RentalAgreement> rentalList = rentalService.fetchAllRentalAgreements();
        for (int i = 1; i <= 12; i++) {
            double monthRevenue = 0;
            YearMonth now = YearMonth.of(YearMonth.now().getYear() + 1, i);
            for (int j = 0; j < rentalList.size(); j++) {
                String date = rentalList.get(j).getEndDate().toString().substring(0, 7);
                YearMonth second = YearMonth.parse(date);
                if (second.isAfter(now)) {
                    monthRevenue += rentalList.get(j).getMthPrice();
                }
            }
            carRevenueList.add(monthRevenue);
        }
        return carRevenueList;
    }

    /*Thomas & Frederik*/
    public List<Integer> carsRentedOutEachMonth() {
        List<Integer> carRevenueList = new ArrayList<>();
        List<RentalAgreement> rentalList = rentalService.fetchAllRentalAgreements();
        for (int i = 1; i <= 12; i++) {
            int monthRented = 0;
            YearMonth now = YearMonth.of(YearMonth.now().getYear() + 1, i);
            for (int j = 0; j < rentalList.size(); j++) {
                String date = rentalList.get(j).getEndDate().toString().substring(0, 7);
                YearMonth second = YearMonth.parse(date);
                if (second.isAfter(now)) {
                    monthRented += rentalList.get(j).getActive();
                }
            }
            carRevenueList.add(monthRented);
        }
        return carRevenueList;
    }

    /*Thomas & Frederik*/
    public int carRentedOut() {
        int carsRented = 0;
        List<Car> carList = carService.fetchAllCars();
        for (Car car : carList) {
            if (car.getAvailable() == 0) {
                carsRented++;
            }
        }
        return carsRented;
    }
    /*Thomas & Frederik*/
    public int carRevenue() {
        int carRevenue = 0;
        List<RentalAgreement> rentalAgreementList = rentalService.fetchAllRentalAgreements();
        for (RentalAgreement rentalAgreement : rentalAgreementList) {
            if (rentalAgreement.getActive() == 1) {
                carRevenue += rentalAgreement.getMthPrice();
            }
        }
        return carRevenue;
    }

    /*Thomas & Frederik*/
    public int carsAvailable() {
        return carService.fetchAllAvailableCars().size();
    }

    /*Thomas & Frederik*/
    @GetMapping("/sales")
    public String salesPage(Model model, HttpSession httpSession) throws CarLeasingException {
        try {
            model.addAttribute("roles", loginController.validateRoles(httpSession));
            if (!loginController.validateLogin(httpSession, role)) {
                return "redirect:/";
            }
        } catch (CarLeasingException e) {
            httpSession.setAttribute("error", e.getMessage());
            return "redirect:/welcome";
        }
        model.addAttribute("carsRented", carRentedOut());
        model.addAttribute("carsAvailable", carsAvailable());
        model.addAttribute("rentalRevenue", carRevenue());


        model.addAttribute("carRevenueList", carRevenueEachMonth());
        model.addAttribute("carsRentedOut", carsRentedOutEachMonth());

        model.addAttribute("userList", userService.getAllEmployees());
        return "sales";
    }


}

