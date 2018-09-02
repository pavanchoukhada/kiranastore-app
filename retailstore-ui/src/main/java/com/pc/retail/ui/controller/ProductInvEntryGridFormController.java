package com.pc.retail.ui.controller;

import com.pc.retail.api.FilterKeyConstants;
import com.pc.retail.dao.InvoiceStatus;
import com.pc.retail.interactor.KiranaAppResult;
import com.pc.retail.interactor.MeasurementType;
import com.pc.retail.interactor.ResultType;
import com.pc.retail.ui.event.handler.GetProductDetailHandler;
import com.pc.retail.ui.event.handler.UpdateProductDetail;
import com.pc.retail.ui.helper.ProductInvEntryGridFormHelper;
import com.pc.retail.util.DataUtil;
import com.pc.retail.vo.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by pavanc on 8/2/17.
 */
public class ProductInvEntryGridFormController implements Initializable, UpdateProductDetail{

    @FXML
    TextField invoiceNoTxt;
    @FXML
    ComboBox<String> invoiceStatusCB;
    @FXML
    DatePicker invoiceDateDP;
    @FXML
    ComboBox<ProductSupplier> supplierCB;
    @FXML
    TextField prdBarcodeTxt;
    @FXML
    TextField productIdTxt;
    @FXML
    TextField prdCodeTxt;

    @FXML
    TextField prdQtyTxt;
    @FXML
    ComboBox qtyUOMCB;
    @FXML
    TextField mrpTxt;
    @FXML
    TextField costPriceTxt;
    @FXML
    TextField lumpsumCostTxt;
    @FXML
    TextField productTotalCostAmountTxt;
    @FXML
    DatePicker expiryDP;

    @FXML
    TextField totalAmountTxt;
    @FXML
    TextField extraCostTxt;
    @FXML
    TextField totalInvoiceAmountTxt;

    @FXML
    TableView<ProductInventory> productInvEntryGrid;

    int invoiceId = -1;

    ObservableList<ProductInventory> productInventoryList = FXCollections.observableArrayList();

    private ProductInvEntryGridFormHelper productInvEntryGridFormHelper;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        productInvEntryGridFormHelper = new ProductInvEntryGridFormHelper();
        populateForm();
        initializeInvGridTable();

