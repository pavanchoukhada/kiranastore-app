package com.pc.retail.ui.controller;

import com.pc.retail.api.FilterKeyConstants;
import com.pc.retail.interactor.KiranaAppResult;
import com.pc.retail.interactor.MeasurementType;
import com.pc.retail.interactor.ResultType;
import com.pc.retail.ui.event.handler.GetProductDetailHandler;
import com.pc.retail.ui.event.handler.ProductLookupHandler;
import com.pc.retail.ui.event.handler.UpdateProductDetail;
import com.pc.retail.vo.*;
import com.pc.ui.vo.BillingGridItem;
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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by pavanc on 8/2/17.
 */
public class BillingFormController implements Initializable, UpdateProductDetail{

    @FXML
    TextField billingNoTxt;
    @FXML
    DatePicker billingDateDP;
    @FXML
    ComboBox<String> prdSearchTextTxt;

    @FXML
    TextField customNumTxt;
    @FXML
    TextField prdBarcodeTxt;
    @FXML
    TextField productDescTxt;

    @FXML
    TextField lastPrdQtyTxt;
    @FXML
    TextField lastPrdQtyUoMTxt;
    @FXML
    TextField prdPriceTxt;

    @FXML
    TextField prdPriceUoMTxt;

    @FXML
    TextField prdTotalAmountTxt;

    @FXML
    TextField totalBillAmountTxt;
    @FXML
    TextField totalBillDiscTxt;
    @FXML
    TextField totalBillValueTxt;
    @FXML
    TextField otherCostPriceTxt;
    @FXML
    TextField perUnitCostIncOfAllTxt;
    @FXML
    TextField totalProductCostIncOfAllTxt;

    @FXML
    TableView<BillingGridItem> productBillingGrid;

    ProductDO productDO;

    ObservableList<BillingGridItem> billingItems = FXCollections.observableArrayList();

    List<BillItems> deletedProductInventoryList = new ArrayList<>();

    List<ProductDO> billedProductDOList = new ArrayList<>();

    @FXML
    Button updateButton;

    @FXML
    Button closeButton;


