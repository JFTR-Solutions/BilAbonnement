package com.example.bilabonnement.models.cars;

public class Model {

  private int modelId;
  private String modelName;
  private String manufacturer;
  private double co2Emission;
  private String fuelType;

  private double range;

  public Model() {
  }

  public Model(int modelId, String modelName, String manufacturer, double co2Emission, String fuelType, double range) {
    this.modelId = modelId;
    this.modelName = modelName;
    this.manufacturer = manufacturer;
    this.co2Emission = co2Emission;
    this.fuelType = fuelType;
    this.range = range;
  }

  public Model(int modelId, String modelName, String manufacturer){
    this.modelName = modelName;
    this.manufacturer = manufacturer;
    this.modelId = modelId;
  }

  public int getModelId() {
    return modelId;
  }

  public void setModelId(int modelId) {
    this.modelId = modelId;
  }

  public String getModelName() {
    return modelName;
  }

  public void setModelName(String modelName) {
    this.modelName = modelName;
  }

  public String getManufacturer() {
    return manufacturer;
  }

  public void setManufacturer(String manufacturer) {
    this.manufacturer = manufacturer;
  }

  public double getCo2Emission() {
    return co2Emission;
  }

  public void setCo2Emission(double co2Emission) {
    this.co2Emission = co2Emission;
  }

  public String getFuelType() {
    return fuelType;
  }

  public void setFuelType(String fuelType) {
    this.fuelType = fuelType;
  }

  public double getRange() {
    return range;
  }

  public void setRange(double range) {
    this.range = range;
  }

  @Override
  public String toString() {
    return "Model Id= " + modelId +
        " | Model Name= " + modelName +
        " | Manufacturer= " + manufacturer +
        " | CO2 Emission= " + co2Emission +
        " | fuelType= " + fuelType +
        " | range= " + range;
  }
}
