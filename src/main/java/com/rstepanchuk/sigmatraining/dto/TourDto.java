package com.rstepanchuk.sigmatraining.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class TourDto {

  private Long id;
  private String name;
  private List<String> countries;
  private Float amount;
  private Integer duration;
  private Instant departureDate;
}
