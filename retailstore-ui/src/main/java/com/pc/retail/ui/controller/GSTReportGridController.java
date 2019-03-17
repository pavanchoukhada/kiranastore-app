package com.pc.retail.ui.controller;

import com.pc.retail.api.GSTReportDO;
import com.pc.retail.api.SupplierGSTReportDO;
import com.pc.retail.interactor.KiranaStoreException;
import com.pc.ui.vo.GSTReportTreeTableDO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by pavanc on 3/10/19.
 */
public class GSTReportGridController implements Initializable{

    @FXML
    TreeTableView<GSTReportTreeTableDO> gstReportDOTreeTableView;

    ObservableList<GSTReportDO> gstReportDOObservableList;

    GSTReportDOClientHelper gstReportDOClientHelper;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeGSTReportGridTable();
    }

    private void loadGSTReportDataInToGrid() throws KiranaStoreException {
        List<GSTReportDO> gstReportDataList = gstReportDOClientHelper.getGSTReportData();
        boolean first = true;
        for(GSTReportDO gstReportDO : gstReportDataList){
            TreeItem<GSTReportTreeTableDO> treeItem = new TreeItem<>();
            GSTReportTreeTableDO gstReportTreeTableDO = new GSTReportTreeTableDO();
            gstReportTreeTableDO.setGstCode(gstReportDO.getGstCode());
            gstReportTreeTableDO.setTotalCGSTAmount(gstReportDO.getTotalCGSTAmount());
            gstReportTreeTableDO.setTotalSGSTAmount(gstReportDO.getTotalSGSTAmount());
            gstReportTreeTableDO.setTotalGSTAmount(gstReportDO.getTotalGSTAmount());
            treeItem.setValue(gstReportTreeTableDO);
            for(SupplierGSTReportDO supplierGSTReportDO : gstReportDO.getSupplierGSTReportDOList()){
                GSTReportTreeTableDO gstReportTreeTableDOSupplier = new GSTReportTreeTableDO();
                gstReportTreeTableDOSupplier.setSupplierCode(supplierGSTReportDO.getSupplierCode());
                gstReportTreeTableDOSupplier.setTotalCGSTAmount(supplierGSTReportDO.getTotalCGSTAmount());
                gstReportTreeTableDOSupplier.setTotalSGSTAmount(supplierGSTReportDO.getTotalSGSTAmount());
                gstReportTreeTableDOSupplier.setTotalGSTAmount(supplierGSTReportDO.getTotalGSTAmount());
                TreeItem<GSTReportTreeTableDO> supplierNode = new TreeItem<>();
                treeItem.getChildren().add(supplierNode);
            }
            if(first) {
                gstReportDOTreeTableView.setRoot(treeItem);
                first = false;
            }

        }
    }

    private void initializeGSTReportGridTable() {
        ObservableList<TreeTableColumn<GSTReportTreeTableDO, ?>> productInvEntryGridColumns = gstReportDOTreeTableView.getColumns();
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
