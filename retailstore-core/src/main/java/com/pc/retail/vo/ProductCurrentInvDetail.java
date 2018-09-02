package com.pc.retail.vo;

import java.util.Date;

public class ProductCurrentInvDetail {
	
	private int productId;
	private String barCode;
	private double quantity;	//quantity in base uom cd or count
	private String qtyUomCd; 	//base uom code
	private double CSP;
	private String priceUomCd;
	private double MRP;
	private double costPrice;   //as per last inv transaction
    private String lastModifyDt;

    private ModificationStatus modificationStatus;
    private Date expiryDate;

    public String getLastModifyDt() {
        return lastModifyDt;
    }

    public void setLastModifyDt(String lastModifyDt) {
        this.lastModifyDt = lastModifyDt;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getQtyUomCd() {
        return qtyUomCd;
    }

    public void setQtyUomCd(String qtyUomCd) {
        this.qtyUomCd = qtyUomCd;
    }

    public double getCSP() {
        return CSP;
    }

    public void setCSP(double CSP) {
        this.CSP = CSP;
    }

    public String getPriceUomCd() {
        return priceUomCd;
    }

    public void setPriceUomCd(String priceUomCd) {
        this.priceUomCd = priceUomCd;
    }

    public double getMRP() {
        return MRP;
    }

    public void setMRP(double MRP) {
        this.MRP = MRP;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public ModificationStatus getModificationStatus() {
        return modificationStatus;
    }

    public void setModificationStatus(ModificationStatus modificationStatus) {
        this.modificationStatus = modificationStatus;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }
}
