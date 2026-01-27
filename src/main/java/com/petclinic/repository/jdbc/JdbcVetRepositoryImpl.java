package com.petclinic.repository.jdbc;

import com.petclinic.model.Specialty;
import com.petclinic.model.Vet;
import com.petclinic.repository.VetRepository;
import com.petclinic.util.EntityUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class JdbcVetRepositoryImpl implements VetRepository {

    private final JdbcClient jdbcClient;

    public JdbcVetRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Collection<Vet> findAll() {
        List<Vet> vets = new ArrayList<>(this.jdbcClient.sql
                ("SELECT id, first_name, last_name FROM vets ORDER BY last_name, first_name")
                .query(BeanPropertyRowMapper.newInstance(Vet.class))
                .list());

        final List<Specialty> specialties = this.jdbcClient.sql("SELECT id, name FROM specialties")
                .query(BeanPropertyRowMapper.newInstance(Specialty.class))
                .list();

        for (Vet vet : vets) {
            final List<Integer> vetspecialtiesIds = this.jdbcClient.sql(
                    "SELECT specialty_id FROM vet_specialties where vet_id=?")
                    .param(vet.getId())
                    .query(new BeanPropertyRowMapper<Integer>() {
                                public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                                    return rs.getInt(1);
                                }
                           }

                    ).list();
            for (int specialityId : vetspecialtiesIds) {
                Specialty specialty = EntityUtils.getById(specialties, Specialty.class, specialityId);
                vet.addSpecialty(specialty);
            }
        }
        return vets;
    }

}
