package com.pc.retail.ui.helper;

import com.pc.retail.vo.ModificationStatus;
import com.pc.retail.vo.ProductInventory;
import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;

import java.util.Date;

/**
 * Created by pavanc on 9/16/18.
 */
public class ProductInventoryRow {
    ProductInventory productInventory;
    Hyperlink hyperlink;

    public ProductInventoryRow(ProductInventory productInventory) {
        this.productInventory = productInventory;
        hyperlink = new Hyperlink(productInventory.getInvoiceRef());
        hyperlink.setId(String.valueOf(productInventory.getInvoiceId()));
    }

    public void setOnMouseClickedAction(EventHandler eventHandler){
        hyperlink.setOnMouseClicked(eventHandler);
    }

    public int getPrdInvId() {
        return productInventory.getPrdInvId();
    }

    public void setPrdInvId(int prdInvId) {
        productInventory.setPrdInvId(prdInvId);
    }

    public String getInvoiceRef() {
        return productInventory.getInvoiceRef();
    }

    public Hyperlink getInvoiceRefWithHyperLink() {
        return hyperlink;
    }

    public void setInvoiceRef(String invoiceRef) {
        productInventory.setInvoiceRef(invoiceRef);
    }

    public double getQuantity() {
        return productInventory.getQuantity();
    }

    public void setQuantity(double quantity) {
        productInventory.setQuantity(quantity);
    }

    public double getPerUnitCost() {
        return productInventory.getPerUnitCost();
    }

    public void setPerUnitCost(double perUnitCost) {
        productInventory.setPerUnitCost(perUnitCost);
    }

    public double getTotalCost() {
        return productInventory.getTotalCost();
    }

    public void setTotalCost(double totalCost) {
        productInventory.setTotalCost(totalCost);
    }

    public boolean isMarkForW() {
        return productInventory.isMarkForW();
    }

    public void setMarkForW(boolean markForW) {
        productInventory.setMarkForW(markForW);
    }

    public double getPercToMarkForW() {
        return productInventory.getPercToMarkForW();
    }

    public void setPercToMarkForW(double percToMarkForW) {
        productInventory.setPercToMarkForW(percToMarkForW);
    }

    public double getMRP() {
        return productInventory.getMRP();
    }

    public void setMRP(double MRP) {
        productInventory.setMRP(MRP);
    }

    public double getRemainingQuantity() {
        return productInventory.getRemainingQuantity();
    }

    public void setRemainingQuantity(double remainingQuantity) {
        productInventory.setRemainingQuantity(remainingQuantity);
    }

    public String getBarCode() {
        return productInventory.getBarCode();
    }

    public void setBarCode(String barCode) {
        productInventory.setBarCode(barCode);
    }

    public int getProductId() {
        return productInventory.getProductId();
    }

    public void setProductId(int productId) {
        productInventory.setProductId(productId);
    }

    public Date getExpiryDate() {
        return productInventory.getExpiryDate();
    }

    public void setExpiryDate(Date expiryDate) {
        productInventory.setExpiryDate(expiryDate);
    }

    public int getStatus() {
        return productInventory.getStatus();
    }

    public void setStatus(int status) {
        productInventory.setStatus(status);
    }

    public String getQtyUOM() {
        return productInventory.getQtyUOM();
    }

    public void setQtyUOM(String qtyUOM) {
        productInventory.setQtyUOM(qtyUOM);
    }

    public void setOtherCost(Double otherCost) {
        productInventory.setOtherCost(otherCost);
    }

    public Double getOtherCost() {
        return productInventory.getOtherCost();
    }

    public ModificationStatus getModificationStatus() {
        return productInventory.getModificationStatus();
    }

    public void setModificationStatus(ModificationStatus modificationStatus) {
        productInventory.setModificationStatus(modificationStatus);
    }

    public String getProductCode() {
        return productInventory.getProductCode();
    }

    public void setProductCode(String productCode) {
        productInventory.setProductCode(productCode);
    }

    public double getPerUnitCostIncludingGST() {
        return productInventory.getPerUnitCostIncludingGST();
    }

    public void setPerUnitCostIncludingGST(double perUnitCostIncludingGST) {
        productInventory.setPerUnitCostIncludingGST(perUnitCostIncludingGST);
    }

    public void setOtherCost(double otherCost) {
        productInventory.setOtherCost(otherCost);
    }

    public double getTotalCostIncludingGST() {
        return productInventory.getTotalCostIncludingGST();
    }

    public void setTotalCostIncludingGST(double totalCostIncludingGST) {
        productInventory.setTotalCostIncludingGST(totalCostIncludingGST);
    }

    public double getFinalAmount() {
        return productInventory.getFinalAmount();
    }

    public void setFinalAmount(double finalAmount) {
        productInventory.setFinalAmount(finalAmount);
    }

    public double getPerUnitCostIncludingAll() {
        return productInventory.getPerUnitCostIncludingAll();
    }

    public void setPerUnitCostIncludingAll(double perUnitCostIncludingAll) {
        productInventory.setPerUnitCostIncludingAll(perUnitCostIncludingAll);
    }

    public double getcGSTRate() {
        return productInventory.getcGSTRate();
    }

    public void setcGSTRate(double cGSTRate) {
        productInventory.setcGSTRate(cGSTRate);
    }

    public double getsGSTRate() {
        return productInventory.getsGSTRate();
    }

    public void setsGSTRate(double sGSTRate) {
        productInventory.setsGSTRate(sGSTRate);
    }

    public double getcGSTAmount() {
        return productInventory.getcGSTAmount();
    }

    public void setcGSTAmount(double cGSTAmount) {
        productInventory.setcGSTAmount(cGSTAmount);
    }

    public double getsGSTAmount() {
        return productInventory.getsGSTAmount();
    }

    public void setsGSTAmount(double sGSTAmount) {
        productInventory.setsGSTAmount(sGSTAmount);
    }

    public double getSalePrice() {
        return productInventory.getSalePrice();
    }

    public void setSalePrice(double salePrice) {
        productInventory.setSalePrice(salePrice);
    }

    public String getSalePriceUOM() {
        return productInventory.getSalePriceUOM();
    }

    public void setSalePriceUOM(String salePriceUOM) {
        productInventory.setSalePriceUOM(salePriceUOM);
    }

    public int getInvoiceId() {
        return productInventory.getInvoiceId();
    }

    public void setInvoiceId(int invoiceId) {
        productInventory.setInvoiceId(invoiceId);
    }

    public Date getInvoiceDate() {
        return productInventory.getInvoiceDate();
    }

    public void setInvoiceDate(Date invoiceDate) {
        productInventory.setInvoiceDate(invoiceDate);
    }

    public int getSupplierId() {
        return productInventory.getSupplierId();
    }

    public void setSupplierId(int supplierId) {
        productInventory.setSupplierId(supplierId);
    }

    public String getSupplierCode() {
        return productInventory.getSupplierCode();
    }

    public void setSupplierCode(String supplierCode) {
        productInventory.setSupplierCode(supplierCode);
    }
}
