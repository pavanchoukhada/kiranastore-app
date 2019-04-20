package com.pc.retail.dao;

import com.pc.retail.dao.referencedata.ReferenceDataStorageClient;
import com.pc.retail.dao.referencedata.ReferenceDataStorageClientImpl;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by pavanc on 5/13/17.
 */
public class StorageManagerImpl implements StorageManager{

    private RetailDataSource retailDataSource;

    public StorageManagerImpl(Properties props){
        retailDataSource = new RetailDataSource(new HikariDataSource(getConfig(props)));
    }

    private HikariConfig getConfig(Properties props){
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
        config.addDataSourceProperty("serverName", props.getProperty("serverName"));
        config.addDataSourceProperty("databaseName", props.getProperty("databaseName"));
        config.addDataSourceProperty("user", props.getProperty("user"));
        config.addDataSourceProperty("password", props.getProperty("password"));
        config.setMaximumPoolSize(20);
        config.setConnectionTimeout(3000);
        config.setValidationTimeout(1000);
        return config;
    }



    public Connection getConnection() throws SQLException{
        return retailDataSource.getConnection();
    }

    public StorageClient getStorageClientForTrans() throws DataAccessException{
        try {
            return new StorageClientImpl(this.getConnection());
        }catch (SQLException sqlEx){
            throw new DataAccessException(sqlEx.getMessage());
        }
    }

    @Override
    public StorageClient getStorageClient() {
       return new StorageClientImpl(this.retailDataSource);
    }

    @Override
    public ReportClientImpl getReportClient() {
        return new ReportClientImpl(this.retailDataSource);
    }

    @Override
    public ReferenceDataStorageClient getReferenceDataStorageClient(){
        return new ReferenceDataStorageClientImpl(retailDataSource);
    }
}
