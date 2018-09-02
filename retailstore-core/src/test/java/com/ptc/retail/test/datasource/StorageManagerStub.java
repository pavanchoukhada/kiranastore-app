package com.ptc.retail.test.datasource;

import com.pc.retail.dao.DataAccessException;
import com.pc.retail.dao.StorageClient;
import com.pc.retail.dao.StorageManager;
import com.pc.retail.dao.referencedata.ReferenceDataStorageClient;

/**
 * Created by pavanc on 5/20/17.
 */
public class StorageManagerStub implements StorageManager {
    @Override
    public StorageClient getStorageClientForTrans() throws DataAccessException {
        return new StorageClientStub();
    }

    @Override
    public StorageClient getStorageClient() {
        return null;
    }

    @Override
    public ReferenceDataStorageClient getReferenceDataStorageClient() throws DataAccessException {
        return null;
    }
}
