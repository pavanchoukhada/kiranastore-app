package com.pc.retail.ui.controller;

import com.pc.retail.api.GSTReportDO;
import com.pc.retail.api.SupplierGSTReportDO;
import com.pc.retail.interactor.KiranaStoreException;
import com.pc.ui.vo.GSTReportTreeTableDO;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;

import java.net.URL;
import java.util.*;

/**
 * Created by pavanc on 3/10/19.
 */
public class GSTReportGridController implements Initializable{

    @FXML
    private TreeTableView<GSTReportTreeTableDO> gstReportTreeTableView;

    @FXML
    private ComboBox<String> groupByCB;

    private GSTReportDOClientHelper gstReportDOClientHelper = new GSTReportDOClientHelper();
    private List<GSTReportDO> gstReportDataList;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            groupByCB.valueProperty().addListener(getChangeListenerForGroupBy());
            groupByCB.setItems( FXCollections.observableArrayList(Arrays.asList("GST", "Supplier")));
            groupByCB.setValue("GST");
            gstReportDataList = gstReportDOClientHelper.getGSTReportData();
            regenerateTreeTableGrid();
        } catch (KiranaStoreException e) {
            e.printStackTrace();
        }
    }

    private ChangeListener<String> getChangeListenerForGroupBy() {
        return (observable, oldValue, newValue) -> {
            if(newValue != null && oldValue!= null && !oldValue.equals(newValue )){
                try {
                    regenerateTreeTableGrid();
                } catch (KiranaStoreException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void regenerateTreeTableGrid() throws KiranaStoreException {
        gstReportTreeTableView.getColumns().clear();
        gstReportTreeTableView.setRoot(null);
        if(groupByCB.getValue().equals("GST")){
            initializeGSTReportGridTableAsPerGSTGrouping();
        }else{
            initializeGSTReportGridTableAsPerSupplierGrouping();
        }
    }


    private void initializeGSTReportGridTableAsPerGSTGrouping() throws KiranaStoreException {
        ObservableList<TreeTableColumn<GSTReportTreeTableDO, ?>> productInvEntryGridColumns = gstReportTreeTableView.getColumns();
        productInvEntryGridColumns.add(createStringTableColumn("GST Code", 120, "gstCode", "gstCode"));
        productInvEntryGridColumns.add(createStringTableColumn("Counter Party", 120, "supplierCode", "supplierCode"));
        productInvEntryGridColumns.add(createNumericTableColumn("CGST", 100, "totalCGSTAmount", "totalCGSTAmount"));
        productInvEntryGridColumns.add(createNumericTableColumn("SGST", 100, "totalSGSTAmount", "totalCGSTAmount"));
        productInvEntryGridColumns.add(createNumericTableColumn("GST Amount", 100, "totalGSTAmount", "totalGSTAmount"));
        loadGSTReportDataInToGridGroupByGST();
    }

    private void initializeGSTReportGridTableAsPerSupplierGrouping() throws KiranaStoreException {
        ObservableList<TreeTableColumn<GSTReportTreeTableDO, ?>> productInvEntryGridColumns = gstReportTreeTableView.getColumns();
        productInvEntryGridColumns.add(createStringTableColumn("Counter Party", 120, "supplierCode", "supplierCode"));
        productInvEntryGridColumns.add(createStringTableColumn("GST Code", 120, "gstCode", "gstCode"));
        productInvEntryGridColumns.add(createNumericTableColumn("CGST", 100, "totalCGSTAmount", "totalCGSTAmount"));
        productInvEntryGridColumns.add(createNumericTableColumn("SGST", 100, "totalSGSTAmount", "totalCGSTAmount"));
        productInvEntryGridColumns.add(createNumericTableColumn("GST Amount", 100, "totalGSTAmount", "totalGSTAmount"));
        loadGSTReportDataInToGridGroupBySupplier();
    }


    private void loadGSTReportDataInToGridGroupByGST() throws KiranaStoreException {
        TreeItem<GSTReportTreeTableDO> rootItem = new TreeItem<>();
        GSTReportTreeTableDO gstReportTreeTableDORootValue = new GSTReportTreeTableDO();
        gstReportTreeTableDORootValue.setGstCode("GST");
        rootItem.setValue(gstReportTreeTableDORootValue);
        rootItem.setExpanded(true);
        gstReportTreeTableView.setRoot(rootItem);
        double totalGSTAmount = 0.0d, totalCGSTAmount=  0.0d, totalSGSTAmount = 0.0d;
        for(GSTReportDO gstReportDO : gstReportDataList){
            TreeItem<GSTReportTreeTableDO> gstTreeItem = new TreeItem<>();

            GSTReportTreeTableDO gstReportTreeTableDO = new GSTReportTreeTableDO();
            gstReportTreeTableDO.setGstCode(gstReportDO.getGstCode());
            gstReportTreeTableDO.setTotalCGSTAmount(gstReportDO.getTotalCGSTAmount());
            gstReportTreeTableDO.setTotalSGSTAmount(gstReportDO.getTotalSGSTAmount());
            gstReportTreeTableDO.setTotalGSTAmount(gstReportDO.getTotalGSTAmount());
            totalCGSTAmount += gstReportDO.getTotalCGSTAmount();
            totalSGSTAmount += gstReportDO.getTotalSGSTAmount();
            totalGSTAmount += gstReportDO.getTotalGSTAmount();
            gstTreeItem.setValue(gstReportTreeTableDO);
            for(SupplierGSTReportDO supplierGSTReportDO : gstReportDO.getSupplierGSTReportDOList()){
                GSTReportTreeTableDO gstReportTreeTableDOSupplier = new GSTReportTreeTableDO();
                gstReportTreeTableDOSupplier.setSupplierCode(supplierGSTReportDO.getSupplierCode());
                gstReportTreeTableDOSupplier.setTotalCGSTAmount(supplierGSTReportDO.getTotalCGSTAmount());
                gstReportTreeTableDOSupplier.setTotalSGSTAmount(supplierGSTReportDO.getTotalSGSTAmount());
                gstReportTreeTableDOSupplier.setTotalGSTAmount(supplierGSTReportDO.getTotalGSTAmount());
                TreeItem<GSTReportTreeTableDO> supplierTreeItem = new TreeItem<>();
                supplierTreeItem.setValue(gstReportTreeTableDOSupplier);
                gstTreeItem.getChildren().add(supplierTreeItem);
            }
            gstReportTreeTableDORootValue.setTotalCGSTAmount(totalCGSTAmount);
            gstReportTreeTableDORootValue.setTotalSGSTAmount(totalSGSTAmount);
            gstReportTreeTableDORootValue.setTotalGSTAmount(totalGSTAmount);

            rootItem.getChildren().add(gstTreeItem);
        }
    }

    private void loadGSTReportDataInToGridGroupBySupplier(){
        TreeItem<GSTReportTreeTableDO> rootItem = new TreeItem<>();
        GSTReportTreeTableDO gstReportTreeTableDORootValue = new GSTReportTreeTableDO();
        gstReportTreeTableDORootValue.setSupplierCode("Supplier");
        rootItem.setValue(gstReportTreeTableDORootValue);
        rootItem.setExpanded(true);
        gstReportTreeTableView.setRoot(rootItem);
        rootItem.getChildren().addAll(transformGSTReportBySupplierGrouping(gstReportTreeTableDORootValue));

    }
    private Collection<TreeItem<GSTReportTreeTableDO>> transformGSTReportBySupplierGrouping(GSTReportTreeTableDO gstReportTreeTableDORootValue){
        Map<String, TreeItem<GSTReportTreeTableDO>> gstReportTreeTableDOMap = new HashMap<>();
        double totalGSTAmount = 0.0d, totalCGSTAmount=  0.0d, totalSGSTAmount = 0.0d;
        for(GSTReportDO gstReportDO : gstReportDataList){
            for(SupplierGSTReportDO supplierGSTReportDO : gstReportDO.getSupplierGSTReportDOList()){
                TreeItem<GSTReportTreeTableDO> supplierTreeItem = gstReportTreeTableDOMap.get(supplierGSTReportDO.getSupplierCode());
                if(supplierTreeItem == null){
                    GSTReportTreeTableDO supplierGrpGSTReportTreeTableDO = new GSTReportTreeTableDO();
                    supplierTreeItem = new TreeItem<>();
                    supplierGrpGSTReportTreeTableDO.setSupplierCode(supplierGSTReportDO.getSupplierCode());
                    gstReportTreeTableDOMap.put(supplierGSTReportDO.getSupplierCode(), supplierTreeItem);
                    supplierTreeItem.setValue(supplierGrpGSTReportTreeTableDO);
                }
                GSTReportTreeTableDO supplierGrpGSTReportTreeTableDO = supplierTreeItem.getValue();
                TreeItem<GSTReportTreeTableDO> gstTreeItem = new TreeItem<>();
                GSTReportTreeTableDO gstGroupGSTReportTreeTableDO = new GSTReportTreeTableDO();
                gstGroupGSTReportTreeTableDO.setGstCode(gstReportDO.getGstCode());
                gstTreeItem.setValue(gstGroupGSTReportTreeTableDO);
                gstGroupGSTReportTreeTableDO.setTotalGSTAmount(supplierGSTReportDO.getTotalGSTAmount());
                gstGroupGSTReportTreeTableDO.setTotalCGSTAmount(supplierGSTReportDO.getTotalCGSTAmount());
                gstGroupGSTReportTreeTableDO.setTotalSGSTAmount(supplierGSTReportDO.getTotalSGSTAmount());
                supplierGrpGSTReportTreeTableDO.setTotalGSTAmount(supplierGrpGSTReportTreeTableDO.getTotalGSTAmount() + supplierGSTReportDO.getTotalGSTAmount());
                supplierGrpGSTReportTreeTableDO.setTotalCGSTAmount(supplierGrpGSTReportTreeTableDO.getTotalCGSTAmount() + supplierGSTReportDO.getTotalCGSTAmount());
                supplierGrpGSTReportTreeTableDO.setTotalSGSTAmount(supplierGrpGSTReportTreeTableDO.getTotalSGSTAmount() + supplierGSTReportDO.getTotalSGSTAmount());
                supplierTreeItem.getChildren().add(gstTreeItem);

                totalCGSTAmount += supplierGSTReportDO.getTotalCGSTAmount();
                totalSGSTAmount += supplierGSTReportDO.getTotalSGSTAmount();
                totalGSTAmount += supplierGSTReportDO.getTotalGSTAmount();

            }
        }
        gstReportTreeTableDORootValue.setTotalCGSTAmount(totalCGSTAmount);
        gstReportTreeTableDORootValue.setTotalSGSTAmount(totalSGSTAmount);
        gstReportTreeTableDORootValue.setTotalGSTAmount(totalGSTAmount);
        return gstReportTreeTableDOMap.values();
    }


    private TreeTableColumn createNumericTableColumn(String columnText, int columnWidth, String propertyName, String columnId) {
        TreeTableColumn<GSTReportTreeTableDO, Integer> gridColumn = new TreeTableColumn<>(columnText);
        gridColumn.setMinWidth(columnWidth);
        gridColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>(propertyName));
        gridColumn.setId(columnId);
        return gridColumn;
    }

    private TreeTableColumn createStringTableColumn(String columnText, int columnWidth, String propertyName, String columnId) {
        TreeTableColumn<GSTReportTreeTableDO, String> gridColumn = new TreeTableColumn<>(columnText);
        gridColumn.setMinWidth(columnWidth);
        gridColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>(propertyName));
        /*gridColumn.setCellValueFactory((Callback<TreeTableColumn.CellDataFeatures<GSTReportTreeTableDO, String>, ObservableValue<String>>) p -> {
            // p.getValue() returns the TreeItem instance for a particular
            // TreeTableView row, and the second getValue() call returns the
            // Person instance contained within the TreeItem.
            return p.getValue().getValue().getSupplierCode();
        });*/
        gridColumn.setId(columnId);
        return gridColumn;
    }

    public void applyFilter(ActionEvent actionEvent) {
    }


}
