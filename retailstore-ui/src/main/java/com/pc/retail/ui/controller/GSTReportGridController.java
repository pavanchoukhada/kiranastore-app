package com.pc.retail.ui.controller;

import com.pc.retail.api.GSTReportDO;
import com.pc.retail.api.InvoiceGSTReportDO;
import com.pc.retail.api.SupplierGSTReportDO;
import com.pc.retail.interactor.KiranaStoreException;
import com.pc.retail.vo.ProductSupplier;
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
import java.time.LocalDate;
import java.util.*;

/**
 * Created by pavanc on 3/10/19.
 */
public class GSTReportGridController implements Initializable{

    @FXML
    private TreeTableView<GSTReportTreeTableDO> gstReportTreeTableView;

    @FXML
    DatePicker fromGstReportDateDP;
    @FXML
    DatePicker toGstReportDateDP;
    @FXML
    ComboBox<ProductSupplier> supplierCB;
    @FXML
    private ComboBox<String> groupByCB;

    private GSTReportDOClientHelper gstReportDOClientHelper = new GSTReportDOClientHelper();

    private Collection<SupplierGSTReportDO> supplierGSTReportDOList;

    private String currentSelectedGrid;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            LocalDate currentDate = LocalDate.now();
            fromGstReportDateDP.setValue(currentDate.withMonth(currentDate.getMonth().getValue()-3));
            toGstReportDateDP.setValue(LocalDate.now());
            groupByCB.valueProperty().addListener(getChangeListenerForGroupBy());
            groupByCB.setItems( FXCollections.observableArrayList(Arrays.asList("GST", "Supplier")));
            groupByCB.setValue("Supplier");
            populateSupplierCB();
            currentSelectedGrid = "Supplier";
            regenerateTreeTableGrid();
        } catch (KiranaStoreException e) {
            e.printStackTrace();
        }
    }

    private void populateSupplierCB() {
        ObservableList<ProductSupplier> supplierList
                = FXCollections.observableArrayList(gstReportDOClientHelper.getSupplierList());
        supplierCB.setItems(supplierList);
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
        productInvEntryGridColumns.add(createStringTableColumn("Product Code", 120, "productCode", "productCode"));
        productInvEntryGridColumns.add(createStringTableColumn("Invoice Ref", 120, "invoiceRef", "invoiceRef"));
        productInvEntryGridColumns.add(createNumericTableColumn("CGST", 100, "totalCGSTAmount", "totalCGSTAmount"));
        productInvEntryGridColumns.add(createNumericTableColumn("SGST", 100, "totalSGSTAmount", "totalCGSTAmount"));
        productInvEntryGridColumns.add(createNumericTableColumn("GST Amount", 100, "totalGSTAmount", "totalGSTAmount"));
        //loadGSTReportDataInToGridGroupByGST();
    }

    private void initializeGSTReportGridTableAsPerSupplierGrouping() throws KiranaStoreException {
        ObservableList<TreeTableColumn<GSTReportTreeTableDO, ?>> productInvEntryGridColumns = gstReportTreeTableView.getColumns();
        productInvEntryGridColumns.add(createStringTableColumn("Supplier", 120, "supplierCode", "supplierCode"));
        productInvEntryGridColumns.add(createStringTableColumn("Invoice Ref", 120, "invoiceRef", "invoiceRef"));
        productInvEntryGridColumns.add(createStringTableColumn("GST Code", 120, "gstCode", "gstCode"));
        productInvEntryGridColumns.add(createStringTableColumn("Product Code", 120, "productCode", "productCode"));
        productInvEntryGridColumns.add(createStringTableColumn("Invoice Amount", 120, "totalInvoiceAmount", "totalInvoiceAmount"));
        productInvEntryGridColumns.add(createStringTableColumn("Taxable Amount", 120, "totalTaxableAmount", "totalTaxableAmount"));
        productInvEntryGridColumns.add(createNumericTableColumn("CGST", 100, "totalCGSTAmount", "totalCGSTAmount"));
        productInvEntryGridColumns.add(createNumericTableColumn("SGST", 100, "totalSGSTAmount", "totalCGSTAmount"));
        productInvEntryGridColumns.add(createNumericTableColumn("GST", 100, "totalGSTAmount", "totalGSTAmount"));
    }

