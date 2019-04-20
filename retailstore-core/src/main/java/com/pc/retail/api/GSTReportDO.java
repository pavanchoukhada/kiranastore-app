package com.pc.retail.api;


import java.util.List;

/**
 * Created by pavanc on 3/10/19.
 */
public class GSTReportDO {
    private String gstCode;
    private double totalInvoiceAmt;
    private double totalTaxableAmt;
    private double totalCGSTAmount;
    private double totalSGSTAmount;
    private double totalGSTAmount;

    List<ProductGSTReportDO> productGSTReportDOList;

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

    public double getTotalTaxableAmt() {
        return totalTaxableAmt;
    }

    public void setTotalTaxableAmt(double totalTaxableAmt) {
        this.totalTaxableAmt = totalTaxableAmt;
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

    public List<ProductGSTReportDO> getProductGSTReportDOList() {
        return productGSTReportDOList;
    }

    public void setProductGSTReportDOList(List<ProductGSTReportDO> productGSTReportDOList) {
        this.productGSTReportDOList = productGSTReportDOList;
    }
}
