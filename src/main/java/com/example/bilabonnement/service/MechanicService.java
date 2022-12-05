package com.example.bilabonnement.service;

import com.example.bilabonnement.repository.MechanicRepository;
import org.springframework.stereotype.Service;

@Service
public class MechanicService {

    MechanicRepository mechanicRepository;

    public void createDamageReport(int price, String placement, String description, int carId, int rentalAgreementId){
        mechanicRepository.createDamageReport(price, placement, description, carId, rentalAgreementId);
    }

}
