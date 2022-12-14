package com.example.bilabonnement.controllers;

import com.example.bilabonnement.exceptions.CarLeasingException;
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

    private final String ROLE = "Forretningsudvikler";
    private LoginController loginController;
    private UserService userService;
    private RentalService rentalService;
    private CarService carService;

    public SalesController(LoginController loginController, UserService userService, RentalService rentalService, CarService carService) {
        this.loginController = loginController;
        this.userService = userService;
        this.rentalService = rentalService;
        this.carService = carService;
    }

    /*Thomas & Frederik*/
    public List<Double> carRevenueEachMonth(int year) throws CarLeasingException {

        /*This method is used to get the carRevenue for each month
        * We start by creating a list for the revenue and then we fetch a list of all rental agreements.
        * */
        List<Double> carRevenueList = new ArrayList<>();
        List<RentalAgreement> rentalList = rentalService.fetchAllRentalAgreements();

        /*Next up we loop through all months (jan-dec) in order to get a number for each month.
        * Also we create a double variable to store the value. We also create a YearMonth object, where we take the current year and add int year to get a different year. The month is then set by i.
        * */
        for (int i = 1; i <= 12; i++) {
            double monthRevenue = 0;
            YearMonth activeYearMonth = YearMonth.of(YearMonth.now().getYear() + year, i);
            /*Next is creating a loop of the rental list, where we get the year & month by using substring to remove the date so that we can parse it to a YearDate object.*/
            for (RentalAgreement rentalAgreement : rentalList) {
                String startDate = rentalAgreement.getStartDate().toString().substring(0, 7);
                String endDate = rentalAgreement.getEndDate().toString().substring(0, 7);

                YearMonth startYearMonth = YearMonth.parse(startDate);
                YearMonth endYearMonth = YearMonth.parse(endDate);

                /*We then check if the start year & month is before OR equal the active yearmonth we created earlier AND if it is after the active yearMonth.
                We use methods from the YearMonth class to check this.
                If true, we add the monthrevenue from the rental agreement to the double we created and afterwards add this value to the list we created.

                */
                if ((startYearMonth.isBefore(activeYearMonth) || startYearMonth.equals(activeYearMonth))
                        && endYearMonth.isAfter(activeYearMonth)) {
                    monthRevenue += rentalAgreement.getMthPrice();
                }
            }
            carRevenueList.add(monthRevenue);
        }

        /*Finally we wish to create a total value to display on the table which sums all revenue. As we are now outside the nested loop,
        we can simply create a new variable call total and then loop through the revenuelist and add the value to total.
        At last we add the total to the carrevenue list which is then placed at the end of our list which we then return to the method that called it*/
        double total = 0;

        for (double d : carRevenueList) {
            total += d;
        }
        carRevenueList.add(total);
        return carRevenueList;
    }


    /*Thomas & Frederik*/
    public List<Integer> carsRentedOutEachMonth(int year) throws CarLeasingException {
        List<Integer> carRevenueList = new ArrayList<>();
        List<RentalAgreement> rentalList = rentalService.fetchAllRentalAgreements();
        for (int i = 1; i <= 12; i++) {
            int monthRented = 0;
            YearMonth activeYearMonth = YearMonth.of(YearMonth.now().getYear() + year, i);
            for (RentalAgreement rentalAgreement : rentalList) {
                String startDate = rentalAgreement.getStartDate().toString().substring(0, 7);
                String endDate = rentalAgreement.getEndDate().toString().substring(0, 7);

                YearMonth startYearMonth = YearMonth.parse(startDate);
                YearMonth endYearMonth = YearMonth.parse(endDate);

                if ((startYearMonth.isBefore(activeYearMonth) || startYearMonth.equals(activeYearMonth)) && endYearMonth.isAfter(activeYearMonth)) {
                    monthRented += rentalAgreement.getActive();
                }
            }
            carRevenueList.add(monthRented);
        }

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
            if (!loginController.validateLogin(httpSession, ROLE)) {
                return "redirect:/";
            }


            java.util.Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int month = cal.get(Calendar.MONTH);

            model.addAttribute("carsRented", carsRentedOutEachMonth(0).get(month));
            model.addAttribute("carsAvailable", carsAvailable());
            model.addAttribute("rentalRevenue", carRevenueEachMonth(0).get(month));

            LocalDate currentdate = LocalDate.now();
            String currentMonth = String.valueOf(currentdate.getMonth());
            currentMonth = currentMonth.toLowerCase();
            currentMonth = currentMonth.substring(0, 1).toUpperCase() + currentMonth.substring(1);

            model.addAttribute("month", currentMonth);
            model.addAttribute("yearLast", getYear(-1));
            model.addAttribute("yearNow", getYear(0));
            model.addAttribute("yearNext", getYear(1));

            model.addAttribute("carRevenueListLastYear", carRevenueEachMonth(-1));
            model.addAttribute("carRevenueListThisYear", carRevenueEachMonth(0));
            model.addAttribute("carRevenueNextYear", carRevenueEachMonth(+1));

            model.addAttribute("carsRentedOutLastYear", carsRentedOutEachMonth(-1));
            model.addAttribute("carsRentedOutThisYear", carsRentedOutEachMonth(0));
            model.addAttribute("carsRentedOutNextYear", carsRentedOutEachMonth(+1));

            model.addAttribute("userList", userService.getAllEmployees());
            return "sales";
        } catch (CarLeasingException e) {
            httpSession.setAttribute("error", e.getMessage());
            return "redirect:/";
        }

    }


    public int getYear(int month) {
        int year = Integer.parseInt(String.valueOf(Year.now()));
        return year + month;
    }

}

