package com.example.bilabonnement.models.cars;

public class Feature {

  private int featureId;
  private String description;

  public Feature() {
  }

  public Feature(int featureId, String description) {
    this.featureId = featureId;
    this.description = description;
  }

  public int getFeatureId() {
    return featureId;
  }

  public void setFeatureId(int featureId) {
    this.featureId = featureId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "Feature Id= " + featureId +
        " | Description= " + description;
  }
}
