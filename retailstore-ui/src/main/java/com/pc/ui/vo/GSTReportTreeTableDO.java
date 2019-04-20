package com.pc.ui.vo;

/**
 * Created by pavanc on 3/17/19.
 */
public class GSTReportTreeTableDO {
    private String gstCode;
    private String supplierCode;
    private String invoiceRef;
    private String productCode;
    private int barCode;
    private double totalInvoiceAmt;
    private double totalCGSTAmount;
    private double totalSGSTAmount;
    private double totalGSTAmount;
    private double totalTaxableAmount;
    private String invoiceDate;

    public String getGstCode() {
        return gstCode;
    }

    public void setGstCode(String gstCode) {
        this.gstCode = gstCode;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getInvoiceRef() {
        return invoiceRef;
    }

    public void setInvoiceRef(String invoiceRef) {
        this.invoiceRef = invoiceRef;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getBarCode() {
        return barCode;
    }

    public void setBarCode(int barCode) {
        this.barCode = barCode;
    }

    public double getTotalInvoiceAmt() {
        return totalInvoiceAmt;
    }

    public void setTotalInvoiceAmount(double totalInvoiceAmt) {
        this.totalInvoiceAmt = totalInvoiceAmt;
    }

    public double getTotalCGSTAmount() {
        return totalCGSTAmount;
    }

    public void setTotalCGSTAmount(double totalCGSTAmount) {
        this.totalCGSTAmount = totalCGSTAmount;
    }

    public double getTotalSGSTAmount() {
        return totalSGSTAmount;
    }

    public void setTotalSGSTAmount(double totalSGSTAmount) {
        this.totalSGSTAmount = totalSGSTAmount;
    }

    public double getTotalGSTAmount() {
        return totalGSTAmount;
    }

    public void setTotalGSTAmount(double totalGSTAmount) {
        this.totalGSTAmount = totalGSTAmount;
    }

    public void setTotalTaxableAmount(double totalTaxableAmount) {
        this.totalTaxableAmount = totalTaxableAmount;
    }

    public double getTotalTaxableAmount() {
        return totalTaxableAmount;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }
}
