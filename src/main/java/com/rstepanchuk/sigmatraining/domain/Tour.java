package com.rstepanchuk.sigmatraining.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Data
public class Tour {

  private Long id;
  private String name;
  private List<String> countries;
  private Float amount;
  private Integer duration;
  private Instant departureDate;
  private Long agencyId;
}
