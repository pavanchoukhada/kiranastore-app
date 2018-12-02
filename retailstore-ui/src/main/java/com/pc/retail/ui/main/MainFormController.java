/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pc.retail.ui.main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author pavanc
 */
public class MainFormController implements Initializable {

    @FXML
    private MenuItem productFormMI;
    @FXML
    private MenuItem productManagerMI;
    @FXML
    private MenuItem invetoryManagerMI;

    @FXML
    private BorderPane borderPane;

    @FXML
    Pane centerPane;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    

    public void launchProductForm(ActionEvent actionEvent) throws IOException{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ProductForm.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root, 720, 550);
        stage.setTitle("Product Form");
        stage.setScene(scene);
        stage.show();
    }

    public void lauchProductMasterGrid(ActionEvent actionEvent) throws IOException{
        resetPaneWith(getClass().getClassLoader().getResource("ProductMasterGrid.fxml"));

    }

    public void launchInvMasterGrid(ActionEvent actionEvent) throws IOException {
        resetPaneWith(getClass().getClassLoader().getResource("InvoiceMasterGrid.fxml"));
    }

    public void launchInvGrid(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ProductInventoryEntryGridForm.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root, 1200, 1200);
        stage.setTitle("Product Inventory Form");

        stage.setScene(scene);
        stage.show();
    }

    public void launchGSTForm(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("GSTForm.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root, 400, 350);
        stage.setTitle("GST Form");
        stage.setScene(scene);
        stage.show();
    }

    public void launchGSTGrid(ActionEvent actionEvent) throws IOException {
        resetPaneWith(getClass().getClassLoader().getResource("GSTGridForm.fxml"));
    }

    private void resetPaneWith(URL resource) throws IOException {
        Parent root = FXMLLoader.load(resource);
        centerPane.getChildren().clear();
        centerPane.getChildren().add(root);
    }

    public void launchSupplierForm(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ProductSupplierForm.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root, 500, 450);
        stage.setTitle("Supplier Form");
        stage.setScene(scene);
        stage.show();
    }

    public void productCategoryForm(ActionEvent actionEvent) {
    }

    public void launchSupplierGrid(ActionEvent actionEvent) {
    }

    public void launchBillingForm(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("BillingGridForm.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root, 500, 450);
        stage.setTitle("Billing Form");
        stage.setScene(scene);
        stage.show();
    }
}
