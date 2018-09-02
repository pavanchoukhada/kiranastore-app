package com.pc.retail.dao;

/**
 * Created by pavanc on 5/20/17.
 */
public enum InvoiceStatus {
    ALL(-1, "All"),
    NEW(0, "New"),
    CLOSED(1, "Closed"),
    INVENTORY_VERIFIED(2, "Inv Verified"),
    NON_VERIFIED(3, "Not Verified");

    private String name;
    private int ind;

    InvoiceStatus(int ind, String name){
        this.ind = ind;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getInd(){
        return ind;
    }

    static InvoiceStatus valueOf(int ind){
        if(ind == 0){
            return NEW;
        }else if (ind == 1){
            return CLOSED;
        }else if (ind == 2){
            return INVENTORY_VERIFIED;
        }else{
            return NON_VERIFIED;
        }
    }

    public static InvoiceStatus getInvoiceStatus(String name){
        if(name.equals(NEW.getName())){
            return NEW;
        }else if (name.equals(CLOSED.getName())){
            return CLOSED;
        }else if (name.equals(INVENTORY_VERIFIED.getName())){
            return INVENTORY_VERIFIED;
        }else{
            return NON_VERIFIED;
        }
    }
}
