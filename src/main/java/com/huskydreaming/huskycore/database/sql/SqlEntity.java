package com.huskydreaming.huskycore.database.sql;

public interface SqlEntity {

    long getId();
    void setId(long id);

    SqlEntityType getEntityType();
}
