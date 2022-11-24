package com.example.bilabonnement.models.cars;

public class Damage {

  private int damageId;
  private int price;
  private String placement;
  private String description;
  private int carId;
  private int rentalAgreementId;

  public Damage() {
  }

  public Damage(int damageId, int price, String placement, String description, int carId, int rentalAgreementId) {
    this.damageId = damageId;
    this.price = price;
    this.placement = placement;
    this.description = description;
    this.carId = carId;
    this.rentalAgreementId = rentalAgreementId;
  }

  public int getDamageId() {
    return damageId;
  }

  public void setDamageId(int damageId) {
    this.damageId = damageId;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public String getPlacement() {
    return placement;
  }

  public void setPlacement(String placement) {
    this.placement = placement;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getCarId() {
    return carId;
  }

  public void setCarId(int carId) {
    this.carId = carId;
  }

  public int getRentalAgreementId() {
    return rentalAgreementId;
  }

  public void setRentalAgreementId(int rentalAgreementId) {
    this.rentalAgreementId = rentalAgreementId;
  }

  @Override
  public String toString() {
    return
        "Damage Id= " + damageId +
        " | Price= " + price +
        " | Placement= " + placement +
        " | Description= " + description +
        " | Car Id= " + carId +
        " | Rental Agreement Id= " + rentalAgreementId;
  }
}
