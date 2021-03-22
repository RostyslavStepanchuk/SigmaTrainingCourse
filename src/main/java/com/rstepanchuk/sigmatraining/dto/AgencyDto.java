package com.rstepanchuk.sigmatraining.dto;

import lombok.Data;

import java.util.List;

@Data
public class AgencyDto {

  private Long id;
  private String name;
  private String phone;
  private String address;
  private List<TransportDto> tourTransport;
  private Integer yearsInBusiness;
  private List<TourDto> tours;
}
