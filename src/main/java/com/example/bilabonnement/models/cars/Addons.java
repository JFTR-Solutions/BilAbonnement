package com.example.bilabonnement.models.cars;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "addons")
public class Addons {

  @Id
  private int addonId;
  private String addonName;
  private String addonDesc;
  private int price;

  public Addons() {
  }

  public Addons(int addonId, String addonName, String addonDesc, int price) {
    this.addonId = addonId;
    this.addonName = addonName;
    this.addonDesc = addonDesc;
    this.price = price;
  }

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

  public int getPrice() {
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
