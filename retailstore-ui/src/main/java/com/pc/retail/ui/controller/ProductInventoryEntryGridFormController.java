package com.pc.retail.ui.controller;

import com.pc.retail.api.FilterKeyConstants;
import com.pc.retail.dao.InvoiceStatus;
import com.pc.retail.interactor.KiranaAppResult;
import com.pc.retail.interactor.MeasurementType;
import com.pc.retail.interactor.ResultType;
import com.pc.retail.ui.event.handler.GetProductDetailHandler;
import com.pc.retail.ui.event.handler.UpdateProductDetail;

import com.pc.retail.ui.helper.ProductInventoryEntryGridFormHelper;
import com.pc.retail.util.DataUtil;
import com.pc.retail.util.UOMConversionUtil;
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
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.xml.crypto.Data;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by pavanc on 8/2/17.
 */
public class ProductInventoryEntryGridFormController implements Initializable, UpdateProductDetail{

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
    TextField productCodeTxt;

    @FXML
    TextField prdQtyTxt;
    @FXML
    ComboBox qtyUOMCB;
    @FXML
    TextField mrpTxt;
    @FXML
    TextField salePriceTxt;
    @FXML
    TextField salePriceUOMTxt;

    @FXML
    TextField perUnitCostTxt;
    @FXML
    DatePicker expiryDP;

    @FXML
    TextField GSTRateTxt;
    @FXML
    TextField GSTValueTxt;

    @FXML
    TextField CGSTRateTxt;
    @FXML
    TextField SGSTRateTxt;

    @FXML
    TextField CGSTValueTxt;
    @FXML
    TextField SGSTValueTxt;
    @FXML
    TextField perUnitCostIncGSTTxt;
    @FXML
    TextField totalProductAmountTxt;
    @FXML
    TextField totalProductAmountIncGSTTxt;
    @FXML
    TextField otherCostPriceTxt;
    @FXML
    TextField perUnitCostIncOfAllTxt;
    @FXML
    TextField totalProductCostIncOfAllTxt;

    @FXML
    TextField totalInvoiceAmountInclAllTxt;

    @FXML
    TextField totalInvoiceAmountTxt;

    @FXML
    TextField totalsGSTAmountTxt;
    @FXML
    TextField totalcGSTAmountTxt;

    @FXML
    TextField totalGSTAmountTxt;

    @FXML
    TableView<ProductInventory> productInvEntryGrid;

    ProductDO productDO;

    int invoiceId = -1;

    ObservableList<ProductInventory> productInventoryList = FXCollections.observableArrayList();

    List<ProductInventory> deletedProductInventoryList = new ArrayList<>();

    private ProductInventoryEntryGridFormHelper productInvEntryGridFormHelper;

    @FXML
    Button closeButton;
    private boolean calledFromOtherForm = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        productInvEntryGridFormHelper = new ProductInventoryEntryGridFormHelper();
        populateForm();
        initializeInvGridTable();

