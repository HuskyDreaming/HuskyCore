package com.huskydreaming.huskycore.database.base;

import com.huskydreaming.huskycore.database.sql.SqlEntityType;

import java.util.StringJoiner;

public class DatabaseQuery {
    private static final String PREFIX = "(";
    private static final String SUFFIX = ")";
    private static final String COMPARE = " = ?";
    private static final String DELIMITER = ", ";

    public static String insert(SqlEntityType type, String pluginName) {
        StringJoiner parameters = new StringJoiner(DELIMITER, PREFIX, SUFFIX);
        StringJoiner values = new StringJoiner(DELIMITER, PREFIX, SUFFIX);

        for (String string : type.toColumns()) {
            parameters.add(string);
            values.add("?");
        }

        return "INSERT INTO " + pluginName + "_" + type.toTable() + " " + parameters + " VALUES " + values;
    }

    public static String update(SqlEntityType type, String pluginName) {
        StringJoiner parameters = new StringJoiner(DELIMITER);

        for (String string : type.toColumns()) {
            parameters.add(string + COMPARE);
        }

        return "UPDATE " + pluginName + "_" + type.toTable() + " SET " + parameters + " WHERE id = ?";
    }

    public static String delete(SqlEntityType type, String pluginName) {
        return "DELETE FROM " + pluginName + "_" + type.toTable() + " WHERE id = ?";
    }

    public static String selectEmpty(SqlEntityType type, String pluginName) {
        return "SELECT * FROM " + pluginName + "_" + type.toTable();
    }

    public static String select(SqlEntityType type, String pluginName) {
        return "SELECT * FROM " + pluginName + "_" + type.toTable() +  "WHERE id = ?";
    }
}