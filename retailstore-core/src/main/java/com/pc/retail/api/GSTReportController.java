package com.pc.retail.api;

import com.pc.retail.dao.DataAccessException;
import com.pc.retail.dao.DataSourceManager;
import com.pc.retail.dao.util.FilterModel;
import com.pc.retail.interactor.KiranaStoreException;
import com.pc.retail.interactor.ProductInventoryInteractor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by pavanc on 5/13/17.
 */
public class GSTReportController {

    public Collection<SupplierGSTReportDO> getGSTReportData(FilterModel filterModel) throws KiranaStoreException {
           return getGSTReportInterator().getSupplierGSTReport(filterModel);
    }

    private GSTReportInteractor getGSTReportInterator() throws KiranaStoreException {
        DataSourceManager dataSourceManager = null;
        try {
            dataSourceManager = DataSourceManager.getInstance();
        } catch (DataAccessException e) {
            throw new KiranaStoreException(e.getMessage());
        }
        return new GSTReportInteractor(dataSourceManager.getGSTReportDAO());
    }
}
