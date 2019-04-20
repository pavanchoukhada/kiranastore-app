package com.pc.retail.dao;

/**
 * Created by pavanc on 3/30/19.
 */
public enum SQLOperator {
    EQUAL("="),
    LESS_THEN("<"),
    LESS_THEN_EQUAL_TO("<="),
    GREATER_THEN(">"),
    GREATER_THEN_EQUAL_TO(">=");

    private String operator;

    SQLOperator(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }
}
