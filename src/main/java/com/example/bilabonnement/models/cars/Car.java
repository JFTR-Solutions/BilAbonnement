package com.example.bilabonnement.models.cars;


public class Car {

  private int carId;
  private byte available;
  private String colour;
  private String VIN;
  private String regNumber;
  private double steelPrice;
  private double  mthPrice;
  private String transmission;
  private int modelId;
  private Model carModel;

  public Car() {
  }

  public Car(int carId, byte available, String colour, String VIN, String regNumber, double steelPrice,
             double mthPrice, String transmission, int modelId) {
    this.carId = carId;
    this.available = available;
    this.colour = colour;
    this.VIN = VIN;
    this.regNumber = regNumber;
    this.steelPrice = steelPrice;
    this.mthPrice = mthPrice;
    this.transmission = transmission;
    this.modelId = modelId;
  }

  public Car(int carId, byte available, String colour, String VIN, String regNumber, double steelPrice,
             double mthPrice, String transmission, int modelId, Model carModel) {
    this.carId = carId;
    this.available = available;
    this.colour = colour;
    this.VIN = VIN;
    this.regNumber = regNumber;
    this.steelPrice = steelPrice;
    this.mthPrice = mthPrice;
    this.transmission = transmission;
    this.modelId = modelId;
    this.carModel = carModel;
  }

  public Car(int carId, String colour, String VIN, String regNumber, double steelPrice, double mthPrice,
             String transmission, int modelId, Model carModel) {
    this.carId = carId;
    this.colour = colour;
    this.VIN = VIN;
    this.regNumber = regNumber;
    this.steelPrice = steelPrice;
    this.mthPrice = mthPrice;
    this.transmission = transmission;
    this.modelId = modelId;
    this.carModel = carModel;
  }

  public int getCarId() {
    return carId;
  }

  public void setCarId(int carId) {
    this.carId = carId;
  }

  public byte isAvailable() {
    return available;
  }

  public void setAvailable(byte available) {
    this.available = available;
  }

  public String getColour() {
    return colour;
  }

  public void setColour(String colour) {
    this.colour = colour;
  }

  public String getVIN() {
    return VIN;
  }

  public void setVIN(String VIN) {
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

  public Model getCarModel() {
    return carModel;
  }

  public void setCarModel(Model carModel) {
    this.carModel = carModel;
  }

  @Override
  public String toString() {
    return
        "Car id= " + carId +
        " | Available= " + available +
        " | Colour= " + colour +
        " | VIN= " + VIN +
        " | Reg Number= " + regNumber +
        " | Steel Price= " + steelPrice +
        " | Monthly Price= " + mthPrice +
        " | Transmission= " + transmission +
        " | Model Id= " + modelId;
  }
}
