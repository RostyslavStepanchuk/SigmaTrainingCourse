package com.rstepanchuk.sigmatraining.repositories.mappers;

import com.rstepanchuk.sigmatraining.domain.Agency;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AgencyRowMapper implements RowMapper<Agency> {

  @Override
  public Agency mapRow(ResultSet rs, int rowNum) throws SQLException {
    Agency result = new BeanPropertyRowMapper<Agency>(Agency.class).mapRow(rs, rowNum);
    result.setYearsInBusiness(rs.getInt("years"));
    return result;
  }

}
