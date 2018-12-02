package com.pc.retail.dao;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.pc.retail.api.FilterKeyConstants;
import com.pc.retail.dao.util.FilterModel;
import com.pc.retail.interactor.MeasurementType;
import com.pc.retail.util.DataUtil;
import com.pc.retail.vo.*;

public class StorageClientImpl implements StorageClient {

    private Connection connectionForMultiTrans;
    private RetailDataSource dataSource;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private boolean multiTrans = false;
    StorageClientImpl(Connection connectionForMultiTrans){
        this.connectionForMultiTrans = connectionForMultiTrans;
        multiTrans = true;
    }

    public StorageClientImpl(RetailDataSource retailDataSource) {
        dataSource = retailDataSource;
    }

    public int getNextVal(String key) throws DataAccessException{
        try(Statement stmt = connectionForMultiTrans.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT nextval('" + key +"')")){
            if(resultSet.next()){
                int id = resultSet.getInt(1);
                return id;
            }
        }catch (SQLException sqlEx){
            throw new DataAccessException(sqlEx.getMessage());
        }
        return -1;
    }

	public int saveProduct(Product product) throws DataAccessException{

        if(product.getProductId() <= 0){
            int productId = insertProduct(product);
            product.setProductId(productId);
        }else{
            updateProduct(product);
        }

        return product.getProductId();
		//save product code
	}

    private void updateProduct(Product product) throws DataAccessException{
        String insertSql = "update product_master set barcode = ?, prd_code = ?, prd_desc = ?, prd_search_key = ? , prd_category = ?, " +
                " price_uom_cd = ?, qty_uom_cd = ?, measure_ind = ?, company_code = ?, base_product = ?, weight = ? , is_base_product = ?, " +
                " csp = ?, gst_group = ? where product_id = ? ";
        try(PreparedStatement preparedStatement = connectionForMultiTrans.prepareStatement( insertSql )) {
            int index = 1;
            preparedStatement.setString(index++, product.getBarcode());
            preparedStatement.setString(index++, product.getPrdCode());
            preparedStatement.setString(index++, product.getPrdDesc());
            preparedStatement.setString(index++, product.getSearchKey());
            preparedStatement.setString(index++, product.getCategory());
            preparedStatement.setString(index++, product.getPriceUomCd());
            preparedStatement.setString(index++, product.getQtyUomCd());
            preparedStatement.setInt(index++, product.getMeasurementType().getValue());
            preparedStatement.setString(index++, product.getCompanyCode());
            preparedStatement.setString(index++, product.getBaseProductBarCode());
            preparedStatement.setDouble(index++, product.getWeight());
            preparedStatement.setBoolean(index++, product.isBaseProductFlag());
            preparedStatement.setDouble(index++, product.getCurrentSellingPrice());
            preparedStatement.setString(index++, product.getGstTaxGroup());
            preparedStatement.setInt(index++, product.getProductId());
            preparedStatement.execute();
        }catch(SQLException sqlEx){
            throw new DataAccessException(sqlEx.getMessage());
        }

    }

