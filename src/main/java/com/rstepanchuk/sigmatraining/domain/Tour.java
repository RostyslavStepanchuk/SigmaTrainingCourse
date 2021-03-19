package com.rstepanchuk.sigmatraining.domain;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class Tour {

  private Long id;
  private String name;
  private List<Country> countries;
  private Float amount;
  private Integer duration;
  private Instant departureDate;
  private Long agencyId;
}
