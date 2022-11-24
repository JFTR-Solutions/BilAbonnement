package com.example.bilabonnement.models.cars;


import java.io.Serializable;


public class CarAddons{

  private int rentalAgreementId;

  private int AddonId;

  public CarAddons() {
  }

  public CarAddons(int rentalAgreementId, int addonId) {
    this.rentalAgreementId = rentalAgreementId;
    AddonId = addonId;
  }

  public int getRentalAgreementId() {
    return rentalAgreementId;
  }

  public void setRentalAgreementId(int rentalAgreementId) {
    this.rentalAgreementId = rentalAgreementId;
  }

  public int getAddonId() {
    return AddonId;
  }

  public void setAddonId(int addonId) {
    AddonId = addonId;
  }

  @Override
  public String toString() {
    return "RentalAgreementId= " + rentalAgreementId +
        " | AddonId=" + AddonId;
  }
}
