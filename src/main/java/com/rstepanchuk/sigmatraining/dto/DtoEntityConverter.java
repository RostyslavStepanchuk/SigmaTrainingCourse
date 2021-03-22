package com.rstepanchuk.sigmatraining.dto;

import com.rstepanchuk.sigmatraining.domain.Agency;
import com.rstepanchuk.sigmatraining.domain.Country;
import com.rstepanchuk.sigmatraining.domain.Tour;
import com.rstepanchuk.sigmatraining.domain.Transport;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor()
public class DtoEntityConverter {

  private final ModelMapper modelMapper;

  public Agency toEntity(AgencyDto dto) {
    Agency agency = map(dto, Agency.class);
    agency.setTours(mapList(dto.getTours(), Tour.class));
    return agency;
  }

  public AgencyDto toDto(Agency entity) {
    AgencyDto dto = map(entity, AgencyDto.class);
    dto.setTours(mapList(entity.getTours(), TourDto.class));
    return dto;
  }

  public Tour toEntity(TourDto dto) {
    return map(dto, Tour.class);
  }

  public TourDto toDto(Tour entity) {
    return map(entity, TourDto.class);
  }

  private <E, T> T map(E source, Class<T> target) {
    if (source == null || target == null) {
      return null;
    }
    return modelMapper.map(source, target);
  }

  private <S, T> List<T> mapList(List<S> source, Class<T> target) {
    if (source == null || target == null || source.isEmpty()) {
      return new ArrayList<>();
    }
    return source
        .stream()
        .map(element -> modelMapper.map(element, target))
        .collect(Collectors.toList());
  }

  public Country toEntity(CountryDto dto) {
    return map(dto, Country.class);
  }

  public CountryDto toDto(Country entity) {
    return map(entity, CountryDto.class);
  }

  public Transport toEntity(TransportDto dto) {
    return map(dto, Transport.class);
  }

  public TransportDto toDto(Transport entity) {
    return map(entity, TransportDto.class);
  }

}
