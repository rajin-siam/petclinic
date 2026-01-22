package com.petclinic.repository.jdbc;

import com.petclinic.model.Owner;
import com.petclinic.repository.OwnerRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;

public class JdbcOwnerRepository implements OwnerRepository {

    private final JdbcClient jdbcClient;
    private final SimpleJdbcInsert insertOwner;

    public JdbcOwnerRepository(DataSource dataSource, JdbcClient jdbcClient) {
        this.insertOwner =  new SimpleJdbcInsert(dataSource)
                .withTableName("owners")
                .usingGeneratedKeyColumns("id");

        this.jdbcClient = jdbcClient;
    }


    @Override
    public Collection<Owner> findByLastName(String lastName) {
        List<Owner> owners = this.jdbcClient.sql("""
            SELECT id, first_name, last_name, address, city, telephone
            FROM owners
            WHERE last_name = :lastName
            """)
                .param("lastname", lastName + "%")
                .query(BeanPropertyRowMapper.newInstance(Owner.class))
                .list();

        loadOwnersPetsAndVisits(owners);
        return owners;
    }

    @Override
    public Owner findById(int id) {
        return null;
    }

    @Override
    public void save(Owner owner) {

    }

    private void loadOwnersPetsAndVisits(List<Owner> owners) {
        for (Owner owner : owners) {
            loadPetsAndVisits(owner);
        }
    }

    private void loadPetsAndVisits(Owner owner) {
        final List<JdbcPet> pets = this.jdbcClient.sql("""
           SELECT pets.id, name, birth_date, type_id, owner_id, visits.id, visit_date, description, pet_id
           FROM pets LEFT OUTER JOIN vists on pets.id = visits.pet_id
              WHERE owner_id = :ownerId ORDER BY pet_id
           """)
                .param("id", owner.getId())
                .query(new PetVisit)
    }
}
