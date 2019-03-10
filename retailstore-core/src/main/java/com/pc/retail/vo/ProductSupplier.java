package com.pc.retail.vo;

/**
 * Created by pavanc on 6/28/17.
 */
public class ProductSupplier {

    private int id;
    private String code;
    private String gstnId;
    private String name;
    private String address;
    private String mobileNo;
    private String phoneNo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGstnId() {
        return gstnId;
    }

    public void setGstnId(String gdtnId) {
        this.gstnId = gdtnId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @Override
    public String toString(){
        return this.getCode();
    }
}
