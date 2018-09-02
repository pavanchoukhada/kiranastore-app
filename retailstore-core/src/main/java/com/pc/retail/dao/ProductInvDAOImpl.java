package com.pc.retail.dao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import com.pc.retail.cache.ProductCache;
import com.pc.retail.dao.util.FilterModel;
import com.pc.retail.interactor.MeasurementType;
import com.pc.retail.util.DataUtil;
import com.pc.retail.util.UOMConversionUtil;
import com.pc.retail.vo.*;

public class ProductInvDAOImpl implements ProductInvDAO {
	

	private StorageManager storageManager;

	public ProductInvDAOImpl(StorageManager storageManager){
        this.storageManager = storageManager;
    }

	public void saveProduct(Product product) throws DataAccessException{
        StorageClient storageClient = storageManager.getStorageClientForTrans();
        try {
            storageClient.saveProduct(product);
        }catch(DataAccessException sqlEx){
            logError(sqlEx);
            storageClient.rollBack();
            throw new DataAccessException( sqlEx.getMessage() );
        }finally {
            storageClient.releaseConnection();
        }
	}

    private void logError(Exception sqlEx) {
	    sqlEx.printStackTrace();
    }

    public void saveProductInventory(ProductInventory productInventory) throws DataAccessException {

    }

	public  List<ProductCurrentInvDetail> getProductInvDtlList(){
		return new ArrayList<ProductCurrentInvDetail>();
	}
	
	public List<Product> getAllProducts() throws DataAccessException {
		// TODO Auto-generated method stub
		return storageManager.getStorageClientForTrans().getProducts(-1);
	}

    public void saveProductWithInvDetail(ProductAndInvDetail productAndInvDetail) throws DataAccessException {
        StorageClient storageClient = storageManager.getStorageClientForTrans();
	    try {
            int productId = storageClient.saveProduct(productAndInvDetail.getProduct());
            productAndInvDetail.getProduct().setProductId( productId );
            if(productAndInvDetail.getProductInventoryDetail().getQuantity() > 0) {
                if(productAndInvDetail.getProductInventoryDetail().getPrdInvId() <= 0 ){
                    productAndInvDetail.getProductInventoryDetail().setModificationStatus(ModificationStatus.NEW);
                }
                productAndInvDetail.setProductId(productAndInvDetail.getProduct().getProductId());
                InventoryTransactionModel invTransModel = createInvTransaction(productAndInvDetail.getProductInventoryDetail());
                int prdInvId = storageClient.saveProductInv(productAndInvDetail.getProductInventoryDetail());
                productAndInvDetail.getProductInventoryDetail().setPrdInvId(prdInvId);
                invTransModel.setExternalRef(prdInvId);
                storageClient.addAuditInventoryTransaction(invTransModel);
                updateProductCurrentInv(storageClient, productAndInvDetail.getProductInventoryDetail());
                processAndSaveProductInvoice(productAndInvDetail, storageClient);
            }
            storageClient.commit();
        }catch(SQLException | DataAccessException sqlEx){
	        logError(sqlEx);
	        storageClient.rollBack();
	        throw new DataAccessException( sqlEx.getMessage() );
        }finally {
            storageClient.releaseConnection();
        }
    }

    private void processAndSaveProductInvoice(ProductAndInvDetail productAndInvDetail, StorageClient storageClient) throws DataAccessException {
        String invoiceRef = productAndInvDetail.getProductInventoryDetail().getInvoiceRef();
        ProductInvoiceMaster productInvoiceMaster = null;
        if(!DataUtil.isEmpty(invoiceRef)) {
            productInvoiceMaster = storageClient.getInvoiceMasterWithDetail(invoiceRef);
        }
        if (productInvoiceMaster == null) {
            productInvoiceMaster = generateInvoice(productAndInvDetail.getProductInventoryDetail());
        }
        storageClient.saveInvoiceMaster(productInvoiceMaster);
    }


