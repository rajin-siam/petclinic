package com.petclinic.repository.jdbc;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class JdbcPetRowMapper implements RowMapper<JdbcPet> {

    public JdbcPet mapRow(ResultSet rs, int rowNum) throws SQLException {
        JdbcPet pet = new JdbcPet();
        pet.setId(rs.getInt("pets.id"));
        pet.setName(rs.getString("name"));
        pet.setBirthDate(rs.getObject("birth_date", LocalDate.class));
        pet.setTypeId(rs.getInt("type_id"));
        pet.setOwnerId(rs.getInt("owner_id"));
        return pet;
    }
}
