package com.pc.retail.vo;


import com.pc.retail.interactor.MeasurementType;

public class Product {

	private int productId;
	private String barcode;
	private String prdCode;	//short desc
	private String prdDesc; //long description
	private String category;	//spices, tooth-paste, pulses etc

    private MeasurementType measurementType;	//weight or count
    private double currentSellingPrice;
	private String priceUomCd;	//per unit for price
	private String qtyUomCd;     //base qty uom

	private String searchKey;	//comma separated various name, should support phonetic search

    private String companyCode;
    private String baseProductCode;
    private double weight;


    private boolean baseProductFlag;

    private String gstTaxGroup;
    private double MRP;


    public double getCurrentSellingPrice() {
        return currentSellingPrice;
    }

    public void setCurrentSellingPrice(double currentSelllingPrice) {
        this.currentSellingPrice = currentSelllingPrice;
    }

    public String getGstTaxGroup() {
        return gstTaxGroup;
    }

    public void setGstTaxGroup(String gstTaxGroup) {
        this.gstTaxGroup = gstTaxGroup;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public String getBaseProductBarCode() {
        return baseProductCode;
    }

    public void setBaseProductBarCode(String baseProductCode) {
        this.baseProductCode = baseProductCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getPrdCode() {
		return prdCode;
	}

	public void setPrdCode(String prdCode) {
		this.prdCode = prdCode;
	}

	public String getPrdDesc() {
		return prdDesc;
	}

	public void setPrdDesc(String prdDesc) {
		this.prdDesc = prdDesc;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public MeasurementType getMeasurementType() {
		return measurementType;
	}

	public void setMeasurementType(MeasurementType measureType) {
		this.measurementType = measureType;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

    public String getQtyUomCd() {
        return qtyUomCd;
    }

    public void setQtyUomCd(String qtyUomCd) {
        this.qtyUomCd = qtyUomCd;
    }

    public String getPriceUomCd() {
        return priceUomCd;
    }

    public void setPriceUomCd(String priceUomCd) {
        this.priceUomCd = priceUomCd;
    }

    public boolean isBaseProductFlag() {
        return baseProductFlag;
    }

    public void setBaseProductFlag(boolean baseProductFlag) {
        this.baseProductFlag = baseProductFlag;
    }

    public double getMRP() {
        return MRP;
    }
}
