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
           return createDummyList();
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

    private List<GSTReportDO>  createDummyList(){
        List<GSTReportDO> gstReportDOList = new ArrayList<>();
        GSTReportDO gstReportDO5 = new GSTReportDO();
        gstReportDO5.setGstCode("GST 5%");
        gstReportDO5.setTotalCGSTAmount(500);
        gstReportDO5.setTotalSGSTAmount(500);
        gstReportDO5.setTotalGSTAmount(1000);
        gstReportDOList.add(gstReportDO5);

        List<SupplierGSTReportDO> supplierGSTReportDOList = new ArrayList<>();
        SupplierGSTReportDO supplierGSTReportDO = new SupplierGSTReportDO();
        supplierGSTReportDO.setSupplierCode("ABC Corporation");
        supplierGSTReportDO.setTotalCGSTAmount(150);
        supplierGSTReportDO.setTotalSGSTAmount(150);
        supplierGSTReportDO.setTotalGSTAmount(300);
        supplierGSTReportDOList.add(supplierGSTReportDO);

        SupplierGSTReportDO supplierGSTReportDO2 = new SupplierGSTReportDO();
        supplierGSTReportDO2.setSupplierCode("XYZ Ind.");
        supplierGSTReportDO2.setTotalCGSTAmount(350);
        supplierGSTReportDO2.setTotalSGSTAmount(350);
        supplierGSTReportDO2.setTotalGSTAmount(700);
        supplierGSTReportDOList.add(supplierGSTReportDO2);

        gstReportDO5.setSupplierGSTReportDOList(supplierGSTReportDOList);


        GSTReportDO gstReportDO12 = new GSTReportDO();
        gstReportDO12.setGstCode("GST 12%");
        gstReportDO12.setTotalCGSTAmount(1500);
        gstReportDO12.setTotalSGSTAmount(1500);
        gstReportDO12.setTotalGSTAmount(3000);
        gstReportDOList.add(gstReportDO12);

        List<SupplierGSTReportDO> supplierGSTReportDOList2 = new ArrayList<>();

        SupplierGSTReportDO supplierGSTReportDO3 = new SupplierGSTReportDO();
        supplierGSTReportDO3.setSupplierCode("ABC Corporation");
        supplierGSTReportDO3.setTotalCGSTAmount(700);
        supplierGSTReportDO3.setTotalSGSTAmount(700);
        supplierGSTReportDO3.setTotalGSTAmount(1400);
        supplierGSTReportDOList2.add(supplierGSTReportDO3);

        SupplierGSTReportDO supplierGSTReportDO4 = new SupplierGSTReportDO();
        supplierGSTReportDO4.setSupplierCode("KBC Trading");
        supplierGSTReportDO4.setTotalCGSTAmount(800);
        supplierGSTReportDO4.setTotalSGSTAmount(800);
        supplierGSTReportDO4.setTotalGSTAmount(1600);
        supplierGSTReportDOList2.add(supplierGSTReportDO4);

        gstReportDO12.setSupplierGSTReportDOList(supplierGSTReportDOList2);

        return gstReportDOList;

    }

}
