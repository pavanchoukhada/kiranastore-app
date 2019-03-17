package com.pc.retail.ui.controller;

import com.pc.retail.api.GSTReportDO;
import com.pc.retail.interactor.KiranaStoreException;
import com.pc.retail.ui.helper.GSTGridFormHelper;
import com.pc.retail.vo.GSTGroupModel;
import com.pc.retail.vo.ProductInventory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by pavanc on 3/10/19.
 */
public class GSTReportGridController implements Initializable{

    @FXML
    TreeTableView<GSTReportDO> gstReportDOTreeTableView;

    ObservableList<GSTReportDO> gstReportDOObservableList;

    GSTReportDOClientHelper gstReportDOClientHelper;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeGSTReportGridTable();
    }

    private void loadGSTReportDataInToGrid() throws KiranaStoreException {
        List<GSTReportDO> gstReportData = gstReportDOClientHelper.getGSTReportData();
        ObservableList<GSTReportDO> gstReportDOObservableList = FXCollections.observableArrayList();

        gstReportDOObservableList = FXCollections.observableArrayList(gstReportData);

    }

    private void initializeGSTReportGridTable() {
        ObservableList<TreeTableColumn<GSTReportDO, ?>> productInvEntryGridColumns = gstReportDOTreeTableView.getColumns();
        productInvEntryGridColumns.add(createStringTableColumn("GST Group Code", 120, "groupCode", "groupCode"));
        productInvEntryGridColumns.add(createNumericTableColumn("GST Rate", 120, "taxRate", "taxRate"));
        productInvEntryGridColumns.add(createNumericTableColumn("CGST", 100, "sGSTRate", "sGSTRate"));
        productInvEntryGridColumns.add(createNumericTableColumn("SGST", 100, "cGSTRate", "cGSTRate"));
    }

    private TreeTableColumn createNumericTableColumn(String columnText, int columnWidth, String propertyName, String columnId) {
        TreeTableColumn gridColumn = new TreeTableColumn(columnText);
        gridColumn.setMinWidth(columnWidth);
        gridColumn.setCellValueFactory(new PropertyValueFactory<GSTReportDO, Integer>(propertyName));
        gridColumn.setId(columnId);
        return gridColumn;
    }

    private TreeTableColumn createStringTableColumn(String columnText, int columnWidth, String propertyName, String columnId) {
        TreeTableColumn gridColumn = new TreeTableColumn(columnText);
        gridColumn.setMinWidth(columnWidth);
        gridColumn.setCellValueFactory(new PropertyValueFactory<GSTReportDO, String>(propertyName));
        gridColumn.setId(columnId);
        return gridColumn;
    }

    public void applyFilter(ActionEvent actionEvent) {
    }


}
