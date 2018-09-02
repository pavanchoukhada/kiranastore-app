package com.pc.retail.vo;

import com.pc.retail.interactor.MeasurementType;

/**
 * saved in inventory cache
 * @author pavanc
 *
 */
public class ProductAndInvDetail {
	private Product product;
	private ProductInventory prdInvEntry;

	public ProductAndInvDetail(){
	    product = new Product();
	    prdInvEntry = new ProductInventory();
    }

    public void setProductId(int productId) {
        this.product.setProductId(productId);
        this.prdInvEntry.setProductId( productId );
    }

    public void setBarcode(String barcode) {
        this.product.setBarcode( barcode );
    }

    public void setPrdCode(String prdCode) {
        this.product.setPrdCode( prdCode );
    }

    public void setPrdDesc(String prdDesc) {
        this.product.setPrdDesc(prdDesc);
    }

    public void setCategory(String category) {
        this.product.setCategory( category );
    }

    public void setMeasurementType(MeasurementType measureType) {

        this.product.setMeasurementType( measureType );
    }

    public void setSearchKey(String searchKey) {
        this.product.setSearchKey( searchKey );
    }

    public void setInvoiceRef(String invoiceRef) {
        this.prdInvEntry.setInvoiceRef( invoiceRef );
    }

    public void setQuantity(double quantity) {
        this.prdInvEntry.setQuantity( quantity );
    }

    public void setQtyUomCd(String qtyUomCd) {
        this.product.setQtyUomCd( qtyUomCd );
    }

    public void setGSTCode(String gstTaxGroup) {
        this.product.setGstTaxGroup(gstTaxGroup);
    }


    public void setPriceUomCd(String priceUomCd) {
        this.product.setPriceUomCd( priceUomCd );
    }

    public void setPerUnitCost(double perUnitCost) {
        this.prdInvEntry.setPerUnitCost( perUnitCost );
    }

    public void setTotalCost(double totalCost) {
        this.prdInvEntry.setTotalCost( totalCost );
    }

    public void setCurrentSellingPrice(double currentSellingPrice) {
        this.product.setCurrentSellingPrice( currentSellingPrice );
    }

    public void setMRP(double MRP) {
        this.prdInvEntry.setMRP( MRP );
    }

    public void setExpiryDate(String expiryDate) {
        this.prdInvEntry.setExpiryDate( expiryDate );
    }

    public void setCompanyCode(String companyCode){
	    this.product.setCompanyCode( companyCode );
    }

    public void setIsBaseProduct(boolean isBaseProduct){
        this.product.setBaseProductFlag(isBaseProduct);
    }

    public void setBaseProduct(String baseProduct){
        this.product.setBaseProductBarCode( baseProduct );
    }

    public String getBaseProduct(){
        return this.product.getBaseProductBarCode();
    }

    public void setProductWeight(double weight) {
        this.product.setWeight( weight );
    }

    public double getProductWeight() {
        return this.product.getWeight();
    }

    public Product getProduct(){
        return this.product;
    }

    public ProductInventory getProductInventoryDetail(){
        return this.prdInvEntry;
    }

    public int getProductInvoiceId(){
        return this.getProductInventoryDetail().getPrdInvId();
    }

    public String getInvoiceRef(){
        return this.getProductInventoryDetail().getInvoiceRef();
    }
}
