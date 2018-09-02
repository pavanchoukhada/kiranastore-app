package com.pc.retail.vo;

/**
 * Created by pavanc on 6/10/17.
 */
public class ProductUOM {

    private String uomCode;
    private double factor;

    public String getUomCode() {
        return uomCode;
    }

    public void setUomCode(String uomCode) {
        this.uomCode = uomCode;
    }

    public double getFactor() {
        return factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }
}
