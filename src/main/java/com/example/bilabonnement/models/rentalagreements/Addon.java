package com.example.bilabonnement.models.rentalagreements;

public class Addon {


  private int addonId;
  private String addonName;
  private String addonShortDesc;
  private String addonDesc;
  private double price;

  public Addon() {
  }

  public Addon(int addonId, String addonName, String addonShortDesc, String addonDesc, double price) {
    this.addonId = addonId;
    this.addonName = addonName;
    this.addonShortDesc = addonShortDesc;
    this.addonDesc = addonDesc;
    this.price = price;
  }

  public String getAddonShortDesc() { return addonShortDesc; }

  public void setAddonShortDesc(String addonShortDesc) { this.addonShortDesc = addonShortDesc; }

  public void setPrice(double price) { this.price = price; }

  public int getAddonId() {
    return addonId;
  }

  public void setAddonId(int addonId) {
    this.addonId = addonId;
  }

  public String getAddonName() {
    return addonName;
  }

  public void setAddonName(String addonName) {
    this.addonName = addonName;
  }

  public String getAddonDesc() {
    return addonDesc;
  }

  public void setAddonDesc(String addonDesc) {
    this.addonDesc = addonDesc;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  @Override
  public String toString() {
    return
        "Addon Id= " + addonId +
        " | Addon Name= " + addonName +
        " | Addon Desc= " + addonDesc +
        " | Price= " + price;
  }
}
