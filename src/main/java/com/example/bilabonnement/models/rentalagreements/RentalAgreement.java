package com.example.bilabonnement.models.rentalagreements;

import com.example.bilabonnement.models.cars.Car;
import com.example.bilabonnement.models.cars.Damage;
import com.example.bilabonnement.models.cars.Model;
import com.example.bilabonnement.models.users.User;

import java.sql.Date;

public class RentalAgreement {

  private int rentalId;
  private Date endDate;
  private Date startDate;
  private double mthPrice;
  private int carId;
  private int mthKmId;
  private int userId;

  private byte active;

  private int modelId;

  private MthKm mthKm;

  private Car car;

  private Model model;

  private User user;

  private Damage damage;


  public RentalAgreement() {
  }

  public RentalAgreement(int rentalId, Date endDate, Date startDate, double mthPrice, int carId, int mthKmId, int userId) {
    this.rentalId = rentalId;
    this.endDate = endDate;
    this.startDate = startDate;
    this.mthPrice = mthPrice;
    this.carId = carId;
    this.mthKmId = mthKmId;
    this.userId = userId;
  }

  public RentalAgreement(int rentalId, Date endDate, Date startDate, double mthPrice, int carId, int mthKmId, int userId,
                         int modelId, MthKm mthKm, Car car, Model model, User user, byte active) {
    this.rentalId = rentalId;
    this.endDate = endDate;
    this.startDate = startDate;
    this.mthPrice = mthPrice;
    this.carId = carId;
    this.mthKmId = mthKmId;
    this.userId = userId;
    this.modelId = modelId;
    this.mthKm = mthKm;
    this.car = car;
    this.model = model;
    this.user = user;
    this.active = active;
  }

  public Car getCar() {
    return car;
  }

  public Model getModel() {
    return model;
  }

  public User getUser() {
    return user;
  }

  public Damage getDamage() {
    return damage;
  }

  public int getRentalId() {
    return rentalId;
  }

  public void setRentalId(int rentalId) {
    this.rentalId = rentalId;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public double getMthPrice() {
    return mthPrice;
  }

  public void setMthPrice(double mthPrice) {
    this.mthPrice = mthPrice;
  }

  public int getCarId() {
    return carId;
  }

  public void setCarId(int carId) {
    this.carId = carId;
  }

  public int getMthKmId() {
    return mthKmId;
  }

  public void setMthKmId(int mthKmId) {
    this.mthKmId = mthKmId;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public byte getActive() { return active;}

  public int getModelId() { return modelId;}

  public MthKm getMthKm() { return mthKm;}

  @Override
  public String toString() {
    return "Rental Id= " + rentalId +
        " | End Date= " + endDate +
        " | Start Date= " + startDate +
        " | Mth Price= " + mthPrice +
        " | Car Id= " + carId +
        " | Mth Km Id= " + mthKmId +
        " | User Id= " + userId;
  }
}
