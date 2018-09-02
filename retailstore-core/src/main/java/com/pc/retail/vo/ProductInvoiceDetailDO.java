package com.pc.retail.vo;

/**
 * Created by pavanc on 7/23/17.
 */
public class ProductInvoiceDetailDO {

    private int invoiceId;
    private int productId;
    private String productShortDesc;
    private int prdInventoryEntryId;
    private double qty; //auto populated from inv
    private double prdInvAmt;
    private double lumpsumCost;
    private double perUnitPrice; //auto calculated
    private double perUnitCost; //auto calculated
    private ModificationStatus modificationStatus ;
    private Object qtyUOM;

    public ProductInvoiceDetailDO(){

    }
    
    public ProductInvoiceDetailDO(ProductInvoiceDetail productInvoiceDetail) {
        this.invoiceId = productInvoiceDetail.getInvoiceId();
        this.productId = productInvoiceDetail.getProductId();
        this.prdInventoryEntryId = productInvoiceDetail.getPrdInventoryEntryId();
        this.qty = productInvoiceDetail.getQty();
        this.qtyUOM = productInvoiceDetail.getQtyUOM();
        this.prdInvAmt = productInvoiceDetail.getPrdInvAmt();
        this.lumpsumCost = productInvoiceDetail.getLumpsumCost();
        this.perUnitCost = productInvoiceDetail.getPerUnitCost();
        this.perUnitPrice = productInvoiceDetail.getPerUnitPrice();
        this.modificationStatus = ModificationStatus.NO_CHANGE;

    }


    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductShortDesc() {
        return productShortDesc;
    }

    public void setProductShortDesc(String productShortDesc) {
        this.productShortDesc = productShortDesc;
    }

    public int getPrdInventoryEntryId() {
        return prdInventoryEntryId;
    }

    public void setPrdInventoryEntryId(int prdInventoryEntryId) {
        this.prdInventoryEntryId = prdInventoryEntryId;
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

    public double getPerUnitPrice() {
        return perUnitPrice;
    }

    public void setPerUnitPrice(double perUnitPrice) {
        this.perUnitPrice = perUnitPrice;
    }

    public double getPerUnitCost() {
        return perUnitCost;
    }

    public void setPerUnitCost(double perUnitCost) {
        this.perUnitCost = perUnitCost;
    }

    public void setQtyUOM(Object qtyUOM) {
        this.qtyUOM = qtyUOM;
    }

    public Object getQtyUOM() {
        return qtyUOM;
    }
}