    @Override
    public List<InventoryTransactionModel> getInvTransactionForProduct(int productId) throws DataAccessException{
        return storageManager.getStorageClientForTrans().getInvTransactions( productId );
    }

    @Override
    public List<ProductCurrentInvDetail> getCurrentInventoryDetail(int productId) throws DataAccessException{
        return storageManager.getStorageClient().getCurrentInventoryForProduct( productId );
    }

    @Override
    public List<ProductInvoiceMaster> getProductInvoiceMasterList(FilterModel filterModel) throws DataAccessException {
        return storageManager.getStorageClient().getProductInvoiceMasterList(filterModel);
    }

    @Override
    public ProductInvoiceMaster getProductInvoiceMasterWithDetail(int invoiceId) throws DataAccessException {
        return storageManager.getStorageClient().getInvoiceMasterWithDetail(invoiceId);
    }



    @Override
    public List<ProductCurrentInvDetail> getAllCurrentInventoryDetail() throws DataAccessException {
        StorageClient storageClient = storageManager.getStorageClient();
        return storageClient.getCurrentInventoryForProduct(-1);
    }

    @Override
    public void saveProductInventoryWithInvoiceDetail(ProductInvoiceMaster productInvoiceMaster) throws DataAccessException {
        StorageClient storageClientForTrans = storageManager.getStorageClientForTrans();
        try {
            storageClientForTrans.saveInvoiceMaster(productInvoiceMaster);
            for(ProductInventory productInventory : productInvoiceMaster.getProductInventoryList()) {
                if (productInventory.getQuantity() > 0 && productInventory.getModificationStatus() != ModificationStatus.NO_CHANGE) {
                    productInventory.setInvoiceId(productInvoiceMaster.getInvoiceId());

                    InventoryTransactionModel invTransModel = createInvTransaction(productInventory);
                    if(productInventory.getPrdInvId() <= 0 ){
                        productInventory.setModificationStatus(ModificationStatus.NEW);
                    }
                    int prdInvId = storageClientForTrans.saveProductInv(productInventory);
                    invTransModel.setExternalRef(prdInvId);
                    storageClientForTrans.addAuditInventoryTransaction(invTransModel);
                    productInventory.setPrdInvId(prdInvId);
                    updateProductCurrentInv(storageClientForTrans, productInventory);
                }
            }

            storageClientForTrans.commit();
        }catch(SQLException | DataAccessException sqlEx){
            storageClientForTrans.rollBack();

            throw new DataAccessException( sqlEx.getMessage() );
        }finally {
            storageClientForTrans.releaseConnection();
        }
    }

    @Override
    public List<ProductInventory> getProductInventories(int productId) throws DataAccessException {
        return storageManager.getStorageClient().getProductInventoriesForProduct(productId);
    }

    @Override
    public int generateBarCode() throws DataAccessException {
        return storageManager.getStorageClientForTrans().generateBarCode();
    }


    private ProductInvoiceMaster generateInvoice(ProductInventory productInventory) {
        ProductInvoiceMaster productInvoiceMaster = new ProductInvoiceMaster();
        productInvoiceMaster.setInvoiceStatus(InvoiceStatus.NEW);
        productInvoiceMaster.setInvoiceDate(new Date(System.currentTimeMillis()));
        productInvoiceMaster.setInvoiceRefId( productInventory.getInvoiceRef());
        return productInvoiceMaster;
    }

    private ProductInvoiceDetail createProductInvoice(ProductInventory productInventory) {
        ProductInvoiceDetail productInvoiceDetail = new ProductInvoiceDetail();
        productInvoiceDetail.setPrdInventoryEntryId( productInventory.getPrdInvId());
        productInvoiceDetail.setProductId(productInventory.getProductId());
        productInvoiceDetail.setBarCode(productInventory.getBarCode());
        productInvoiceDetail.setQty( productInventory.getQuantity() );
        productInvoiceDetail.setModificationStatus(productInventory.getModificationStatus());
        return productInvoiceDetail;
    }

