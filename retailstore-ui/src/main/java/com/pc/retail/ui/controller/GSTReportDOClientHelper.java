package com.pc.retail.ui.controller;

import com.pc.retail.api.GSTReportController;
import com.pc.retail.api.GSTReportDO;
import com.pc.retail.dao.util.FilterModel;
import com.pc.retail.interactor.KiranaStoreException;

import java.util.List;

/**
 * Created by pavanc on 3/17/19.
 */
public class GSTReportDOClientHelper {

    public List<GSTReportDO> getGSTReportData() throws KiranaStoreException {
        GSTReportController gstReportController = new GSTReportController();
        return gstReportController.getProductWithDetail(new FilterModel());
    }
}
