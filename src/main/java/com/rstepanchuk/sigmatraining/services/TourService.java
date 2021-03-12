package com.rstepanchuk.sigmatraining.services;

import com.rstepanchuk.sigmatraining.domain.Tour;
import com.rstepanchuk.sigmatraining.dto.DtoEntityConverter;
import com.rstepanchuk.sigmatraining.dto.TourDto;
import com.rstepanchuk.sigmatraining.exceptions.ApplicationException;
import com.rstepanchuk.sigmatraining.exceptions.NotFoundException;
import com.rstepanchuk.sigmatraining.repositories.TourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class TourService {

  private final TourRepository tourRepository;
  private final DtoEntityConverter converter;

  public TourDto create (TourDto input) {
    Tour tour = converter.toEntity(input);
    Optional<Tour> newTour = tourRepository.create(tour);
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
    return converter.toDto(tourRepository.getById(id)
        .orElseThrow(()->new NotFoundException("Tour was not found")));
  }

  public List<TourDto> getAll() {
    return tourRepository.getAll()
        .stream()
        .map(converter::toDto)
        .collect(Collectors.toList());
  }

  public List<TourDto> getAllByAgencyId(Long id) {
    return tourRepository.getAllByAgencyId(id)
        .stream()
        .map(converter::toDto)
        .collect(Collectors.toList());
  }
}
