package com.rstepanchuk.sigmatraining.repositories;

import com.rstepanchuk.sigmatraining.domain.Tour;
import com.rstepanchuk.sigmatraining.repositories.mappers.TourRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class TourRepository implements CrudRepository<Tour> {

  private JdbcTemplate jdbcTemplate;
  private TourRowMapper tourRowMapper;

  @Autowired
  public TourRepository(JdbcTemplate jdbcTemplate, TourRowMapper tourRowMapper) {
    this.jdbcTemplate = jdbcTemplate;
    this.tourRowMapper = tourRowMapper;
  }

  @Override
  public Optional<Tour> create(Tour entity) {
    KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
    String insertSql = "INSERT INTO public.tours (name, amount, duration, departure, agency_id) VALUES (?, ?, ?, ?, ?)";
    int rowsAffected = jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, entity.getName());
      ps.setFloat(2, entity.getAmount());
      ps.setInt(3, entity.getDuration());
      ps.setTimestamp(4, Timestamp.from(entity.getDepartureDate()));
      ps.setLong(5, entity.getAgencyId());
      return ps;
    }, generatedKeyHolder);
    if (rowsAffected == 0) {
      return Optional.empty();
    }
    return getById((long) generatedKeyHolder.getKeys().get("id"));
  }

  @Override
  public Optional<Tour> update(Tour entity) {
    String updateSql = "UPDATE public.tours " +
        "SET name=?, amount=?, duration=?, departure=?, agency_id=? WHERE ID=?";
    int rowsAffected = jdbcTemplate.update(updateSql,
        entity.getName(),
        entity.getAmount(),
        entity.getDuration(),
        Timestamp.from(entity.getDepartureDate()),
        entity.getAgencyId(),
        entity.getId()
    );
    if (rowsAffected == 0) {
      return Optional.empty();
    }
    return getById(entity.getId());
  }

  @Override
  public void delete(Tour entity) {
    jdbcTemplate.update("delete from tours_to_countries where tour_id = ?; " +
            "delete from tours where id = ?; "
        , entity.getId(), entity.getId());
  }

  @Override
  public Optional<Tour> getById(Long id) {
    Tour tour = jdbcTemplate.queryForObject("select * from tours where id = ?", tourRowMapper, id);
    List<String> countries = jdbcTemplate.queryForList("select c.name " +
        "from tours_to_countries tc " +
        "join countries c on tc.country_id = c.id " +
        "where tc.tour_id = ?", String.class, id);
    if (tour != null) {
      tour.setCountries(countries);
    }
    return Optional.ofNullable(tour);
  }

  @Override
  public List<Tour> getAll() {
    List<Tour> result = jdbcTemplate.query("select * from tours", tourRowMapper);
    String transportQuery = "select c.name, tc.tour_id " +
        "from tours_to_countries tc " +
        "join countries c on tc.country_id = c.id";
    Map<Long, List<String>> countryToTour = jdbcTemplate
        .queryForList(transportQuery)
        .stream()
        .collect(Collectors.groupingBy(row ->(Long) row.get("tour_id"),
            Collectors.mapping(row -> (String) row.get("name"), Collectors.toList())));
    result.forEach(tour->{
      tour.setCountries(countryToTour.getOrDefault(tour.getId(), new ArrayList<>()));
    });
    return result;
  }

  @Override
  public Set<String> getAllNames() {
    // get names of 3 cheapest tours before May of this year
    return getAll()
        .stream()
        .filter(tour->tour.getDepartureDate().isBefore(Instant.parse("2021-05-01T00:00:00Z")))
        .sorted(Comparator.comparingDouble(Tour::getAmount))
        .map(Tour::getName)
        .limit(3)
        .collect(Collectors.toSet());
  }
}
