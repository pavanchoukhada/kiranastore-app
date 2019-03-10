package com.pc.retail.ui.controller;

import com.pc.retail.ui.helper.ProductSupplierFormHelper;
import com.pc.retail.vo.ProductInventory;
import com.pc.retail.vo.ProductSupplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by pavanc on 3/2/19.
 */
public class SupplierGridFormController  implements Initializable {


    @FXML
    TableView<ProductSupplier> productSuppliersGrid;

    ProductSupplierFormHelper productSupplierFormHelper;

    ObservableList<ProductSupplier> supplierObservableList;

    public SupplierGridFormController(){
        productSupplierFormHelper = new ProductSupplierFormHelper();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeSupplierGridTable();
        loadSupplierGrid();
    }



    public void loadSupplierGrid(){
        supplierObservableList = FXCollections.observableArrayList(productSupplierFormHelper.getSupplierList());
        productSuppliersGrid.setItems(supplierObservableList);

    }

    private void initializeSupplierGridTable() {
        ObservableList productInvEntryGridColumns = productSuppliersGrid.getColumns();
        productInvEntryGridColumns.add(createStringTableColumn("Supplier Code", 120, "code", "code"));
        productInvEntryGridColumns.add(createNumericTableColumn("Supplier Name", 160, "name", "name"));
        productInvEntryGridColumns.add(createNumericTableColumn("GST No.", 100, "gstnId", "gstnId"));
        productInvEntryGridColumns.add(createStringTableColumn("Mobile No.", 100, "mobileNo", "mobileNo"));
    }

    private TableColumn createNumericTableColumn(String columnText, int columnWidth, String propertyName, String columnId) {
        TableColumn productIdCol = new TableColumn(columnText);
        productIdCol.setMinWidth(columnWidth);
        productIdCol.setCellValueFactory(new PropertyValueFactory<ProductInventory, String>(propertyName));
        productIdCol.setId(columnId);
        return productIdCol;
    }

    private TableColumn createStringTableColumn(String columnText, int columnWidth, String propertyName, String columnId) {
        TableColumn productIdCol = new TableColumn(columnText);
        productIdCol.setMinWidth(columnWidth);
        productIdCol.setCellValueFactory(new PropertyValueFactory<ProductInventory, String>(propertyName));
        productIdCol.setId(columnId);
        return productIdCol;
    }

    public void addNewSupplier(ActionEvent actionEvent) throws IOException {
        launchSupplierForm(new ProductSupplier());
    }

    private void launchSupplierForm(ProductSupplier productSupplier) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("ProductSupplierForm.fxml"));


        Parent root1 = fxmlLoader.load();

        ProductSupplierFormController controller =
                fxmlLoader.getController();
        controller.initData(productSupplier);
        controller.hookGSTGrid(this);
        Stage stage = new Stage();
        stage.setTitle("GST Form");
        stage.setScene(new Scene(root1));
        stage.show();
    }

    @FXML
    public void modifySupplier(ActionEvent actionEvent) throws IOException{
        ProductSupplier productSupplier =
                productSuppliersGrid.getItems().get(productSuppliersGrid.getSelectionModel().getSelectedIndex());
        launchSupplierForm(productSupplier);

    }

    private ButtonType generateResponseToUser(Alert.AlertType warning, String message) {
        Alert alert = new Alert(warning, message);
        Optional<ButtonType> buttonType = alert.showAndWait();
        return buttonType.get();
    }

    @FXML
    public void deleteGSTGroup(ActionEvent actionEvent){
        ButtonType buttonType = generateResponseToUser(Alert.AlertType.CONFIRMATION,"Do you want to delete selected Item?");
        if(buttonType == ButtonType.OK) {
            productSuppliersGrid.getItems().remove(productSuppliersGrid.getSelectionModel().getSelectedIndex());
        }
    }

    public void refreshGrid(ActionEvent actionEvent) {
        loadSupplierGrid();
    }
}
