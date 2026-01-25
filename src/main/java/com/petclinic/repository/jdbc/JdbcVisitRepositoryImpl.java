package com.petclinic.repository.jdbc;

import com.petclinic.model.Visit;
import com.petclinic.repository.VisitRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.List;

public class JdbcVisitRepositoryImpl implements VisitRepository {

    private final JdbcClient jdbcClient;

    private final SimpleJdbcInsert insertVisit;

    public JdbcVisitRepositoryImpl(DataSource dataSource, JdbcClient jdbcClient) {

        this.insertVisit = new SimpleJdbcInsert(dataSource)
                .withTableName("visits")
                .usingGeneratedKeyColumns("id");

        this.jdbcClient = jdbcClient;
    }

    public void save(Visit visit) {
        if(visit.isNew()) {
            Number newKey = this.insertVisit.executeAndReturnKey(
                    createVisitParameterSource(visit));
            visit.setId(newKey.intValue());
        } else {
            throw new UnsupportedOperationException("Visit update not supported");
        }
    }

    public List<Visit> findByPetId(Integer petId) {
        JdbcPet pet = this.jdbcClient
                .sql("SELECT id, name, birth_date, type_id, owner_id FROM pets WHERE id=:id")
                .param("id", petId)
                .query(new JdbcPetRowMapper())
                .single();

        List<Visit> visits = this.jdbcClient
                .sql("SELECT id as visit_id, visit_date, description FROM visits WHERE pet_id=:id")
                .param("id", petId)
                .query(new JdbcVisitRowMapper())
                .list();

        for(Visit visit : visits) {
            visit.setPet(pet);
        }
        return visits;
    }

    private MapSqlParameterSource createVisitParameterSource(Visit visit) {
        return new MapSqlParameterSource()
                .addValue("id", visit.getId())
                .addValue("date", visit.getDate())
                .addValue("description", visit.getDescription())
                .addValue("pet_id", visit.getPet().getId());
    }

}
