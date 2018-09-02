package com.pc.retail.dao.file;

import com.pc.retail.dao.DataAccessException;
import com.pc.retail.dao.StorageClient;
import com.pc.retail.dao.StorageManager;
import com.pc.retail.dao.referencedata.ReferenceDataStorageClient;

/**
 * Created by pavanc on 6/17/17.
 */
public class FileStorageManagerImpl implements StorageManager {
    private String fileStoragePath;

    FileStorageManagerImpl(String fileStoragePath) {
        this.fileStoragePath = fileStoragePath;
    }

    @Override
    public StorageClient getStorageClientForTrans() throws DataAccessException {
        return new FileStorageClientImpl(fileStoragePath);
    }

    @Override
    public StorageClient getStorageClient() {
        return null;
    }

    @Override
    public ReferenceDataStorageClient getReferenceDataStorageClient() throws DataAccessException {
        return new ReferenceDataFileStorageClientImpl( fileStoragePath );
    }

}
