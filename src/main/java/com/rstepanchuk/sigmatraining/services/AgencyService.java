package com.rstepanchuk.sigmatraining.services;

import com.rstepanchuk.sigmatraining.domain.Agency;
import com.rstepanchuk.sigmatraining.domain.Transport;
import com.rstepanchuk.sigmatraining.dto.AgencyDto;
import com.rstepanchuk.sigmatraining.dto.DtoEntityConverter;
import com.rstepanchuk.sigmatraining.dto.TransportDto;
import com.rstepanchuk.sigmatraining.exceptions.ApplicationException;
import com.rstepanchuk.sigmatraining.exceptions.NotFoundException;
import com.rstepanchuk.sigmatraining.repositories.AgencyRepository;
import com.rstepanchuk.sigmatraining.repositories.TransportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class AgencyService {

  private final AgencyRepository agencyRepository;
  private final TransportRepository transportRepository;
  private final TourService tourService;
  private final DtoEntityConverter converter;

  @Transactional
  public AgencyDto create(AgencyDto input) {
    Agency agency = converter.toEntity(input);
    Optional<Agency> newAgency = agencyRepository.create(agency);
    newAgency.ifPresent(entity-> {
      transportRepository.connectToParent(agency.getTourTransport(), entity.getId());
      entity.setTourTransport(agency.getTourTransport());
    });
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
    agency.setTourTransport(transportRepository.getAllByParentId(id));
    AgencyDto result = converter.toDto(agency);
    result.setTours(tourService.getAllByAgencyId(id));
    return result;
  }

  public List<AgencyDto> getAll() {
    return collectFullDtoData(agencyRepository.getAll());
  }

  public List<AgencyDto> getBySearchAcrossAllFields(String searchInput) {
    List<Agency> agencies = agencyRepository.searchAcrossAllFields(searchInput);
    return collectFullDtoData(
        agencies,
        transportRepository.getForParentListGroupedByParentId(agencies
            .stream()
            .map(Agency::getId)
            .collect(Collectors.toList()))
        );
  }

  public List<AgencyDto> getBySearchAcrossAllFields(String searchInput, int pageNumber, int pageSize) {
    List<Agency> agencies = agencyRepository.searchAcrossAllFields(searchInput, pageNumber, pageSize);
    return collectFullDtoData(
        agencies,
        transportRepository.getForParentListGroupedByParentId(agencies
            .stream()
            .map(Agency::getId)
            .collect(Collectors.toList()))
    );
  }

  private List<AgencyDto> collectFullDtoData(List<Agency> agencies, Map<Long, List<Transport>> transportByAgency) {
    return agencies
        .stream()
        .map(converter::toDto)
        .peek(agencyDto -> {
          agencyDto.setTours(tourService.getAllByAgencyId(agencyDto.getId())); // add Tours
          List<TransportDto> transport = transportByAgency.getOrDefault(agencyDto.getId(), new ArrayList<>())
              .stream()
              .map(converter::toDto)
              .collect(Collectors.toList());
          agencyDto.setTourTransport(transport); // add Transports
        })
        .collect(Collectors.toList());
  }

  private List<AgencyDto> collectFullDtoData(List<Agency> agencies) {
    Map<Long, List<Transport>> transportByAgency = transportRepository.getAllGroupedByParentId();
    return collectFullDtoData(agencies, transportByAgency);
  }

}
