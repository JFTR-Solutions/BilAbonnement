package com.example.bilabonnement.models.cars;

import java.io.Serializable;

public class CarAddonsId implements Serializable {

  private int rentalAgreementId;

  private int AddonId;

  public CarAddonsId(int rentalAgreementId, int addonId) {
    this.rentalAgreementId = rentalAgreementId;
    AddonId = addonId;
  }
}
