package com.pc.retail.dao;

import com.pc.retail.dao.file.FileStorageManagerFactory;
import com.pc.retail.dao.file.FileStorageManagerImpl;
import com.pc.retail.interactor.KiranaStoreException;
import com.zaxxer.hikari.HikariConfig;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import static com.pc.retail.client.services.ReferenceDataConstants.*;
import static com.pc.retail.dao.file.FileStorageClientImpl.*;

/**
 * Created by pavanc on 5/13/17.
 */
public class DataSourceManager {
    
    private static DataSourceManager dataSourceManager;
    private StorageManager storageManager;

    private static String STORAGE_TYPE = "STORAGE_TYPE";
    private static String STORAGE_TYPE_FILE = "STORAGE_TYPE_FILE";
    private static String FILE_STORAGE_PATH = "FILE_STORAGE_PATH";

    private DataSourceManager() throws DataAccessException{
        try {
            Properties props = new Properties();
            props.load(this.getClass().getClassLoader().getResourceAsStream("kiranastore-config.properties"));
            storageManager = getStorageManager(props);
        }catch (IOException ioEx){
            throw new DataAccessException("Application can not start, " + ioEx.getMessage());
        }
    }

    private StorageManager getStorageManager(Properties props) throws DataAccessException{
        String storageType = props.getProperty(STORAGE_TYPE);
        if(STORAGE_TYPE_FILE.equals( storageType )){
            String fileStoragePath = props.getProperty(FILE_STORAGE_PATH);
            return FileStorageManagerFactory.getFileStorageManager(fileStoragePath);
        }
        return new StorageManagerImpl(props);
    }


    public static DataSourceManager getInstance() throws DataAccessException{
        if(dataSourceManager == null){
            dataSourceManager = buildDataSourceManager();
        }
        return dataSourceManager;
    }

    private static DataSourceManager buildDataSourceManager() throws DataAccessException{
        return new DataSourceManager();
    }

    public ProductInvDAO getProductInvDAO() {
        return new ProductInvDAOImpl(getStorageManager());
    }

    public BillingDAO getBillingDAO() {
        return new BillingDAO(getStorageManager());
    }


    private StorageManager getStorageManager() {
        return storageManager;
    }

    public ReferenceDataDAO getReferenceDataDAO() {
        return new ReferenceDataDAOImpl( getStorageManager() );
    }
}
