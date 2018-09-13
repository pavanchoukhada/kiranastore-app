package com.pc.retail.client.services;

import com.pc.retail.dao.util.FilterModel;
import com.pc.retail.interactor.KiranaAppResult;
import com.pc.retail.interactor.KiranaStoreException;
import com.pc.retail.vo.*;

import java.util.List;
import java.util.logging.Filter;

/**
 * Created by pavanc on 6/10/17.
 */
public interface ProductInventoryService {

    KiranaAppResult saveProduct(Product product) throws KiranaStoreException;

    KiranaAppResult saveProductAndInvDetails(ProductAndInvDetail productAndInvDetail) throws KiranaStoreException;

    ProductDO getProductDetails(FilterModel filterModel) throws KiranaStoreException;

    List<Product> getProducts(FilterModel filterModel) throws KiranaStoreException;

    List<ProductInvoiceMasterDO> getProductInvoices(FilterModel filterModel) throws KiranaStoreException;

    ProductInvoiceMaster getProductInvoiceDetail(int invoiceRefId) throws KiranaStoreException;

    List<ProductAndInvDO> getAllProductsWithInvDetails(FilterModel filterModel) throws KiranaStoreException;

    KiranaAppResult saveProductInventoryWithInvoiceDetail(ProductInvoiceMaster productInvoiceMaster) throws KiranaStoreException;

    int generateBarCode() throws KiranaStoreException;

    List<ProductInventory> getInventoryTransactionsForProduct(int productId) throws KiranaStoreException;

}
