package com.pc.retail.dao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavanc on 6/10/17.
 */
public class QueryModel {

    private List<String> selectList = new ArrayList<>();
    private String tableName;
    private String whereClause;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getWhereClause() {
        return whereClause;
    }

    public void setWhereClause(String whereClause) {
        this.whereClause = whereClause;
    }

    public List<String> getSelectList() {
        return selectList;
    }

    public void addToSelect(String selectColumn){
        selectList.add( selectColumn );
    }

    public String getQuery() {
        return "select " + getSelectClause() + " from " + tableName + " " + whereClause;
    }

    private String getSelectClause() {
        if(selectList == null ||  selectList.size() == 0)
            return "*";
        else
            return convertToCommSeperateString(selectList);
    }

    private String convertToCommSeperateString(List<String> selectList) {
        StringBuilder stringBuilder = new StringBuilder();
        for(String columnName : selectList){
            stringBuilder.append(columnName).append(", ");
        }
        String selectClause = stringBuilder.toString();
        return selectClause.substring(0, selectClause.length() - 1);
    }
}
