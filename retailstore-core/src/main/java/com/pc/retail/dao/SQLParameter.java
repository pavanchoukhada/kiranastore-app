package com.pc.retail.dao;



/**
 * Created by pavanc on 8/18/18.
 */
public class SQLParameter {
    String paramName;
    Object paramValue;
    DataType paramDataType;
    SQLOperator sqlOperator = SQLOperator.EQUAL;


    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public Object getParamValue() {
        return paramValue;
    }

    public void setParamValue(Object paramValue) {
        this.paramValue = paramValue;
    }

    public DataType getParamDataType() {
        return paramDataType;
    }

    public void setParamDataType(DataType paramDataType) {
        this.paramDataType = paramDataType;
    }

    public SQLOperator getSqlOperator() {
        return sqlOperator;
    }

    public void setSqlOperator(SQLOperator sqlOperator) {
        this.sqlOperator = sqlOperator;
    }
}
