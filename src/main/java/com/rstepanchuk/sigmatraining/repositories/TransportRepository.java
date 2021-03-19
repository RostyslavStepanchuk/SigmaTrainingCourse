package com.rstepanchuk.sigmatraining.repositories;

import com.rstepanchuk.sigmatraining.domain.Transport;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class TransportRepository implements ChildEntityRepository<Transport> {

  private final JdbcTemplate jdbcTemplate;

  @Override
  public List<Transport> getAllByParentId(Long parentId) {
    return jdbcTemplate.query(
        "select t.id, t.name " +
            "from agencies_to_transport at " +
            "join transport t on at.transport_id = t.id " + "" +
            "where at.agency_id = ?",
        new BeanPropertyRowMapper<>(Transport.class),
        parentId);
  }

  @Override
  public Map<Long, List<Transport>> getAllGroupedByParentId() {
    String transportQuery = "select at.agency_id, t.name, t.id " +
        "from agencies_to_transport at " +
        "join transport t on at.transport_id = t.id";

    List<Map<String, Object>> test = jdbcTemplate
        .queryForList(transportQuery);
    return jdbcTemplate
        .queryForList(transportQuery)
        .stream()
        .collect(Collectors.groupingBy(
            row ->(Long) row.get("id"),
            Collectors.mapping(row -> {
              Transport transport = new Transport();
              transport.setId((Long)row.get("id"));
              transport.setName((String)row.get("name"));
              return transport;
            }, Collectors.toList())
        ));
  }

  @Override
  public void connectToParent(List<Transport> entities, Long parentId) {
    String insertSql = "INSERT INTO public.agencies_to_transport (agency_id, transport_id) VALUES (? , ?);";
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
