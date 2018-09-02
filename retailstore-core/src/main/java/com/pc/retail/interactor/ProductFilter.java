package com.pc.retail.interactor;

/**
 * Created by pavanc on 8/25/18.
 */
public class ProductFilter {

    private String barCode;
    private int productId;
    private Boolean isBaseProductFlag;

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductId() {
        return productId;
    }

    public Boolean getIsBaseProductFlag() {
        return isBaseProductFlag;
    }

    public void setIsBaseProductFlag(Boolean isBaseProductFlag) {
        this.isBaseProductFlag = isBaseProductFlag;
    }
}
