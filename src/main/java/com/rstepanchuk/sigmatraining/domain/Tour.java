package com.rstepanchuk.sigmatraining.domain;

import java.time.Instant;
import java.util.List;

public class Tour {

  private String name;
  private List<String> countries;
  private Float amount;
  private Integer duration;
  private Instant departureDate;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getCountries() {
    return countries;
  }

  public void setCountries(List<String> countries) {
    this.countries = countries;
  }

  public Float getAmount() {
    return amount;
  }

  public void setAmount(Float amount) {
    this.amount = amount;
  }

  public Integer getDuration() {
    return duration;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }

  public Instant getDepartureDate() {
    return departureDate;
  }

  public void setDepartureDate(Instant departureDate) {
    this.departureDate = departureDate;
  }
}
