package com.pc.retail.ui.controller;

import com.pc.retail.ui.helper.GSTGridFormHelper;
import com.pc.retail.vo.GSTGroupModel;
import com.pc.retail.vo.ProductInventory;
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
 * Created by pavanc on 8/20/17.
 */
public class GSTGridFormController implements Initializable {

    @FXML
    TableView<GSTGroupModel> gstGroupGrid;

    GSTGridFormHelper gstGridFormHelper;

    ObservableList<GSTGroupModel> gstGroupModels;

    public GSTGridFormController(){
        gstGridFormHelper = new GSTGridFormHelper();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeInvGridTable();
        loadGSTGroupGrid();
    }

    private void loadGSTGroupGrid(){
        gstGroupModels = FXCollections.observableArrayList(gstGridFormHelper.getGSTGroupModelList());
        gstGroupGrid.setItems(gstGroupModels);
    }

    private void initializeInvGridTable() {
        ObservableList productInvEntryGridColumns = gstGroupGrid.getColumns();
        productInvEntryGridColumns.add(createStringTableColumn("GST Group Code", 120, "groupCode", "groupCode"));
        productInvEntryGridColumns.add(createNumericTableColumn("GST Rate", 120, "taxRate", "taxRate"));
        productInvEntryGridColumns.add(createNumericTableColumn("CGST", 100, "sGSTRate", "sGSTRate"));
        productInvEntryGridColumns.add(createNumericTableColumn("SGST", 100, "cGSTRate", "cGSTRate"));
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

    public void addNewGSTGroup(ActionEvent actionEvent) throws IOException{
        launchGSTForm(new GSTGroupModel());
    }

    private void launchGSTForm(GSTGroupModel gstGroupModel) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("GSTForm.fxml"));


        Parent root1 = fxmlLoader.load();

        GSTFormController controller =
                fxmlLoader.<GSTFormController>getController();
        controller.initData(gstGroupModel);
        controller.hookGSTGrid(this);
        Stage stage = new Stage();
        stage.setTitle("GST Form");
        stage.setScene(new Scene(root1));
        stage.show();
    }

    @FXML
    public void modifyGSTGroup(ActionEvent actionEvent) throws IOException{
        GSTGroupModel gstGroupModel =
                gstGroupGrid.getItems().get(gstGroupGrid.getSelectionModel().getSelectedIndex());
        launchGSTForm(gstGroupModel);

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

            gstGroupGrid.getItems().remove(gstGroupGrid.getSelectionModel().getSelectedIndex());
        }
    }
}
