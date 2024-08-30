package com.huskydreaming.huskycore.utilities;

import com.huskydreaming.huskycore.enumeration.SqlOption;
import com.huskydreaming.huskycore.interfaces.database.sql.SqlEntityType;

import java.util.StringJoiner;

public class Query {

    private static final String PREFIX = "(";
    private static final String SUFFIX = ")";
    private static final String COMPARE = " = ?";
    private static final String DELIMITER = ",";
    public static final String WHERE = " WHERE id" + COMPARE;

    public static String insert(SqlEntityType type) {
        StringJoiner parameters = newArray();
        StringJoiner values = newArray();

        for (String string : type.toColumns()) {
            parameters.add(string);
            values.add("?");
        }

        return "INSERT INTO " + type.toTable() + " " + parameters + " VALUES " + values;
    }

    public static String update(SqlEntityType type) {
        StringJoiner parameters = newArray(COMPARE + DELIMITER);

        for (String string :  type.toColumns()) {
            parameters.add(string);
        }

        return "UPDATE " + type.toTable() + " SET " + parameters + WHERE;
    }

    public static String delete(SqlEntityType type) {
        return delete(type, SqlOption.WHERE);
    }

    public static String delete(SqlEntityType type, SqlOption option) {
        return "DELETE FROM " + type.toTable() + option;
    }

    public static String select(SqlEntityType type) {
        return select(type, SqlOption.WHERE);
    }

    public static String select(SqlEntityType type, SqlOption option) {
        return "SELECT * FROM " + type.toTable() + option;
    }

    private static StringJoiner newArray() {
        return newArray(DELIMITER);
    }

    private static StringJoiner newArray(String delimiter) {
        return new StringJoiner(delimiter, PREFIX, SUFFIX);
    }
}