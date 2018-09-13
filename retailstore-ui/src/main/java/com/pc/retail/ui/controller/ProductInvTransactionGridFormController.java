package com.pc.retail.ui.controller;

import com.pc.retail.api.FilterKeyConstants;
import com.pc.retail.ui.event.handler.GetProductDetailHandler;
import com.pc.retail.ui.event.handler.UpdateProductDetail;
import com.pc.retail.ui.helper.ProductInventoryGridFormHelper;
import com.pc.retail.vo.ProductDO;
import com.pc.retail.vo.ProductInventory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by pavanc on 8/2/17.
 */
public class ProductInvTransactionGridFormController implements Initializable, UpdateProductDetail{

    @FXML
    TextField barcodeTxt;
    @FXML
    TextField productIdTxt;

    @FXML
    TableView<ProductInventory> productInventoryMasterGrid;

    int productId = -1;

    ObservableList<ProductInventory> productInventoryList = FXCollections.observableArrayList();

    private ProductInventoryGridFormHelper productInventoryGridFormHelper;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        productInventoryGridFormHelper = new ProductInventoryGridFormHelper();
        initializeInvGridTable();
        initializeBarCodeTxtListeners();
        initializeProductIdTxtListeners();
    }

    private void initializeProductIdTxtListeners() {
        productIdTxt.setOnKeyPressed(getKeyPressHandler(FilterKeyConstants.PRODUCT_ID, productIdTxt));
    }

    private void initializeBarCodeTxtListeners() {
        barcodeTxt.setOnKeyPressed(getKeyPressHandler(FilterKeyConstants.BARCODE, barcodeTxt));
    }

    private EventHandler<? super KeyEvent> getKeyPressHandler(String lookupKey, TextField textField) {
        return new GetProductDetailHandler(this, lookupKey, textField);
    }

    private void initializeInvGridTable() {

        ObservableList productInvEntryGridColumns = productInventoryMasterGrid.getColumns();
        TableColumn stringTableColumn = createStringTableColumn("Invoice Ref", 120, "invoiceRef", "gridInvoiceRef");
        
        productInvEntryGridColumns.add(stringTableColumn);
        productInvEntryGridColumns.add(createStringTableColumn("Invoice Date", 120, "invoiceDate", "gridInvoiceDate"));
        productInvEntryGridColumns.add(createStringTableColumn("Supplier", 120, "supplierCode", "gridSupplier"));
        productInvEntryGridColumns.add(createStringTableColumn("Barcode", 120, "barCode", "gridBarCodeId"));
        productInvEntryGridColumns.add(createStringTableColumn("Product Code", 120, "productCode", "gridProductCode"));
        productInvEntryGridColumns.add(createNumericTableColumn("Quantity", 120, "quantity", "gridQtyId"));
        productInvEntryGridColumns.add(createStringTableColumn("Qty UOM", 50, "qtyUOM", "gridQtyUOMId"));
        productInvEntryGridColumns.add(createNumericTableColumn("MRP", 100, "MRP", "gridMRPId"));
        productInvEntryGridColumns.add(createNumericTableColumn("Per Unit Cost", 100, "perUnitCost", "gridPerUnitCostId"));
        productInvEntryGridColumns.add(createNumericTableColumn("CGST", 100, "totalCost", "cGSTAmount"));
        productInvEntryGridColumns.add(createNumericTableColumn("SGST", 100, "totalCost", "sGSTAmount"));
        productInvEntryGridColumns.add(createNumericTableColumn("Per Unit Cost(Incl GST)", 100, "perUnitCostIncludingGST", "perUnitCostIncludingGST"));
        productInvEntryGridColumns.add(createNumericTableColumn("Other Cost", 100, "otherCost", "otherCost"));
        productInvEntryGridColumns.add(createNumericTableColumn("Total Cost", 100, "finalAmount", "finalAmount"));
        productInventoryMasterGrid.setItems(productInventoryList);
    }

    private TableColumn createStringTableColumn(String columnText, int columnWidth, String propertyName, String columnId) {
        TableColumn productIdCol = new TableColumn(columnText);
        productIdCol.setMinWidth(columnWidth);
        productIdCol.setCellValueFactory(new PropertyValueFactory<ProductInventory, String>(propertyName));
        productIdCol.setId(columnId);
        return productIdCol;
    }

    private TableColumn createNumericTableColumn(String columnText, int columnWidth, String propertyName, String columnId) {
        TableColumn productIdCol = new TableColumn(columnText);
        productIdCol.setMinWidth(columnWidth);
        productIdCol.setCellValueFactory(new PropertyValueFactory<ProductInventory, String>(propertyName));
        productIdCol.setId(columnId);
        return productIdCol;
    }


    public void initData(int productId) {
        this.productId = productId;
        productInventoryList.addAll(productInventoryGridFormHelper.getInventories(productId));
    }

    @Override
    public void updateForm(ProductDO product) {
        this.productId = product.getProductId();
        productInventoryList.clear();
        productInventoryList.addAll(productInventoryGridFormHelper.getInventories(productId));

    }
}