    private void updateProductCurrentInv(StorageClient storageClientTrans, ProductInventory productInventory) throws  DataAccessException{
	    StorageClient storageClient = storageManager.getStorageClient();
        List<ProductCurrentInvDetail> currentInvModels = storageClient.getCurrentInventoryForProduct(productInventory.getProductId());
        ProductCurrentInvDetail productCurrentInvDetail;
	    if(productInventory.getModificationStatus() == ModificationStatus.DELETED){
            productCurrentInvDetail = currentInvModels.get(0);
            double qtyInProductQtyUoM = getQuantityInProductQtyUoM(productInventory);
            productCurrentInvDetail.setQuantity(productCurrentInvDetail.getQuantity() - qtyInProductQtyUoM);
            productCurrentInvDetail.setModificationStatus(ModificationStatus.MODIFIED);
        }else {
	        if(currentInvModels.size() > 0){
                productCurrentInvDetail = currentInvModels.get(0);
                productCurrentInvDetail.setModificationStatus(ModificationStatus.MODIFIED);
            }else{
                productCurrentInvDetail = new ProductCurrentInvDetail();
                productCurrentInvDetail.setProductId(productInventory.getProductId());
                productCurrentInvDetail.setModificationStatus(ModificationStatus.NEW);
            }
            productCurrentInvDetail.setMRP(productInventory.getMRP());
            productCurrentInvDetail.setCostPrice(productInventory.getPerUnitCost());
            productCurrentInvDetail.setBarCode(productInventory.getBarCode());
            double qtyInProductQtyUoM = getQuantityInProductQtyUoM(productInventory);
            productCurrentInvDetail.setQuantity(productCurrentInvDetail.getQuantity() + qtyInProductQtyUoM);
            productCurrentInvDetail.setExpiryDate(productInventory.getExpiryDate());
        }
        storageClientTrans.saveCurrentInventoryForProduct(productCurrentInvDetail);

    }

    private double getQuantityInProductQtyUoM(ProductInventory productInventory) {
        try {
            Product product = ProductCache.getInstance().getProductFromBarCode(productInventory.getBarCode());
            if(product.getMeasurementType() == MeasurementType.WEIGHT) {
                String invQtyUom = productInventory.getQtyUOM();
                String productQtyUoM = product.getQtyUomCd();
                return UOMConversionUtil.convert(productInventory.getQuantity(), invQtyUom, productQtyUoM);
            }else{
                return productInventory.getQuantity();
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private InventoryTransactionModel createInvTransaction( ProductInventory productInventoryDetail ) {
        InventoryTransactionModel inventoryTransactionModel = new InventoryTransactionModel();
        inventoryTransactionModel.setProductId( productInventoryDetail.getProductId() );
        inventoryTransactionModel.setBarCode(productInventoryDetail.getBarCode());
        inventoryTransactionModel.setQuantity(productInventoryDetail.getQuantity());
        if (productInventoryDetail.getPrdInvId() <= 0) {
            inventoryTransactionModel.setTransactionType(InventoryTransactionType.NEW_INVENTORY);
        }else if (productInventoryDetail.getModificationStatus() == ModificationStatus.DELETED) {
            inventoryTransactionModel.setTransactionType(InventoryTransactionType.CANCEL_NEW_INVENTORY);
        }else{
            inventoryTransactionModel.setTransactionType(InventoryTransactionType.MODIFY_NEW_INVENTORY);
        }
        inventoryTransactionModel.setTransDate(new Date(System.currentTimeMillis()));
        inventoryTransactionModel.setExternalRef( productInventoryDetail.getPrdInvId());
        return inventoryTransactionModel;
    }


    public Product getProduct(String productId) {
		// TODO Auto-generated method stub
		return null;
	}


	public ProductCurrentInvDetail getProductInvDtl() {
		// TODO Auto-generated method stub
		return null;
	}

}
