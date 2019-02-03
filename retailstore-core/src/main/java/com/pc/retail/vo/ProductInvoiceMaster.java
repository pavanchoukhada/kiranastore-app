package com.pc.retail.vo;

import com.pc.retail.dao.InvoiceStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ProductInvoiceMaster {

    private int invoiceId;
	private String invoiceRefId;
	private double lumpsumCost;
	private double totalAmountExclGST;
	private double totalAmountInclAll;
	private int supplierId;
	private Date invoiceDate;
	private InvoiceStatus invoiceStatus;
	private List<ProductInventory> productInventoryList = new ArrayList<>();
	//payment Details
	private double paidAmount;
	private boolean fullyPaid;
	private String lastModifyDt;

	private double cGSTAmount;
	private double sGSTAmount;


    public String getInvoiceRefId() {
        return invoiceRefId;
    }

    public void setInvoiceRefId(String invoiceRefId) {
        this.invoiceRefId = invoiceRefId;
    }

    public InvoiceStatus getInvoiceStatus() {
        return invoiceStatus;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public double getTotalAmountInclAll() {
        return totalAmountInclAll;
    }

    public void setTotalAmountInclAll(double totalAmountInclAll) {
        this.totalAmountInclAll = totalAmountInclAll;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public void setInvoiceStatus(InvoiceStatus invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public List<ProductInventory> getProductInventoryList() {
        return productInventoryList;
    }

    public void setProductInventoryList(List<ProductInventory> productInventoryList) {
        this.productInventoryList = productInventoryList;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public boolean isFullyPaid() {
        return fullyPaid;
    }

    public void setFullyPaid(boolean fullyPaid) {
        this.fullyPaid = fullyPaid;
    }

    public String getLastModifyDt() {
        return lastModifyDt;
    }

    public void setLastModifyDt(String lastModifyDt) {
        this.lastModifyDt = lastModifyDt;
    }

    public void addProductInvoice(ProductInventory productInventory) {
        productInventoryList.add(productInventory);
    }

    public double getLumpsumCost() {
        return lumpsumCost;
    }

    public void setLumpsumCost(double lumpsumCost) {
        this.lumpsumCost = lumpsumCost;
    }

    public double getTotalAmountExclGST() {
        return totalAmountExclGST;
    }

    public void setTotalAmountExclGST(double totalAmountExclGST) {
        this.totalAmountExclGST = totalAmountExclGST;
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

    public double getGSTAmount(){
        return this.sGSTAmount + this.cGSTAmount;
    }
}
