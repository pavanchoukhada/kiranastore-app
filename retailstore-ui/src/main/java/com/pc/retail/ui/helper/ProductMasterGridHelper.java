package com.pc.retail.ui.helper;

import com.pc.retail.api.FilterKeyConstants;
import com.pc.retail.client.services.*;
import com.pc.retail.dao.DataAccessException;
import com.pc.retail.dao.util.FilterModel;
import com.pc.retail.interactor.KiranaStoreException;
import com.pc.retail.ui.controller.ProductMasterGridController;
import com.pc.retail.vo.Product;
import com.pc.retail.vo.ProductAndInvDO;
import com.pc.retail.vo.ProductCategory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by pavanc on 8/25/17.
 */
public class ProductMasterGridHelper {

    public List<String> getProductCategories() {
        ReferenceDataService referenceDataService = new ReferenceDataServiceImpl();
        try {
            Collection<ProductCategory> categories = referenceDataService.getProductCategories();
            List<String> prdCatValues = new ArrayList<>();
            prdCatValues.add("All");
            for(ProductCategory productCategory : categories){
                prdCatValues.add(productCategory.getCategoryName());
            }
            return prdCatValues;

        } catch (DataAccessException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Collection<String> getProductCompanies() {
        ReferenceDataService referenceDataService = new ReferenceDataServiceImpl();
        List<String> prdCompanies = new ArrayList<>();
        try {
            prdCompanies.add("All");
            prdCompanies.addAll(referenceDataService.getProductCompanies());
            return prdCompanies;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<ProductAndInvDO> getProducts(ProductMasterGridController productMasterGridController) throws KiranaStoreException {
        RetailAppClientLocator retailAppClientLocator = new RetailAppClientLocator();
        RetailAppClient retailAppClient = retailAppClientLocator.getRetailAppClient();
        ProductInventoryService productInventoryService =  retailAppClient.getProductInventoryService();
        try {
            String productCompany = productMasterGridController.getCompanyCB().getValue();
            String productCategory = productMasterGridController.getCategoryCB().getValue();
            String productDesc = productMasterGridController.getSearchKeyTxt().getText();
            FilterModel filterModel = new FilterModel();
            if(IsValid(productCompany)) {
                filterModel.addFilter(FilterKeyConstants.PRODUCT_COMPANY, productCompany);
            }
            if(IsValid(productCategory)) {
                filterModel.addFilter(FilterKeyConstants.PRODUCT_CATEGORY, productCategory);
            }
            if(IsValid(productDesc)) {
                filterModel.addFilter(FilterKeyConstants.PRODUCT_DESC, productDesc);
            }
            return productInventoryService.getAllProductsWithInvDetails(filterModel);
        } catch (KiranaStoreException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private boolean IsValid(String productCategory) {
        return !"All".equals(productCategory) && productCategory != null && !"".equals(productCategory);
    }

    public Product getProduct(String barcode) {
        RetailAppClientLocator retailAppClientLocator = new RetailAppClientLocator();
        RetailAppClient retailAppClient = retailAppClientLocator.getRetailAppClient();
        ProductInventoryService productInventoryService =  retailAppClient.getProductInventoryService();
        try {
            FilterModel filterModel = new FilterModel();
            filterModel.addFilter(FilterKeyConstants.BARCODE, barcode);
            return productInventoryService.getAllProductsWithInvDetails(filterModel).get(0).getProduct();

        }  catch (KiranaStoreException e) {
            e.printStackTrace();
            return null;
        }
    }
}
