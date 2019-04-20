package com.pc.retail.api;

import java.util.List;

/**
 * Created by pavanc on 3/31/19.
 */
public class InvoiceGSTReportDO {

    private String invoiceRef;
    private String invoiceDate;
    private double invoiceAmt;
    private double totalCGSTAmount;
    private double totalSGSTAmount;
    private double totalGSTAmount;
    private List<GSTReportDO> gstReportDOList;
    private double totalTaxableAmount;

    public String getInvoiceRef() {
        return invoiceRef;
    }

    public void setInvoiceRef(String invoiceRef) {
        this.invoiceRef = invoiceRef;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public double getInvoiceAmt() {
        return invoiceAmt;
    }

    public void setInvoiceAmt(double invoiceAmt) {
        this.invoiceAmt = invoiceAmt;
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

    public List<GSTReportDO> getGstReportDOList() {
        return gstReportDOList;
    }

    public void setGstReportDOList(List<GSTReportDO> gstReportDOList) {
        this.gstReportDOList = gstReportDOList;
    }

    public double getTotalTaxableAmount() {
        return totalTaxableAmount;
    }

    public void setTotalTaxableAmount(double totalTaxableAmount) {
        this.totalTaxableAmount = totalTaxableAmount;
    }
}
