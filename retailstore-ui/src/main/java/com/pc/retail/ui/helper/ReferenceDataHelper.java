package com.pc.retail.ui.helper;

import com.pc.retail.client.services.ReferenceDataService;
import com.pc.retail.client.services.ReferenceDataServiceImpl;
import com.pc.retail.dao.DataAccessException;
import com.pc.retail.vo.ProductSupplier;

import java.util.ArrayList;
import java.util.List;

public class ReferenceDataHelper {
    public ReferenceDataHelper() {
    }

    public List<ProductSupplier> getSupplierList() {
        ReferenceDataService referenceDataService = new ReferenceDataServiceImpl();
        try {
            List<ProductSupplier> productSuppliers = new ArrayList<ProductSupplier>();
            ProductSupplier productSupplier = new ProductSupplier();
            productSupplier.setCode("All");
            productSupplier.setName("All");
            productSupplier.setId(-1);
            productSuppliers.add(productSupplier);
            productSuppliers.addAll(referenceDataService.getSuppliers());
            return productSuppliers;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return new ArrayList<ProductSupplier>();
        }
    }
}