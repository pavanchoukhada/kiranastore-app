package com.pc.retail.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by pavanc on 3/30/19.
 */
public class QueryUtil {

    public static void setParameter
            (List<SQLParameter> sqlParameterList,
             PreparedStatement preparedStatement) throws SQLException {
        int index = 0;
        for (SQLParameter sqlParameter : sqlParameterList) {
            index++;
            switch (sqlParameter.getParamDataType()) {
                case DATE:
                    preparedStatement.setDate(index, (Date) sqlParameter.getParamValue());
                    break;
                case STRING:
                    preparedStatement.setString(index, (String) sqlParameter.getParamValue());
                    break;
                case INTEGER:
                    preparedStatement.setInt(index, (Integer) sqlParameter.getParamValue());
                    break;
            }
        }
    }

    public static String createWhereClause
            (List<SQLParameter> sqlParameterList) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean first = true;
        for (SQLParameter sqlParameter : sqlParameterList) {
            stringBuilder.append(first?"":" AND ")
                    .append(sqlParameter.getParamName())
                    .append(sqlParameter.getSqlOperator().getOperator()).append(" ? ");
            first = false;
        }
        return stringBuilder.toString();
    }

}