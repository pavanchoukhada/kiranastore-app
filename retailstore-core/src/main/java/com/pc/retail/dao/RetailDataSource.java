package com.pc.retail.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by pavanc on 8/6/17.
 */
public class RetailDataSource {

    DataSource dataSource;

    public RetailDataSource(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public Connection getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        if(connection.getAutoCommit()){
            connection.setAutoCommit(false);
        }
        return connection;
    }
}
