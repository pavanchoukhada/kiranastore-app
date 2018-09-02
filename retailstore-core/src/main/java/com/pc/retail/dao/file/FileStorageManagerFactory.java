package com.pc.retail.dao.file;

import com.pc.retail.dao.DataAccessException;
import com.pc.retail.dao.StorageManager;

import java.io.File;
import java.io.IOException;

import static com.pc.retail.client.services.ReferenceDataConstants.*;
import static com.pc.retail.dao.file.FileStorageClientImpl.*;

/**
 * Created by pavanc on 6/28/17.
 */
public class FileStorageManagerFactory {
    public static StorageManager getFileStorageManager(String fileStoragePath) throws DataAccessException {
        try {
            initFileStorage(fileStoragePath);
        }catch (IOException ioEx){
            throw new DataAccessException(ioEx.getMessage());
        }
        return new FileStorageManagerImpl(fileStoragePath);
    }

    private static void initFileStorage(String fileStoragePath) throws IOException{
        createFileIfDoesNotExist(fileStoragePath + "\\" + PRD_INV_TRANS_FILE);
        createFileIfDoesNotExist(fileStoragePath + "\\" + PRD_MASTER_FILE);
        createFileIfDoesNotExist(fileStoragePath + "\\" + PRD_CURR_INV_FILE);
        createFileIfDoesNotExist(fileStoragePath + "\\" + PRD_INV_FILE);
        createFileIfDoesNotExist(fileStoragePath + "\\" + PRODUCT_SUPPLIER_OBJECT_NAME + ".json");
        createFileIfDoesNotExist(fileStoragePath + "\\" + PRODUCT_CATEGORY_OBJECT_NAME + ".json");
        createFileIfDoesNotExist(fileStoragePath + "\\" + PRODUCT_COMPANY_OBJECT_NAME + ".json");
        createFileIfDoesNotExist(fileStoragePath + "\\" + PRODUCT_UOM_OBJECT_NAME + ".json");
    }

    private static void createFileIfDoesNotExist(String filePath) throws IOException{
        File file = new File(filePath);
        if(!file.exists())
            file.createNewFile();
    }

}
