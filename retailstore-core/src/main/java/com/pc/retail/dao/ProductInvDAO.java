package com.pc.retail.dao;

import java.util.List;

import com.pc.retail.dao.util.FilterModel;
import com.pc.retail.vo.*;

public interface ProductInvDAO {

	public List<ProductCurrentInvDetail> getProductInvDtlList() throws DataAccessException;

	public void saveProduct(Product prd) throws DataAccessException;

	public Product getProduct(String productId) throws DataAccessException;
	
	public List<Product> getAllProducts() throws DataAccessException;

	public void saveProductWithInvDetail(ProductAndInvDetail productAndInvDetail) throws DataAccessException;

	public List<InventoryTransactionModel> getInvTransactionForProduct(int productId) throws DataAccessException;

    List<ProductInvoiceMaster> getProductInvoiceMasterList(FilterModel filterModel) throws DataAccessException;

    ProductInvoiceMaster getProductInvoiceMasterWithDetail(int invoiceId) throws DataAccessException;

    List<ProductCurrentInvDetail> getAllCurrentInventoryDetail() throws DataAccessException;

    void saveProductInventoryWithInvoiceDetail(ProductInvoiceMaster productInvoiceMaster) throws DataAccessException;

    List<ProductCurrentInvDetail> getCurrentInventoryDetail(int productId) throws DataAccessException;

    List<ProductInventory> getProductInventories(int productId) throws DataAccessException;

    int generateBarCode() throws DataAccessException;
}
