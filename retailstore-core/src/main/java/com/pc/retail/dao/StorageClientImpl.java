package com.pc.retail.dao;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.pc.retail.api.FilterKeyConstants;
import com.pc.retail.dao.util.FilterModel;
import com.pc.retail.interactor.MeasurementType;
import com.pc.retail.util.DataUtil;
import com.pc.retail.vo.*;

public class StorageClientImpl implements StorageClient {

    private Connection connection;
    private RetailDataSource dataSource;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private boolean multiTrans = false;
    StorageClientImpl(Connection connection){
        this.connection = connection;
        multiTrans = true;
    }

    public StorageClientImpl(RetailDataSource retailDataSource) {
        dataSource = retailDataSource;
    }

    public int getNextVal(String key) throws DataAccessException{
        try(Statement stmt = connection.createStatement();
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
        try(PreparedStatement preparedStatement = connection.prepareStatement( insertSql )) {
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
        try(PreparedStatement preparedStatement = connection.prepareStatement( insertSql )) {
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
        try(PreparedStatement preparedStatement = connection.prepareStatement( insertSql )) {
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
            connection.commit();
        } catch (SQLException sqlEx) {
            throw new DataAccessException(sqlEx.getMessage());
        }
    }

    @Override
    public void rollBack() throws DataAccessException{
        try {
            connection.rollback();
        } catch (SQLException sqlEx) {
            throw new DataAccessException(sqlEx.getMessage());
        }
    }

    @Override
    public List<ProductCurrentInvDetail> getCurrentInventoryForProduct(int productId) throws DataAccessException {
        String selectQuery = "select product_id, barcode, current_quantity, csp, mrp, per_unit_cost, qty_uom_cd ,price_uom_cd, current_w_qty, expiry_date, " +
                " last_trans_date, last_inv_trans_type, last_trans_ref from product_curr_inv_detail ";
        if(productId > 0){
            selectQuery = selectQuery + " where product_id = " + productId;
        }
        List<ProductCurrentInvDetail> productCurrentInvDetailList = new ArrayList<>();
        Connection connection = getConnection();
        try(
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
                productCurrentInvDetail.setLastInvTransType(resultSet.getInt("last_inv_trans_type"));
                productCurrentInvDetailList.add(productCurrentInvDetail);
            }
        } catch (SQLException e) {
            logError(e);
            throw new DataAccessException(e.getMessage());
        }
        finally {
            closeConnection(connection);
        }
        return productCurrentInvDetailList;
    }

    private void closeConnection(Connection connection) throws DataAccessException {
        if(!multiTrans){
            try {
                connection.close();
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
        }
    }

    private Connection getConnection() throws DataAccessException {
        if(multiTrans){
            return connection;
        }else {
            try {
                return dataSource.getConnection();
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
        }
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
                " expiry_date, last_trans_date, last_inv_trans_type, last_trans_ref) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement( insertSql )) {
            int index = 1;
            preparedStatement.setInt(index++, productCurrentInvDetail.getProductId());
            preparedStatement.setString(index++, productCurrentInvDetail.getBarCode());
            preparedStatement.setDouble(index++, productCurrentInvDetail.getQuantity());
            preparedStatement.setDouble(index++, productCurrentInvDetail.getCSP());
            preparedStatement.setDouble(index++, productCurrentInvDetail.getMRP());
            preparedStatement.setDouble(index++, productCurrentInvDetail.getCostPrice());
            preparedStatement.setString(index++, productCurrentInvDetail.getQtyUomCd());
            preparedStatement.setString(index++, productCurrentInvDetail.getPriceUomCd());
            Date date = null;
            if(productCurrentInvDetail.getExpiryDate() != null) {
                date = new Date(productCurrentInvDetail.getExpiryDate().getTime());
            }
            preparedStatement.setDate(index++, date);
            preparedStatement.setDate(index++, new Date(System.currentTimeMillis()));
            preparedStatement.setInt(index++, productCurrentInvDetail.getLastInvTransType());
            preparedStatement.setInt(index, productCurrentInvDetail.getLastInvTransRef());
            preparedStatement.execute();
        }
    }

    private void updateProductCurrentInventoryDetail(ProductCurrentInvDetail productCurrentInvDetail) throws SQLException{
        String insertSql = "update product_curr_inv_detail set barcode = ?, current_quantity= ?, csp = ?, mrp = ?, per_unit_cost = ?, qty_uom_cd =?, price_uom_cd = ?, " +
                " last_trans_date = ?, last_inv_trans_type=?, last_trans_ref = ? where product_id = ? ";
        try(PreparedStatement preparedStatement = connection.prepareStatement( insertSql )) {
            int index = 1;
            preparedStatement.setString(index++, productCurrentInvDetail.getBarCode());
            preparedStatement.setDouble(index++, productCurrentInvDetail.getQuantity());
            preparedStatement.setDouble(index++, productCurrentInvDetail.getCSP());
            preparedStatement.setDouble(index++, productCurrentInvDetail.getMRP());
            preparedStatement.setDouble(index++, productCurrentInvDetail.getCostPrice());
            preparedStatement.setString(index++, productCurrentInvDetail.getQtyUomCd());
            preparedStatement.setString(index++, productCurrentInvDetail.getPriceUomCd());
            preparedStatement.setDate(index++, new Date(System.currentTimeMillis()));
            preparedStatement.setInt(index++, productCurrentInvDetail.getLastInvTransType());
            preparedStatement.setInt(index++, productCurrentInvDetail.getLastInvTransRef());
            preparedStatement.setInt(index, productCurrentInvDetail.getProductId());
            preparedStatement.execute();
        }
    }

    @Override
    public void saveInvoiceMaster(ProductInvoiceMaster productInvoiceMaster) throws DataAccessException {
        if(productInvoiceMaster.getInvoiceId() <= 0){
            int invoiceId = insertProductInvoiceMaster(productInvoiceMaster);
            productInvoiceMaster.setInvoiceId(invoiceId);
        }else{
            updateProductInvoiceMaster(productInvoiceMaster);
        }
    }

    private void updateProductInvoiceMaster(ProductInvoiceMaster productInvoiceMaster) throws DataAccessException{

        String insertSql = " update product_invoice_master set invoice_ref = ?, supplier_id = ?, invoice_date= ?, invoice_status = ?, " +
                "lumpsum_cost = ?, s_gst_amount= ?, c_gst_amount= ?, invoice_amount = ?, total_amount = ?, paid_amount = ?, last_modify_dt = ? " +
                " where invoice_id = ? ";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)){
            int index = 1;
            preparedStatement.setString(index++, productInvoiceMaster.getInvoiceRefId());
            preparedStatement.setInt(index++, productInvoiceMaster.getSupplierId());
            preparedStatement.setDate(index++, new Date(productInvoiceMaster.getInvoiceDate().getTime()));
            preparedStatement.setInt(index++, productInvoiceMaster.getInvoiceStatus().getInd());
            preparedStatement.setDouble(index++, productInvoiceMaster.getLumpsumCost());
            preparedStatement.setDouble(index++, productInvoiceMaster.getsGSTAmount());
            preparedStatement.setDouble(index++, productInvoiceMaster.getcGSTAmount());
            preparedStatement.setDouble(index++, productInvoiceMaster.getTotalAmountExclGST());
            preparedStatement.setDouble(index++, productInvoiceMaster.getTotalAmountInclAll());
            preparedStatement.setDouble(index++, productInvoiceMaster.getPaidAmount());
            preparedStatement.setDate(index++, new Date(System.currentTimeMillis()));
            preparedStatement.setInt(index, productInvoiceMaster.getInvoiceId());
            preparedStatement.execute();
        }
        catch (SQLException sqlEx){
            logError(sqlEx);
            throw new DataAccessException(sqlEx.getMessage());
        }
    }


    @Override
    public int generateBarCode() throws DataAccessException {
        return getNextVal("product_barcode_seq");
    }


    private int insertProductInvoiceMaster(ProductInvoiceMaster productInvoiceMaster) throws DataAccessException{
        int invoiceId = getNextVal("product_invoice_id_seq");
        String insertSql = "insert into product_invoice_master(invoice_id, invoice_ref, supplier_id, invoice_date, invoice_status, " +
                "lumpsum_cost, s_gst_amount, c_gst_amount, invoice_amount, total_amount, paid_amount, last_modify_dt) " +
                "	values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)){
            int index = 1;
            preparedStatement.setInt(index++, invoiceId);
            preparedStatement.setString(index++, productInvoiceMaster.getInvoiceRefId());
            preparedStatement.setInt(index++, productInvoiceMaster.getSupplierId());
            preparedStatement.setDate(index++, new Date(productInvoiceMaster.getInvoiceDate().getTime()));
            preparedStatement.setInt(index++, productInvoiceMaster.getInvoiceStatus().getInd());
            preparedStatement.setDouble(index++, productInvoiceMaster.getLumpsumCost());
            preparedStatement.setDouble(index++, productInvoiceMaster.getsGSTAmount());
            preparedStatement.setDouble(index++, productInvoiceMaster.getcGSTAmount());
            preparedStatement.setDouble(index++, productInvoiceMaster.getTotalAmountExclGST());
            preparedStatement.setDouble(index++, productInvoiceMaster.getTotalAmountInclAll());
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


    @Override
    public List<ProductInvoiceMaster> getProductInvoiceMasterList(FilterModel filterModel) throws DataAccessException {
        List<SQLParameter> sqlParameterList = new ArrayList<>();
        String fromInvoiceDate = filterModel.getFilterValue(FilterKeyConstants.FROM_INVOICE_DATE);
        if (fromInvoiceDate != null && !"".equals(fromInvoiceDate)) {
            SQLParameter sqlParameter = new SQLParameter();
            sqlParameter.setParamDataType(DataType.DATE);
            sqlParameter.setParamValue(DataUtil.getSqlDateValue(fromInvoiceDate));
            sqlParameter.setSqlOperator(SQLOperator.GREATER_THEN_EQUAL_TO);
            sqlParameter.setParamName("invoice_date");
            sqlParameterList.add(sqlParameter);
        }

        String toInvoiceDate = filterModel.getFilterValue(FilterKeyConstants.TO_INVOICE_DATE);
        if (toInvoiceDate != null && !"".equals(toInvoiceDate)) {
            SQLParameter sqlParameter = new SQLParameter();
            sqlParameter.setParamDataType(DataType.DATE);
            sqlParameter.setParamValue(DataUtil.getSqlDateValue(toInvoiceDate));
            sqlParameter.setSqlOperator(SQLOperator.LESS_THEN_EQUAL_TO);
            sqlParameter.setParamName("invoice_date");
            sqlParameterList.add(sqlParameter);
        }

        String invoiceStatus = filterModel.getFilterValue(FilterKeyConstants.INVOICE_STATUS);
        if (invoiceStatus != null && !"".equals(invoiceStatus)) {
            int ind = InvoiceStatus.getInvoiceStatus(invoiceStatus).getInd();
            SQLParameter sqlParameter = new SQLParameter();
            sqlParameter.setParamDataType(DataType.INTEGER);
            sqlParameter.setParamValue(ind);
            sqlParameter.setParamName("invoice_status");
        }

        String supplierIdVal =  filterModel.getFilterValue(FilterKeyConstants.SUPPLIER_ID);
        if (supplierIdVal != null && !"".equals(supplierIdVal)) {
            int supplierId = Integer.parseInt(supplierIdVal);
            SQLParameter sqlParameter = new SQLParameter();
            sqlParameter.setParamName("supplier_id");
            sqlParameter.setParamDataType(DataType.INTEGER);
            sqlParameter.setParamValue(supplierId);
        }
        return getProductInvoiceMasters("SELECT * FROM product_invoice_master ", sqlParameterList);
    }


    private List<ProductInvoiceMaster> getProductInvoiceMasters(String selectQuery, List<SQLParameter> sqlParameterList) throws DataAccessException {
        try(Connection connection = getConnection();
            PreparedStatement statement
                    = connection.prepareStatement(selectQuery + " WHERE " + appendWhereClause(sqlParameterList));
            ) {
            QueryUtil.setParameter(sqlParameterList, statement);
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
                productInvoiceMaster.setsGSTAmount(resultSet.getDouble("s_gst_amount"));
                productInvoiceMaster.setcGSTAmount(resultSet.getDouble("c_gst_amount"));
                productInvoiceMaster.setTotalAmountExclGST(resultSet.getDouble("invoice_amount"));
                productInvoiceMaster.setTotalAmountInclAll(resultSet.getDouble("total_amount"));
                productInvoiceMaster.setPaidAmount(resultSet.getDouble("paid_amount"));
                productInvoiceMasterList.add(productInvoiceMaster);
            }
            return productInvoiceMasterList;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private String appendWhereClause(List<SQLParameter> sqlParameterList) {
        if(!sqlParameterList.isEmpty()) {
            return QueryUtil.createWhereClause(sqlParameterList);
        }
        return "";
    }


    @Override
    public ProductInvoiceMaster getInvoiceMasterWithDetail(List<SQLParameter> sqlParameterList) throws DataAccessException {
        String selectQuery = "SELECT * FROM product_invoice_master";
        List<ProductInvoiceMaster> productInvoiceMasterList = getProductInvoiceMasters(selectQuery, sqlParameterList);
        if(productInvoiceMasterList.size() > 0){
            ProductInvoiceMaster productInvoiceMaster = productInvoiceMasterList.get(0);
            productInvoiceMaster.setProductInventoryList(getProductInventories(sqlParameterList));
            return productInvoiceMaster;
        }else {
            throw new DataAccessException("No Invoice Record found for given query parameter");
        }
    }

    private List<ProductInventory> getProductInventories(List<SQLParameter> sqlParameterList) throws DataAccessException{
        String selectQuery = "SELECT * FROM product_inv_record " + (sqlParameterList.isEmpty() ? "" : " WHERE " + appendWhereClause(sqlParameterList));
        return getProductInventoryListForQuery(selectQuery, false, sqlParameterList);
    }

    public List<ProductInventory> getProductInventoriesForProduct(List<SQLParameter> sqlParameterList) throws DataAccessException{

        String selectQuery = "SELECT ir.*,im.invoice_date, im.supplier_id,im.invoice_ref FROM product_inv_record ir, product_invoice_master im " +
                " where ir.invoice_id=im.invoice_id " + (sqlParameterList.isEmpty() ? "" : " AND " + appendWhereClause(sqlParameterList));
        return getProductInventoryListForQuery(selectQuery, true, sqlParameterList);
    }

    private List<ProductInventory> getProductInventoryListForQuery(String selectQuery,
                                                                   boolean loadInvoiceDetail,
                                                                   List<SQLParameter> sqlParameterList) throws DataAccessException {
        ResultSet resultSet = null;
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(selectQuery)) {
            QueryUtil.setParameter(sqlParameterList, statement);
            resultSet = statement.executeQuery();
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
                productInventory.setFinalAmountInclAll(resultSet.getDouble("final_amount"));
                productInventory.setCGSTRate(resultSet.getDouble("cgst_rate"));
                productInventory.setSGSTRate(resultSet.getDouble("sgst_rate"));
                productInventory.setPerUnitCGSTAmount(resultSet.getDouble("cgst_amount"));
                productInventory.setPerUnitSGSTAmount(resultSet.getDouble("sgst_amount"));
                productInventory.setTotalCGSTAmount(resultSet.getDouble("cgst_total_amount"));
                productInventory.setTotalSGSTAmount(resultSet.getDouble("sgst_total_amount"));
                productInventory.setGstGroupCode(resultSet.getString("gst_group_code"));
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
        }finally {
            close(resultSet);
        }
    }

    private void close(ResultSet resultSet) throws DataAccessException {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
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
        try(Statement statement = connection.createStatement();
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
            connection.close();
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
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSql)){
            preparedStatement.setInt(1, productInventoryDetail.getPrdInvId());
            preparedStatement.execute();
        }
        catch (SQLException sqlEx){
            throw new DataAccessException(sqlEx.getMessage());
        }
    }


    private int insertProductInv(ProductInventory productInventory) throws DataAccessException {
        int prdInvId = getNextVal("prd_inv_id_seq");
        String insertSql = "insert into product_inv_record(prd_inv_id, product_id, barcode, invoice_id, expiry_date, quantity, mrp, status, " +
                " per_unit_cost, inv_date, remaining_qty, qty_uom_cd, per_unit_cost_gst , other_cost , total_cost ," +
                " total_cost_gst, per_unit_cost_all, final_amount, sgst_rate, cgst_rate, sgst_amount, cgst_amount, sgst_total_amount, cgst_total_amount, gst_group_code) " +
                "	values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)){
            int index = 1;
            preparedStatement.setInt(index++, prdInvId);
            preparedStatement.setInt(index++, productInventory.getProductId());
            preparedStatement.setString(index++, productInventory.getBarCode());
            preparedStatement.setInt(index++, productInventory.getInvoiceId());
            preparedStatement.setDate(index++, new Date(productInventory.getExpiryDate().getTime()));
            preparedStatement.setDouble(index++, productInventory.getQuantity());
            preparedStatement.setDouble(index++, productInventory.getMRP());
            preparedStatement.setInt(index++, productInventory.getStatus());
            preparedStatement.setDouble(index++, productInventory.getPerUnitCost());
            preparedStatement.setDate(index++, new Date(System.currentTimeMillis()));
            preparedStatement.setDouble(index++, productInventory.getRemainingQuantity());
            preparedStatement.setString(index++, productInventory.getQtyUOM());
            preparedStatement.setDouble(index++, productInventory.getPerUnitCostIncludingGST());
            preparedStatement.setDouble(index++, productInventory.getOtherCost());
            preparedStatement.setDouble(index++, productInventory.getTotalCost());
            preparedStatement.setDouble(index++, productInventory.getTotalCostIncludingGST());
            preparedStatement.setDouble(index++, productInventory.getPerUnitCostIncludingAll());
            preparedStatement.setDouble(index++, productInventory.getFinalAmountInclAll());
            preparedStatement.setDouble(index++, productInventory.getSGSTRate());
            preparedStatement.setDouble(index++, productInventory.getCGSTRate());
            preparedStatement.setDouble(index++, productInventory.getPerUnitSGSTAmount());
            preparedStatement.setDouble(index++, productInventory.getPerUnitCGSTAmount());
            preparedStatement.setDouble(index++, productInventory.getTotalSGSTAmount());
            preparedStatement.setDouble(index++, productInventory.getTotalCGSTAmount());
            preparedStatement.setString(index, productInventory.getGstGroupCode());

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
                " total_cost_gst = ?, per_unit_cost_all = ?, final_amount = ?, sgst_rate = ?, " +
                " cgst_rate = ?, sgst_amount = ?, cgst_amount = ?, sgst_total_amount = ?, cgst_total_amount = ? , gst_group_code=? " +
                " where prd_inv_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSql)){
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
            preparedStatement.setDouble(index++, productInventoryDetail.getFinalAmountInclAll());
            preparedStatement.setDouble(index++, productInventoryDetail.getSGSTRate());
            preparedStatement.setDouble(index++, productInventoryDetail.getCGSTRate());
            preparedStatement.setDouble(index++, productInventoryDetail.getPerUnitSGSTAmount());
            preparedStatement.setDouble(index++, productInventoryDetail.getPerUnitCGSTAmount());
            preparedStatement.setDouble(index++, productInventoryDetail.getTotalSGSTAmount());
            preparedStatement.setDouble(index++, productInventoryDetail.getTotalCGSTAmount());
            preparedStatement.setString(index++, productInventoryDetail.getGstGroupCode());
            preparedStatement.setInt(index, productInventoryDetail.getPrdInvId());
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
