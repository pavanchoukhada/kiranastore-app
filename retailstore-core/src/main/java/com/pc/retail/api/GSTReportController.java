package com.pc.retail.api;

import com.pc.retail.dao.DataAccessException;
import com.pc.retail.dao.DataSourceManager;
import com.pc.retail.dao.util.FilterModel;
import com.pc.retail.interactor.KiranaAppResult;
import com.pc.retail.interactor.KiranaStoreException;
import com.pc.retail.interactor.ProductInventoryInteractor;
import com.pc.retail.vo.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by pavanc on 5/13/17.
 */
public class GSTReportController {

    public List<GSTReportDO> getProductWithDetail(FilterModel filterModel) throws KiranaStoreException {
           return new ArrayList<>();
    }

    private ProductInventoryInteractor getProductInventoryInteractor() throws KiranaStoreException {
        DataSourceManager dataSourceManager = null;
        try {
            dataSourceManager = DataSourceManager.getInstance();
        } catch (DataAccessException e) {
            throw new KiranaStoreException(e.getMessage());
        }
        return new ProductInventoryInteractor(dataSourceManager.getProductInvDAO());
    }

    private void createDummyList(){
        List<GSTReportDO> gstReportDOList = new ArrayList<>();
        GSTReportDO gstReportDO5 = new GSTReportDO();
        gstReportDO5.setGstCode("GST 5%");
        gstReportDO5.setTotalCGSTAmount(500);
        gstReportDO5.setTotalSGSTAmount(500);
        gstReportDO5.setTotalGSTAmount(1000);
        gstReportDOList.add(gstReportDO5);

        GSTReportDO gstReportSupplier1 = new GSTReportDO();
        gstReportSupplier1.setSupplierCode("ABC Corporation");
        gstReportSupplier1.setTotalCGSTAmount(150);
        gstReportSupplier1.setTotalSGSTAmount(150);
        gstReportSupplier1.setTotalGSTAmount(300);
        gstReportDOList.add(gstReportSupplier1);

        GSTReportDO gstReportSupplier2 = new GSTReportDO();
        gstReportSupplier2.setSupplierCode("XYZ Ind.");
        gstReportSupplier2.setTotalCGSTAmount(350);
        gstReportSupplier2.setTotalSGSTAmount(350);
        gstReportSupplier2.setTotalGSTAmount(700);
        gstReportDOList.add(gstReportSupplier2);


        GSTReportDO gstReportDO12 = new GSTReportDO();
        gstReportDO12.setGstCode("GST 12%");
        gstReportDO12.setTotalCGSTAmount(1500);
        gstReportDO12.setTotalSGSTAmount(1500);
        gstReportDO12.setTotalGSTAmount(3000);
        gstReportDOList.add(gstReportDO12);

        GSTReportDO gstReportSupplier3 = new GSTReportDO();
        gstReportSupplier1.setSupplierCode("ABC Corporation");
        gstReportSupplier1.setTotalCGSTAmount(700);
        gstReportSupplier1.setTotalSGSTAmount(700);
        gstReportSupplier1.setTotalGSTAmount(1400);
        gstReportDOList.add(gstReportSupplier3);

        GSTReportDO gstReportSupplier4 = new GSTReportDO();
        gstReportSupplier2.setSupplierCode("KBC Trading");
        gstReportSupplier2.setTotalCGSTAmount(800);
        gstReportSupplier2.setTotalSGSTAmount(800);
        gstReportSupplier2.setTotalGSTAmount(1600);
        gstReportDOList.add(gstReportSupplier4);

    }

}
