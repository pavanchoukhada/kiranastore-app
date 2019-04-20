package com.pc.retail.ui.controller;

import com.pc.retail.api.FilterKeyConstants;
import com.pc.retail.api.GSTReportController;
import com.pc.retail.api.SupplierGSTReportDO;
import com.pc.retail.dao.util.FilterModel;
import com.pc.retail.interactor.KiranaStoreException;
import com.pc.retail.ui.helper.ReferenceDataHelper;
import com.pc.retail.util.DataUtil;
import com.pc.retail.vo.ProductSupplier;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * Created by pavanc on 3/17/19.
 */
public class GSTReportDOClientHelper {

    public Collection<SupplierGSTReportDO> getGSTReportData(GSTReportGridController gstReportGridController) throws KiranaStoreException {
        GSTReportController gstReportController = new GSTReportController();
        return gstReportController.getGSTReportData(createFilter(gstReportGridController));
    }

    private FilterModel createFilter(GSTReportGridController gstReportGridController){
        FilterModel filterModel = new FilterModel();

        LocalDate fromLocalDate = gstReportGridController.fromGstReportDateDP.getValue();
        filterModel.addFilter(FilterKeyConstants.FROM_DATE, DataUtil.getDateStr(fromLocalDate));

        LocalDate toLocalDate = gstReportGridController.toGstReportDateDP.getValue();
        filterModel.addFilter(FilterKeyConstants.TO_DATE, DataUtil.getDateStr(toLocalDate));

        if(gstReportGridController.getSupplierCB().getValue() != null) {
            int supplierId = gstReportGridController.getSupplierCB().getValue().getId();
            if (supplierId > 0) {
                filterModel.addFilter(FilterKeyConstants.SUPPLIER_ID, String.valueOf(supplierId));
            }
        }
        return filterModel;
    }

    public List<ProductSupplier> getSupplierList() {
        return new ReferenceDataHelper().getSupplierList();
    }

}
