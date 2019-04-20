package com.pc.retail.api;

import java.util.List;

/**
 * Created by pavanc on 3/17/19.
 */
public class SupplierGSTReportDO {

    private String supplierCode;
    private String supplierGSTNo;
    private double totalInvoiceAmt;
    private double totalCGSTAmount;
    private double totalSGSTAmount;
    private double totalGSTAmount;

    List<InvoiceGSTReportDO> invoiceGSTReportDOList;
    private double totalTaxableAmount;

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public double getTotalInvoiceAmt() {
        return totalInvoiceAmt;
    }

    public void setTotalInvoiceAmt(double totalInvoiceAmt) {
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

    public List<InvoiceGSTReportDO> getInvoiceGSTReportDOList() {
        return invoiceGSTReportDOList;
    }

    public void setInvoiceGSTReportDOList(List<InvoiceGSTReportDO> invoiceGSTReportDOList) {
        this.invoiceGSTReportDOList = invoiceGSTReportDOList;
    }

    public void setTotalTaxableAmount(double totalTaxableAmount) {
        this.totalTaxableAmount = totalTaxableAmount;
    }

    public double getTotalTaxableAmount() {
        return totalTaxableAmount;
    }

    public String getSupplierGSTNo() {
        return supplierGSTNo;
    }

    public void setSupplierGSTNo(String supplierGSTNo) {
        this.supplierGSTNo = supplierGSTNo;
    }
}