        productInvEntryGrid.setRowFactory(getTableRowSelectionHandler());
        initializeBarCodeTxtListeners();
        initializeProductIdTxtListeners();
        prdBarcodeTxt.requestFocus();
        invoiceDateDP.setValue(LocalDate.now());
        prdQtyTxt.focusedProperty().addListener(getTotalProductInvAmountCalcListener());
        lumpsumCostTxt.focusedProperty().addListener(getTotalProductInvAmountCalcListener());
        costPriceTxt.focusedProperty().addListener(getTotalProductInvAmountCalcListener());
        extraCostTxt.focusedProperty().addListener(getTotalInvAmountCalcListener());
        totalAmountTxt.setDisable(true);
        totalInvoiceAmountTxt.setDisable(true);
    }

    private ChangeListener<Boolean> getTotalProductInvAmountCalcListener() {
        return new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue.booleanValue()) {
                    System.out.println("Changed");
                    double prdQty = DataUtil.getDouble(prdQtyTxt.getText());
                    double perUnitCost = DataUtil.getDouble(costPriceTxt.getText());
                    double extraCost = DataUtil.getDouble(lumpsumCostTxt.getText());
                    productTotalCostAmountTxt.setText(String.valueOf(calculateTotalAmountPerProduct(prdQty, perUnitCost, extraCost)));
                }
            }

        };
    }

    private ChangeListener<Boolean> getTotalInvAmountCalcListener() {
        return new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue.booleanValue()) {
                    System.out.println("Changed");
                    calcTotalInvoiceAmount();
                }
            }

        };
    }

    private void calcTotalInvoiceAmount() {
        double totalAmount = DataUtil.getDouble(totalAmountTxt.getText());
        double extraCost = DataUtil.getDouble(extraCostTxt.getText());
        totalInvoiceAmountTxt.setText(String.valueOf(DataUtil.round2(totalAmount + extraCost)));
    }


    private double calculateTotalAmountPerProduct(double prdQty, double perUnitCost, double extraCost) {
        double result = prdQty * perUnitCost + extraCost;
        return DataUtil.round2(result);
    }

    private Callback getTableRowSelectionHandler() {
        return tv -> {
            TableRow<ProductInventory> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                    ProductInventory rowData = row.getItem();
                    populateFields(row);

                }
            });
            return row ;
        };
    }

    private void populateFields(TableRow<ProductInventory> row) {
        ProductInventory productInventory = row.getItem();
        productIdTxt.setText(String.valueOf(productInventory.getProductId()));
        prdBarcodeTxt.setText(productInventory.getBarCode());
        prdCodeTxt.setText(productInventory.getProductCode());
        prdQtyTxt.setText(String.valueOf(productInventory.getQuantity()));
        qtyUOMCB.setValue(String.valueOf(productInventory.getQtyUOM()));
        lumpsumCostTxt.setText(String.valueOf(productInventory.getOtherCost()));
        costPriceTxt.setText(String.valueOf(productInventory.getPerUnitCost()));
        productTotalCostAmountTxt.setText(String.valueOf(productInventory.getTotalCost()));
        expiryDP.getEditor().setText(productInventory.getExpiryDate());
        mrpTxt.setText(String.valueOf(productInventory.getQuantity()));
    }

    private void initializeInvGridTable() {

        ObservableList productInvEntryGridColumns = productInvEntryGrid.getColumns();
        productInvEntryGridColumns.add(createStringTableColumn("Product Id", 120, "productId", "gridProductId"));
        productInvEntryGridColumns.add(createStringTableColumn("Product Code", 120, "productCode", "gridProductCode"));
        productInvEntryGridColumns.add(createNumericTableColumn("Quantity", 120, "quantity", "gridQtyId"));
        productInvEntryGridColumns.add(createStringTableColumn("Qty UOM", 50, "qtyUOM", "gridQtyUOMId"));
        productInvEntryGridColumns.add(createNumericTableColumn("MRP", 100, "MRP", "gridMRPId"));
        productInvEntryGridColumns.add(createNumericTableColumn("Per Unit Cost", 100, "perUnitCost", "gridPerUnitCostId"));
        productInvEntryGridColumns.add(createNumericTableColumn("Lumpsum Cost", 100, "lumpsumCost", "gridLumpsumId"));
        productInvEntryGridColumns.add(createNumericTableColumn("Total Cost", 100, "totalCost", "gridTotalCostId"));
        productInvEntryGridColumns.add(createStringTableColumn("Expiry Date", 100, "expiryDate", "gridExpiryDateId"));

        productInvEntryGrid.setItems(productInventoryList);
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

    private void populateForm() {
        populateInvoiceStatusCB();
        populateSupplierCB();
        populateUOMCB();
    }

    private void populateUOMCB() {
        ObservableList<String> uomCodeList
                = FXCollections.observableArrayList(productInvEntryGridFormHelper.getUOMList());
        qtyUOMCB.setItems(uomCodeList);
    }

    private void populateSupplierCB() {
        ObservableList<ProductSupplier> supplierList
                = FXCollections.observableArrayList(productInvEntryGridFormHelper.getSupplierList());
        supplierCB.setItems(supplierList);
    }


    private void populateInvoiceStatusCB() {
        ObservableList<String> invoiceStatusList
                = FXCollections.observableArrayList(productInvEntryGridFormHelper.getInvoiceStatusList());
        invoiceStatusCB.setItems(invoiceStatusList);
        invoiceStatusCB.setValue(InvoiceStatus.NEW.getName());
    }

    public void updateToInvTable(ActionEvent actionEvent) {
        String message = validateForm();
        ProductInventory productInventory = null;
        if(DataUtil.isEmpty(message)) {
            try {
                productInventory = getProductInvoiceDO();
            } catch (Exception e) {
                message = e.getMessage();
            }
        }
        if(!DataUtil.isEmpty(message)){
            generateResponseToUser(Alert.AlertType.ERROR, message);
            return;
        }
        int index = getArrayIndexForProductId(productInventory.getProductId());
        if(index >= 0){
            productInventoryList.set(index, productInventory);
        }else {
            productInventoryList.add(productInventory);
        }
        calcTotalAmount();
        clearProductInvFields();
    }

    private void calcTotalAmount() {
        double totalAmount = 0.0d;
        for(ProductInventory productInventory : productInventoryList){
            totalAmount = totalAmount + calculateTotalAmountPerProduct(productInventory.getQuantity(), productInventory.getPerUnitCost(), productInventory.getOtherCost());
        }
        totalAmountTxt.setText(String.valueOf(totalAmount));
        calcTotalInvoiceAmount();

    }

    private ButtonType generateResponseToUser(Alert.AlertType warning, String message) {
        Alert alert = new Alert(warning, message);
        Optional<ButtonType> buttonType = alert.showAndWait();
        return buttonType.get();
    }


    private String validateForm() {
        if(DataUtil.isEmpty(productIdTxt.getText()) && DataUtil.isEmpty(prdBarcodeTxt.getText())){
            return "Please enter Barcode or Product Id";
        }

        if(DataUtil.isEmpty(prdQtyTxt.getText())){
            return "Pleae enter Quantity";
        }
        return "";

    }

    private int getArrayIndexForProductId(int productId) {
        for(int index = 0; index < productInventoryList.size(); index++){
            ProductInventory productInventory = productInventoryList.get(index);
            if(productInventory.getProductId() == productId){
                return index;
            }
        }
        return -1;
    }

    private ProductInventory getProductInvoiceDO() throws Exception{
        ProductInventory productInventory = new ProductInventory();
        productInventory.setProductId(DataUtil.getIntegerValue(productIdTxt.getText(), "Product Id"));
        productInventory.setBarCode(prdBarcodeTxt.getText());
        productInventory.setProductCode(prdCodeTxt.getText());
        productInventory.setQuantity(DataUtil.getDoubleValue(prdQtyTxt.getText(), "Quantity"));
        productInventory.setQtyUOM((String)qtyUOMCB.getValue());
        productInventory.setMRP(DataUtil.getDoubleValue(mrpTxt.getText(), "MRP"));
        productInventory.setOtherCost(DataUtil.getDoubleValue(lumpsumCostTxt.getText(), "Lumpsum Cost"));
        productInventory.setPerUnitCost(DataUtil.getDoubleValue(costPriceTxt.getText(), "Per Unit Cost Price"));
        productInventory.setTotalCost(DataUtil.getDoubleValue(productTotalCostAmountTxt.getText(),"Total Cost Amount"));
        productInventory.setExpiryDate(DataUtil.getDate(expiryDP.getValue()));

        return productInventory;
    }



    public void submitForm(ActionEvent actionEvent) {
        KiranaAppResult kiranaAppResult = productInvEntryGridFormHelper.submit(this);
        showResponseToUser(kiranaAppResult);
    }

    public void closeForm(ActionEvent actionEvent) {
    }

    public void resetForm(ActionEvent actionEvent) {
    }

    public void clearFields(ActionEvent actionEvent) {
        clearProductInvFields();
    }

    private void clearProductInvFields() {
        productIdTxt.clear();
        prdBarcodeTxt.clear();
        prdCodeTxt.clear();
        prdQtyTxt.clear();
        qtyUOMCB.getEditor().clear();
        lumpsumCostTxt.clear();
        costPriceTxt.clear();
        productTotalCostAmountTxt.clear();
        expiryDP.getEditor().clear();
        prdBarcodeTxt.clear();
        prdCodeTxt.clear();
        mrpTxt.clear();
        prdBarcodeTxt.requestFocus();
    }

    public List<ProductInventory> getProductInventoryList() {
        return productInventoryList;
    }

    public void setProductInventoryList(List<ProductInventory> productInventoryList) {
        this.productInventoryList.addAll(productInventoryList);
    }

    public TextField getInvoiceNoTxt() {
        return invoiceNoTxt;
    }

    public void setInvoiceNoTxt(TextField invoiceNoTxt) {
        this.invoiceNoTxt = invoiceNoTxt;
    }

    public ComboBox<String> getInvoiceStatusCB() {
        return invoiceStatusCB;
    }

    public void setInvoiceStatusCB(ComboBox<String> invoiceStatusCB) {
        this.invoiceStatusCB = invoiceStatusCB;
    }

    public DatePicker getInvoiceDateDP() {
        return invoiceDateDP;
    }

    public void setInvoiceDateDP(DatePicker invoiceDateDP) {
        this.invoiceDateDP = invoiceDateDP;
    }

    public ComboBox<ProductSupplier> getSupplierCB() {
        return supplierCB;
    }

    public void setSupplierCB(ComboBox<ProductSupplier> supplierCB) {
        this.supplierCB = supplierCB;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    private void showResponseToUser(KiranaAppResult kiranaAppResult) {
        if(kiranaAppResult.getResultType() == ResultType.SUCCESS){
            generateResponseToUser(Alert.AlertType.INFORMATION, "Information Saved Successfully");
        }else if(kiranaAppResult.getResultType() == ResultType.APP_ERROR){
            generateResponseToUser(Alert.AlertType.WARNING, kiranaAppResult.getMessage());
        }else{
            generateResponseToUser(Alert.AlertType.ERROR, "System Error, Contact Suppport:"+ kiranaAppResult.getMessage());
        }
    }

    private void initializeProductIdTxtListeners() {
        productIdTxt.setOnKeyPressed(getKeyPressHandler(FilterKeyConstants.PRODUCT_ID, productIdTxt));
    }

    private void initializeBarCodeTxtListeners() {
        prdBarcodeTxt.setOnKeyPressed(getKeyPressHandler(FilterKeyConstants.BARCODE, prdBarcodeTxt));
    }

    private EventHandler<? super KeyEvent> getKeyPressHandler(String lookupKey, TextField textField) {
        return new GetProductDetailHandler(this, lookupKey, textField);
    }

    public void updateForm(ProductDO product) {
        if(product != null) {
            this.prdCodeTxt.setText(product.getPrdCode());
            this.prdBarcodeTxt.setText(product.getBarcode());
            if(product.getMeasurementType()== MeasurementType.COUNT) {
                this.qtyUOMCB.setDisable(true);
            }
            this.productIdTxt.setText(String.valueOf(product.getProductId()));
        }else{
            clearProductInvFields();
        }
    }

    public void deleteFromInvTable(ActionEvent actionEvent) {
        ButtonType buttonType = generateResponseToUser(Alert.AlertType.CONFIRMATION,"Do you want to delete this");
        if(buttonType == ButtonType.OK) {
            productInvEntryGrid.getSelectionModel().getSelectedItem().setModificationStatus(ModificationStatus.DELETED);
            int rowIndex = productInvEntryGrid.getSelectionModel().getSelectedIndex();
            productInventoryList.remove(rowIndex);
            clearProductInvFields();
            calcTotalAmount();
        }
    }
}
