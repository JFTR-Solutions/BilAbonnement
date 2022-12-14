package com.example.bilabonnement.controllers;

import com.example.bilabonnement.exceptions.CarLeasingException;

import com.example.bilabonnement.service.CarService;
import com.example.bilabonnement.service.MechanicService;
import com.example.bilabonnement.service.RentalService;
import com.example.bilabonnement.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;


@Controller
public class MechanicController {

    private final String ROLE = "Skade og udbedring";

    private LoginController loginController;
    private UserService userService;
    private MechanicService mechanicService;
    private RentalService rentalService;
    private CarService carService;


    public MechanicController(LoginController loginController, UserService userService, MechanicService mechanicService,
                              RentalService rentalService, CarService carService) {
        this.loginController = loginController;
        this.userService = userService;
        this.mechanicService = mechanicService;
        this.rentalService = rentalService;
        this.carService = carService;
    }

    @GetMapping("/mechanic")
    public String frontdeskPage(Model model, HttpSession httpSession) throws CarLeasingException {
        try {
            model.addAttribute("roles", loginController.validateRoles(httpSession));
            if (!loginController.validateLogin(httpSession, ROLE)) {
                return "redirect:/";
            }


        } catch (CarLeasingException e) {
            httpSession.setAttribute("error", e.getMessage());
            return "redirect:/welcome";
        }

        model.addAttribute("agreements", rentalService.fetchAllRentalAgreements());
        model.addAttribute("carlist", carService.fetchAllCars());
        model.addAttribute("userlist", userService.getAllEmployees());

        return "mechanic";
    }

    @GetMapping("/create-damagereport/{carid}/{rentalagreementid}")
    public String showDamageReport(@PathVariable("carid") int carId, @PathVariable("rentalagreementid") int rentalId,
                                   HttpSession httpSession, Model model) throws CarLeasingException {
        if (!loginController.validateLogin(httpSession, ROLE)) {
            return "redirect:/";
        }
        model.addAttribute("car", carService.findCarById(carId));
        model.addAttribute("rentalagreementid", rentalId);
        model.addAttribute("damages", mechanicService.fetchAllDamagesForRentalId(rentalId));
        model.addAttribute("totalsumdamages", mechanicService.fetchTotalSumDamages(rentalId));
        model.addAttribute("totalnumdamages", mechanicService.fetchTotalNumDamages(rentalId));
        return "createdamagereport";
    }


    @PostMapping("/create-damagereport")
    public String createDamageReport(@RequestParam("price") int price, @RequestParam("placement") String placement,
                                     @RequestParam("description") String description, @RequestParam("carId") int carId,
                                     @RequestParam("rentalAgreementId") int rentalAgreementId) throws CarLeasingException {
        mechanicService.createDamageReport(price, placement, description, carId, rentalAgreementId);

        return "redirect:/create-damagereport/" + carId + "/" + rentalAgreementId;
    }

    @GetMapping("/delete-damage/{id}/{carid}/{rentalagreementid}")
    public String deleteDamage(@PathVariable("id") int damageId, @PathVariable("carid") int carId,
                               @PathVariable("rentalagreementid") int rentalAgreementId, HttpSession httpSession)
            throws CarLeasingException {
        if (!loginController.validateLogin(httpSession, ROLE)) {
            return "redirect:/";
        }
        mechanicService.deleteDamageId(damageId);

        return "redirect:/create-damagereport/" + carId + "/" + rentalAgreementId;
    }

    @GetMapping("/end-rental/{rentalagreementid}")
    public String endRental(@PathVariable("rentalagreementid") int rentalAgreementId,
                            HttpSession httpSession) throws CarLeasingException {
        if (!loginController.validateLogin(httpSession, ROLE)) {
            return "redirect:/";
        }
        rentalService.endRental(rentalAgreementId);

        return "redirect:/mechanic";
    }

    //Jonathan
    @GetMapping("/reopen-rentalagreement/{carid}/{rentalagreementid}")
    public String showRentalAgreementNotActive(@PathVariable("carid") int carId, @PathVariable("rentalagreementid") int rentalId,
                                               HttpSession httpSession, Model model) throws CarLeasingException {
        if (!loginController.validateLogin(httpSession, ROLE)) {
            return "redirect:/";
        }
        model.addAttribute("car", carService.findCarById(carId));
        model.addAttribute("rentalagreementid", rentalId);
        model.addAttribute("damages", mechanicService.fetchAllDamagesForRentalId(rentalId));
        model.addAttribute("totalsumdamages", mechanicService.fetchTotalSumDamages(rentalId));
        model.addAttribute("totalnumdamages", mechanicService.fetchTotalNumDamages(rentalId));
        return "reopenrentalagreement";
    }

    //Jonathan
    @GetMapping("/reopen-agreement/{carid}/{rentalagreementid}")
    public String reopenAgreement(@PathVariable("carid") int carId, @PathVariable("rentalagreementid") int rentalAgreementId,
                                  HttpSession httpSession) throws CarLeasingException {
        if (!loginController.validateLogin(httpSession, ROLE)) {
            return "redirect:/";
        }
        rentalService.reopenRentalAgreement(rentalAgreementId);

        return "redirect:/create-damagereport/" + carId + "/" + rentalAgreementId;
    }

}
