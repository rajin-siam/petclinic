package com.petclinic.util;

import com.petclinic.model.BaseEntity;
import org.springframework.orm.ObjectRetrievalFailureException;

import java.util.Collection;

public class EntityUtils {

    private EntityUtils(){}

    public static <T extends BaseEntity> T getById(Collection<T> entities, Class<T> entityClass, int entityId) {

        for(T entity : entities) {
            if(entity.getId() == entityId && entityClass.isInstance(entity)) {
                return entity;
            }
        }
        throw new ObjectRetrievalFailureException(entityClass, entityId);
    }
}
