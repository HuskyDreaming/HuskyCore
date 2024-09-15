package com.huskydreaming.huskycore.interfaces.database.sql;

public interface SqlEntity {

    long getId();
    void setId(long id);

    SqlEntityType getEntityType();
}
