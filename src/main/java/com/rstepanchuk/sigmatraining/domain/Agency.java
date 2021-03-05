package com.rstepanchuk.sigmatraining.domain;

import java.util.List;

public class Agency {

  private String name;
  private String phone;
  private String address;
  private List<String> tourTransport;
  private String yearsInBusiness;
  private List<Tour> tours;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public List<String> getTourTransport() {
    return tourTransport;
  }

  public void setTourTransport(List<String> tourTransport) {
    this.tourTransport = tourTransport;
  }

  public String getYearsInBusiness() {
    return yearsInBusiness;
  }

  public void setYearsInBusiness(String yearsInBusiness) {
    this.yearsInBusiness = yearsInBusiness;
  }

  public List<Tour> getTours() {
    return tours;
  }

  public void setTours(List<Tour> tours) {
    this.tours = tours;
  }
}
