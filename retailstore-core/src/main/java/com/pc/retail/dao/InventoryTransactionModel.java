package com.pc.retail.dao;

import java.util.Date;

/**
 * Created by pavanc on 5/13/17.
 */
public class InventoryTransactionModel {
    private int productId;
    private InventoryTransactionType transactionType;
    private int externalRef;
    private double quantity;
    private String transDesc;
    private double transPrice;
    private Date transDate;
    private String barCode;

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Date getTransDate() {
        return transDate;
    }

    public InventoryTransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(InventoryTransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public int getExternalRef() {
        return externalRef;
    }

    public void setExternalRef(int externalRef) {
        this.externalRef = externalRef;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getTransDesc() {
        return transDesc;
    }

    public void setTransDesc(String transDesc) {
        this.transDesc = transDesc;
    }

    public double getTransPrice() {
        return transPrice;
    }

    public void setTransPrice(double transPrice) {
        this.transPrice = transPrice;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }
}
