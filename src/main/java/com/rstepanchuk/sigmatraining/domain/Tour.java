package com.rstepanchuk.sigmatraining.domain;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class Tour {

  private Long id;
  private String name;
  private List<String> countries;
  private Float amount;
  private Integer duration;
  private Instant departureDate;
  private Long agencyId;

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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getAgencyId() {
    return agencyId;
  }

  public void setAgencyId(Long agencyId) {
    this.agencyId = agencyId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Tour tour = (Tour) o;
    return Objects.equals(id, tour.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Tour{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", countries=" + countries +
        ", amount=" + amount +
        ", duration=" + duration +
        ", departureDate=" + departureDate +
        '}';
  }
}
