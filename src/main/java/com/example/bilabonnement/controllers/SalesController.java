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
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class SalesController {

    private final String role = "Forretningsudvikler";
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
    public List<Double> carRevenueEachMonth(int month) {
        List<Double> carRevenueList = new ArrayList<>();
        List<RentalAgreement> rentalList = rentalService.fetchAllRentalAgreements();
        for (int i = 1; i <= 12; i++) {
            double monthRevenue = 0;
            YearMonth activeYearMonth = YearMonth.of(YearMonth.now().getYear() + month, i);
            for (int j = 0; j < rentalList.size(); j++) {
                String startDate = rentalList.get(j).getStartDate().toString().substring(0, 7);
                String endDate = rentalList.get(j).getEndDate().toString().substring(0, 7);

                YearMonth startYearMonth = YearMonth.parse(startDate);
                YearMonth endYearMonth = YearMonth.parse(endDate);

                if ((startYearMonth.isBefore(activeYearMonth) || startYearMonth.equals(activeYearMonth))
                        && endYearMonth.isAfter(activeYearMonth)) {
                    monthRevenue += rentalList.get(j).getMthPrice();
                }
            }
            carRevenueList.add(monthRevenue);
        }
        double total = 0;

        for (double d: carRevenueList) {
            total+=d;
        }
        carRevenueList.add(total);
        return carRevenueList;
    }

    /*Thomas & Frederik*/
    public List<Integer> carsRentedOutEachMonth() {
        List<Integer> carRevenueList = new ArrayList<>();
        List<RentalAgreement> rentalList = rentalService.fetchAllRentalAgreements();
        for (int i = 1; i <= 12; i++) {
            int monthRented = 0;
            YearMonth activeYearMonth = YearMonth.of(YearMonth.now().getYear(), i);
            for (int j = 0; j < rentalList.size(); j++) {
                String startDate = rentalList.get(j).getStartDate().toString().substring(0, 7);
                String endDate = rentalList.get(j).getEndDate().toString().substring(0, 7);

                YearMonth startYearMonth = YearMonth.parse(startDate);
                YearMonth endYearMonth = YearMonth.parse(endDate);

                if ((startYearMonth.isBefore(activeYearMonth) || startYearMonth.equals(activeYearMonth)) && endYearMonth.isAfter(activeYearMonth)) {
                    monthRented += rentalList.get(j).getActive();
                }
            }
            carRevenueList.add(monthRented);
        }
        int total = 0;

        for (int d: carRevenueList) {
            total+=d;
        }
        carRevenueList.add(total);

        return carRevenueList;
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

        java.util.Date date= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);

        model.addAttribute("carsRented", carsRentedOutEachMonth().get(month));
        model.addAttribute("carsAvailable", carsAvailable());
        model.addAttribute("rentalRevenue", carRevenueEachMonth(0).get(month));

        LocalDate currentdate = LocalDate.now();
        String currentMonth = String.valueOf(currentdate.getMonth());
        currentMonth= currentMonth.toLowerCase();
        currentMonth = currentMonth.substring(0,1).toUpperCase() + currentMonth.substring(1);

        model.addAttribute("month", currentMonth);
        model.addAttribute("yearNow", getYear(0));
        model.addAttribute("yearLast", getYear(-1));
        model.addAttribute("yearNext", getYear(1));

        model.addAttribute("carRevenueList", carRevenueEachMonth(0));
        model.addAttribute("carRevenueListLastYear", carRevenueEachMonth(-1));
        model.addAttribute("carRevenueNextYear", carRevenueEachMonth(+1));

        model.addAttribute("userList", userService.getAllEmployees());
        return "sales";
    }


    public int getYear(int month){
        int year = Integer.parseInt(String.valueOf(Year.now()));
        return year + month;
    }

}

