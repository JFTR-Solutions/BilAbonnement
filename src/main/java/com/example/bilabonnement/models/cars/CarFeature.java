package com.example.bilabonnement.models.cars;

public class CarFeature {

  private int carId;
  private int featureId;

  public CarFeature() {
  }

  public CarFeature(int carId, int featureId) {
    this.carId = carId;
    this.featureId = featureId;
  }

  public int getCarId() {
    return carId;
  }

  public void setCarId(int carId) {
    this.carId = carId;
  }

  public int getFeatureId() {
    return featureId;
  }

  public void setFeatureId(int featureId) {
    this.featureId = featureId;
  }

  @Override
  public String toString() {
    return "Car Id= " + carId +
        " | Feature Id= " + featureId;
  }
}
