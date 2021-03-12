package com.rstepanchuk.sigmatraining.domain;

import lombok.Data;

import java.util.List;

@Data
public class Agency {

  private Long id;
  private String name;
  private String phone;
  private String address;
  private List<String> tourTransport;
  private Integer yearsInBusiness;
  private List<Tour> tours;
}
