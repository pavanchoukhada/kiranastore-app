package com.pc.retail.vo;

public class BillItems {

	private String barCode;
	private double quantity;
	private String qtyUomCd;	//Per Unit, 40KG Bags, 100KG Bags, 100G Packet etc
	private double salePrice;
	private String priceUOMCd;
	private double MRP;
	private double saving;

    public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public String getQtyUomCd() {
		return qtyUomCd;
	}
	public void setQtyUomCd(String qtyUomCd) {
		this.qtyUomCd = qtyUomCd;
	}
	public double getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}
	public String getPriceUOMCd() {
		return priceUOMCd;
	}
	public void setPriceUOMCd(String priceUOMCd) {
		this.priceUOMCd = priceUOMCd;
	}
	public double getMRP() {
		return MRP;
	}
	public void setMRP(double mRP) {
		MRP = mRP;
	}
	public double getSaving() {
		return saving;
	}
	public void setSaving(double saving) {
		this.saving = saving;
	}
}
