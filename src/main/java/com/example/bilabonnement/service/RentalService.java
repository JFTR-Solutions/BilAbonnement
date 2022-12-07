package com.example.bilabonnement.service;

import com.example.bilabonnement.models.rentalagreements.Addon;
import com.example.bilabonnement.models.rentalagreements.MthKm;
import com.example.bilabonnement.models.rentalagreements.RentalAgreement;
import com.example.bilabonnement.repository.RentalRepository;
import org.springframework.beans.factory.BeanCurrentlyInCreationException;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class RentalService {

    RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {this.rentalRepository = rentalRepository;}

    public void addRentalAgreement(int carId, int userId, int mthKmId, Date endDate, Date startDate, double mthPrice) {
        rentalRepository.addRentalAgreement(carId, userId, mthKmId, endDate, startDate, mthPrice);
    }
    public int findRentalAgreementIdByCarId(int carId) {
        return rentalRepository.findRentalAgreementIdByCarId(carId);
    }

    public void addCarAddon(int rentalId, int addonId) { rentalRepository.addCarAddon(rentalId, addonId);}

    public List<Addon> fetchAllAddons() {return rentalRepository.fetchAllAddons();}

    public MthKm findmthKmById(int id) { return rentalRepository.findMthKmById(id); }

    public List<MthKm> fetchAllMthKm() { return rentalRepository.fetchAllMthKm(); }

    public List<RentalAgreement> fetchAllRentalAgreements() { return rentalRepository.fetchAllRentalAgreements(); }

    public RentalAgreement findRentalAgreementById(int id){ return rentalRepository.findRentalAgreementById(id); }

    public List<Addon> findCarAddonsByRentalId(int rentalId) { return rentalRepository.findCarAddonsByRentalId(rentalId); }
}
