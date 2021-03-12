package com.rstepanchuk.sigmatraining.repositories.mappers;

import com.rstepanchuk.sigmatraining.domain.Tour;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TourRowMapper implements RowMapper<Tour> {

  @Override
  public Tour mapRow(ResultSet rs, int rowNum) throws SQLException {
    Tour tour = new BeanPropertyRowMapper<>(Tour.class).mapRow(rs, rowNum);
    tour.setDepartureDate(rs.getTimestamp("departure").toInstant());
    return tour;
  }
}
