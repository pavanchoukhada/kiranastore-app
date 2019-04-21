package com.pc.retail.api;

import com.pc.retail.dao.DataAccessException;
import com.pc.retail.dao.GSTReportDAO;
import com.pc.retail.dao.util.FilterModel;
import com.pc.retail.interactor.KiranaStoreException;

import java.util.Collection;

/**
 * Created by pavanc on 4/13/19.
 */
public class GSTReportInteractor {
    private GSTReportDAO gstReportDAO;

    public GSTReportInteractor(GSTReportDAO gstReportDAO) {
        this.gstReportDAO = gstReportDAO;
    }

    public Collection<SupplierGSTReportDO> getSupplierGSTReport(FilterModel filterModel) throws KiranaStoreException {
        try {
            return gstReportDAO.getGSTReport(filterModel);
        } catch (DataAccessException e) {
            e.printStackTrace();;
                throw new KiranaStoreException(e.getMessage());
        }
    }
}
