package com.pc.retail.vo;

import java.util.Date;

/**
 * 
 * @author pavanc
 * when user add any product to inventory
 *
 */
public class ProductInventory {

    private int prdInvId;
    private String barCode;
    private int productId;
    private String productCode;
    private Date expiryDate;

	private String invoiceRef;
	private int invoiceId;

	private int status;	// accepted, rejected, sample order etc
	private double quantity;	//100 40KG Bags or 50 100KG Bags or 20 10KG Bags etc
    private double remainingQuantity; //quantity - soldQuantity first come first serve in base uom
    private String qtyUOM;

    private boolean markForW;
	private double percToMarkForW;

	private double MRP;
    private double salePrice;
    private String salePriceUOM;
    private double perUnitCost; //if it is weight then as per qty uom selected by user
    private double perUnitCostIncludingGST; //if it is weight then as per qtyUomCd
    private double otherCost;

    private double totalCost;   //perUnitCost * qty
    private double totalCostIncludingGST;   //perUnitCost * qty

    /**
     * totalCostIncludingGST + otherCost;
     */
    private double finalAmount;
    private double perUnitCostIncludingAll;   //perUnitCost * qty

    private double cGSTRate;
    private double sGSTRate;
    private double cGSTAmount;  //total amount for product
    private double sGSTAmount;

    public ProductInventory(){
    }


    private ModificationStatus modificationStatus;

    public int getPrdInvId() {
        return prdInvId;
    }

    public void setPrdInvId(int prdInvId) {
        this.prdInvId = prdInvId;
    }


    public String getInvoiceRef() {
        return invoiceRef;
    }

    public void setInvoiceRef(String invoiceRef) {
        this.invoiceRef = invoiceRef;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }


    public double getPerUnitCost() {
        return perUnitCost;
    }

    public void setPerUnitCost(double perUnitCost) {
        this.perUnitCost = perUnitCost;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public boolean isMarkForW() {
        return markForW;
    }

    public void setMarkForW(boolean markForW) {
        this.markForW = markForW;
    }

    public double getPercToMarkForW() {
        return percToMarkForW;
    }

    public void setPercToMarkForW(double percToMarkForW) {
        this.percToMarkForW = percToMarkForW;
    }

    public double getMRP() {
        return MRP;
    }

    public void setMRP(double MRP) {
        this.MRP = MRP;
    }

    public double getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(double remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public String getBarCode() {
		return this.barCode;
	}

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

	public int getProductId() {
		return this.productId;
	}

	public void setProductId(int productId) {
        this.productId = productId;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

    public String getQtyUOM() {
        return qtyUOM;
    }

    public void setQtyUOM(String qtyUOM) {
        this.qtyUOM = qtyUOM;
    }

    public void setOtherCost(Double otherCost) {
        this.otherCost = otherCost;
    }

    public Double getOtherCost() {
        return otherCost;
    }

    public ModificationStatus getModificationStatus() {
        return modificationStatus;
    }

    public void setModificationStatus(ModificationStatus modificationStatus) {
        this.modificationStatus = modificationStatus;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode){
        this.productCode = productCode;
    }

    public double getPerUnitCostIncludingGST() {
        return perUnitCostIncludingGST;
    }

    public void setPerUnitCostIncludingGST(double perUnitCostIncludingGST) {
        this.perUnitCostIncludingGST = perUnitCostIncludingGST;
    }

    public void setOtherCost(double otherCost) {
        this.otherCost = otherCost;
    }

    public double getTotalCostIncludingGST() {
        return totalCostIncludingGST;
    }

    public void setTotalCostIncludingGST(double totalCostIncludingGST) {
        this.totalCostIncludingGST = totalCostIncludingGST;
    }

    public double getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(double finalAmount) {
        this.finalAmount = finalAmount;
    }

    public double getPerUnitCostIncludingAll() {
        return perUnitCostIncludingAll;
    }

    public void setPerUnitCostIncludingAll(double perUnitCostIncludingAll) {
        this.perUnitCostIncludingAll = perUnitCostIncludingAll;
    }

    public double getcGSTRate() {
        return cGSTRate;
    }

    public void setcGSTRate(double cGSTRate) {
        this.cGSTRate = cGSTRate;
    }

    public double getsGSTRate() {
        return sGSTRate;
    }

    public void setsGSTRate(double sGSTRate) {
        this.sGSTRate = sGSTRate;
    }

    public double getcGSTAmount() {
        return cGSTAmount;
    }

    public void setcGSTAmount(double cGSTAmount) {
        this.cGSTAmount = cGSTAmount;
    }

    public double getsGSTAmount() {
        return sGSTAmount;
    }

    public void setsGSTAmount(double sGSTAmount) {
        this.sGSTAmount = sGSTAmount;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public String getSalePriceUOM() {
        return salePriceUOM;
    }

    public void setSalePriceUOM(String salePriceUOM) {
        this.salePriceUOM = salePriceUOM;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }
}
