package com.pc.retail.dao.file;

import com.pc.retail.api.FilterKeyConstants;
import com.pc.retail.dao.*;
import com.pc.retail.dao.util.FilterModel;
import com.pc.retail.vo.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by pavanc on 6/17/17.
 */
public class FileStorageClientImpl implements StorageClient {

    public static final Map<String, Integer> BASE_IDS = new HashMap<>();
    private static String PRODUCT_ID_CODE = "product_id";
    private static String PRODUCT_INV_ID_CODE = "product_inv_id";
    public static String SUPPLIER_ID_CODE = "supplier_id";

    static {
        BASE_IDS.put(PRODUCT_ID_CODE, 101);
        BASE_IDS.put(SUPPLIER_ID_CODE, 10101);
        BASE_IDS.put(PRODUCT_INV_ID_CODE, 101);
    }

    private String dataStoragePath;
    public static final String PRD_INV_FILE = "prd_inv.json";
    public static final String PRD_CURR_INV_FILE = "prd_current_inv.json";
    public static final String PRD_INV_TRANS_FILE = "prd_inv_trans.json";
    public static final String PRD_MASTER_FILE = "product_master.json";
    public static final String PRD_INVOICE_MASTER_FILE = "prd_invoice_master.json";
    public static final String UNIQUE_IDS_FILE = "unique_ids.json";

    ObjectMapper objectMapper = new ObjectMapper();

    public FileStorageClientImpl(String dataStoragePath){
        this.dataStoragePath =  dataStoragePath;
    }

    @Override
    public int saveProductInv(ProductInventory productInventory) throws DataAccessException {
        List<ProductInventory> productInventoryList = getAllProductsInventoryEntryRecord();
        updateProductInventoryInList(productInventoryList, productInventory);
        try {
            File file = new File(dataStoragePath + "\\" + PRD_INV_FILE);
            objectMapper.writeValue(file, productInventoryList);
        }catch (IOException ioEx){
            throw new DataAccessException(ioEx.getMessage());
        }
        return productInventory.getPrdInvId();
    }

    private void updateProductInventoryInList(List<ProductInventory> productInventoryList, ProductInventory productInventory) throws DataAccessException {
        if(productInventory.getPrdInvId() > 0 ){
            boolean foundAndSaved = false;
            for(int index = 0; index < productInventoryList.size(); index++ ){
                ProductInventory savedProductInv = productInventoryList.get( index );
                if( productInventory.getPrdInvId() == savedProductInv.getPrdInvId()){
                    productInventoryList.set( index, productInventory);
                    foundAndSaved = true;
                }
            }
            if(!foundAndSaved)
                throw new DataAccessException("Product Inv Detail not found for product id " + productInventory.getProductId() );
        }else {
            productInventory.setPrdInvId(getUniqueId(PRODUCT_INV_ID_CODE));
            productInventoryList.add(productInventory);
        }
    }

    private List<ProductInventory> getAllProductsInventoryEntryRecord() throws DataAccessException{
        try {
            File file = new File(dataStoragePath + "\\" + PRD_INV_FILE);
            return objectMapper.readValue(file, new TypeReference<List<ProductInventory>>() { });
        }catch (EOFException eofEx){
            return new ArrayList<>();
        }catch (IOException ioEx) {
            throw new DataAccessException(ioEx.getMessage());
        }
    }

    @Override
    public int saveProduct(Product product) throws DataAccessException {
        List<Product> products = getProducts(-1);
        updateProductInList(products, product);
        try {
            File file = new File(dataStoragePath + "\\" + PRD_MASTER_FILE);
            objectMapper.writeValue(file, products);
        }catch (IOException ioEx){
            throw new DataAccessException(ioEx.getMessage());
        }
        return product.getProductId();
    }

    private void updateProductInList(List<Product> products, Product product) throws DataAccessException{
        if(product.getProductId() > 0 ){
            boolean foundAndSaved = false;
            for(int index = 0; index < products.size(); index++ ){
                Product savedProduct = products.get( index );
                if( savedProduct.getProductId() == product.getProductId()){
                    products.set( index, product);
                    foundAndSaved = true;
                }
            }
            if(!foundAndSaved)
                throw new DataAccessException("Product not found for product id " + product.getProductId() );
        }else {
            product.setProductId(getUniqueId(PRODUCT_ID_CODE));
            products.add(product);
        }
    }

    private int getUniqueId(String key) throws DataAccessException{
        Map<String, Integer> uniqueIdMap = loadUniqueIds();
        Integer uniqueId = uniqueIdMap.get( key );
        if (uniqueId == null){
            uniqueId = BASE_IDS.get( key );
        }
        uniqueIdMap.put(key, (uniqueId + 1));
        persistUniqueIds(uniqueIdMap);
        return uniqueId;
    }