    private int insertProduct(Product product) throws DataAccessException {
        int productId = getNextVal("product_id_seq");
        String insertSql = "insert into product_master(product_id, barcode, prd_code, prd_desc, prd_search_key, prd_category, " +
                            " price_uom_cd, qty_uom_cd, measure_ind, company_code, base_product, weight, is_base_product, csp, gst_group) " +
                            "	values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement preparedStatement = connectionForMultiTrans.prepareStatement( insertSql )) {
            int index = 1;
            preparedStatement.setInt(index++, productId);
            preparedStatement.setString(index++, product.getBarcode());
            preparedStatement.setString(index++, product.getPrdCode());
            preparedStatement.setString(index++, product.getPrdDesc());
            preparedStatement.setString(index++, product.getSearchKey());
            preparedStatement.setString(index++, product.getCategory());
            preparedStatement.setString(index++, product.getPriceUomCd());
            preparedStatement.setString(index++, product.getQtyUomCd());
            preparedStatement.setInt(index++, product.getMeasurementType().getValue());
            preparedStatement.setString(index++, product.getCompanyCode());
            preparedStatement.setString(index++, product.getBaseProductBarCode());
            preparedStatement.setDouble(index++, product.getWeight());
            preparedStatement.setBoolean(index++, product.isBaseProductFlag());
            preparedStatement.setDouble(index++, product.getCurrentSellingPrice());
            preparedStatement.setString(index++, product.getGstTaxGroup());
            preparedStatement.execute();
        }catch(SQLException sqlEx){
            logError(sqlEx);
            throw new DataAccessException(sqlEx.getMessage());
        }
        return productId;
    }

    @Override
    public void addAuditInventoryTransaction(InventoryTransactionModel inventoryTransactionModel) throws SQLException {
        String insertSql = "insert into product_inv_transaction(prd_inv_trans_id, prd_id, barcode, external_ref, trans_type, trans_date, trans_desc, trans_price) " +
                "	values (nextval('prd_inv_trans_id_seq'), ?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement preparedStatement = connectionForMultiTrans.prepareStatement( insertSql )) {
            int index = 1;
            preparedStatement.setInt(index++, inventoryTransactionModel.getProductId());
            preparedStatement.setString(index++, inventoryTransactionModel.getBarCode());
            preparedStatement.setInt(index++, inventoryTransactionModel.getExternalRef());
            preparedStatement.setInt(index++, inventoryTransactionModel.getTransactionType().getId());
            preparedStatement.setDate(index++, new Date(inventoryTransactionModel.getTransDate().getTime()));
            preparedStatement.setString(index++, inventoryTransactionModel.getTransDesc());
            preparedStatement.setDouble(index++, inventoryTransactionModel.getTransPrice());
            preparedStatement.execute();
        }
    }

    @Override
    public void commit() throws DataAccessException{
        try {
            connectionForMultiTrans.commit();
        } catch (SQLException sqlEx) {
            throw new DataAccessException(sqlEx.getMessage());
        }
    }

    @Override
    public void rollBack() throws DataAccessException{
        try {
            connectionForMultiTrans.rollback();
        } catch (SQLException sqlEx) {
            throw new DataAccessException(sqlEx.getMessage());
        }
    }

    @Override
    public List<ProductCurrentInvDetail> getCurrentInventoryForProduct(int productId) throws DataAccessException {
        String selectQuery = "select product_id, barcode, current_quantity, csp, mrp, per_unit_cost, qty_uom_cd ,price_uom_cd, current_w_qty, expiry_date, " +
                " last_trans_date from product_curr_inv_detail ";
        if(productId > 0){
            selectQuery = selectQuery + " where product_id = " + productId;
        }
        List<ProductCurrentInvDetail> productCurrentInvDetailList = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectQuery)) {

            while(resultSet.next()){
                ProductCurrentInvDetail productCurrentInvDetail = new ProductCurrentInvDetail();
                productCurrentInvDetail.setProductId(resultSet.getInt("product_id"));
                productCurrentInvDetail.setBarCode(resultSet.getString("barcode"));
                productCurrentInvDetail.setPriceUomCd(resultSet.getString("price_uom_cd"));
                productCurrentInvDetail.setQtyUomCd(resultSet.getString("qty_uom_cd"));
                productCurrentInvDetail.setQuantity(resultSet.getDouble("current_quantity"));
                productCurrentInvDetail.setCSP(resultSet.getDouble("csp"));
                productCurrentInvDetail.setMRP(resultSet.getDouble("mrp"));
                productCurrentInvDetail.setCostPrice(resultSet.getDouble("per_unit_cost"));
                productCurrentInvDetail.setLastModifyDt(resultSet.getString("last_trans_date"));
                productCurrentInvDetail.setExpiryDate(resultSet.getDate("expiry_date"));
                productCurrentInvDetail.setModificationStatus(ModificationStatus.NO_CHANGE);
                productCurrentInvDetailList.add(productCurrentInvDetail);
            }
        } catch (SQLException e) {
            logError(e);
            throw new DataAccessException(e.getMessage());
        }
        return productCurrentInvDetailList;
    }

    @Override
    public void saveCurrentInventoryForProduct(ProductCurrentInvDetail productCurrentInvDetail) throws DataAccessException {
        try {
            if (productCurrentInvDetail.getModificationStatus() == ModificationStatus.NEW) {
                insertProductCurrentInventoryDetail(productCurrentInvDetail);
            } else {
                updateProductCurrentInventoryDetail(productCurrentInvDetail);
            }
        }catch (SQLException sqlEx){
            logError(sqlEx);
            throw new DataAccessException(sqlEx.getMessage());
        }
    }

    private void insertProductCurrentInventoryDetail(ProductCurrentInvDetail productCurrentInvDetail) throws SQLException{
        String insertSql = "insert into product_curr_inv_detail(product_id, barcode, current_quantity, csp, mrp, per_unit_cost, qty_uom_cd ,price_uom_cd, " +
                " expiry_date, last_trans_date) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement preparedStatement = connectionForMultiTrans.prepareStatement( insertSql )) {
            int index = 1;
            preparedStatement.setInt(index++, productCurrentInvDetail.getProductId());
            preparedStatement.setString(index++, productCurrentInvDetail.getBarCode());
            preparedStatement.setDouble(index++, productCurrentInvDetail.getQuantity());
            preparedStatement.setDouble(index++, productCurrentInvDetail.getCSP());
            preparedStatement.setDouble(index++, productCurrentInvDetail.getMRP());
            preparedStatement.setDouble(index++, productCurrentInvDetail.getCostPrice());
            preparedStatement.setString(index++, productCurrentInvDetail.getQtyUomCd());
            preparedStatement.setString(index++, productCurrentInvDetail.getPriceUomCd());
            preparedStatement.setDate(index++, new Date(productCurrentInvDetail.getExpiryDate().getTime()));
            preparedStatement.setDate(index++, new Date(System.currentTimeMillis()));
            preparedStatement.execute();
        }
    }

    private void updateProductCurrentInventoryDetail(ProductCurrentInvDetail productCurrentInvDetail) throws SQLException{
        String insertSql = "update product_curr_inv_detail set barcode = ?, current_quantity= ?, csp = ?, mrp = ?, per_unit_cost = ?, qty_uom_cd =?, price_uom_cd = ?, " +
                " last_trans_date = ? where product_id = ? ";
        try(PreparedStatement preparedStatement = connectionForMultiTrans.prepareStatement( insertSql )) {
            int index = 1;
            preparedStatement.setString(index++, productCurrentInvDetail.getBarCode());
            preparedStatement.setDouble(index++, productCurrentInvDetail.getQuantity());
            preparedStatement.setDouble(index++, productCurrentInvDetail.getCSP());
            preparedStatement.setDouble(index++, productCurrentInvDetail.getMRP());
            preparedStatement.setDouble(index++, productCurrentInvDetail.getCostPrice());
            preparedStatement.setString(index++, productCurrentInvDetail.getQtyUomCd());
            preparedStatement.setString(index++, productCurrentInvDetail.getPriceUomCd());
            preparedStatement.setDate(index++, new Date(System.currentTimeMillis()));
            preparedStatement.setInt(index++, productCurrentInvDetail.getProductId());
            preparedStatement.execute();
        }
    }

    @Override
    public ProductInvoiceMaster getInvoiceMasterWithDetail(String invoiceRef) throws DataAccessException {
        String selectQuery = "select * from product_invoice_master where invoice_ref=" + getQuotedString(invoiceRef);
        List<ProductInvoiceMaster> productInvoiceMasterList = getProductInvoiceMasters(selectQuery, new ArrayList<>());
        if(productInvoiceMasterList.size() > 0){
            return productInvoiceMasterList.get(0);
        }else{
            return null;
        }

    }

    @Override
    public void saveInvoiceMaster(ProductInvoiceMaster productInvoiceMaster) throws DataAccessException {
        if(productInvoiceMaster.getInvoiceId() <= 0){
            int invoiceId = insertProductInvoiceMaster(productInvoiceMaster);
            productInvoiceMaster.setInvoiceId(invoiceId);
            //insertProductInvoiceDetail(invoiceId, productInvoiceMaster.getProductInventoryList());
        }else{
            updateProductInvoiceMaster(productInvoiceMaster);
            //updateProductInvoiceDetail(productInvoiceMaster.getInvoiceId(), productInvoiceMaster.getProductInventoryList());
        }
    }

    private void updateProductInvoiceMaster(ProductInvoiceMaster productInvoiceMaster) throws DataAccessException{

        String insertSql = " update product_invoice_master set invoice_ref = ?, supplier_id = ?, invoice_date= ?, invoice_status = ?, " +
                "lumpsum_cost = ?, invoice_amount = ?, total_amount = ?, paid_amount = ?, last_modify_dt = ? " +
                " where invoice_id = ? ";
        try (PreparedStatement preparedStatement = connectionForMultiTrans.prepareStatement(insertSql)){
            int index = 1;
            preparedStatement.setString(index++, productInvoiceMaster.getInvoiceRefId());
            preparedStatement.setInt(index++, productInvoiceMaster.getSupplierId());
            preparedStatement.setDate(index++, new Date(productInvoiceMaster.getInvoiceDate().getTime()));
            preparedStatement.setInt(index++, productInvoiceMaster.getInvoiceStatus().getInd());
            preparedStatement.setDouble(index++, productInvoiceMaster.getLumpsumCost());
            preparedStatement.setDouble(index++, productInvoiceMaster.getPrdInvAmt());
            preparedStatement.setDouble(index++, productInvoiceMaster.getTotalInvAmt());
            preparedStatement.setDouble(index++, productInvoiceMaster.getPaidAmount());
            preparedStatement.setDate(index++, new Date(System.currentTimeMillis()));
            preparedStatement.setInt(index++, productInvoiceMaster.getInvoiceId());
            preparedStatement.execute();
        }
        catch (SQLException sqlEx){
            logError(sqlEx);
            throw new DataAccessException(sqlEx.getMessage());
        }
    }

    private void updateProductInvoiceDetail(int invoiceId, List<ProductInvoiceDetail> productInvoiceDetailList) throws DataAccessException{
        updateProductInvoiceDetail(productInvoiceDetailList);
        insertProductInvoiceDetail(invoiceId, productInvoiceDetailList);
        deleteProductInvoiceDetail(productInvoiceDetailList);
    }

    private void deleteProductInvoiceDetail(List<ProductInvoiceDetail> productInvoiceDetailList) throws DataAccessException{
        String deleteSql = "delete from product_invoice_detail where  prd_inv_id = ? ";
        try (PreparedStatement preparedStatement = connectionForMultiTrans.prepareStatement(deleteSql)){
            for(ProductInvoiceDetail productInvoiceDetail : productInvoiceDetailList) {
                if(productInvoiceDetail.getModificationStatus() == ModificationStatus.DELETED) {
                    preparedStatement.setInt(1, productInvoiceDetail.getPrdInventoryEntryId());
                    preparedStatement.addBatch();
                }
            }
            preparedStatement.executeBatch();
        }
        catch (SQLException sqlEx){
            logError(sqlEx);
            throw new DataAccessException(sqlEx.getMessage());
        }
    }

    private void updateProductInvoiceDetail(List<ProductInvoiceDetail> prdInvDtlUpdateList) throws DataAccessException {
        String insertSql = "update product_invoice_detail set product_id = ?,barcode = ?, lump_sum_cost = ?, per_unit_price = ?, per_unit_cost = ?," +
                " prd_inv_amt = ?, qty = ? where  prd_inv_id = ?";
        try (PreparedStatement preparedStatement = connectionForMultiTrans.prepareStatement(insertSql)){
            for(ProductInvoiceDetail productInvoiceDetail : prdInvDtlUpdateList) {
                if(productInvoiceDetail.getModificationStatus() == ModificationStatus.MODIFIED) {
                    int index = 1;
                    preparedStatement.setInt(index++, productInvoiceDetail.getProductId());
                    preparedStatement.setString(index++, productInvoiceDetail.getBarCode());
                    preparedStatement.setDouble(index++, productInvoiceDetail.getLumpsumCost());
                    preparedStatement.setDouble(index++, productInvoiceDetail.getPerUnitPrice());
                    preparedStatement.setDouble(index++, productInvoiceDetail.getPerUnitCost());
                    preparedStatement.setDouble(index++, productInvoiceDetail.getPrdInvAmt());
                    preparedStatement.setDouble(index++, productInvoiceDetail.getQty());
                    preparedStatement.setInt(index++, productInvoiceDetail.getPrdInventoryEntryId());
                    preparedStatement.addBatch();
                }
            }
            preparedStatement.executeBatch();
        }
        catch (SQLException sqlEx){
            logError(sqlEx);
            throw new DataAccessException(sqlEx.getMessage());
        }
    }

    @Override
    public List<ProductInvoiceDetail> getProductInvoiceDetails(int invoiceId) throws DataAccessException {
        String selectQuery = "select product_id, prd_inv_id, barcode, lump_sum_cost, per_unit_price, per_unit_cost," +
                " prd_inv_amt, qty from product_invoice_detail where invoice_id = ?";
        try(Statement statement = connectionForMultiTrans.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery)) {

            List<ProductInvoiceDetail> productInvoiceDetailList = new ArrayList<>();
            while(resultSet.next()){
                ProductInvoiceDetail productInvoiceDetail = new ProductInvoiceDetail();
                productInvoiceDetail.setInvoiceId(invoiceId);
                productInvoiceDetail.setProductId(resultSet.getInt("product_id"));
                productInvoiceDetail.setPrdInventoryEntryId(resultSet.getInt("prd_inv_id"));
                productInvoiceDetail.setBarCode(resultSet.getString("barcode"));
                productInvoiceDetail.setLumpsumCost(resultSet.getDouble("lump_sum_cost"));
                productInvoiceDetail.setPerUnitPrice(resultSet.getDouble("per_unit_price"));
                productInvoiceDetail.setPerUnitCost(resultSet.getDouble("per_unit_cost"));
                productInvoiceDetail.setPrdInvAmt(resultSet.getDouble("prd_inv_amt"));
                productInvoiceDetail.setQty(resultSet.getDouble("qty"));
                productInvoiceDetailList.add(productInvoiceDetail);
            }
            return productInvoiceDetailList;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public int generateBarCode() throws DataAccessException {
        return getNextVal("product_barcode_seq");
    }


    private int insertProductInvoiceMaster(ProductInvoiceMaster productInvoiceMaster) throws DataAccessException{
        int invoiceId = getNextVal("product_invoice_id_seq");
        String insertSql = "insert into product_invoice_master(invoice_id, invoice_ref, supplier_id, invoice_date, invoice_status, " +
                "lumpsum_cost, invoice_amount, total_amount, paid_amount, last_modify_dt) " +
                "	values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connectionForMultiTrans.prepareStatement(insertSql)){
            int index = 1;
            preparedStatement.setInt(index++, invoiceId);
            preparedStatement.setString(index++, productInvoiceMaster.getInvoiceRefId());
            preparedStatement.setInt(index++, productInvoiceMaster.getSupplierId());
            preparedStatement.setDate(index++, new Date(productInvoiceMaster.getInvoiceDate().getTime()));
            preparedStatement.setInt(index++, productInvoiceMaster.getInvoiceStatus().getInd());
            preparedStatement.setDouble(index++, productInvoiceMaster.getLumpsumCost());
            preparedStatement.setDouble(index++, productInvoiceMaster.getPrdInvAmt());
            preparedStatement.setDouble(index++, productInvoiceMaster.getTotalInvAmt());
            preparedStatement.setDouble(index++, productInvoiceMaster.getPaidAmount());
            preparedStatement.setDate(index, new Date(System.currentTimeMillis()));
            preparedStatement.execute();
        }
        catch (SQLException sqlEx){
            logError(sqlEx);
            throw new DataAccessException(sqlEx.getMessage());
        }
        return invoiceId;
    }

    private void insertProductInvoiceDetail(int invoiceId, List<ProductInvoiceDetail> productInvoiceDetailList) throws DataAccessException {
        String insertSql = "insert into product_invoice_detail(invoice_id, product_id, prd_inv_id, barcode, lump_sum_cost, per_unit_price, per_unit_cost," +
                " prd_inv_amt, qty) " +
                "	values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connectionForMultiTrans.prepareStatement(insertSql)){
            for(ProductInvoiceDetail productInvoiceDetail : productInvoiceDetailList) {
                if(productInvoiceDetail.getModificationStatus() == null || productInvoiceDetail.getModificationStatus() == ModificationStatus.NEW) {
                    int index = 1;
                    preparedStatement.setInt(index++, invoiceId);
                    preparedStatement.setInt(index++, productInvoiceDetail.getProductId());
                    preparedStatement.setInt(index++, productInvoiceDetail.getPrdInventoryEntryId());
                    preparedStatement.setString(index++, productInvoiceDetail.getBarCode());
                    preparedStatement.setDouble(index++, productInvoiceDetail.getLumpsumCost());
                    preparedStatement.setDouble(index++, productInvoiceDetail.getPerUnitPrice());
                    preparedStatement.setDouble(index++, productInvoiceDetail.getPerUnitCost());
                    preparedStatement.setDouble(index++, productInvoiceDetail.getPrdInvAmt());
                    preparedStatement.setDouble(index++, productInvoiceDetail.getQty());
                    preparedStatement.addBatch();
                }
            }
            preparedStatement.executeBatch();
        }
        catch (SQLException sqlEx){
            logError(sqlEx);
            throw new DataAccessException(sqlEx.getMessage());
        }
    }

    @Override
    public List<ProductInvoiceMaster> getProductInvoiceMasterList(FilterModel filterModel) throws DataAccessException {
        StringBuilder selectClause= new StringBuilder("SELECT * FROM product_invoice_master ");
        List<Date> dateList = new ArrayList<>();
        StringBuilder whereClause = new StringBuilder();
        String fromInvoiceDate = (String) filterModel.getFilterValue(FilterKeyConstants.FROM_INVOICE_DATE);
        if (fromInvoiceDate != null && !"".equals(fromInvoiceDate)) {
            whereClause.append("invoice_date >= ?");
            dateList.add(getDateValue(fromInvoiceDate));
        }

        String toInvoiceDate = filterModel.getFilterValue(FilterKeyConstants.TO_INVOICE_DATE);
        if (toInvoiceDate != null && !"".equals(toInvoiceDate)) {
            if(whereClause.length() > 0){
                whereClause.append(" and ");
            }
            whereClause.append("invoice_date <= ?");
            dateList.add(getDateValue(toInvoiceDate));
        }

        String invoiceStatus = filterModel.getFilterValue(FilterKeyConstants.INVOICE_STATUS);
        if (invoiceStatus != null && !"".equals(invoiceStatus)) {
            int ind = InvoiceStatus.getInvoiceStatus(invoiceStatus).getInd();
            if(whereClause.length() > 0){
                whereClause.append(" and ");
            }
            whereClause.append("invoice_status = ").append(ind);
        }

        String supplierIdVal =  filterModel.getFilterValue(FilterKeyConstants.SUPPLIER_ID);
        if (supplierIdVal != null && !"".equals(supplierIdVal)) {
            int supplierId = Integer.parseInt(supplierIdVal);
            if(whereClause.length() > 0){
                whereClause.append(" and ");
            }
            whereClause.append("supplier_id = ").append(supplierId);
        }
        if(whereClause.length() > 0){
            selectClause.append(" WHERE ").append(whereClause);
        }
        return getProductInvoiceMasters(selectClause.toString(), dateList);
    }

    private Date getDateValue(String toInvoiceDate) {
        try {
            return new Date(simpleDateFormat.parse(toInvoiceDate).getTime());
        } catch (ParseException e) {
            return new Date(System.currentTimeMillis());
        }
    }

    private List<ProductInvoiceMaster> getProductInvoiceMasters(String selectQuery, List<Date> dateList) throws DataAccessException {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(selectQuery);
            ) {
            int index=1;
            for(Date date : dateList){
                statement.setDate(index, date);
                index++;
            }
            ResultSet resultSet = statement.executeQuery();
            List<ProductInvoiceMaster> productInvoiceMasterList = new ArrayList<>();
            while(resultSet.next()){
                ProductInvoiceMaster productInvoiceMaster = new ProductInvoiceMaster();
                productInvoiceMaster.setInvoiceId(resultSet.getInt("invoice_id"));
                productInvoiceMaster.setInvoiceRefId(resultSet.getString("invoice_ref"));
                productInvoiceMaster.setSupplierId(resultSet.getInt("supplier_id"));
                productInvoiceMaster.setInvoiceStatus(InvoiceStatus.valueOf(resultSet.getInt("invoice_status")));
                productInvoiceMaster.setInvoiceDate(resultSet.getDate("invoice_date"));
                productInvoiceMaster.setLumpsumCost(resultSet.getDouble("lumpsum_cost"));
                productInvoiceMaster.setPrdInvAmt(resultSet.getDouble("invoice_amount"));
                productInvoiceMaster.setTotalInvAmt(resultSet.getDouble("total_amount"));
                productInvoiceMaster.setPaidAmount(resultSet.getDouble("paid_amount"));
                productInvoiceMasterList.add(productInvoiceMaster);
            }
            return productInvoiceMasterList;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private String getQuotedString(String value) {
        return "'" + value + "'";
    }

    @Override
    public ProductInvoiceMaster getInvoiceMasterWithDetail(int invoiceId) throws DataAccessException {
        String selectQuery = "SELECT * FROM product_invoice_master where invoice_id = " + invoiceId;
        List<ProductInvoiceMaster> productInvoiceMasterList = getProductInvoiceMasters(selectQuery, new ArrayList<>());
        if(productInvoiceMasterList.size() > 0){
            ProductInvoiceMaster productInvoiceMaster = productInvoiceMasterList.get(0);
            productInvoiceMaster.setProductInventoryList(getProductInventories(invoiceId));
            return productInvoiceMaster;
        }else {
            throw new DataAccessException("No Invoice Record found for " + invoiceId);
        }
    }

    private List<ProductInventory> getProductInventories(int invoiceId) throws DataAccessException{
        String selectQuery = "SELECT * FROM product_inv_record where invoice_id = " + invoiceId;
        return getProductInventoryListForQuery(selectQuery, false);
    }

    public List<ProductInventory> getProductInventoriesForProduct(int productId) throws DataAccessException{
        String selectQuery = "SELECT ir.*,im.invoice_date, im.supplier_id,im.invoice_ref FROM product_inv_record ir, product_invoice_master im " +
                " where ir.invoice_id=im.invoice_id and product_id = " + productId;
        return getProductInventoryListForQuery(selectQuery, true);
    }

    private List<ProductInventory> getProductInventoryListForQuery(String selectQuery, boolean loadInvoiceDetail) throws DataAccessException {
        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery)) {
            List<ProductInventory> productInventories = new ArrayList<>();
            while(resultSet.next()){
                ProductInventory productInventory = new ProductInventory();
                productInventory.setModificationStatus(ModificationStatus.NO_CHANGE);
                productInventory.setPrdInvId(resultSet.getInt("prd_inv_id"));
                productInventory.setProductId(resultSet.getInt("product_id"));
                productInventory.setBarCode(resultSet.getString("barcode"));
                productInventory.setInvoiceId(resultSet.getInt("invoice_id"));
                productInventory.setExpiryDate(resultSet.getDate("expiry_date"));
                productInventory.setQuantity(resultSet.getDouble("quantity"));
                productInventory.setMRP(resultSet.getDouble("mrp"));
                productInventory.setStatus(resultSet.getInt("status"));
                productInventory.setPerUnitCost(resultSet.getDouble("per_unit_cost"));
                productInventory.setRemainingQuantity(resultSet.getDouble("remaining_qty"));
                productInventory.setQtyUOM(resultSet.getString("qty_uom_cd"));
                productInventory.setPerUnitCostIncludingGST(resultSet.getDouble("per_unit_cost_gst"));
                productInventory.setOtherCost(resultSet.getDouble("other_cost"));
                productInventory.setTotalCost(resultSet.getDouble("total_cost"));
                productInventory.setTotalCostIncludingGST(resultSet.getDouble("total_cost_gst"));
                productInventory.setPerUnitCostIncludingAll(resultSet.getDouble("per_unit_cost_all"));
                productInventory.setFinalAmount(resultSet.getDouble("final_amount"));
                productInventory.setcGSTRate(resultSet.getDouble("cgst_rate"));
                productInventory.setsGSTRate(resultSet.getDouble("sgst_rate"));
                productInventory.setcGSTAmount(resultSet.getDouble("cgst_amount"));
                productInventory.setsGSTAmount(resultSet.getDouble("sgst_amount"));
                if(loadInvoiceDetail) {
                    productInventory.setInvoiceDate(resultSet.getDate("invoice_date"));
                    productInventory.setSupplierId(resultSet.getInt("supplier_id"));
                    productInventory.setInvoiceRef(resultSet.getString("invoice_ref"));
                }
                productInventories.add(productInventory);
            }
            return productInventories;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public List<Product> getProducts(int productId) throws DataAccessException {
        String selectQuery = "select product_id, barcode, prd_code, prd_desc, prd_search_key, prd_category, " +
                " price_uom_cd, qty_uom_cd, measure_ind, company_code, base_product, weight, is_base_product, csp, gst_group from product_master";
        if(productId > 0){
            selectQuery = selectQuery + " product_id = " + productId;
        }
        try(Statement statement = connectionForMultiTrans.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery)) {

            List<Product> products = new ArrayList<>();
            while(resultSet.next()){
                Product product = new Product();
                product.setProductId(resultSet.getInt("product_id"));
                product.setBarcode(resultSet.getString("barcode"));
                product.setPrdCode(resultSet.getString("prd_code"));
                product.setPrdDesc(resultSet.getString("prd_desc"));
                product.setSearchKey(resultSet.getString("prd_search_key"));
                product.setCategory(resultSet.getString("prd_category"));
                product.setPriceUomCd(resultSet.getString("price_uom_cd"));
                product.setQtyUomCd(resultSet.getString("qty_uom_cd"));
                product.setMeasurementType(MeasurementType.valueOf(resultSet.getInt("measure_ind")));
                product.setCompanyCode(resultSet.getString("company_code"));
                product.setBaseProductBarCode(resultSet.getString("base_product"));
                product.setWeight(resultSet.getDouble("weight"));
                product.setBaseProductFlag(resultSet.getBoolean("is_base_product"));
                product.setCurrentSellingPrice(resultSet.getDouble("csp"));
                product.setGstTaxGroup(resultSet.getString("gst_group"));
                products.add(product);
            }
            return products;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }



    @Override
    public void releaseConnection() {
        try {
            connectionForMultiTrans.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public int saveProductInv(ProductInventory productInventoryDetail) throws DataAccessException{

        if(productInventoryDetail.getPrdInvId() <= 0) {
            int prdInvId = insertProductInv(productInventoryDetail);
            productInventoryDetail.setPrdInvId(prdInvId);
        }else if ( productInventoryDetail.getModificationStatus() == ModificationStatus.DELETED){
            deleteProductInv(productInventoryDetail);
        }else{
            updateProductInv(productInventoryDetail);
        }
        return productInventoryDetail.getPrdInvId();
    }

    private void deleteProductInv(ProductInventory productInventoryDetail) throws DataAccessException{
        String updateSql = "delete from product_inv_record  where prd_inv_id = ?";
        try (PreparedStatement preparedStatement = connectionForMultiTrans.prepareStatement(updateSql)){
            preparedStatement.setInt(1, productInventoryDetail.getPrdInvId());
            preparedStatement.execute();
        }
        catch (SQLException sqlEx){
            throw new DataAccessException(sqlEx.getMessage());
        }
    }


    private int insertProductInv(ProductInventory productInventoryDetail) throws DataAccessException {
        int prdInvId = getNextVal("prd_inv_id_seq");
        String insertSql = "insert into product_inv_record(prd_inv_id, product_id, barcode, invoice_id, expiry_date, quantity, mrp, status, " +
                " per_unit_cost, inv_date, remaining_qty, qty_uom_cd, per_unit_cost_gst , other_cost , total_cost ," +
                " total_cost_gst, per_unit_cost_all, final_amount, cgst_rate, sgst_rate, cgst_amount, sgst_amount) " +
                "	values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connectionForMultiTrans.prepareStatement(insertSql)){
            int index = 1;
            preparedStatement.setInt(index++, prdInvId);
            preparedStatement.setInt(index++, productInventoryDetail.getProductId());
            preparedStatement.setString(index++, productInventoryDetail.getBarCode());
            preparedStatement.setInt(index++, productInventoryDetail.getInvoiceId());
            preparedStatement.setDate(index++, new Date(productInventoryDetail.getExpiryDate().getTime()));
            preparedStatement.setDouble(index++, productInventoryDetail.getQuantity());
            preparedStatement.setDouble(index++, productInventoryDetail.getMRP());
            preparedStatement.setInt(index++, productInventoryDetail.getStatus());
            preparedStatement.setDouble(index++, productInventoryDetail.getPerUnitCost());
            preparedStatement.setDate(index++, new Date(System.currentTimeMillis()));
            preparedStatement.setDouble(index++, productInventoryDetail.getRemainingQuantity());
            preparedStatement.setString(index++, productInventoryDetail.getQtyUOM());
            preparedStatement.setDouble(index++, productInventoryDetail.getPerUnitCostIncludingGST());
            preparedStatement.setDouble(index++, productInventoryDetail.getOtherCost());
            preparedStatement.setDouble(index++, productInventoryDetail.getTotalCost());
            preparedStatement.setDouble(index++, productInventoryDetail.getTotalCostIncludingGST());
            preparedStatement.setDouble(index++, productInventoryDetail.getPerUnitCostIncludingAll());
            preparedStatement.setDouble(index++, productInventoryDetail.getFinalAmount());
            preparedStatement.setDouble(index++, productInventoryDetail.getcGSTRate());
            preparedStatement.setDouble(index++, productInventoryDetail.getsGSTRate());
            preparedStatement.setDouble(index++, productInventoryDetail.getcGSTAmount());
            preparedStatement.setDouble(index++, productInventoryDetail.getsGSTAmount());
            preparedStatement.execute();
        }
        catch (SQLException sqlEx){
            sqlEx.printStackTrace();
            throw new DataAccessException(sqlEx.getMessage());
        }
        return prdInvId;
    }

    private void updateProductInv(ProductInventory productInventoryDetail) throws DataAccessException {

        String updateSql = "update product_inv_record set product_id = ?, barcode = ?, invoice_id = ?, expiry_date = ?, " +
                " quantity = ?, mrp = ?, status = ?, per_unit_cost = ?, " +
                " remaining_qty = ?, inv_date = ?,  qty_uom_cd= ?, " +
                " per_unit_cost_gst =? , other_cost = ?, total_cost = ? ," +
                " total_cost_gst = ?, per_unit_cost_all = ?, final_amount = ?, cgst_rate = ?, " +
                " sgst_rate = ?, cgst_amount = ?, sgst_amount = ? " +
                " where prd_inv_id = ?";
        try (PreparedStatement preparedStatement = connectionForMultiTrans.prepareStatement(updateSql)){
            int index = 1;
            preparedStatement.setInt(index++, productInventoryDetail.getProductId());
            preparedStatement.setString(index++, productInventoryDetail.getBarCode());
            preparedStatement.setInt(index++, productInventoryDetail.getInvoiceId());
            preparedStatement.setDate(index++, new Date(productInventoryDetail.getExpiryDate().getTime()));
            preparedStatement.setDouble(index++, productInventoryDetail.getQuantity());
            preparedStatement.setDouble(index++, productInventoryDetail.getMRP());
            preparedStatement.setInt(index++, productInventoryDetail.getStatus());
            preparedStatement.setDouble(index++, productInventoryDetail.getPerUnitCost());
            preparedStatement.setDouble(index++, productInventoryDetail.getRemainingQuantity());
            preparedStatement.setDate(index++, new Date(System.currentTimeMillis()));
            preparedStatement.setString(index++, productInventoryDetail.getQtyUOM());
            preparedStatement.setDouble(index++, productInventoryDetail.getPerUnitCostIncludingGST());
            preparedStatement.setDouble(index++, productInventoryDetail.getOtherCost());
            preparedStatement.setDouble(index++, productInventoryDetail.getTotalCost());
            preparedStatement.setDouble(index++, productInventoryDetail.getTotalCostIncludingGST());
            preparedStatement.setDouble(index++, productInventoryDetail.getPerUnitCostIncludingAll());
            preparedStatement.setDouble(index++, productInventoryDetail.getFinalAmount());
            preparedStatement.setDouble(index++, productInventoryDetail.getcGSTRate());
            preparedStatement.setDouble(index++, productInventoryDetail.getsGSTRate());
            preparedStatement.setDouble(index++, productInventoryDetail.getcGSTAmount());
            preparedStatement.setDouble(index++, productInventoryDetail.getsGSTAmount());
            preparedStatement.setInt(index++, productInventoryDetail.getPrdInvId());
            preparedStatement.execute();
        }
        catch (SQLException sqlEx){
            throw new DataAccessException(sqlEx.getMessage());
        }
    }

    public List<InventoryTransactionModel> getInvTransactions(int productId){
        return new ArrayList<>();
    }

    private void logError(Exception exception){
        exception.printStackTrace();
    }
}
