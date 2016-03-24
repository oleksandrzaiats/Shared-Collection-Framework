package com.scf.server.application.utils;

public class Filtering {
    String columnName;
    String operator;
    String value;
    String relationWithPrevious;

    public Filtering(String columnName, String operator, String value) {
        this.columnName = columnName;
        this.operator = operator;
        this.value = value;
    }

    public Filtering(String columnName, String operator, String value, String relationWithPrevious) {
        this.columnName = columnName;
        this.operator = operator;
        this.value = value;
        this.relationWithPrevious = relationWithPrevious;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getOperator() {
        return operator;
    }

    public String getValue() {
        return value;
    }

    public String getRelationWithPrevious() {
        return relationWithPrevious;
    }
}
