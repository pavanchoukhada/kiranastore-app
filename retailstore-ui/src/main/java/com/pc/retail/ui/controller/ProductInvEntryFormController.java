/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pc.retail.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.pc.retail.api.FilterKeyConstants;
import com.pc.retail.interactor.KiranaAppResult;
import com.pc.retail.interactor.MeasurementType;
import com.pc.retail.interactor.ResultType;
import com.pc.retail.ui.event.handler.GetProductDetailHandler;
import com.pc.retail.ui.event.handler.UpdateProductDetail;
import com.pc.retail.ui.helper.ProductInvEntryFormHelper;
import com.pc.retail.vo.ProductDO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author pavanc
 */
public class ProductInvEntryFormController implements Initializable, UpdateProductDetail {

    @FXML
    TextField prdBarCodeTxt;
    @FXML
    TextField prdDescriptionTxt;
    @FXML
    TextField prdShortCodeTxt;
    @FXML
    TextField prdSearchKeyTxt;
    @FXML
    ComboBox<String> prdCategoryCB;
    @FXML
    ComboBox<String> prdCompanyCB;
    @FXML
    ToggleGroup prdMeasurementGroup;

    @FXML
    RadioButton weightToggle;
    @FXML
    RadioButton countToggle;


    @FXML
    ComboBox<String> prdWeightUnitCB;
    @FXML
    ComboBox<String> prdPriceUOMCB;
    @FXML
    ComboBox<String> prdGSTTaxCB;
    @FXML
    TextField prdWeightTxt;
    @FXML
    CheckBox prdIsFoodGradeCB;
    @FXML
    TextField baseProductTxt;
    @FXML
    TextField prdQuantityTxt;
    @FXML
    TextField prdExpiryDtTxt;
    @FXML
    TextField prdMRPTxt;
    @FXML
    TextField prdCSPTxt;
    @FXML
    TextField prdInvoiceRefTxt;

    @FXML
    TextField productIdTxt;

    @FXML
    Button closeButton;

    ProductInvEntryFormHelper productInvEntryFormHelper;

    private int productId;

