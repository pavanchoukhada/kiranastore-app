package com.pc.retail.dao;

import com.pc.retail.api.GSTReportDO;
import com.pc.retail.vo.ProductInvoiceDetail;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavanc on 3/24/19.
 */
public class GSTReportStorageClientImpl implements GSTReportStorageClient {

    private Connection connection;
    private RetailDataSource dataSource;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");


    public GSTReportStorageClientImpl(Connection connection){
        this.connection = connection;
    }


    @Override
    public List<GSTReportDO> getProductInvoiceDetails(int invoiceId) throws DataAccessException {
        String selectQuery = "select barcode, prd_inv_amt from product_invoice_detail where invoice_id = ?";
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery)) {

            List<GSTReportDO> productInvoiceDetailList = new ArrayList<>();
            while(resultSet.next()){

            }
            return productInvoiceDetailList;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
