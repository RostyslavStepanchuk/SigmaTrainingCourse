package com.rstepanchuk.sigmatraining.services;

import com.rstepanchuk.sigmatraining.domain.Country;
import com.rstepanchuk.sigmatraining.domain.Tour;
import com.rstepanchuk.sigmatraining.dto.CountryDto;
import com.rstepanchuk.sigmatraining.dto.DtoEntityConverter;
import com.rstepanchuk.sigmatraining.dto.TourDto;
import com.rstepanchuk.sigmatraining.exceptions.ApplicationException;
import com.rstepanchuk.sigmatraining.exceptions.NotFoundException;
import com.rstepanchuk.sigmatraining.repositories.CountryRepository;
import com.rstepanchuk.sigmatraining.repositories.TourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class TourService {

  private final TourRepository tourRepository;
  private final CountryRepository countryRepository;
  private final DtoEntityConverter converter;

  public TourDto create (TourDto input) {
    Tour tour = converter.toEntity(input);
    Optional<Tour> newTour = tourRepository.create(tour);
    newTour.ifPresent(entity-> {
      countryRepository.connectToParent(tour.getCountries(), entity.getId());
      entity.setCountries(tour.getCountries());
    });
    return converter.toDto(newTour.orElseThrow(() -> new ApplicationException("Unable to create new tour")));
  };

  public TourDto update(TourDto input) {
    Tour Tour = converter.toEntity(input);
    Optional<Tour> newTour = tourRepository.update(Tour);
    return converter.toDto(newTour.orElseThrow(() -> new ApplicationException("Unable to update Tour")));
  }

  public void delete(Long id) {
    tourRepository.delete(id);
  }

  public TourDto getById(Long id) {
    Tour tour = tourRepository.getById(id)
        .orElseThrow(() -> new NotFoundException("Tour was not found"));
    tour.setCountries(countryRepository.getAllByParentId(tour.getId()));
    return converter.toDto(tour);
  }

  public List<TourDto> getAll() {
    return collectFullDtoData(tourRepository.getAll());
  }

  public List<TourDto> getAllByAgencyId(Long id) {
    return collectFullDtoData(tourRepository.getAllByAgencyId(id));
  }

  private List<TourDto> collectFullDtoData(List<Tour> tours) {
    Map<Long, List<Country>> countriesByTour = countryRepository.getAllGroupedByParentId();
    return tours
        .stream()
        .map(converter::toDto)
        .peek(tourDto -> {
          List<CountryDto> countries = countriesByTour.getOrDefault(tourDto.getId(), new ArrayList<>())
              .stream()
              .map(converter::toDto)
              .collect(Collectors.toList());
          tourDto.setCountries(countries);
        })
        .collect(Collectors.toList());
  }
}
