package com.rstepanchuk.sigmatraining.listeners;

import com.rstepanchuk.sigmatraining.domain.Agency;
import com.rstepanchuk.sigmatraining.domain.Tour;
import com.rstepanchuk.sigmatraining.dto.DtoEntityConverter;
import com.rstepanchuk.sigmatraining.dto.AgencyDto;
import com.rstepanchuk.sigmatraining.repositories.AgencyRepository;
import com.rstepanchuk.sigmatraining.repositories.TourRepository;
import com.rstepanchuk.sigmatraining.services.AgencyService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

@Component
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ApplicationReadyListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationReadyListener.class);

  private final AgencyRepository agencyRepository;
  private final TourRepository tourRepository;
  private final AgencyService agencyService;
  private final DtoEntityConverter dtoEntityConverter;

//  @EventListener(ApplicationReadyEvent.class)
  public void applicationReadyHandler(){
    runAgencyCheck();
    runToursCheck();
  }

  @EventListener(ApplicationReadyEvent.class)
  public void dtoChecker(){
    List<AgencyDto> allAgencies = agencyService.getAll(); // get all
    int useless = 0;
  }

  private void runAgencyCheck(){
    StringJoiner agencyReport = new StringJoiner("\n");
    // test entity for creation
    Agency newAgency = new Agency();
    newAgency.setName("New Agency");
    newAgency.setPhone("+380442342343");
    newAgency.setAddress("Some new Address");
    newAgency.setYearsInBusiness(3);
    agencyReport.add("\n************AGENCY REPOSITORY************");
    Optional<Agency> createdAgency = agencyRepository.create(newAgency); // inserting entity
    agencyReport.add("CREATE:").add(createdAgency.toString());

    createdAgency.ifPresent(a->a.setName("Updated Agency"));
    Optional<Agency> update = agencyRepository.update(createdAgency // updating entity
        .orElseThrow(() -> new RuntimeException("Unable to update agency as it was not created properly")));
    agencyReport.add("UPDATE:").add(update.toString());

    Optional<Agency> agency = agencyRepository.getById(5L); // get by id
    agencyReport.add("GET BY ID:").add(agency.toString());

    agencyRepository.delete(createdAgency.get());

    agencyReport.add("GET ALL:");
    List<Agency> allAgencies = agencyRepository.getAll(); // get all
    allAgencies.forEach(item->agencyReport.add(item.toString()));

    LOGGER.info(agencyReport.toString()); // print the report
  }

  private void runToursCheck(){
//     new local instance
    Tour newTour = new Tour();
    newTour.setName("Newly created tour");
    newTour.setAmount(755.00F);
    newTour.setDuration(12);
    newTour.setDepartureDate(Instant.now());
    newTour.setCountries(Arrays.asList("USA", "France"));
    newTour.setAgencyId(7L);

    StringJoiner tourReport = new StringJoiner("\n");


    tourReport.add("\n************TOUR REPOSITORY************");
    Optional<Tour> createdTour = tourRepository.create(newTour); // inserting entity
    tourReport.add("CREATE:").add(createdTour.toString());

    createdTour.ifPresent(a->a.setName("Updated Tour"));
    Optional<Tour> update = tourRepository.update(createdTour // updating entity
        .orElseThrow(() -> new RuntimeException("Unable to update agency as it was not created properly")));
    tourReport.add("UPDATE:").add(update.toString());

    Optional<Tour> agency = tourRepository.getById(2L); // get by id
    tourReport.add("GET BY ID:").add(agency.toString());

    tourRepository.delete(createdTour.get());

    tourReport.add("GET ALL:");
    List<Tour> allAgencies = tourRepository.getAll(); // get all
    allAgencies.forEach(item->tourReport.add(item.toString()));

    LOGGER.info(tourReport.toString()); // print the report
  }

}
