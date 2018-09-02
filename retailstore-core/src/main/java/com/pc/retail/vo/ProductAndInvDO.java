package com.pc.retail.vo;


import com.pc.retail.interactor.MeasurementType;

import java.util.Date;

/**
 * Mainly used for Product Manager display
 */
public class ProductAndInvDO {

    private double MRP;
    private double currentAvailableQty;
    private Date expiryDate;
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
        return MRP;
    }

    public void setMRP(double MRP) {
        this.MRP = MRP;
    }

    public double getCurrentAvailableQty() {
        return currentAvailableQty;
    }

    public void setCurrentAvailableQty(double currentAvailableQty) {
        this.currentAvailableQty = currentAvailableQty;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCompanyCode(){
        return product.getCompanyCode();
    }
    public Product getProduct() {
        return product;
    }
}
