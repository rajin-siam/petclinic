package com.petclinic.repository.jdbc;

import com.petclinic.model.Visit;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class JdbcVisitRowMapper implements RowMapper<Visit> {

    public Visit mapRow(ResultSet rs, int row) throws SQLException {
            Visit visit = new Visit();
            visit.setId(rs.getInt("visit_id"));
            visit.setDate(rs.getObject("visit_date", LocalDate.class));
            visit.setDescription(rs.getString("description"));
            return visit;
    }
}
