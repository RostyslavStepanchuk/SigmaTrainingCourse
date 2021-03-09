package com.rstepanchuk.sigmatraining.repositories;

import com.rstepanchuk.sigmatraining.domain.Agency;
import com.rstepanchuk.sigmatraining.repositories.mappers.AgencyRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class AgencyRepository implements CrudRepository<Agency> {

  private final JdbcTemplate jdbcTemplate;
  private final AgencyRowMapper agencyRowMapper;

  @Autowired
  public AgencyRepository(JdbcTemplate jdbcTemplate, AgencyRowMapper agencyRowMapper) {
    this.jdbcTemplate = jdbcTemplate;
    this.agencyRowMapper = agencyRowMapper;
  }

  @Override
  public Optional<Agency> create(Agency entity) {
    KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
    String insertSql = "INSERT INTO public.agencies (name, phone, address, years) VALUES (?, ?, ?, ?);";
    int rowsAffected = jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, entity.getName());
      ps.setString(2, entity.getPhone());
      ps.setString(3, entity.getAddress());
      ps.setInt(4, entity.getYearsInBusiness());
      return ps;
    }, generatedKeyHolder);
    if (rowsAffected == 0) {
      return Optional.empty();
    }
    return getById((long) generatedKeyHolder.getKeys().get("id"));
  }

  @Override
  public Optional<Agency> update(Agency entity) {
    String updateSql = "UPDATE public.agencies " +
        "SET name = ?, phone =?, address=?, years=? WHERE ID=?";
    int rowsAffected = jdbcTemplate.update(updateSql,
        entity.getName(),
        entity.getPhone(),
        entity.getAddress(),
        entity.getYearsInBusiness(),
        entity.getId()
    );
    if (rowsAffected == 0) {
      return Optional.empty();
    }
    return getById(entity.getId());
  }

  @Override
  public void delete(Agency entity) {
    jdbcTemplate.update("delete from agencies_to_transport where agency_id = ?; " +
            "delete from agencies where id = ?; "
        , entity.getId(), entity.getId());
  }

  @Override
  public Optional<Agency> getById(Long id) {
    Agency agency = jdbcTemplate.queryForObject("select * from agencies where id = ?", agencyRowMapper, id);
    List<String> transports = jdbcTemplate.queryForList("select t.name " +
        "from agencies_to_transport at " +
        "join transport t on at.transport_id = t.id " + "" +
        "where at.agency_id = ?", String.class, id);
    if (agency != null) {
      agency.setTourTransport(transports);
    }
    return Optional.ofNullable(agency);
  }

  @Override
  public List<Agency> getAll() {
    List<Agency> result = jdbcTemplate.query("select * from agencies", agencyRowMapper);
    String transportQuery = "select at.agency_id, t.name " +
        "from agencies_to_transport at " +
        "join transport t on at.transport_id = t.id";
    Map<Long, List<String>> transportsToAgency = jdbcTemplate
        .queryForList(transportQuery)
        .stream()
        .collect(Collectors.groupingBy(row ->(Long) row.get("agency_id"),
            Collectors.mapping(row -> (String) row.get("name"), Collectors.toList())));
    result.forEach(agency->{
      agency.setTourTransport(transportsToAgency.getOrDefault(agency.getId(), new ArrayList<>()));
    });
    return result;
  }
}
