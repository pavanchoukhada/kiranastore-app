package com.pc.retail.ui.controller;

import com.pc.retail.interactor.KiranaAppResult;
import com.pc.retail.interactor.ResultType;
import com.pc.retail.ui.helper.ProductSupplierFormHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by pavanc on 7/23/17.
 */
public class ProductSupplierFormController implements Initializable{

    @FXML
    TextField supplierShortCodeTxt;
    @FXML
    TextField supplierNameTxt;
    @FXML
    TextField supplierMobileNoTxt;
    @FXML
    TextField supplierPhoneNoTxt;
    @FXML
    TextField supplierAddressTxt;
    @FXML
    TextField suppplierGSTNNoTxt;

    @FXML
    Button closeButton;

    ProductSupplierFormHelper productSupplierFormHelper;
    private int supplierId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productSupplierFormHelper = new ProductSupplierFormHelper();
        supplierShortCodeTxt.requestFocus();
    }

    @FXML
    public void submitForm(ActionEvent event){
        KiranaAppResult kiranaAppResult = productSupplierFormHelper.submitForm(this);
        showResponseToUser(kiranaAppResult);
        resetForm();
    }

    private void resetForm() {
        supplierShortCodeTxt.clear();
        supplierNameTxt.clear();
        supplierMobileNoTxt.clear();
        supplierPhoneNoTxt.clear();
        supplierAddressTxt.clear();
        suppplierGSTNNoTxt.clear();
        supplierShortCodeTxt.requestFocus();
    }

    private void showResponseToUser(KiranaAppResult kiranaAppResult) {
        if(kiranaAppResult.getResultType() == ResultType.SUCCESS){
            generateResponseToUser(Alert.AlertType.INFORMATION, "Product Saved Successfully");
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
    public void closeForm(ActionEvent actionEvent) {
        Stage stage = (Stage)closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void submitFormAndClose(ActionEvent actionEvent) {
        submitForm(actionEvent);
        closeForm(actionEvent);
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierShortCode(){
        return this.supplierShortCodeTxt.getText();
    }

    public String getSupplierName(){
        return this.supplierNameTxt.getText();
    }

    public String getSupplierMobileNo(){
        return this.supplierMobileNoTxt.getText();
    }

    public String getSupplierPhoneNo(){
        return this.supplierPhoneNoTxt.getText();
    }

    public String getSupplierAddress(){
        return this.supplierAddressTxt.getText();
    }

    public String getSupplierGSTNNo(){
        return this.suppplierGSTNNoTxt.getText();
    }

}
