package com.pc.retail.dao;

import com.pc.retail.vo.ModificationStatus;
import com.pc.retail.vo.ProductInventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavanc on 3/31/19.
 */
public class ReportClientImpl {
    private RetailDataSource retailDataSource;

    public ReportClientImpl(RetailDataSource retailDataSource) {
        this.retailDataSource = retailDataSource;
    }

    public List<ProductInventory> getInventoryAndGSTData(List<SQLParameter> sqlParameterList) throws DataAccessException {
        String selectQuery = "SELECT product_id, barcode, status, total_cost, total_cost_gst, " +
                "cgst_total_amount, sgst_total_amount, gst_group_code, im.invoice_date, im.supplier_id,im.invoice_ref FROM product_inv_record ir, product_invoice_master im " +
                " where ir.invoice_id=im.invoice_id ";
        return getProductInventoryListForQuery(selectQuery, sqlParameterList);
    }

    private List<ProductInventory> getProductInventoryListForQuery(String selectQuery, List<SQLParameter> sqlParameterList) throws DataAccessException {
        ResultSet resultSet = null;
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(selectQuery + appendWhereClause(sqlParameterList))) {
            QueryUtil.setParameter(sqlParameterList, statement);
            resultSet = statement.executeQuery();
            List<ProductInventory> productInventories = new ArrayList<>();
            while(resultSet.next()){
                ProductInventory productInventory = new ProductInventory();
                productInventory.setModificationStatus(ModificationStatus.NO_CHANGE);
                productInventory.setProductId(resultSet.getInt("product_id"));
                productInventory.setBarCode(resultSet.getString("barcode"));
                productInventory.setStatus(resultSet.getInt("status"));
                productInventory.setTotalCostIncludingGST(resultSet.getDouble("total_cost_gst"));
                productInventory.setTotalCGSTAmount(resultSet.getDouble("cgst_total_amount"));
                productInventory.setTotalSGSTAmount(resultSet.getDouble("sgst_total_amount"));
                productInventory.setInvoiceDate(resultSet.getDate("invoice_date"));
                productInventory.setSupplierId(resultSet.getInt("supplier_id"));
                productInventory.setTotalCost(resultSet.getDouble("total_cost"));
                productInventory.setTotalGSTAmountForInv(productInventory.getTotalCGSTAmount() + productInventory.getTotalSGSTAmount());
                productInventory.setInvoiceRef(resultSet.getString("invoice_ref"));
                productInventory.setGstGroupCode(resultSet.getString("gst_group_code"));
                productInventories.add(productInventory);
            }
            return productInventories;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        }finally {
            close(resultSet);
        }
    }

    private void close(ResultSet resultSet) throws DataAccessException {
        try {
            if(resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private Connection getConnection() throws SQLException {
        return retailDataSource.getConnection();
    }

    private String appendWhereClause(List<SQLParameter> sqlParameterList) {
        if(!sqlParameterList.isEmpty()) {
            return " WHERE " + QueryUtil.createWhereClause(sqlParameterList);
        }
        return "";
    }


}
