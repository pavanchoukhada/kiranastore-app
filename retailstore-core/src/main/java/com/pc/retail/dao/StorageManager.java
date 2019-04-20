package com.pc.retail.dao;

import com.pc.retail.dao.referencedata.ReferenceDataStorageClient;

/**
 * Created by pavanc on 5/14/17.
 */
public interface StorageManager {

    StorageClient getStorageClientForTrans() throws DataAccessException;

    StorageClient getStorageClient();

    ReportClientImpl getReportClient();

    ReferenceDataStorageClient getReferenceDataStorageClient() throws DataAccessException;


}