    boolean updateEnabled;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeProductGridTable();
        initializeBarCodeTxtListeners();
        getPrdSearchTextTxt().setOnKeyPressed(getKeyPressHandlerForProductSearch());
        prdBarcodeTxt.requestFocus();
        prdBarcodeTxt.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    resetFormAsUpdateIsOver();
                }
            }
        });
    }

    private void initializeBarCodeTxtListeners() {
        prdBarcodeTxt.setOnKeyPressed(getKeyPressHandler(FilterKeyConstants.BARCODE, prdBarcodeTxt));
    }

    private EventHandler<? super KeyEvent> getKeyPressHandler(String lookupKey, TextField textField) {
        return new GetProductDetailHandler(this, lookupKey, textField);
    }

    private EventHandler<? super KeyEvent> getKeyPressHandlerForProductSearch() {
        return new ProductLookupHandler(this);
    }



    private Callback getTableRowSelectionHandler() {
        return tv -> {
            TableRow<BillingGridItem> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                    BillingGridItem rowData = row.getItem();
                    populateFields(rowData);

                }
            });
            return row ;
        };
    }

    private void populateFields(BillingGridItem rowData) {
        Optional<ProductDO> any = billedProductDOList.stream().filter(productDO -> productDO.getBarcode().equals(rowData.getBarCode())).findAny();
        if(any.isPresent()) {
            productDO = any.get();
            productDescTxt.setText(productDO.getPrdCode());
            if(productDO.getMeasurementType() != MeasurementType.COUNT) {
                this.lastPrdQtyUoMTxt.setText(productDO.getQtyUomCd());
                this.prdPriceUoMTxt.setText(productDO.getPriceUomCd());
            }
            this.lastPrdQtyTxt.setText(String.valueOf(rowData.getQuantity()));
            this.prdPriceTxt.setText(String.valueOf(rowData.getSalePrice()));
            this.prdTotalAmountTxt.setText(String.valueOf((rowData.getTotalValue())));
            this.lastPrdQtyTxt.setEditable(true);
            this.prdPriceTxt.setEditable(true);
            updateButton.setDisable(false);
            updateEnabled = true;
            this.lastPrdQtyTxt.requestFocus();
        }
    }

    private void initializeProductGridTable() {

        ObservableList productBillingGridColumns = productBillingGrid.getColumns();
        productBillingGridColumns.add(createStringTableColumn("Product Desc", 120, "productDesc", "gridProductCode"));
        productBillingGridColumns.add(createNumericTableColumn("Quantity", 120, "displayFormattedQty", "gridQtyId"));
        productBillingGridColumns.add(createNumericTableColumn("MRP", 100, "MRP", "gridMRPId"));
        productBillingGridColumns.add(createNumericTableColumn("CGST", 100, "totalCost", "cGSTAmount"));
        productBillingGridColumns.add(createNumericTableColumn("SGST", 100, "totalCost", "sGSTAmount"));
        productBillingGridColumns.add(createNumericTableColumn("Price", 100, "salePrice", "gridSalePrice"));
        productBillingGridColumns.add(createNumericTableColumn("Discount", 100, "saving", "savingGrid"));
        productBillingGridColumns.add(createNumericTableColumn("Total Amount", 100, "totalValue", "totalGridItemValue"));
        productBillingGrid.setItems(billingItems);
        productBillingGrid.setRowFactory(getTableRowSelectionHandler());
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



    private ButtonType generateResponseToUser(Alert.AlertType warning, String message) {
        Alert alert = new Alert(warning, message);
        Optional<ButtonType> buttonType = alert.showAndWait();
        return buttonType.get();
    }



    public void closeForm(ActionEvent actionEvent) {
        Stage stage = (Stage)closeButton.getScene().getWindow();
        stage.close();
    }

    public void updateBillingGrid(ActionEvent actionEvent){
        updateProductDetails(productDO);
        addItemToBillingGrid(productDO);
    }




    public TextField getBillingNoTxt() {
        return billingNoTxt;
    }

    public DatePicker getBillingDateDP() {
        return billingDateDP;
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


    public void updateForm(ProductDO product) {
        if(product != null) {
            updateProductDetails(product);
            addItemToBillingGrid(product);
        }
    }

    private void addItemToBillingGrid(ProductDO product) {
        BillingGridItem billingGridItem = createBillItem(product);
        prdTotalAmountTxt.setText(String.valueOf(billingGridItem.getTotalValue()));
        updateBillingGrid(billingGridItem, product);
    }

    private void updateBillingGrid(BillingGridItem newBillingGridItem, ProductDO product) {
        Optional<BillingGridItem> any =
                productBillingGrid.getItems().stream().filter(billingGridItem1 -> billingGridItem1.getBarCode().equals(newBillingGridItem.getBarCode())).findAny();
        if(any.isPresent()){
            updateExistingBillingRecord(any.get(), newBillingGridItem, product);
        }else{
            billedProductDOList.add(product);
            productBillingGrid.getItems().add(newBillingGridItem);
        }

    }

    private void updateExistingBillingRecord(BillingGridItem existingItem, BillingGridItem newBillingGridItem, ProductDO productDo) {
        if(updateEnabled){
            existingItem.setQuantity(0);
            existingItem.setTotalValue(0);
            if(existingItem.getSalePrice() != newBillingGridItem.getSalePrice()){
                existingItem.setIsDiscounted(true);
            }
        }
        existingItem.setSalePrice(newBillingGridItem.getSalePrice());
        existingItem.setQuantity(existingItem.getQuantity() + newBillingGridItem.getQuantity());
        String unit = "";
        if(productDo.getMeasurementType() == MeasurementType.WEIGHT){
            unit = productDo.getQtyUomCd();
        }
        existingItem.setDisplayFormattedQty(getDisplayFormattedQty(unit, existingItem.getQuantity()));
        existingItem.setTotalValue(existingItem.getTotalValue() + newBillingGridItem.getTotalValue());
        productBillingGrid.refresh();
    }

    private BillingGridItem createBillItem(ProductDO productDo) {
        double salePrice = Double.valueOf(prdPriceTxt.getText());
        double qty = Double.valueOf(lastPrdQtyTxt.getText());
        double productAmt = salePrice * qty;
        BillingGridItem billingGridItem = new BillingGridItem();
        billingGridItem.setBarCode(productDo.getBarcode());
        billingGridItem.setProductDesc(productDo.getPrdCode());
        billingGridItem.setQuantity(qty);
        billingGridItem.setSalePrice(salePrice);
        billingGridItem.setMRP(productDo.getMRP());
        billingGridItem.setQtyUomCd(productDo.getQtyUomCd());
        String unit = "";
        if(productDo.getMeasurementType() == MeasurementType.WEIGHT){
            unit = productDo.getQtyUomCd();
        }
            billingGridItem.setDisplayFormattedQty(getDisplayFormattedQty(unit, qty));
        if(productDo.getMRP() > 0) {
            billingGridItem.setSaving((productDo.getMRP() - salePrice) * qty);
        }
        billingGridItem.setTotalValue(productAmt);
        return billingGridItem;
    }

    private String getDisplayFormattedQty(String qtyUomCd, double qty) {
        return qty + " " + qtyUomCd;
    }

    private void updateProductDetails(ProductDO product) {
        this.productDescTxt.setText(product.getPrdCode());
        if(product.getMeasurementType() != MeasurementType.COUNT) {
            this.lastPrdQtyUoMTxt.setText(product.getQtyUomCd());
            this.prdPriceUoMTxt.setText(product.getPriceUomCd());
        }
        this.lastPrdQtyTxt.setText("1");
        this.prdPriceTxt.setText(String.valueOf(product.getCurrentSellingPrice()));
        this.productDO = product;
        prdBarcodeTxt.clear();
        prdBarcodeTxt.requestFocus();
    }


    public void submitForm(ActionEvent actionEvent) {
    }

    public void resetForm(ActionEvent actionEvent) {
    }

    public void updateItemInGrid(ActionEvent actionEvent) {
        addItemToBillingGrid(productDO);
        resetFormAsUpdateIsOver();
    }

    private void resetFormAsUpdateIsOver() {
        updateEnabled=false;
        updateButton.setDisable(true);
        lastPrdQtyTxt.setEditable(false);
        prdPriceTxt.setEditable(false);
        prdBarcodeTxt.requestFocus();
    }

    public ComboBox<String> getPrdSearchTextTxt() {
        return prdSearchTextTxt;
    }
}
