package com.pc.retail.vo;


import com.pc.retail.interactor.MeasurementType;

public class ProductDO {

    private Product product;
    private double gstRate;
    private double cGSTRate;
    private double sGSTRate;
    private String baseProductCode;

    public ProductDO(Product product) {
        this.product = product;
    }

    public double getCurrentSellingPrice() {
        return product.getCurrentSellingPrice();
    }

    public String getGstTaxGroup() {
        return product.getGstTaxGroup();
    }

    public double getWeight() {
        return product.getWeight();
    }

    public String getCompanyCode() {
        return product.getCompanyCode();
    }

    public int getProductId() {
		return product.getProductId();
	}

	public String getBarcode() {
		return product.getBarcode();
	}

	public String getPrdCode() {
		return product.getPrdCode();
	}

	public String getPrdDesc() {
		return product.getPrdDesc();
	}

	public String getCategory() {
		return product.getCategory();
	}

	public MeasurementType getMeasurementType() {
		return product.getMeasurementType();
	}

	public String getSearchKey() {
		return product.getSearchKey();
	}

    public String getQtyUomCd() {
        return product.getQtyUomCd();
    }

    public String getPriceUomCd() {
        return product.getPriceUomCd();
    }

    public boolean isBaseProductFlag() {
        return product.isBaseProductFlag();
    }

    public double getcGSTRate() {
        return cGSTRate;
    }

    public void setcGSTRate(double cGSTRate) {
        this.cGSTRate = cGSTRate;
    }

    public double getsGSTRate() {
        return sGSTRate;
    }

    public void setsGSTRate(double sGSTRate) {
        this.sGSTRate = sGSTRate;
    }

    public double getGstRate() {
        return gstRate;
    }

    public void setGstRate(double gstRate) {
        this.gstRate = gstRate;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setProductId(int productId) {
        this.product.setProductId(productId);
    }

    public double getMRP() {
        return product.getMRP();
    }

    public void setBaseProductBarCode(String baseProductBarCode) {
        product.setBaseProductBarCode(baseProductBarCode);
    }

    public String getBaseProductBarCode() {
        return product.getBaseProductBarCode();
    }

    public void setBaseProductCode(String baseProductCode) {
        this.baseProductCode = baseProductCode;
    }

    public String getBaseProductCode() {
        return baseProductCode;
    }


}
