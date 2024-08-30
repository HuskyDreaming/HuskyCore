package com.huskydreaming.huskycore.interfaces.database.sql;

public interface SqlEntity {

    int getId();
    void setId(int id);

    SqlEntityType getEntityType();
}
