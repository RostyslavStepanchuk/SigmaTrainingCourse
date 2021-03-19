package com.rstepanchuk.sigmatraining.repositories;

import com.rstepanchuk.sigmatraining.domain.Agency;
import com.rstepanchuk.sigmatraining.repositories.mappers.AgencyRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class AgencyRepository implements CrudRepository<Agency> {

  private final JdbcTemplate jdbcTemplate;
  private final AgencyRowMapper agencyRowMapper;

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
    return Optional.ofNullable(agency);
  }

  @Override
  public List<Agency> getAll() {
    return jdbcTemplate.query("select * from agencies", agencyRowMapper);
  }

  public Set<String> getAllNames() {
    // get names of agencies that have more than 3 years experience
    return getAll()
        .stream()
        .filter(agency -> agency.getYearsInBusiness() > 3)
        .sorted(Comparator.comparingInt(Agency::getYearsInBusiness))
        .map(Agency::getName)
        .collect(Collectors.toSet());
  }

  public List<Agency> searchAcrossAllFields(String searchInput) {
    return null;
  }
}
