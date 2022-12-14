package com.example.bilabonnement.service;

import com.example.bilabonnement.exceptions.CarLeasingException;
import com.example.bilabonnement.models.cars.Damage;
import com.example.bilabonnement.repository.MechanicRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MechanicService {

    MechanicRepository mechanicRepository;

    public MechanicService(MechanicRepository mechanicRepository) throws CarLeasingException {
        this.mechanicRepository = mechanicRepository;
    }

    public void createDamageReport(int price, String placement, String description, int carId, int rentalAgreementId)
            throws CarLeasingException{
        mechanicRepository.createDamageReport(price, placement, description, carId, rentalAgreementId);
    }

    public List<Damage> fetchAllDamagesForRentalId(int rentalId) throws CarLeasingException {
        return mechanicRepository.fetchAllDamagesForRentalId(rentalId);
    }

    public void deleteDamageId(int damageId) throws CarLeasingException{
        mechanicRepository.deleteDamageId(damageId);
    }

    public double fetchTotalSumDamages(int rentalId) throws CarLeasingException {
        return mechanicRepository.fetchTotalSumDamages(rentalId);
    }

    public int fetchTotalNumDamages(int rentalId) throws CarLeasingException {
        return mechanicRepository.fetchTotalNumDamages(rentalId);
    }
}
