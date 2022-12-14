package com.example.bilabonnement.service;

import com.example.bilabonnement.exceptions.CarLeasingException;
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

    public void addRentalAgreement(int carId, int userId, int mthKmId, Date endDate, Date startDate, double mthPrice)
            throws CarLeasingException{
        rentalRepository.addRentalAgreement(carId, userId, mthKmId, endDate, startDate, mthPrice);
    }
    public int findRentalAgreementIdByCarId(int carId) throws CarLeasingException {
        return rentalRepository.findRentalAgreementIdByCarId(carId);
    }

    public void addCarAddon(int rentalId, int addonId) throws CarLeasingException {
        rentalRepository.addCarAddon(rentalId, addonId);}

    public List<Addon> fetchAllAddons() throws CarLeasingException {return rentalRepository.fetchAllAddons();}

    public MthKm findmthKmById(int id) throws CarLeasingException { return rentalRepository.findMthKmById(id); }

    public List<MthKm> fetchAllMthKm() throws CarLeasingException{ return rentalRepository.fetchAllMthKm(); }

    public List<RentalAgreement> fetchAllRentalAgreements() throws CarLeasingException {
        return rentalRepository.fetchAllRentalAgreements(); }

    public RentalAgreement findRentalAgreementById(int id) throws CarLeasingException{
        return rentalRepository.findRentalAgreementById(id); }

    public List<Addon> findCarAddonsByRentalId(int rentalId) throws CarLeasingException {
        return rentalRepository.findCarAddonsByRentalId(rentalId); }

    public void endRental(int rentalAgreementId) throws CarLeasingException{
        rentalRepository.endRental(rentalAgreementId); }

  public void reopenRentalAgreement(int rentalAgreementId) throws CarLeasingException {
        rentalRepository.reopenRentalAgreement(rentalAgreementId);
  }
}