    private void persistUniqueIds(Map<String, Integer> uniqueIdMap) throws DataAccessException {
        try {
            File file = new File(dataStoragePath + "\\" + UNIQUE_IDS_FILE);
            objectMapper.writeValue(file, uniqueIdMap);
        }catch (IOException ioEx){
            throw new DataAccessException(ioEx.getMessage());
        }
    }

    private Map<String, Integer>  loadUniqueIds() throws DataAccessException{
        try {
            File file = new File(dataStoragePath + "\\" + UNIQUE_IDS_FILE);
            createIfDoesNotExist(file);
            return objectMapper.readValue(file, new TypeReference<Map<String, Integer>>(){});
        } catch (EOFException eofEx){
            return new HashMap<>();
        } catch (IOException ioEx){
            throw new DataAccessException(ioEx.getMessage());
        }

    }

    private void createIfDoesNotExist(File file) throws IOException{
        if(!file.exists())
            file.createNewFile();
    }

    @Override
    public void addAuditInventoryTransaction(InventoryTransactionModel inventoryTransactionModel) throws DataAccessException {
        try {
            List<InventoryTransactionModel> inventoryTransactionModels = getInvTransactions();
            inventoryTransactionModels.add( inventoryTransactionModel );
            File file = new File(dataStoragePath + "\\" + PRD_INV_TRANS_FILE);
            objectMapper.writeValue(file, inventoryTransactionModels);
        }catch (IOException ioEx){
            throw new DataAccessException(ioEx.getMessage());
        }
    }

    private List<InventoryTransactionModel> getInvTransactions() throws DataAccessException{
        try {
            File file = new File(dataStoragePath + "\\" + PRD_INV_TRANS_FILE);
            if(!file.exists()){
                file.createNewFile();
            }
            return objectMapper.readValue(file, new TypeReference<List<InventoryTransactionModel>>(){});
        }catch (EOFException ioEx){
            return new ArrayList<>();
        }catch (IOException ioEx){
            throw new DataAccessException(ioEx.getMessage());
        }
    }

    @Override
    public List<InventoryTransactionModel> getInvTransactions(int productId) throws DataAccessException {
         return getInvTransactions().stream().filter(inventoryTransactionModel ->
                 inventoryTransactionModel.getProductId() == productId)
                 .collect(Collectors.toList());
    }

    @Override
    public void commit() throws DataAccessException {

    }

    @Override
    public void rollBack() throws DataAccessException {

    }

    @Override
    public List<ProductCurrentInvDetail> getCurrentInventoryForProduct(int productId) throws DataAccessException {
        List<ProductCurrentInvDetail> productCurrentInvDetailList = getProductCurrentInvDetails( );
        return productCurrentInvDetailList.stream().filter(
                productCurrentInvDetail -> productCurrentInvDetail.getProductId() == productId
        ).collect(Collectors.toList());
    }

