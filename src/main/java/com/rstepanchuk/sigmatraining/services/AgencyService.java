package com.rstepanchuk.sigmatraining.services;

import com.rstepanchuk.sigmatraining.domain.Agency;
import com.rstepanchuk.sigmatraining.dto.DtoEntityConverter;
import com.rstepanchuk.sigmatraining.dto.AgencyDto;
import com.rstepanchuk.sigmatraining.exceptions.ApplicationException;
import com.rstepanchuk.sigmatraining.exceptions.NotFoundException;
import com.rstepanchuk.sigmatraining.repositories.AgencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class AgencyService {

  private final AgencyRepository agencyRepository;
  private final TourService tourService;
  private final DtoEntityConverter converter;

  public AgencyDto create(AgencyDto input) {
    Agency agency = converter.toEntity(input);
    Optional<Agency> newAgency = agencyRepository.create(agency);
    return converter.toDto(newAgency.orElseThrow(() -> new ApplicationException("Unable to create new agency")));
  }

  public AgencyDto update(AgencyDto input) {
    Agency agency = converter.toEntity(input);
    Optional<Agency> newAgency = agencyRepository.update(agency);
    return converter.toDto(newAgency.orElseThrow(() -> new ApplicationException("Unable to update agency")));
  }

  public void delete(Long id) {
    agencyRepository.delete(id);
  }

  public AgencyDto getById(Long id) {
    Agency agency = agencyRepository.getById(id)
        .orElseThrow(()->new NotFoundException("Agency was not found"));
    AgencyDto result = converter.toDto(agency);
    result.setTours(tourService.getAllByAgencyId(id));
    return result;
  }

  public List<AgencyDto> getAll() {
    List<Agency> result = agencyRepository.getAll();
    return result
        .stream()
        .map(converter::toDto)
        .peek(agencyDto -> agencyDto.setTours(tourService.getAllByAgencyId(agencyDto.getId())))
        .collect(Collectors.toList());
  }
}
