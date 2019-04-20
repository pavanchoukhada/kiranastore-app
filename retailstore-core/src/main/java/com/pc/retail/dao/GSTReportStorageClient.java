package com.pc.retail.dao;

import com.pc.retail.api.GSTReportDO;

import java.util.List;

/**
 * Created by pavanc on 3/24/19.
 */
public interface GSTReportStorageClient {

    List<GSTReportDO> getProductInvoiceDetails(int invoiceId) throws DataAccessException;

}
