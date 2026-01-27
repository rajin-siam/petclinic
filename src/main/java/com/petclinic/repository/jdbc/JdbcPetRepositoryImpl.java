package com.petclinic.repository.jdbc;

import com.petclinic.model.Owner;
import com.petclinic.model.Pet;
import com.petclinic.model.PetType;
import com.petclinic.repository.OwnerRepository;
import com.petclinic.repository.PetRepository;
import com.petclinic.util.EntityUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class JdbcPetRepositoryImpl implements PetRepository {

    private final JdbcClient jdbcClient;

    private final SimpleJdbcInsert insertPet;

    private final OwnerRepository ownerRepository;

    public JdbcPetRepositoryImpl(JdbcClient jdbcClient, DataSource dataSource, OwnerRepository ownerRepository) {
        this.jdbcClient = jdbcClient;
        this.insertPet = new SimpleJdbcInsert(dataSource)
                .withTableName("pets")
                .usingGeneratedKeyColumns("id");

        this.ownerRepository = ownerRepository;
    }

    public List<PetType> findPetTypes() {
        return this.jdbcClient
                .sql("SELECT id, name FROM types ORDER BY name")
                .query(BeanPropertyRowMapper.newInstance(PetType.class))
                .list();
    }

    public Pet findById(int id) {
        int ownerId;
        try {
            ownerId = this.jdbcClient
                    .sql("SELECT owner_id FROM pets WHERE id = :id")
                    .param("id", id)
                    .query(Integer.class)
                    .single();
        } catch (EmptyResultDataAccessException ex) {
            throw new ObjectRetrievalFailureException(Pet.class, id);
        }
        Owner owner = this.ownerRepository.findById(ownerId);
        return EntityUtils.getById(owner.getPets(), Pet.class, id);
    }

    public void save(Pet pet) {
        if(pet.isNew()) {
            Number newKey = this.insertPet
                    .executeAndReturnKey(createPetParameterSource(pet));
            pet.setId(newKey.intValue());
        } else {
            this.jdbcClient
                    .sql("""
                            UPDATE pets 
                            SET name=:name, birth_date=:birth_date, type_id=:type_id, owner_id=:owner_id 
                            WHERE id=:id
                            """)
                        .paramSource(createPetParameterSource(pet))
                        .update();
        }
    }

    private MapSqlParameterSource createPetParameterSource(Pet pet) {
        return new MapSqlParameterSource()
                .addValue("id", pet.getId())
                .addValue("name", pet.getName())
                .addValue("birth_date", pet.getBirthDate())
                .addValue("type_id", pet.getType().getId())
                .addValue("owner_id", pet.getOwner().getId());
    }
}