    @Override
    public void saveCurrentInventoryForProduct(ProductCurrentInvDetail productCurrentInvDetail) throws DataAccessException {

        List<ProductCurrentInvDetail> productCurrentInvDetailList = getProductCurrentInvDetails();
        updateProductCurrInvDetailInList(productCurrentInvDetail, productCurrentInvDetailList);

        File file = new File(dataStoragePath + "\\" + PRD_CURR_INV_FILE);
        try {
            objectMapper.writeValue(file, productCurrentInvDetailList);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void updateProductCurrInvDetailInList(ProductCurrentInvDetail productCurrentInvDetail, List<ProductCurrentInvDetail> productCurrentInvDetailList) {
        boolean foundAndSaved = false;
        for(int index = 0; index < productCurrentInvDetailList.size(); index++ ){
            ProductCurrentInvDetail savedPrdCurrInvDtl = productCurrentInvDetailList.get( index );
            if( savedPrdCurrInvDtl.getProductId() == productCurrentInvDetail.getProductId()){
                productCurrentInvDetailList.set( index, productCurrentInvDetail);
                foundAndSaved = true;
            }
        }
        if(!foundAndSaved)
            productCurrentInvDetailList.add( productCurrentInvDetail );
    }

    private List<ProductCurrentInvDetail> getProductCurrentInvDetails( ) throws DataAccessException {
        try {
            File file = new File(dataStoragePath + "\\" + PRD_CURR_INV_FILE);
            if(!file.exists())
                file.createNewFile();
            return objectMapper.readValue(file, new TypeReference<List<ProductCurrentInvDetail>>() {
            });
        }catch (EOFException eofEx){
            return new ArrayList<>();
        }
        catch (IOException ioEx) {
            throw new DataAccessException(ioEx.getMessage());
        }
    }



    private List<ProductInvoiceMaster> loadInvoiceDetails() throws DataAccessException{
        try {
            File file = new File(dataStoragePath + "\\" + PRD_INVOICE_MASTER_FILE );
            if(!file.exists())
                file.createNewFile();
            return objectMapper.readValue(file, new TypeReference<List<ProductInvoiceMaster>>() {
            });
        }catch (EOFException eofEx){
            return new ArrayList<>();
        }
        catch (IOException ioEx) {
            throw new DataAccessException(ioEx.getMessage());
        }
    }

    @Override
    public void saveInvoiceMaster(ProductInvoiceMaster productInvoiceMaster) throws DataAccessException {
        List<ProductInvoiceMaster> productInvoiceMasterList = loadInvoiceDetails();
        updateInvoiceMasterToList(productInvoiceMaster, productInvoiceMasterList);
        try {
            File file = new File(dataStoragePath + "\\"  + PRD_INVOICE_MASTER_FILE );
            objectMapper.writeValue(file, productInvoiceMasterList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ProductInvoiceMaster> getProductInvoiceMasterList(FilterModel filterModel) throws DataAccessException{
        List<ProductInvoiceMaster> productInvoiceMasterList = getAllProductInvoiceMasterRecord();
        String invoiceDate = (String)filterModel.getFilterValue(FilterKeyConstants.FROM_INVOICE_DATE);
        if(invoiceDate != null && !"".equals(invoiceDate)){
            productInvoiceMasterList = productInvoiceMasterList.stream().filter(
                    productInvoiceMaster -> productInvoiceMaster.getInvoiceDate().equals(invoiceDate)).collect(Collectors.toList());
        }

        String invoiceStatus = (String)filterModel.getFilterValue(FilterKeyConstants.INVOICE_STATUS);
        if(invoiceStatus != null && !"".equals(invoiceStatus)){
            productInvoiceMasterList = productInvoiceMasterList.stream().filter(
                    productInvoiceMaster -> productInvoiceMaster.getInvoiceStatus().equals(invoiceStatus)).collect(Collectors.toList());
        }

        String supplierIdVal = (String)filterModel.getFilterValue(FilterKeyConstants.SUPPLIER_ID);
        if(supplierIdVal != null && !"".equals(supplierIdVal)){
            int supplierId = Integer.parseInt(supplierIdVal);
            productInvoiceMasterList = productInvoiceMasterList.stream().filter(
                    productInvoiceMaster -> productInvoiceMaster.getSupplierId() == supplierId).collect(Collectors.toList());
        }

        return productInvoiceMasterList;
    }

    @Override
    public ProductInvoiceMaster getInvoiceMasterWithDetail(List<SQLParameter> sqlParameterList) throws DataAccessException {
        return null;
    }


    private List<ProductInvoiceMaster> getAllProductInvoiceMasterRecord() throws DataAccessException {
        try {
            File file = new File(dataStoragePath + "\\" + PRD_INVOICE_MASTER_FILE);
            return objectMapper.readValue(file, new TypeReference<List<ProductInvoiceMaster>>() { });
        } catch(EOFException eofEx){
            return new ArrayList<>();
        } catch (IOException ioEx) {
            throw new DataAccessException(ioEx.getMessage());
        }
    }


    private void updateInvoiceMasterToList(ProductInvoiceMaster productInvoiceMaster, List<ProductInvoiceMaster> productInvoiceMasterList) {
        boolean foundAndSaved = false;
        for(int index = 0; index < productInvoiceMasterList.size(); index++ ){
            ProductInvoiceMaster savedProductInvoiceMaster = productInvoiceMasterList.get( index );
            if( savedProductInvoiceMaster.getInvoiceId() == productInvoiceMaster.getInvoiceId()){
                productInvoiceMasterList.set( index, productInvoiceMaster);
                foundAndSaved = true;
            }
        }
        if(!foundAndSaved)
            productInvoiceMasterList.add(productInvoiceMaster);
    }

    private Product getProduct(int productId) throws DataAccessException {
        List<Product> products = getProducts(productId);
        Product product = null;
        Optional<Product> productOptional = products.stream().filter(savedProduct -> savedProduct.getProductId() == productId).findAny();
        if (productOptional.isPresent()){
            product = productOptional.get();
        }
        return product;
    }

    @Override
    public List<Product> getProducts(int productId) throws DataAccessException {
        try {
            File file = new File(dataStoragePath + "\\" + PRD_MASTER_FILE);
            return objectMapper.readValue(file, new TypeReference<List<Product>>() { });
        } catch(EOFException eofEx){
            return new ArrayList<>();
        } catch (IOException ioEx) {
            throw new DataAccessException(ioEx.getMessage());
        }

    }


    @Override
    public int generateBarCode() throws DataAccessException {
        return 0;
    }

    @Override
    public List<ProductInventory> getProductInventoriesForProduct(List<SQLParameter> sqlParameterList) throws DataAccessException {
        return null;
    }


    @Override
    public void releaseConnection() {

    }
}
