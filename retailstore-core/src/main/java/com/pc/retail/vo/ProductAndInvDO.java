package com.pc.retail.vo;


import com.pc.retail.interactor.MeasurementType;

import java.util.Date;

/**
 * Mainly used for Product Manager display
 */
public class ProductAndInvDO {

    private Product product;

    public ProductAndInvDO(Product product){
        this.product = product;
    }

    public int getProductId() {
        return this.product.getProductId();
    }

    public String getBarcode() {
        return product.getBarcode();
    }

    public String getPrdCode() {
        return product.getPrdCode();
    }

    public String getPrdDesc() {
        return product.getPrdDesc();
    }

    public String getCategory() {
        return product.getCategory();
    }

    public MeasurementType getMeasurementType() {
        return this.product.getMeasurementType();
    }

    public double getCurrentSellingPrice() {
        return product.getCurrentSellingPrice();
    }

    public String getPriceUomCd() {
        return product.getPriceUomCd();
    }

    public String getQtyUomCd() {
        return product.getQtyUomCd();
    }

    public String getSearchKey() {
        return product.getSearchKey();
    }

    public double getWeight() {
        return product.getWeight();
    }

    public String getGstTaxGroup() {
        return product.getGstTaxGroup();
    }

    public double getMRP() {
        return product.getMRP();
    }

    public double getCurrentAvailableQty() {
        return product.getProductCurrentInvDetail().getQuantity();
    }

    public Date getExpiryDate() {
        return product.getProductCurrentInvDetail().getExpiryDate();
    }

    public String getCompanyCode(){
        return product.getCompanyCode();
    }

    public Product getProduct() {
        return product;
    }
}
