package com.example.bilabonnement.models.rentalagreements;

public class MthKm {

  private int kmId;
  private int km;
  private int price;

  public MthKm() {
  }

  public MthKm(int kmId, int km, int price) {
    this.kmId = kmId;
    this.km = km;
    this.price = price;
  }

  public int getKmId() {
    return kmId;
  }

  public void setKmId(int kmId) {
    this.kmId = kmId;
  }

  public int getKm() {
    return km;
  }

  public void setKm(int km) {
    this.km = km;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  @Override
  public String toString() {
    return "Km Id= " + kmId +
        " | Km= " + km +
        " | Price= " + price;
  }
}