    /**
     * Initializes the controller class.
     */
    public void initialize(URL url, ResourceBundle rb) {
        productInvEntryFormHelper = new ProductInvEntryFormHelper();
        initializeBarCodeTxtListeners();
        initializeProductIdTxtListeners();
        populateFields();
        prdMeasurementGroup.setUserData(MeasurementType.COUNT);
        setMeasurementFlag();
        prdMeasurementGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                setMeasurementFlag();
            }
        });
        // TODO
    }

    private void setMeasurementFlag() {
        if (prdMeasurementGroup.getSelectedToggle() != null) {
            int userData = Integer.parseInt((String)prdMeasurementGroup.getSelectedToggle().getUserData());
            if (userData == MeasurementType.COUNT.getValue()) {
                prdPriceUOMCB.setValue("Per Unit");
                prdPriceUOMCB.setDisable(true);
            }else{
                prdPriceUOMCB.setValue("KG");
                prdPriceUOMCB.setDisable(false);
            }
        }
    }

    private void populateFields() {
        ObservableList<String> productCategories
                = FXCollections.observableArrayList(productInvEntryFormHelper.getProductCategories());
        prdCategoryCB.setItems(productCategories);

        ObservableList<String> productUOMList
                = FXCollections.observableArrayList(productInvEntryFormHelper.getUOMList());
        prdPriceUOMCB.setItems(productUOMList);
        prdWeightUnitCB.setItems(productUOMList);

        ObservableList<String> productCompList
                = FXCollections.observableArrayList(productInvEntryFormHelper.getProductCompanies());
        prdCompanyCB.setItems(productCompList);

        ObservableList<String> gstGroupCodes = FXCollections.observableArrayList(productInvEntryFormHelper.getGSTGroupCode());
        prdGSTTaxCB.setItems(gstGroupCodes);
    }

    private void initializeProductIdTxtListeners() {
        productIdTxt.setOnKeyPressed(getKeyPressHandler(FilterKeyConstants.PRODUCT_ID, getProductIdTxt()));
    }

    private void initializeBarCodeTxtListeners() {
        prdBarCodeTxt.setOnKeyPressed(getKeyPressHandler(FilterKeyConstants.BARCODE, getPrdBarCodeTxt()));
    }

    private EventHandler<? super KeyEvent> getKeyPressHandler(String lookupKey, TextField textField) {
        return new GetProductDetailHandler(this, lookupKey, textField);
    }

    @FXML
    public void submitForm(ActionEvent event){
        KiranaAppResult kiranaAppResult = productInvEntryFormHelper.submitForm(this);
        showResponseToUser(kiranaAppResult);
    }

    private void showResponseToUser(KiranaAppResult kiranaAppResult) {
        if(kiranaAppResult.getResultType() == ResultType.SUCCESS){
            generateResponseToUser(Alert.AlertType.INFORMATION, "Product Saved Successfully");
            resetForm();
        }else if(kiranaAppResult.getResultType() == ResultType.APP_ERROR){
            generateResponseToUser(Alert.AlertType.WARNING, kiranaAppResult.getMessage());
        }else{
            generateResponseToUser(Alert.AlertType.ERROR, "System Error, Contact Suppport:"+ kiranaAppResult.getMessage());
        }
    }

    private void generateResponseToUser(Alert.AlertType warning, String message) {
        Alert alert = new Alert(warning, message);
        alert.showAndWait();
    }

    @FXML
    public void resetForm() {
        this.prdInvoiceRefTxt.clear();
        this.prdCSPTxt.clear();
        this.prdMRPTxt.clear();
        this.prdExpiryDtTxt.clear();
        this.prdWeightTxt.clear();
        this.prdWeightUnitCB.setValue("");
        this.prdPriceUOMCB.setValue("");
        this.prdShortCodeTxt.clear();
        this.prdCategoryCB.setValue("");
        this.prdBarCodeTxt.clear();
        this.prdQuantityTxt.clear();
        this.prdDescriptionTxt.clear();
        this.prdSearchKeyTxt.clear();
        this.productIdTxt.clear();
        this.prdBarCodeTxt.requestFocus();
        this.prdExpiryDtTxt.clear();
        this.prdCompanyCB.setValue("");
        this.prdGSTTaxCB.setValue("");
        this.productId = -1;
    }


    public void updateForm(ProductDO product) {
        if(product != null) {
            this.prdShortCodeTxt.setText(product.getPrdCode());
            this.prdSearchKeyTxt.setText(product.getSearchKey());
            this.prdDescriptionTxt.setText(product.getPrdDesc());
            this.prdCSPTxt.setText(String.valueOf(product.getCurrentSellingPrice()));
            this.prdCompanyCB.setValue(product.getCompanyCode());
            this.prdPriceUOMCB.setValue(product.getPriceUomCd());
            this.prdCategoryCB.setValue(product.getCategory());

            if(product.getMeasurementType() == MeasurementType.WEIGHT){
                this.weightToggle.setSelected(true);
            }else{
                this.countToggle.setSelected(true);
            }

            this.prdWeightTxt.setText(String.valueOf(product.getWeight()));
            this.prdWeightUnitCB.setValue(product.getQtyUomCd());
            this.prdGSTTaxCB.setValue(product.getGstTaxGroup());
            this.productIdTxt.setText(String.valueOf(product.getProductId()));
            this.productId = product.getProductId();
        }else{
            String barCode = this.prdBarCodeTxt.getText();
            resetForm();
            this.prdBarCodeTxt.setText(barCode);
        }
    }

    @FXML
    public void closeForm(ActionEvent actionEvent) {
        Stage stage = (Stage)closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void submitFormAndClose(ActionEvent actionEvent) {
        submitForm(actionEvent);
        closeForm(actionEvent);
    }

    public TextField getPrdBarCodeTxt() {
        return prdBarCodeTxt;
    }

    public void setPrdBarCodeTxt(TextField prdBarCodeTxt) {
        this.prdBarCodeTxt = prdBarCodeTxt;
    }

    public TextField getPrdDescriptionTxt() {
        return prdDescriptionTxt;
    }

    public void setPrdDescriptionTxt(TextField prdDescriptionTxt) {
        this.prdDescriptionTxt = prdDescriptionTxt;
    }

    public TextField getPrdShortCodeTxt() {
        return prdShortCodeTxt;
    }

    public void setPrdShortCodeTxt(TextField prdShortCodeTxt) {
        this.prdShortCodeTxt = prdShortCodeTxt;
    }

    public TextField getPrdSearchKeyTxt() {
        return prdSearchKeyTxt;
    }

    public void setPrdSearchKeyTxt(TextField prdSearchKeyTxt) {
        this.prdSearchKeyTxt = prdSearchKeyTxt;
    }

    public ComboBox<String> getPrdCategoryCB() {
        return prdCategoryCB;
    }

    public void setPrdCategoryCB(ComboBox<String> prdCategoryCB) {
        this.prdCategoryCB = prdCategoryCB;
    }

    public ComboBox<String> getPrdCompanyCB() {
        return prdCompanyCB;
    }

    public void setPrdCompanyCB(ComboBox<String> prdCompanyCB) {
        this.prdCompanyCB = prdCompanyCB;
    }

    public ToggleGroup getPrdMeasurementGroup() {
        return prdMeasurementGroup;
    }

    public void setPrdMeasurementGroup(ToggleGroup prdMeasurementGroup) {
        this.prdMeasurementGroup = prdMeasurementGroup;
    }

    public ComboBox<String> getPrdWeightUnitCB() {
        return prdWeightUnitCB;
    }

    public void setPrdWeightUnitCB(ComboBox<String> prdWeightUnitCB) {
        this.prdWeightUnitCB = prdWeightUnitCB;
    }

    public ComboBox<String> getPrdPriceUOMCB() {
        return prdPriceUOMCB;
    }

    public void setPrdPriceUOMCB(ComboBox<String> prdPriceUOMCB) {
        this.prdPriceUOMCB = prdPriceUOMCB;
    }

    public TextField getPrdWeightTxt() {
        return prdWeightTxt;
    }

    public void setPrdWeightTxt(TextField prdWeightTxt) {
        this.prdWeightTxt = prdWeightTxt;
    }

    public CheckBox getPrdIsFoodGradeCB() {
        return prdIsFoodGradeCB;
    }

    public void setPrdIsFoodGradeCB(CheckBox prdIsFoodGradeCB) {
        this.prdIsFoodGradeCB = prdIsFoodGradeCB;
    }

    public TextField getBaseProductTxt() {
        return baseProductTxt;
    }

    public void setBaseProductTxt(TextField baseProductTxt) {
        this.baseProductTxt = baseProductTxt;
    }

    public TextField getPrdQuantityTxt() {
        return prdQuantityTxt;
    }

    public void setPrdQuantityTxt(TextField prdQuantityTxt) {
        this.prdQuantityTxt = prdQuantityTxt;
    }

    public TextField getPrdExpiryDtTxt() {
        return prdExpiryDtTxt;
    }

    public void setPrdExpiryDtTxt(TextField prdExpiryDtTxt) {
        this.prdExpiryDtTxt = prdExpiryDtTxt;
    }

    public TextField getPrdMRPTxt() {
        return prdMRPTxt;
    }

    public void setPrdMRPTxt(TextField prdMRPTxt) {
        this.prdMRPTxt = prdMRPTxt;
    }

    public TextField getPrdCSPTxt() {
        return prdCSPTxt;
    }

    public void setPrdCSPTxt(TextField prdCSPTxt) {
        this.prdCSPTxt = prdCSPTxt;
    }

    public TextField getPrdInvoiceRefTxt() {
        return prdInvoiceRefTxt;
    }

    public void setPrdInvoiceRefTxt(TextField prdInvoiceRefTxt) {
        this.prdInvoiceRefTxt = prdInvoiceRefTxt;
    }

    public Button getCloseButton() {
        return closeButton;
    }

    public void setCloseButton(Button closeButton) {
        this.closeButton = closeButton;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public ComboBox<String> getPrdGSTTaxCB() {
        return prdGSTTaxCB;
    }

    public void setPrdGSTTaxCB(ComboBox<String> prdGSTTaxCB) {
        this.prdGSTTaxCB = prdGSTTaxCB;
    }

    public TextField getProductIdTxt() {
        return productIdTxt;
    }

    public void setProductIdTxt(TextField productIdTxt) {
        this.productIdTxt = productIdTxt;
    }

    public void generateBarCode(ActionEvent actionEvent) {
    }
}
