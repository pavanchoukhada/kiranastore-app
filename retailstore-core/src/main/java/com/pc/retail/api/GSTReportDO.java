package com.pc.retail.api;


import java.util.List;

/**
 * Created by pavanc on 3/10/19.
 */
public class GSTReportDO {
    private String gstCode;
    private double totalInvoiceAmt;
    private double totalCGSTAmount;
    private double totalSGSTAmount;
    private double totalGSTAmount;

    List<SupplierGSTReportDO> supplierGSTReportDOList;

    public String getGstCode() {
        return gstCode;
    }

    public void setGstCode(String gstCode) {
        this.gstCode = gstCode;
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

    public List<SupplierGSTReportDO> getSupplierGSTReportDOList() {
        return supplierGSTReportDOList;
    }

    public void setSupplierGSTReportDOList(List<SupplierGSTReportDO> supplierGSTReportDOList) {
        this.supplierGSTReportDOList = supplierGSTReportDOList;
    }
}
