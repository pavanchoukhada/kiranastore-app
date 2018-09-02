package com.pc.retail.vo;

public class ProductInvoiceDetail {

    private int invoiceId;
    private int productId;
    private String barCode;
    private int prdInventoryEntryId;
    private double qty; //auto populated from inv
    private double prdInvAmt;
    private double perUnitPrice;
    private double lumpsumCost;
    private double perUnitCost; //auto calculated

    private boolean markForW;
    private String qtyUOM;

    private ModificationStatus modificationStatus;

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getPrdInventoryEntryId() {
        return prdInventoryEntryId;
    }

    public void setPrdInventoryEntryId(int prdInventoryEntryId) {
        this.prdInventoryEntryId = prdInventoryEntryId;
    }

    public double getPerUnitPrice() {
        return perUnitPrice;
    }

    public void setPerUnitPrice(double perUnitPrice) {
        this.perUnitPrice = perUnitPrice;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getPrdInvAmt() {
        return prdInvAmt;
    }

    public void setPrdInvAmt(double prdInvAmt) {
        this.prdInvAmt = prdInvAmt;
    }

    public double getLumpsumCost() {
        return lumpsumCost;
    }

    public void setLumpsumCost(double lumpsumCost) {
        this.lumpsumCost = lumpsumCost;
    }

    public boolean isMarkForW() {
        return markForW;
    }

    public void setMarkForW(boolean markForW) {
        this.markForW = markForW;
    }

    public double getPerUnitCost() {
        return perUnitCost;
    }

    public void setPerUnitCost(double perUnitCost) {
        this.perUnitCost = perUnitCost;
    }

    public void setQtyUOM(String qtyUOM) {
        this.qtyUOM = qtyUOM;
    }

    public String getQtyUOM() {
        return qtyUOM;
    }

    public void setModificationStatus(ModificationStatus modificationStatus) {
        this.modificationStatus = modificationStatus;
    }

    public ModificationStatus getModificationStatus() {
        return modificationStatus;
    }
}
