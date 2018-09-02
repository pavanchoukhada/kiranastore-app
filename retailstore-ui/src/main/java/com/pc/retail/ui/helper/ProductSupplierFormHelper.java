package com.pc.retail.ui.helper;

import com.pc.retail.client.services.ReferenceDataService;
import com.pc.retail.client.services.ReferenceDataServiceImpl;
import com.pc.retail.dao.DataAccessException;
import com.pc.retail.interactor.KiranaAppResult;
import com.pc.retail.interactor.ResultType;
import com.pc.retail.ui.controller.ProductSupplierFormController;
import com.pc.retail.util.DataUtil;
import com.pc.retail.vo.ProductSupplier;

/**
 * Created by pavanc on 7/23/17.
 */
public class ProductSupplierFormHelper {


    public KiranaAppResult submitForm(ProductSupplierFormController productSupplierFormController){
        try{
            ProductSupplier productSupplier = transformForm(productSupplierFormController);
            if(productSupplier.getCode() == null || "".equals(productSupplier.getCode())){
                return new KiranaAppResult(ResultType.APP_ERROR,"Supplier Short Code is Mandatory");
            }
            ReferenceDataService referenceDataService = new ReferenceDataServiceImpl();
            return referenceDataService.saveSupplier(productSupplier);

        } catch (DataAccessException e) {
            e.printStackTrace();
            return new KiranaAppResult(ResultType.SYSTEM_ERROR,"System Error, Call Support");
        }
    }

    private ProductSupplier transformForm(ProductSupplierFormController productSupplierFormController) {
        ProductSupplier productSupplier = new ProductSupplier();
        productSupplier.setId(productSupplierFormController.getSupplierId());
        productSupplier.setCode(productSupplierFormController.getSupplierShortCode());
        productSupplier.setName(productSupplierFormController.getSupplierName());
        productSupplier.setMobileNo(DataUtil.getInteger(productSupplierFormController.getSupplierMobileNo()));
        productSupplier.setPhoneNo(productSupplierFormController.getSupplierPhoneNo());
        productSupplier.setAddress(productSupplierFormController.getSupplierAddress());
        productSupplier.setGdtnId(productSupplierFormController.getSupplierGSTNNo());
        return productSupplier;
    }

    private Integer getNumber(String numberStrVal) {
        if(isEmpty(numberStrVal))
            return 0;
        else
            return Integer.parseInt(numberStrVal);
    }

    private boolean isEmpty(String text) {
        return text == null || "".equals(text);
    }

}
