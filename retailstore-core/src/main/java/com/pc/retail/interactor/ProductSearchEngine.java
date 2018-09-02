package com.pc.retail.interactor;

import com.pc.retail.api.FilterKeyConstants;
import com.pc.retail.dao.util.FilterModel;
import com.pc.retail.vo.Product;

import java.util.ArrayList;
import java.util.List;

import static com.pc.retail.dao.util.FilterModel.*;


/**
 * Created by pavanc on 7/29/18.
 */
public class ProductSearchEngine {

    private List<Product> products;

    public ProductSearchEngine(List<Product> products) {
        this.products = products;
    }

    public List<Product> filterProducts(FilterModel filterModel) {
        List<Product> filteredProducts = new ArrayList<>();
        String searchKey = filterModel.getFilterValue(FilterKeyConstants.PRODUCT_DESC);
        String productCompany = filterModel.getFilterValue(FilterKeyConstants.PRODUCT_COMPANY);
        String productCategory = filterModel.getFilterValue(FilterKeyConstants.PRODUCT_CATEGORY);
        boolean validResult;
        for(Product product : products) {
            validResult = filterAsPerSearchAny(searchKey, product);
            if (validResult){
                validResult = filterAsPerProductCompany(productCompany, product);
            }
            if(validResult) {
                validResult = filterAsPerProductCategory(productCategory, product);
            }
            if(validResult){
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }

    private boolean filterAsPerSearchAny(String searchKey, Product product) {
        if (isNull(searchKey)) {
            return true;
        }
        if (product.getPrdCode().toUpperCase().contains(searchKey.toUpperCase())) {
            return true;
        }
        return false;
    }

    private boolean filterAsPerProductCompany(String productCompany, Product product) {
        if (isNull(productCompany)) {
            return true;
        }
        if (product.getCompanyCode() != null &&
                product.getCompanyCode().toUpperCase().contains(productCompany.toUpperCase())) {
            return true;
        }
        return false;
    }

    private boolean filterAsPerProductCategory(String productCategory, Product product) {
        if (isNull(productCategory)) {
            return true;
        }
        if (product.getCategory() != null &
                product.getCategory().toUpperCase().contains(productCategory.toUpperCase())) {
            return true;
        }
        return false;
    }

    private boolean isNull(String searchKey) {
        return searchKey == null || "".equals(searchKey);
    }
}


