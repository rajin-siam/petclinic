package com.petclinic.repository.jdbc;

import com.petclinic.model.Owner;
import com.petclinic.model.PetType;
import com.petclinic.repository.OwnerRepository;
import com.petclinic.util.EntityUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.orm.ObjectRetrievalFailureException;

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
        Owner owner;
        try {
            owner = this.jdbcClient.sql("""
                    SELECT id, first_name, last_name, address, city, telephone''
                    FROM owners WHERE id = :id
                    """)
                    .param("id", id)
                    .query(BeanPropertyRowMapper.newInstance(Owner.class))
                    .single();
            } catch (EmptyResultDataAccessException ex) {
                throw new ObjectRetrievalFailureException(Owner.class, id);
            }
        loadPetsAndVisits(owner);
        return owner;
    }

    @Override
    public void save(Owner owner) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(owner);
        if(owner.isNew()) {
            Number newKey = this.insertOwner.executeAndReturnKey(parameterSource);
            owner.setId(newKey.intValue());
        } else {
            this.jdbcClient.sql("""
                    UPDATE SET first_name = :firstName, last_name = :lastName, address = :address, city = :city, telephone = :telephone
                    WHERE id = :id
                    """)
                    .paramSource(parameterSource)
                    .update();
        }
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
                .query(new JdbcPetVisitExtractor());
        Collection<PetType> petTypes = getPetTypes();
        for( JdbcPet pet : pets) {
            pet.setType(EntityUtils.getById(petTypes, PetType.class, pet.getTypeId()));
            owner.addPet(pet);
        }
    }

    private Collection<PetType> getPetTypes() {
        return this.jdbcClient.sql("SELECT id, name FROM types ORDER BY name")
                .query(BeanPropertyRowMapper.newInstance(PetType.class))
                .list();
    }


}