/*
    private void loadGSTReportDataInToGridGroupByGST() throws KiranaStoreException {
        TreeItem<GSTReportTreeTableDO> rootItem = new TreeItem<>();
        GSTReportTreeTableDO gstReportTreeTableDORootValue = new GSTReportTreeTableDO();
        gstReportTreeTableDORootValue.setGstCode("GST");
        rootItem.setValue(gstReportTreeTableDORootValue);
        rootItem.setExpanded(true);
        gstReportTreeTableView.setRoot(rootItem);
        double totalGSTAmount = 0.0d, totalCGSTAmount=  0.0d, totalSGSTAmount = 0.0d;
        for(GSTReportDO gstReportDO : supplierGSTReportDOList){
            TreeItem<GSTReportTreeTableDO> gstTreeItem = new TreeItem<>();

            GSTReportTreeTableDO gstReportTreeTableDO = new GSTReportTreeTableDO();
            gstReportTreeTableDO.setGstCode(gstReportDO.getGstCode());
            gstReportTreeTableDO.setTotalCGSTAmount(gstReportDO.getTotalCGSTAmount());
            gstReportTreeTableDO.setTotalSGSTAmount(gstReportDO.getTotalSGSTAmount());
            gstReportTreeTableDO.setTotalGSTAmount(gstReportDO.getTotalGSTAmount());
            gstTreeItem.setValue(gstReportTreeTableDO);
            addSupplierToGSTGroupTreeNode(gstReportDO, gstTreeItem);
            rootItem.getChildren().add(gstTreeItem);
            totalCGSTAmount += gstReportDO.getTotalCGSTAmount();
            totalSGSTAmount += gstReportDO.getTotalSGSTAmount();
            totalGSTAmount += gstReportDO.getTotalGSTAmount();

        }
        gstReportTreeTableDORootValue.setTotalCGSTAmount(totalCGSTAmount);
        gstReportTreeTableDORootValue.setTotalSGSTAmount(totalSGSTAmount);
        gstReportTreeTableDORootValue.setTotalGSTAmount(totalGSTAmount);

    }

    private void addSupplierToGSTGroupTreeNode(GSTReportDO gstReportDO, TreeItem<GSTReportTreeTableDO> gstTreeItem) {
        for(SupplierGSTReportDO supplierGSTReportDO : gstReportDO.getProductGSTReportDOList()){
            GSTReportTreeTableDO gstReportTreeTableDOSupplier = new GSTReportTreeTableDO();
            gstReportTreeTableDOSupplier.setSupplierCode(supplierGSTReportDO.getSupplierCode());
            gstReportTreeTableDOSupplier.setTotalCGSTAmount(supplierGSTReportDO.getTotalCGSTAmount());
            gstReportTreeTableDOSupplier.setTotalSGSTAmount(supplierGSTReportDO.getTotalSGSTAmount());
            gstReportTreeTableDOSupplier.setTotalGSTAmount(supplierGSTReportDO.getTotalGSTAmount());
            TreeItem<GSTReportTreeTableDO> supplierTreeItem = new TreeItem<>(gstReportTreeTableDOSupplier);
            gstTreeItem.getChildren().add(supplierTreeItem);
            addProductsToTreeItem(supplierGSTReportDO, supplierTreeItem);
        }
    }

    private void addProductsToTreeItem(SupplierGSTReportDO supplierGSTReportDO, TreeItem<GSTReportTreeTableDO> gstTreeItem) {
        for(ProductGSTReportDO productGSTReportDO : supplierGSTReportDO.getProductGSTReportDOList()){
            GSTReportTreeTableDO gstReportTreeTableDO = new GSTReportTreeTableDO();
            gstReportTreeTableDO.setProductCode(productGSTReportDO.getProductCode());
            gstReportTreeTableDO.setTotalCGSTAmount(productGSTReportDO.getTotalCGSTAmount());
            gstReportTreeTableDO.setTotalSGSTAmount(productGSTReportDO.getTotalSGSTAmount());
            gstReportTreeTableDO.setTotalGSTAmount(productGSTReportDO.getTotalGSTAmount());
            gstReportTreeTableDO.setInvoiceRef(productGSTReportDO.getInvoiceRef());
            gstTreeItem.getChildren().add(new TreeItem<>(gstReportTreeTableDO));
        }
    }
*/
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
        Collection<TreeItem<GSTReportTreeTableDO>> gstReportTreeTableDOList = new ArrayList<>();
        double totalCGSTAmount = 0.0d;
        double totalSGSTAmount = 0.0d;
        double totalGSTAmount = 0.0d;
        double totalTaxableAmount = 0.0d;
        for(SupplierGSTReportDO supplierGSTReportDO : supplierGSTReportDOList){
            GSTReportTreeTableDO supplierGSTReportTreeTableDO = new GSTReportTreeTableDO();
            supplierGSTReportTreeTableDO.setSupplierCode(supplierGSTReportDO.getSupplierCode());
            supplierGSTReportTreeTableDO.setTotalCGSTAmount(supplierGSTReportDO.getTotalCGSTAmount());
            supplierGSTReportTreeTableDO.setTotalSGSTAmount(supplierGSTReportDO.getTotalSGSTAmount());
            supplierGSTReportTreeTableDO.setTotalGSTAmount(supplierGSTReportDO.getTotalGSTAmount());
            supplierGSTReportTreeTableDO.setTotalInvoiceAmount(supplierGSTReportDO.getTotalInvoiceAmt());
            supplierGSTReportTreeTableDO.setTotalTaxableAmount(supplierGSTReportDO.getTotalTaxableAmount());
            TreeItem<GSTReportTreeTableDO> supplierTreeItem = new TreeItem<>(supplierGSTReportTreeTableDO);
            gstReportTreeTableDOList.add(supplierTreeItem);
            totalCGSTAmount += supplierGSTReportDO.getTotalCGSTAmount();
            totalSGSTAmount += supplierGSTReportDO.getTotalSGSTAmount();
            totalGSTAmount += supplierGSTReportDO.getTotalGSTAmount();
            totalTaxableAmount += supplierGSTReportDO.getTotalTaxableAmount();
            for(InvoiceGSTReportDO invoiceGSTReportDO : supplierGSTReportDO.getInvoiceGSTReportDOList()){
                GSTReportTreeTableDO invoiceGSTReportTreeTableDO = new GSTReportTreeTableDO();
                invoiceGSTReportTreeTableDO.setInvoiceRef(invoiceGSTReportDO.getInvoiceRef());
                invoiceGSTReportTreeTableDO.setInvoiceDate(invoiceGSTReportDO.getInvoiceDate());
                invoiceGSTReportTreeTableDO.setTotalCGSTAmount(invoiceGSTReportDO.getTotalCGSTAmount());
                invoiceGSTReportTreeTableDO.setTotalSGSTAmount(invoiceGSTReportDO.getTotalSGSTAmount());
                invoiceGSTReportTreeTableDO.setTotalGSTAmount(invoiceGSTReportDO.getTotalGSTAmount());
                invoiceGSTReportTreeTableDO.setTotalInvoiceAmount(invoiceGSTReportDO.getInvoiceAmt());
                invoiceGSTReportTreeTableDO.setTotalTaxableAmount(invoiceGSTReportDO.getTotalTaxableAmount());
                TreeItem<GSTReportTreeTableDO> invoiceTreeItem = new TreeItem<>(invoiceGSTReportTreeTableDO);
                supplierTreeItem.getChildren().add(invoiceTreeItem);
                for(GSTReportDO gstReportDO : invoiceGSTReportDO.getGstReportDOList()){
                    GSTReportTreeTableDO gstGSTReportTreeTableDO = new GSTReportTreeTableDO();
                    gstGSTReportTreeTableDO.setGstCode(gstReportDO.getGstCode());
                    gstGSTReportTreeTableDO.setTotalCGSTAmount(gstReportDO.getTotalCGSTAmount());
                    gstGSTReportTreeTableDO.setTotalSGSTAmount(gstReportDO.getTotalSGSTAmount());
                    gstGSTReportTreeTableDO.setTotalGSTAmount(gstReportDO.getTotalGSTAmount());
                    gstGSTReportTreeTableDO.setTotalInvoiceAmount(gstReportDO.getTotalInvoiceAmt());
                    gstGSTReportTreeTableDO.setTotalTaxableAmount(gstReportDO.getTotalTaxableAmt());
                    TreeItem<GSTReportTreeTableDO> gstTreeItem = new TreeItem<>(gstGSTReportTreeTableDO);
                    invoiceTreeItem.getChildren().add(gstTreeItem);
                }
            }
        }
        gstReportTreeTableDORootValue.setTotalCGSTAmount(totalCGSTAmount);
        gstReportTreeTableDORootValue.setTotalSGSTAmount(totalSGSTAmount);
        gstReportTreeTableDORootValue.setTotalGSTAmount(totalGSTAmount);
        gstReportTreeTableDORootValue.setTotalTaxableAmount(totalTaxableAmount);
        return gstReportTreeTableDOList;
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
        gridColumn.setId(columnId);
        return gridColumn;
    }

    @FXML
    public void applyFilter(ActionEvent actionEvent) throws KiranaStoreException {
        supplierGSTReportDOList = gstReportDOClientHelper.getGSTReportData(this);
        loadGSTReportDataInToGridGroupBySupplier();
    }

    public ComboBox<ProductSupplier> getSupplierCB() {
        return supplierCB;
    }
}
