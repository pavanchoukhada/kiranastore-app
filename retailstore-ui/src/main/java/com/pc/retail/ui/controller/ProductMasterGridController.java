package com.pc.retail.ui.controller;

import com.pc.retail.interactor.KiranaStoreException;
import com.pc.retail.ui.helper.ProductMasterGridHelper;
import com.pc.retail.vo.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by pavanc on 7/16/17.
 */
public class ProductMasterGridController  implements Initializable {

    @FXML
    ComboBox<String> categoryCB;

    @FXML
    ComboBox<String> companyCB;

    @FXML
    TextField searchKeyTxt;

    @FXML
    TableView<ProductAndInvDO> productMasterGrid;

    ProductMasterGridHelper productMasterGridHelper;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productMasterGridHelper = new ProductMasterGridHelper();
        populateForm();
        initializeInvGridTable();
        productMasterGrid.setRowFactory(getTableRowSelectionHandler());
        populateProductMasterGrid();
        setMenu();
    }


    private Callback getTableRowSelectionHandler() {
        return tv -> {
            TableRow<ProductAndInvDO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    ProductAndInvDO rowData = row.getItem();
                    try {
                        launchProductForm(rowData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
            return row ;
        };
    }

    private void launchProductForm(ProductAndInvDO productAndInvDO) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("ProductForm.fxml"));
        Parent root1 = fxmlLoader.load();
        ProductFormController productFormController = (ProductFormController) fxmlLoader.getController();
        if(productAndInvDO != null) {
            productFormController.initData(productAndInvDO.getBarcode(), this);
        }
        Stage stage = new Stage();
        stage.setTitle("Product Form");
        stage.setScene(new Scene(root1));
        stage.setHeight(250);
        stage.setHeight(620);
        stage.show();
    }

    private void launchProductInventoryGrid(int productId) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("ProductInventoryGrid.fxml"));
        Parent root1 = fxmlLoader.load();
        ProductInvTransactionGridFormController productFormController = (ProductInvTransactionGridFormController) fxmlLoader.getController();
        productFormController.initData(productId);
        Stage stage = new Stage();
        stage.setTitle("Product Inventory History");
        stage.setScene(new Scene(root1));
        stage.setHeight(800);
        stage.setWidth(1200);
        stage.show();
    }


    private void setMenu(){
        final ContextMenu contextMenu = new ContextMenu();
        MenuItem barcodeGenMI = new MenuItem("Generate Barcode");
        MenuItem invetoryHistoryMI = new MenuItem("Inventory History");
        MenuItem addProductMI = new MenuItem("Add Product");
        contextMenu.getItems().addAll(barcodeGenMI, invetoryHistoryMI, addProductMI);
        invetoryHistoryMI.setOnAction(getMenuActionForInventoryHistory());
        addProductMI.setOnAction(getMenuActionForAddProduct());
        productMasterGrid.setContextMenu(contextMenu);
    }

    private EventHandler<ActionEvent> getMenuActionForInventoryHistory() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    launchProductInventoryGrid(productMasterGrid.getSelectionModel().getSelectedItem().getProductId());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private EventHandler<ActionEvent> getMenuActionForAddProduct() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    launchProductForm(null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }


    private void populateForm() {
        populateCompanyCB();
        populateCategoryCB();
    }

    private void populateCategoryCB() {
        ObservableList<String> productCategories
                = FXCollections.observableArrayList(productMasterGridHelper.getProductCategories());
        categoryCB.setItems(productCategories);
    }


    private void populateCompanyCB() {
        ObservableList<String> productCompanies
                = FXCollections.observableArrayList(productMasterGridHelper.getProductCompanies());
        companyCB.setItems(productCompanies);
    }

    private void initializeInvGridTable() {
        ObservableList productInvEntryGridColumns = productMasterGrid.getColumns();
        productInvEntryGridColumns.add(createStringTableColumn("Bar Code", 120, "barcode", "barcode"));
        productInvEntryGridColumns.add(createStringTableColumn("Short Code", 100, "prdCode", "prdCode"));
        productInvEntryGridColumns.add(createStringTableColumn("Description", 100, "prdDesc", "prdDesc"));
        productInvEntryGridColumns.add(createStringTableColumn("Category", 100, "category", "category"));
        productInvEntryGridColumns.add(createStringTableColumn("Company", 120, "companyCode", "companyCode"));
        productInvEntryGridColumns.add(createNumericTableColumn("CSP", 60, "currentSellingPrice", "currentSellingPrice"));
        productInvEntryGridColumns.add(createStringTableColumn("Price UOM", 60, "priceUomCd", "priceUomCd"));
        productInvEntryGridColumns.add(createNumericTableColumn("MRP", 60, "MRP", "MRP"));
        productInvEntryGridColumns.add(createNumericTableColumn("Current Qty", 60, "currentAvailableQty", "currentAvailableQty"));
        productInvEntryGridColumns.add(createStringTableColumn("Qty UOM", 60, "qtyUomCd", "qtyUomCd"));
        productInvEntryGridColumns.add(createStringTableColumn("Expiry Date", 60, "expiryDate", "expiryDate"));
        productInvEntryGridColumns.add(createStringTableColumn("GST Group", 60, "gstTaxGroup", "gstTaxGroup"));
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
        populateProductMasterGrid();
    }

    private void populateProductMasterGrid() {
        try {
            List<ProductAndInvDO> productList = productMasterGridHelper.getProducts(this);
            if(productList.isEmpty()){
                generateResponseToUser(Alert.AlertType.INFORMATION,"No Record found for given criteria");
            }else{
                populateProductMasterGrid(productList);
            }
        } catch (KiranaStoreException e) {
            generateResponseToUser(Alert.AlertType.ERROR,"Contact Support. Error:" + e.getMessage());
        }
    }

    private void populateProductMasterGrid(List<ProductAndInvDO> productList) {
        productMasterGrid.setItems(FXCollections.observableArrayList(productList));
    }

    private void generateResponseToUser(Alert.AlertType warning, String message) {
        Alert alert = new Alert(warning, message);
        alert.showAndWait();
    }

    public ComboBox<String> getCompanyCB() {
        return companyCB;
    }

    public void setCompanyCB(ComboBox<String> companyCB) {
        this.companyCB = companyCB;
    }

    public TextField getSearchKeyTxt() {
        return searchKeyTxt;
    }

    public void setSearchKeyTxt(TextField searchKeyTxt) {
        this.searchKeyTxt = searchKeyTxt;
    }

    public ComboBox<String> getCategoryCB() {
        return categoryCB;
    }
}
