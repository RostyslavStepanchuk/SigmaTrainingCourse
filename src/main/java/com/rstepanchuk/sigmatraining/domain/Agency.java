package com.rstepanchuk.sigmatraining.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Agency {

  private Long id;
  private String name;
  private String phone;
  private String address;
  private List<String> tourTransport;
  private Integer yearsInBusiness;
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

  public Integer getYearsInBusiness() {
    return yearsInBusiness;
  }

  public void setYearsInBusiness(Integer yearsInBusiness) {
    this.yearsInBusiness = yearsInBusiness;
  }

  public List<Tour> getTours() {
    return tours;
  }

  public void setTours(List<Tour> tours) {
    this.tours = tours;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Agency agency = (Agency) o;
    return Objects.equals(id, agency.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Agency{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", phone='" + phone + '\'' +
        ", address='" + address + '\'' +
        ", tourTransport=" + tourTransport +
        ", yearsInBusiness=" + yearsInBusiness +
        ", tours=" + tours +
        "}";
  }
}
