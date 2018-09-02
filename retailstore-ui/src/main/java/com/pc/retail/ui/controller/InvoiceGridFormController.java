package com.pc.retail.ui.controller;

import com.pc.retail.interactor.KiranaStoreException;
import com.pc.retail.ui.helper.InvoiceGridFormHelper;
import com.pc.retail.vo.ProductInventory;
import com.pc.retail.vo.ProductInvoiceMasterDO;
import com.pc.retail.vo.ProductSupplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by pavanc on 7/16/17.
 */
public class InvoiceGridFormController  implements Initializable {

    @FXML
    ComboBox<String> invoiceStatusCB;
    @FXML
    DatePicker fromInvoiceDateDP;
    @FXML
    DatePicker toInvoiceDateDP;

    @FXML
    ComboBox<ProductSupplier> supplierCB;

    @FXML
    TableView<ProductInvoiceMasterDO> invoiceMasterGrid;

    InvoiceGridFormHelper invoiceGridFormHelper;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        invoiceGridFormHelper = new InvoiceGridFormHelper();
        populateForm();
        LocalDate currentDate = LocalDate.now();
        fromInvoiceDateDP.setValue(currentDate.withYear(currentDate.getYear()-1));
        toInvoiceDateDP.setValue(LocalDate.now());
        initializeInvGridTable();
        invoiceMasterGrid.setRowFactory(getTableRowSelectionHandler());
    }

    private Callback getTableRowSelectionHandler() {
        return tv -> {
            TableRow<ProductInvoiceMasterDO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    ProductInvoiceMasterDO rowData = row.getItem();
                    try {
                        launchInventoryGridForm(rowData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
            return row ;
        };
    }

    private void launchInventoryGridForm(ProductInvoiceMasterDO productInvoiceMasterDO) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("ProductInventoryEntryGridForm.fxml"));
        Parent root1 = fxmlLoader.load();
        ProductInventoryEntryGridFormController productInventoryEntryGridFormController = (ProductInventoryEntryGridFormController)fxmlLoader.getController();
        productInventoryEntryGridFormController.initData(productInvoiceMasterDO.getInvoiceId());
        Stage stage = new Stage();
        stage.setTitle("Invoice Detail Form");
        stage.setScene(new Scene(root1));
        stage.show();
    }

    private void populateForm() {
        populateInvoiceStatusCB();
        populateSupplierCB();
        LocalDate today = LocalDate.now();
        fromInvoiceDateDP.setValue(LocalDate.of(today.getYear(), today.getMonth().getValue(), today.getDayOfMonth()));
        toInvoiceDateDP.setValue(LocalDate.now());
    }

    private void populateSupplierCB() {
        ObservableList<ProductSupplier> supplierList
                = FXCollections.observableArrayList(invoiceGridFormHelper.getSupplierList());
        supplierCB.setItems(supplierList);
    }


    private void populateInvoiceStatusCB() {
        ObservableList<String> invoiceStatusList
                = FXCollections.observableArrayList(invoiceGridFormHelper.getInvoiceStatusList());
        invoiceStatusCB.setItems(invoiceStatusList);
    }

    private void initializeInvGridTable() {
        ObservableList productInvEntryGridColumns = invoiceMasterGrid.getColumns();
        productInvEntryGridColumns.add(createStringTableColumn("Invoice Id", 120, "invoiceId", "gridInvoiceId"));
        productInvEntryGridColumns.add(createStringTableColumn("Invoice Ref", 120, "invoiceRefId", "invoiceRefId"));
        productInvEntryGridColumns.add(createStringTableColumn("Invoice Date", 100, "invoiceDate", "invoiceDate"));
        productInvEntryGridColumns.add(createNumericTableColumn("Invoice Status", 100, "invoiceStatus", "invoiceStatus"));
        productInvEntryGridColumns.add(createStringTableColumn("Supplier Name", 100, "supplierName", "supplierName"));
        productInvEntryGridColumns.add(createNumericTableColumn("Other Cost", 120, "lumpsumCost", "lumpsumCost"));
        productInvEntryGridColumns.add(createNumericTableColumn("Invoice Amount", 100, "prdInvAmt", "prdInvAmt"));
        productInvEntryGridColumns.add(createNumericTableColumn("Total Amount", 100, "totalAmount", "gridTotalCostId"));
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

    @FXML
    public void applyFilter(ActionEvent actionEvent) {
        try {
            List<ProductInvoiceMasterDO> productInvoiceMasterDOList = invoiceGridFormHelper.getInvoiceMasterRecord(this);
            if(productInvoiceMasterDOList.isEmpty()){
                generateResponseToUser(Alert.AlertType.INFORMATION,"No Record found for given criteria");
            }else{
                populateInvoiceMasterGrid(productInvoiceMasterDOList);
            }
        } catch (KiranaStoreException e) {
            generateResponseToUser(Alert.AlertType.ERROR,"Contact Support. Error:" + e.getMessage());
        }
    }

    private void populateInvoiceMasterGrid(List<ProductInvoiceMasterDO> productInvoiceMasterDOList) {
        invoiceMasterGrid.setItems(FXCollections.observableArrayList(productInvoiceMasterDOList));
    }

    private void generateResponseToUser(Alert.AlertType warning, String message) {
        Alert alert = new Alert(warning, message);
        alert.showAndWait();
    }

    public ComboBox<String> getInvoiceStatusCB() {
        return invoiceStatusCB;
    }

    public void setInvoiceStatusCB(ComboBox<String> invoiceStatusCB) {
        this.invoiceStatusCB = invoiceStatusCB;
    }

    public DatePicker getFromInvoiceDateDP() {
        return fromInvoiceDateDP;
    }

    public void setFromInvoiceDateDP(DatePicker fromInvoiceDateDP) {
        this.fromInvoiceDateDP = fromInvoiceDateDP;
    }

    public DatePicker getToInvoiceDateDP() {
        return toInvoiceDateDP;
    }

    public void setToInvoiceDateDP(DatePicker toInvoiceDateDP) {
        this.toInvoiceDateDP = toInvoiceDateDP;
    }

    public ComboBox<ProductSupplier> getSupplierCB() {
        return supplierCB;
    }

}
