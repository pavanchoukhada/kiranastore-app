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
import com.pc.retail.interactor.KiranaStoreException;
import com.pc.retail.interactor.MeasurementType;
import com.pc.retail.interactor.ResultType;
import com.pc.retail.ui.event.handler.GetProductDetailHandler;
import com.pc.retail.ui.event.handler.UpdateProductDetail;
import com.pc.retail.ui.helper.ProductInvEntryFormHelper;
import com.pc.retail.vo.Product;
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
public class ProductFormController implements Initializable , UpdateProductDetail{

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
    ComboBox<ComboKeyValuePair> prdBaseProduct;
    @FXML
    ToggleGroup prdBaseProductChoiceGrp;

    @FXML
    ComboBox<String> prdCompanyCB;
    @FXML
    ToggleGroup prdMeasurementGroup;
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
    TextField prdCSPTxt;

    @FXML
    RadioButton weightToggle;
    @FXML
    RadioButton countToggle;

    @FXML
    RadioButton yesToggle;
    @FXML
    RadioButton noToggle;

    @FXML
    TextField productIdTxt;

    @FXML
    Button closeButton;
    @FXML
    Button generateBarcodeButton;

    ProductInvEntryFormHelper productInvEntryFormHelper;

    ProductMasterGridController productMasterGridController;

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
        prdMeasurementGroup.getToggles().get(0).setSelected(true);
        setMeasurementFlag();
        this.noToggle.setSelected(true);
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

        ObservableList<ComboKeyValuePair> baseProductList = FXCollections.observableArrayList(productInvEntryFormHelper.getBaseProductList());
        prdBaseProduct.setItems(baseProductList);

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
        productIdTxt.setDisable(false);
        getPrdBarCodeTxt().setDisable(false);
        generateBarcodeButton.setVisible(true);
        //productMasterGridController.refresh();
        getPrdBarCodeTxt().requestFocus();
    }

    private void showResponseToUser(KiranaAppResult kiranaAppResult) {
        if(kiranaAppResult.getResultType() == ResultType.SUCCESS){
            generateResponseToUser(Alert.AlertType.INFORMATION, "Product Saved Successfully");
            resetForm();
        }else if(kiranaAppResult.getResultType() == ResultType.APP_ERROR){
            generateResponseToUser(Alert.AlertType.WARNING, kiranaAppResult.getMessage());
        }else{
            generateResponseToUser(Alert.AlertType.ERROR, "System Error, Contact Support:"+ kiranaAppResult.getMessage());
        }
    }

    private void generateResponseToUser(Alert.AlertType warning, String message) {
        Alert alert = new Alert(warning, message);
        alert.showAndWait();
    }

    @FXML
    public void resetForm() {
        this.prdCSPTxt.clear();
        this.prdWeightTxt.clear();
        this.prdWeightUnitCB.setValue("");
        this.prdPriceUOMCB.setValue("");
        this.prdShortCodeTxt.clear();
        this.prdCategoryCB.setValue("");
        this.prdBarCodeTxt.clear();
        this.prdDescriptionTxt.clear();
        this.prdSearchKeyTxt.clear();
        this.productIdTxt.clear();
        this.prdBarCodeTxt.requestFocus();
        this.prdCompanyCB.setValue("");
        this.productId = -1;
        this.prdGSTTaxCB.setValue("");
        this.prdBaseProduct.setValue(new ComboKeyValuePair());
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
            if(product.getBaseProductBarCode() != null) {
                this.prdBaseProduct.setValue(new ComboKeyValuePair(product.getBaseProductBarCode(), product.getBaseProductCode()));
            }
            if(product.isBaseProductFlag()) {
                this.yesToggle.setSelected(true);
            }else{
                this.noToggle.setSelected(true);
            }
            if(product.getMeasurementType() == MeasurementType.WEIGHT){
                this.weightToggle.setSelected(true);
            }else{
                this.countToggle.setSelected(true);
            }
            if(product.getWeight() > 0) {
                this.prdWeightTxt.setText(String.valueOf(product.getWeight()));
            }
            this.prdWeightUnitCB.setValue(product.getQtyUomCd());
            this.getPrdBarCodeTxt().setText(product.getBarcode());
            this.productIdTxt.setText(String.valueOf(product.getProductId()));
            this.productId = product.getProductId();
            this.prdGSTTaxCB.setValue(product.getGstTaxGroup());
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

    public TextField getPrdDescriptionTxt() {
        return prdDescriptionTxt;
    }

    public TextField getPrdShortCodeTxt() {
        return prdShortCodeTxt;
    }

    public TextField getPrdSearchKeyTxt() {
        return prdSearchKeyTxt;
    }

    public ComboBox<String> getPrdCategoryCB() {
        return prdCategoryCB;
    }

    public ComboBox<String> getPrdCompanyCB() {
        return prdCompanyCB;
    }

    public ToggleGroup getPrdMeasurementGroup() {
        return prdMeasurementGroup;
    }

    public ComboBox<String> getPrdWeightUnitCB() {
        return prdWeightUnitCB;
    }

    public ComboBox<String> getPrdPriceUOMCB() {
        return prdPriceUOMCB;
    }

    public TextField getPrdWeightTxt() {
        return prdWeightTxt;
    }

    public ComboBox<ComboKeyValuePair> getPrdBaseProduct() {
        return prdBaseProduct;
    }

    public TextField getPrdCSPTxt() {
        return prdCSPTxt;
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

    public TextField getProductIdTxt() {
        return productIdTxt;
    }

    public void setProductIdTxt(TextField productIdTxt) {
        this.productIdTxt = productIdTxt;
    }

    public void generateBarCode(ActionEvent actionEvent) throws KiranaStoreException {
        int barCode = productInvEntryFormHelper.generateBarCode();
        getPrdBarCodeTxt().setText(String.valueOf(barCode));
    }

    public ToggleGroup getPrdBaseProductChoiceGrp() {
        return prdBaseProductChoiceGrp;
    }

    public CheckBox getPrdIsFoodGradeCB() {
        return prdIsFoodGradeCB;
    }

    public void initData(String barCode, ProductMasterGridController productMasterGridController) {
        this.productMasterGridController = productMasterGridController;
        updateForm(productInvEntryFormHelper.getProductInfo(barCode));
        productIdTxt.setDisable(true);
        getPrdBarCodeTxt().setDisable(true);
        generateBarcodeButton.setVisible(false);
    }
}
