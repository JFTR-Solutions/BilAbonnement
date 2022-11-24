package com.example.bilabonnement.models.cars;


public class Car {

  private int carId;
  private boolean isAvailable;
  private String colour;
  private int VIN;
  private String regNumber;
  private double steelPrice;
  private double  mthPrice;
  private String transmission;
  private int modelId;

  public Car() {
  }

  public Car(int carId, boolean isAvailable, String colour, int VIN, String regNumber, double steelPrice, double mthPrice, String transmission, int modelId) {
    this.carId = carId;
    this.isAvailable = isAvailable;
    this.colour = colour;
    this.VIN = VIN;
    this.regNumber = regNumber;
    this.steelPrice = steelPrice;
    this.mthPrice = mthPrice;
    this.transmission = transmission;
    this.modelId = modelId;
  }

  public int getCarId() {
    return carId;
  }

  public void setCarId(int carId) {
    this.carId = carId;
  }

  public boolean isAvailable() {
    return isAvailable;
  }

  public void setAvailable(boolean available) {
    isAvailable = available;
  }

  public String getColour() {
    return colour;
  }

  public void setColour(String colour) {
    this.colour = colour;
  }

  public int getVIN() {
    return VIN;
  }

  public void setVIN(int VIN) {
    this.VIN = VIN;
  }

  public String getRegNumber() {
    return regNumber;
  }

  public void setRegNumber(String regNumber) {
    this.regNumber = regNumber;
  }

  public double getSteelPrice() {
    return steelPrice;
  }

  public void setSteelPrice(double steelPrice) {
    this.steelPrice = steelPrice;
  }

  public double getMthPrice() {
    return mthPrice;
  }

  public void setMthPrice(double mthPrice) {
    this.mthPrice = mthPrice;
  }

  public String getTransmission() {
    return transmission;
  }

  public void setTransmission(String transmission) {
    this.transmission = transmission;
  }

  public int getModelId() {
    return modelId;
  }

  public void setModelId(int modelId) {
    this.modelId = modelId;
  }

  @Override
  public String toString() {
    return
        "Car id= " + carId +
        " | Is Available= " + isAvailable +
        " | Colour= " + colour +
        " | VIN= " + VIN +
        " | Reg Number= " + regNumber +
        " | Steel Price= " + steelPrice +
        " | Monthly Price= " + mthPrice +
        " | Transmission= " + transmission +
        " | Model Id= " + modelId;
  }
}
