package com.pc.retail.api;

import java.util.List;

/**
 * Created by pavanc on 3/17/19.
 */
public class SupplierGSTReportDO {

    private String invoiceRef;
    private String supplierCode;
    private String invoiceDate;

    private double totalInvoiceAmt;
    private double totalCGSTAmount;
    private double totalSGSTAmount;
    private double totalGSTAmount;

    List<ProductGSTReportDO> productGSTReportDOList;
}
