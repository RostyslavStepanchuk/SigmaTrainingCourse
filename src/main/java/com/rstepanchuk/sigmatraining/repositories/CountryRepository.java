package com.rstepanchuk.sigmatraining.repositories;

import com.rstepanchuk.sigmatraining.domain.Country;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor()
public class CountryRepository implements ChildEntityRepository<Country> {

  private final JdbcTemplate jdbcTemplate;

  @Override
  public List<Country> getAllByParentId(Long parentId) {
    return jdbcTemplate.query(
        "select c.name, c.id " +
        "from tours_to_countries tc " +
        "join countries c on tc.country_id = c.id " +
        "where tc.tour_id = ?",
        new BeanPropertyRowMapper<>(Country.class),
        parentId);
  }

  @Override
  public Map<Long, List<Country>> getForParentListGroupedByParentId(List<Long> parentIds) {
    String arguments = parentIds.stream().map(id -> "?").collect(Collectors.joining(","));
    long [] argTypes = new long[parentIds.size()];
    Arrays.fill(argTypes, Types.BIGINT);
    String query = "select c.name, tc.tour_id " +
        "from tours_to_countries tc " +
        "join countries c on tc.country_id = c.id " +
        "where tc.tour_id in (" +  arguments + ")";

    return jdbcTemplate
        .queryForList(query, parentIds.toArray(), argTypes)
        .stream()
        .collect(Collectors.groupingBy(row -> (Long) row.get("tour_id"),
            Collectors.mapping(row -> {
                  Country country = new Country();
                  country.setId((Long)row.get("id"));
                  country.setName((String)row.get("name"));
                  return country;
                },
                Collectors.toList())));
  }

  @Override
  public Map<Long, List<Country>> getAllGroupedByParentId() {
    String transportQuery = "select c.name, tc.tour_id " +
        "from tours_to_countries tc " +
        "join countries c on tc.country_id = c.id";
    return jdbcTemplate
        .queryForList(transportQuery)
        .stream()
        .collect(Collectors.groupingBy(row -> (Long) row.get("tour_id"),
            Collectors.mapping(row -> {
                  Country country = new Country();
                  country.setId((Long)row.get("id"));
                  country.setName((String)row.get("name"));
                  return country;
                },
                Collectors.toList())));
  }

  @Override
  public void connectToParent(List<Country> entities, Long parentId) {
    String insertSql = "INSERT INTO public.tours_to_countries (tour_id, country_id) VALUES (? , ?);";
    jdbcTemplate.batchUpdate(insertSql,
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setLong(1, parentId);
            ps.setLong(2, entities.get(i).getId());
          }

          @Override
          public int getBatchSize() {
            return entities.size();
          }
        });
  }

}
