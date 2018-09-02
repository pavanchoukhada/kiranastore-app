package com.pc.retail.vo;

import java.util.Date;
import java.util.List;

public class BillingDetail {

	private String billNum;
	private Date billDate;
	private String custId;
	private List<BillItems> items;
	private double totalSaving;
	private double totalBillAmt;
	private double extraDisc;
	private double extraCharges;
	
	
	public String getBillNum() {
		return billNum;
	}
	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}
	public Date getBillDate() {
		return billDate;
	}
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}

	public double getTotalSaving() {
		return totalSaving;
	}
	public void setTotalSaving(double totalSaving) {
		this.totalSaving = totalSaving;
	}
	public double getTotalBillAmt() {
		return totalBillAmt;
	}
	public void setTotalBillAmt(double totalBillAmt) {
		this.totalBillAmt = totalBillAmt;
	}
	public double getExtraDisc() {
		return extraDisc;
	}
	public void setExtraDisc(double extraDisc) {
		this.extraDisc = extraDisc;
	}
	public double getExtraCharges() {
		return extraCharges;
	}
	public void setExtraCharges(double extraCharges) {
		this.extraCharges = extraCharges;
	}

	public List<BillItems> getItems() {
		return items;
	}

	public void addItem(BillItems billItem) {
		items.add(billItem);
	}

	
}
