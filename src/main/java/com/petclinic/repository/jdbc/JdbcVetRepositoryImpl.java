package com.petclinic.repository.jdbc;

import com.petclinic.model.Speciality;
import com.petclinic.model.Vet;
import com.petclinic.repository.VetRepository;
import com.petclinic.util.EntityUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

        final List<Speciality> specialities = this.jdbcClient.sql("SELECT id, name FROM specialities")
                .query(BeanPropertyRowMapper.newInstance(Speciality.class))
                .list();

        for (Vet vet : vets) {
            final List<Integer> vetSpecialitiesIds = this.jdbcClient.sql(
                    "SELECT speciality_id FROM vet_specialites where vet_id=?")
                    .param(vet.getId())
                    .query(new BeanPropertyRowMapper<Integer>() {
                                public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                                    return rs.getInt(1);
                                }
                           }

                    ).list();
            for (int specialityId : vetSpecialitiesIds) {
                Speciality speciality = EntityUtils.getById(specialities, Speciality.class, specialityId);
                vet.addSpecialty(speciality);
            }
        }
        return vets;
    }

}
