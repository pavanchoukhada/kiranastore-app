package com.pc.retail.dao;

/**
 * Created by pavanc on 5/13/17.
 */
public enum InventoryTransactionType {
    NEW_INVENTORY(1, "New Inventory"),
    MODIFY_NEW_INVENTORY(2, "Modification Of Inventory"),
    CANCEL_NEW_INVENTORY(3, "Cancelled New Inv"),
    NEW_BILLING(4, "Billing"),
    MODIFY_BILLING(5, "Modication in Billing"),
    ADJUSTMENT(6, "Stock Adjustment"),
    LOSS(7, "Loss of Inventory");

    private int id;
    private String desc;

    private InventoryTransactionType(int id, String desc){
        this.id = id;
        this.desc = desc;
    }

    public int getId(){
        return this.id;
    }

    public String getDescription(){
        return this.desc;
    }

}
