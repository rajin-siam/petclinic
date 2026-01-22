package com.petclinic.repository.jdbc;

import com.petclinic.model.Pet;

class JdbcPet extends Pet {

    private int typeId;

    private int ownerId;

    public int getTypeId() {
        return this.typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

}