        productInvEntryGrid.setRowFactory(getTableRowSelectionHandler());
        initializeBarCodeTxtListeners();
        initializeProductIdTxtListeners();
        prdBarcodeTxt.requestFocus();
        invoiceDateDP.setValue(LocalDate.now());
        prdQtyTxt.focusedProperty().addListener(calculateAndSetAllFieldListener());
        perUnitCostTxt.focusedProperty().addListener(calculateAndSetAllFieldListener());
        perUnitCostIncGSTTxt.focusedProperty().addListener(perUnitCostIncGSTListener());
        totalProductAmountIncGSTTxt.focusedProperty().addListener(totalCostIncGSTListener());
        otherCostPriceTxt.focusedProperty().addListener(otherCostListener());
        totalInvoiceAmountInclAllTxt.setDisable(true);
        totalInvoiceAmountTxt.setDisable(true);
        totalGSTAmountTxt.setDisable(true);
        totalcGSTAmountTxt.setDisable(true);
        totalsGSTAmountTxt.setDisable(true);
        GSTRateTxt.setDisable(true);
        CGSTRateTxt.setDisable(true);
        SGSTRateTxt.setDisable(true);
        salePriceUOMTxt.setDisable(true);
    }

    private ChangeListener<Boolean> perUnitCostIncGSTListener() {
        return new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue.booleanValue()) {
                    double prdQty = getProductQuantity(DataUtil.getDouble(prdQtyTxt.getText()), (String)qtyUOMCB.getValue());
                    double perUnitCostIncGST = DataUtil.getDouble(perUnitCostIncGSTTxt.getText());
                    if(prdQty >0 && perUnitCostIncGST > 0) {
                        double gstRate = DataUtil.getDouble(GSTRateTxt.getText());
                        double perUnitCostPrice = (perUnitCostIncGST * 100)/ ( 100 + gstRate);
                        double gstValue = perUnitCostIncGST - perUnitCostPrice;
                        double otherCost = DataUtil.getDouble(otherCostPriceTxt.getText());
                        double perUnitOtherCost = DataUtil.round4(otherCost/prdQty);

                        double totalCostIncGST = DataUtil.round2(prdQty * perUnitCostIncGST);
                        perUnitCostTxt.setText(DataUtil.convertToText(perUnitCostPrice));
                        totalProductAmountTxt.setText(String.valueOf(DataUtil.round2(prdQty * perUnitCostPrice)));
                        totalProductAmountIncGSTTxt.setText(DataUtil.convertToText(totalCostIncGST));
                        perUnitCostIncOfAllTxt.setText(DataUtil.convertToText(perUnitCostIncGST + perUnitOtherCost));
                        totalProductCostIncOfAllTxt.setText(DataUtil.convertToText(totalCostIncGST + otherCost));

                        GSTValueTxt.setText(DataUtil.convertToText(gstValue));

                        double cGSTRate = DataUtil.getDouble(CGSTRateTxt.getText());
                        double sGSTRate = DataUtil.getDouble(SGSTRateTxt.getText());
                        CGSTValueTxt.setText(DataUtil.convertToText(DataUtil.round2(gstValue * (cGSTRate/100))));
                        SGSTValueTxt.setText(DataUtil.convertToText(DataUtil.round2(gstValue * (sGSTRate/100))));
                    }
                }
            }
        };
    }

    private double getProductQuantity(double aDouble, String value) {
        return UOMConversionUtil.convert(aDouble, value, salePriceUOMTxt.getText());
    }

    private ChangeListener<Boolean> totalCostIncGSTListener() {
        return new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue.booleanValue()) {
                    double prdQty = getProductQuantity(DataUtil.getDouble(prdQtyTxt.getText()), (String)qtyUOMCB.getValue());
                    double totalCostIncGST = DataUtil.getDouble(totalProductAmountIncGSTTxt.getText());
                    if(prdQty >0 && totalCostIncGST > 0) {
                        double perUnitCostIncGST = DataUtil.round2(totalCostIncGST/prdQty);
                        double gstRate = DataUtil.getDouble(GSTRateTxt.getText());
                        double perUnitCostPrice = (perUnitCostIncGST * 100)/ ( 100 + gstRate);
                        double gstValue = perUnitCostIncGST - perUnitCostPrice;
                        double otherCost = DataUtil.getDouble(otherCostPriceTxt.getText());
                        double perUnitOtherCost = DataUtil.round4(otherCost/prdQty);

                        perUnitCostTxt.setText(DataUtil.convertToText(perUnitCostPrice));
                        totalProductAmountTxt.setText(String.valueOf(DataUtil.round2(prdQty * perUnitCostPrice)));
                        totalProductAmountIncGSTTxt.setText(DataUtil.convertToText(totalCostIncGST));
                        perUnitCostIncOfAllTxt.setText(DataUtil.convertToText(perUnitCostIncGST + perUnitOtherCost));
                        totalProductCostIncOfAllTxt.setText(DataUtil.convertToText(totalCostIncGST + otherCost));

                        GSTValueTxt.setText(DataUtil.convertToText(gstValue));

                        double cGSTRate = DataUtil.getDouble(CGSTRateTxt.getText());
                        double sGSTRate = DataUtil.getDouble(SGSTRateTxt.getText());
                        CGSTValueTxt.setText(DataUtil.convertToText(DataUtil.round2(gstValue * (cGSTRate/100))));
                        SGSTValueTxt.setText(DataUtil.convertToText(DataUtil.round2(gstValue * (sGSTRate/100))));
                    }
                }
            }
        };
    }

    private ChangeListener<Boolean> calculateAndSetAllFieldListener() {
        return new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue.booleanValue()) {
                    double prdQty = getProductQuantity(DataUtil.getDouble(prdQtyTxt.getText()), (String)qtyUOMCB.getValue());
                    double perUnitCost = DataUtil.getDouble(perUnitCostTxt.getText());
                    if(prdQty >0 && perUnitCost > 0) {
                        double cGSTRate = DataUtil.getDouble(CGSTRateTxt.getText());
                        double sGSTRate = DataUtil.getDouble(SGSTRateTxt.getText());

                        double perUnitCostWithGST = calculatePerUnitCostWithGSTFromPerUnitCost(perUnitCost, cGSTRate, sGSTRate);
                        double totalCostIncGST = DataUtil.round2(prdQty * perUnitCostWithGST);
                        double otherCost = DataUtil.getDouble(otherCostPriceTxt.getText());
                        double perUnitOtherCost = DataUtil.round4(otherCost/prdQty);

                        perUnitCostIncGSTTxt.setText(DataUtil.convertToText(perUnitCostWithGST));
                        totalProductAmountTxt.setText(String.valueOf(DataUtil.round2(prdQty * perUnitCost)));
                        totalProductAmountIncGSTTxt.setText(DataUtil.convertToText(totalCostIncGST));
                        perUnitCostIncOfAllTxt.setText(DataUtil.convertToText(perUnitCostWithGST+ perUnitOtherCost));
                        totalProductCostIncOfAllTxt.setText(DataUtil.convertToText(totalCostIncGST + otherCost));
                    }
                }
            }

            private double calculatePerUnitCostWithGSTFromPerUnitCost(double perUnitCost, double cGSTRate, double sGSTRate) {
                double cGSTValue = DataUtil.round4(perUnitCost *  (cGSTRate/100));
                CGSTValueTxt.setText(DataUtil.convertToText(cGSTValue));
                double sGSTValue = DataUtil.round4(perUnitCost *  (sGSTRate/100));
                SGSTValueTxt.setText(DataUtil.convertToText(sGSTValue));
                GSTValueTxt.setText(DataUtil.convertToText(cGSTValue + sGSTValue));
                return DataUtil.round2(perUnitCost + cGSTValue + sGSTValue);
            }

        };
    }

    private ChangeListener<Boolean> otherCostListener() {
        return new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue.booleanValue()) {
                    double prdQty = getProductQuantity(DataUtil.getDouble(prdQtyTxt.getText()), (String)qtyUOMCB.getValue());
                    if(prdQty >0 ) {
                        double perUnitCostIncGST = DataUtil.getDouble(perUnitCostIncGSTTxt.getText());
                        double otherCost = DataUtil.getDouble(otherCostPriceTxt.getText());
                        double perUnitOtherCost = DataUtil.round4(otherCost/prdQty);
                        perUnitCostIncOfAllTxt.setText(DataUtil.convertToText(DataUtil.round2(perUnitCostIncGST + perUnitOtherCost)));

                        double totalCostIncGST = DataUtil.getDouble(totalProductAmountIncGSTTxt.getText());
                        totalProductCostIncOfAllTxt.setText(DataUtil.convertToText(totalCostIncGST + otherCost));
                    }
                }
            }
        };
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
        productCodeTxt.setText(productInventory.getProductCode());
        expiryDP.getEditor().setText(DataUtil.getDateStr(productInventory.getExpiryDate()));
        mrpTxt.setText(String.valueOf(productInventory.getMRP()));
        prdQtyTxt.setText(String.valueOf(productInventory.getQuantity()));
        if(!DataUtil.isEmpty(productInventory.getQtyUOM())) {
            qtyUOMCB.setValue(String.valueOf(productInventory.getQtyUOM()));
        }
        salePriceTxt.setText(String.valueOf(productInventory.getSalePrice()));
        salePriceUOMTxt.setText(String.valueOf(productInventory.getSalePriceUOM()));
        GSTRateTxt.setText(String.valueOf(productInventory.getCGSTRate() + productInventory.getSGSTRate()));
        GSTValueTxt.setText(String.valueOf(productInventory.getPerUnitCGSTAmount() + productInventory.getPerUnitSGSTAmount()));
        CGSTRateTxt.setText(String.valueOf(productInventory.getCGSTRate()));
        SGSTRateTxt.setText(String.valueOf(productInventory.getSGSTRate()));
        CGSTValueTxt.setText(String.valueOf(productInventory.getPerUnitCGSTAmount()));
        SGSTValueTxt.setText(String.valueOf(productInventory.getPerUnitSGSTAmount()));
        perUnitCostTxt.setText(String.valueOf(productInventory.getPerUnitCost()));
        perUnitCostIncGSTTxt.setText(String.valueOf(productInventory.getPerUnitCostIncludingGST()));
        perUnitCostIncOfAllTxt.setText(String.valueOf(productInventory.getPerUnitCostIncludingAll()));
        totalProductAmountTxt.setText(String.valueOf(productInventory.getTotalCost()));
        totalProductAmountIncGSTTxt.setText(String.valueOf(productInventory.getTotalCostIncludingGST()));
        totalProductCostIncOfAllTxt.setText(String.valueOf(productInventory.getFinalAmountInclAll()));
    }

    private void initializeInvGridTable() {

        ObservableList productInvEntryGridColumns = productInvEntryGrid.getColumns();
        //productInvEntryGridColumns.add(createStringTableColumn("Product Id", 120, "productId", "gridProductId"));
        productInvEntryGridColumns.add(createStringTableColumn("Barcode", 120, "barCode", "gridBarCodeId"));
        productInvEntryGridColumns.add(createStringTableColumn("Product Code", 120, "productCode", "gridProductCode"));
        productInvEntryGridColumns.add(createNumericTableColumn("Quantity", 70, "quantity", "gridQtyId"));
        productInvEntryGridColumns.add(createStringTableColumn("Qty UOM", 50, "qtyUOM", "gridQtyUOMId"));
        productInvEntryGridColumns.add(createNumericTableColumn("MRP", 60, "MRP", "gridMRPId"));
        productInvEntryGridColumns.add(createNumericTableColumn("Sale Price", 70, "salePrice", "gridQtyId"));
        productInvEntryGridColumns.add(createStringTableColumn("Price UOM", 60, "salePriceUOM", "gridQtyId"));
        productInvEntryGridColumns.add(createNumericTableColumn("Per Unit Cost", 90, "perUnitCost", "gridPerUnitCostId"));
        productInvEntryGridColumns.add(createNumericTableColumn("GST Per Unit", 90, "perUnitGSTAmount", "perUnitGSTAmount"));
        productInvEntryGridColumns.add(createNumericTableColumn("PU Cost(Incl GST)", 70, "perUnitCostIncludingGST", "perUnitCostIncludingGST"));
        productInvEntryGridColumns.add(createNumericTableColumn("Other Cost", 100, "otherCost", "otherCost"));
        productInvEntryGridColumns.add(createNumericTableColumn("PU Cost(Incl All)", 70, "perUnitCostIncludingAll", "perUnitCostIncludingAll"));
        productInvEntryGridColumns.add(createNumericTableColumn("Total Amount", 80, "totalCost", "totalCost"));
        productInvEntryGridColumns.add(createNumericTableColumn("GST Amount", 80, "totalGSTAmountForInv", "totalGSTAmountForInv"));
        productInvEntryGridColumns.add(createNumericTableColumn("Total Amount(Incl GST)", 120, "finalAmountInclAll", "finalAmountInclAll"));
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
                productInventory = ceateProductInvFromInput();
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
            ProductInventory existingProductInventory = productInventoryList.get(index);
            productInventory.setPrdInvId(existingProductInventory.getPrdInvId());
            if(productInventory.getPrdInvId() > 0){
                productInventory.setModificationStatus(ModificationStatus.MODIFIED);
            }
            productInventoryList.set(index, productInventory);
        }else {
            productInventory.setModificationStatus(ModificationStatus.NEW);
            productInventoryList.add(productInventory);
        }
        calcTotalAmount();
        clearProductInvFields();

    }

    private void calcTotalAmount() {
        double totalAmount = 0.0d;
        double totalGSTAmount = 0.0d;
        double totalcGSTAmount = 0.0d;
        double totalsGSTAmount = 0.0d;
        double totalAmountIncAll = 0.0d;
        for(ProductInventory productInventory : productInventoryList){
            totalAmountIncAll = totalAmountIncAll + productInventory.getFinalAmountInclAll();
            totalGSTAmount = totalGSTAmount + productInventory.getTotalGSTAmountForInv();
            totalsGSTAmount = totalsGSTAmount + productInventory.getTotalSGSTAmount();
            totalcGSTAmount = totalcGSTAmount + productInventory.getTotalCGSTAmount();
            totalAmount = totalAmount + productInventory.getTotalCost();
        }
        totalInvoiceAmountTxt.setText(DataUtil.convertToText(totalAmount));
        totalInvoiceAmountInclAllTxt.setText(DataUtil.convertToText(totalAmountIncAll));
        totalGSTAmountTxt.setText(DataUtil.convertToText(totalGSTAmount));
        totalsGSTAmountTxt.setText(DataUtil.convertToText(totalsGSTAmount));
        totalcGSTAmountTxt.setText(DataUtil.convertToText(totalcGSTAmount));
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

    private ProductInventory ceateProductInvFromInput() throws Exception{
        ProductInventory productInventory = new ProductInventory();
        productInventory.setProductId(DataUtil.getIntegerValue(productIdTxt.getText(), "Product Id"));
        productInventory.setBarCode(prdBarcodeTxt.getText());
        productInventory.setProductCode(productCodeTxt.getText());
        productInventory.setQuantity(DataUtil.getDoubleValue(prdQtyTxt.getText(), "Quantity"));
        if(!DataUtil.isEmpty((String) qtyUOMCB.getValue())) {
            productInventory.setQtyUOM((String) qtyUOMCB.getValue());
        }
        productInventory.setMRP(DataUtil.getDoubleValue(mrpTxt.getText(), "MRP"));
        productInventory.setSalePrice(DataUtil.getDoubleValue(salePriceTxt.getText(), "Sale Price"));
        productInventory.setSalePriceUOM(salePriceUOMTxt.getText());
        productInventory.setExpiryDate(DataUtil.getDate(expiryDP.getValue()));

        productInventory.setCGSTRate(DataUtil.getDouble(CGSTRateTxt.getText()));
        productInventory.setSGSTRate(DataUtil.getDouble(SGSTRateTxt.getText()));

        productInventory.setPerUnitCGSTAmount(DataUtil.getDoubleValue(CGSTValueTxt.getText(), "CGST Value"));
        productInventory.setPerUnitSGSTAmount(DataUtil.getDoubleValue(SGSTValueTxt.getText(), "SGST Value"));

        productInventory.setTotalSGSTAmount(productInventory.getQuantity() * productInventory.getPerUnitSGSTAmount());
        productInventory.setTotalCGSTAmount(productInventory.getQuantity() * productInventory.getPerUnitCGSTAmount());

        productInventory.setOtherCost(DataUtil.getDoubleValue(otherCostPriceTxt.getText(), "Other Cost"));

        productInventory.setPerUnitCost(DataUtil.getDoubleValue(perUnitCostTxt.getText(), "Per Unit Cost Price"));
        productInventory.setPerUnitCostIncludingGST(DataUtil.getDoubleValue(perUnitCostIncGSTTxt.getText(), "Per Unit Cost Inc GST"));
        productInventory.setPerUnitCostIncludingAll(DataUtil.getDoubleValue(perUnitCostIncOfAllTxt.getText(), "Per Unit Cost Inc All"));

        productInventory.setTotalCost(DataUtil.getDoubleValue(totalProductAmountTxt.getText(),"Total Cost Amount"));
        productInventory.setTotalCostIncludingGST(DataUtil.getDoubleValue(totalProductAmountIncGSTTxt.getText(),"Total Cost Amt (Inc GST)"));
        productInventory.setFinalAmountInclAll(DataUtil.getDoubleValue(totalProductCostIncOfAllTxt.getText(),"Total Cost Amt(Inc All)"));

        return productInventory;
    }



    public void submitForm(ActionEvent actionEvent) {
        KiranaAppResult kiranaAppResult;
        if (productInventoryList.size() > 0) {
            kiranaAppResult = productInvEntryGridFormHelper.submit(this);
        }else{
            kiranaAppResult = new KiranaAppResult(ResultType.APP_ERROR, "Product Inventory is not added in the table for save");
        }
        showResponseToUser(kiranaAppResult);
        if(calledFromOtherForm){
            closeForm(actionEvent);
        }
        if(kiranaAppResult.getResultType() == ResultType.SUCCESS) {
            invoiceNoTxt.clear();
            invoiceNoTxt.requestFocus();
            supplierCB.getEditor().clear();
            invoiceStatusCB.getEditor().clear();
        }

    }

    public void closeForm(ActionEvent actionEvent) {
        Stage stage = (Stage)closeButton.getScene().getWindow();
        stage.close();
    }

    public void resetForm(ActionEvent actionEvent) {
        this.getDeletedProductInventoryList().clear();
        clearProductInvFields();
        productInventoryList.clear();
        invoiceNoTxt.clear();
        supplierCB.getEditor().clear();
        invoiceStatusCB.getEditor().clear();
    }

    public void clearFields(ActionEvent actionEvent) {
        clearProductInvFields();
    }

    private void clearProductInvFields() {
        productIdTxt.clear();
        prdBarcodeTxt.clear();
        productCodeTxt.clear();
        prdQtyTxt.clear();
        qtyUOMCB.getEditor().clear();
        perUnitCostTxt.clear();
        totalProductCostIncOfAllTxt.clear();
        expiryDP.getEditor().clear();
        prdBarcodeTxt.clear();
        productCodeTxt.clear();
        mrpTxt.clear();
        prdBarcodeTxt.requestFocus();
        salePriceTxt.clear();
        salePriceUOMTxt.clear();
        perUnitCostTxt.clear();
        otherCostPriceTxt.clear();
        perUnitCostIncGSTTxt.clear();
        perUnitCostIncOfAllTxt.clear();
        totalProductAmountTxt.clear();
        totalProductAmountIncGSTTxt.clear();
        totalProductCostIncOfAllTxt.clear();
        GSTRateTxt.clear();
        CGSTRateTxt.clear();
        SGSTRateTxt.clear();
        GSTValueTxt.clear();
        CGSTValueTxt.clear();
        SGSTValueTxt.clear();
        prdBarcodeTxt.requestFocus();
    }

    public List<ProductInventory> getProductInventoryList() {
        return productInventoryList;
    }

    public TextField getInvoiceNoTxt() {
        return invoiceNoTxt;
    }

    public ComboBox<String> getInvoiceStatusCB() {
        return invoiceStatusCB;
    }

    public DatePicker getInvoiceDateDP() {
        return invoiceDateDP;
    }

    public ComboBox<ProductSupplier> getSupplierCB() {
        return supplierCB;
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
            generateResponseToUser(Alert.AlertType.ERROR, kiranaAppResult.getMessage());
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
            this.productCodeTxt.setText(product.getPrdCode());
            this.prdBarcodeTxt.setText(product.getBarcode());
            if(product.getMeasurementType()== MeasurementType.COUNT) {
                this.qtyUOMCB.setDisable(true);
                this.qtyUOMCB.getEditor().clear();
                this.qtyUOMCB.setValue("");
            }else{
                this.qtyUOMCB.setDisable(false);
                this.qtyUOMCB.setValue(product.getQtyUomCd());
            }
            this.productIdTxt.setText(String.valueOf(product.getProductId()));
            this.productDO = product;
            this.salePriceTxt.setText(DataUtil.convertToText(product.getCurrentSellingPrice()));
            this.salePriceUOMTxt.setText(product.getPriceUomCd());
            this.GSTRateTxt.setText(DataUtil.convertToText(product.getGstRate()));
            this.CGSTRateTxt.setText(DataUtil.convertToText(product.getcGSTRate()));
            this.SGSTRateTxt.setText(DataUtil.convertToText(product.getsGSTRate()));
            prdQtyTxt.requestFocus();
        }else{
            clearProductInvFields();
        }
    }

    public void deleteFromInvTable(ActionEvent actionEvent) {
        ButtonType buttonType = generateResponseToUser(Alert.AlertType.CONFIRMATION,"Do you want to delete this");
        if(buttonType == ButtonType.OK) {
            ProductInventory productInventory = productInvEntryGrid.getSelectionModel().getSelectedItem();
            productInventory.setModificationStatus(ModificationStatus.DELETED);
            deletedProductInventoryList.add(productInventory);
            int rowIndex = productInvEntryGrid.getSelectionModel().getSelectedIndex();
            productInventoryList.remove(rowIndex);
            clearProductInvFields();
            calcTotalAmount();
        }
    }

    public List<ProductInventory> getDeletedProductInventoryList() {
        return deletedProductInventoryList;
    }

    public TextField getTotalInvoiceAmountInclAllTxt() {
        return totalInvoiceAmountInclAllTxt;
    }

    public TextField getTotalInvoiceAmountTxt() {
        return totalInvoiceAmountTxt;
    }

    public TextField getTotalsGSTAmountTxt() {
        return totalsGSTAmountTxt;
    }

    public TextField getTotalcGSTAmountTxt() {
        return totalcGSTAmountTxt;
    }

    public TextField getTotalGSTAmountTxt() {
        return totalGSTAmountTxt;
    }

    public void initData(int invoiceId) {
        calledFromOtherForm = true;
        this.invoiceId = invoiceId;
        productInvEntryGridFormHelper.loadForm(invoiceId, this);
        calcTotalAmount();
    }
}
