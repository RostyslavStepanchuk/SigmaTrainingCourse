package com.rstepanchuk.sigmatraining.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class TourDto {

  private Long id;
  private String name;
  private List<CountryDto> countries;
  private Float amount;
  private Integer duration;
  private Instant departureDate;
  private Long agencyId;

}
