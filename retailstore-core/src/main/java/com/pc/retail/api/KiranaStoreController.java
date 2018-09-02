package com.pc.retail.api;

import com.pc.retail.dao.DataAccessException;
import com.pc.retail.dao.DataSourceManager;
import com.pc.retail.dao.util.FilterModel;
import com.pc.retail.interactor.KiranaStoreException;
import com.pc.retail.interactor.KiranaAppResult;
import com.pc.retail.interactor.ProductInventoryInteractor;
import com.pc.retail.vo.*;

import java.util.List;


/**
 * Created by pavanc on 5/13/17.
 */
public class KiranaStoreController {

    public KiranaAppResult saveProductWithInventory(ProductAndInvDetail productAndInvDetail) throws KiranaStoreException {
        ProductInventoryInteractor productInventoryInteractor = getProductInventoryInteractor();
        return productInventoryInteractor.saveProductWithInvDetail(productAndInvDetail);
    }

    public KiranaAppResult saveProduct(Product product) throws KiranaStoreException {
        ProductInventoryInteractor productInventoryInteractor = getProductInventoryInteractor();
        return productInventoryInteractor.saveProduct(product);
    }

    public ProductDO getProductWithDetail(FilterModel filterModel) throws KiranaStoreException {
        ProductInventoryInteractor productInventoryInteractor = getProductInventoryInteractor();
        return productInventoryInteractor.getProduct(filterModel);
    }

    private ProductInventoryInteractor getProductInventoryInteractor() throws KiranaStoreException {
        DataSourceManager dataSourceManager = null;
        try {
            dataSourceManager = DataSourceManager.getInstance();
        } catch (DataAccessException e) {
            throw new KiranaStoreException(e.getMessage());
        }
        return new ProductInventoryInteractor(dataSourceManager.getProductInvDAO());
    }

    public List<ProductInvoiceMasterDO> getProductInvoices(FilterModel filterModel) throws KiranaStoreException{
        ProductInventoryInteractor productInventoryInteractor = getProductInventoryInteractor();
        return productInventoryInteractor.getProductInvoices(filterModel);

    }

    public ProductInvoiceMaster getProductInvoiceMaster(int invoiceRefId)  throws KiranaStoreException {
        ProductInventoryInteractor productInventoryInteractor = getProductInventoryInteractor();
        return productInventoryInteractor.getProductInvoiceMasterWithDetails(invoiceRefId);
    }

    public List<ProductAndInvDO> getProductsWithInvDetails(FilterModel filterModel) throws KiranaStoreException {
        ProductInventoryInteractor productInventoryInteractor = getProductInventoryInteractor();
        return productInventoryInteractor.getProductsWithInvDetails(filterModel);
    }

    public List<Product> getAllProducts(FilterModel filterModel) throws KiranaStoreException {
        ProductInventoryInteractor productInventoryInteractor = getProductInventoryInteractor();
        return productInventoryInteractor.getAllProducts(filterModel);
    }


    public List<ProductInventory> getProductInventories(int productId) throws KiranaStoreException {
        ProductInventoryInteractor productInventoryInteractor = getProductInventoryInteractor();
        return productInventoryInteractor.getProductInventories(productId);
    }

    public KiranaAppResult saveProductInventoryWithInvoiceDetail(ProductInvoiceMaster productInvoiceMaster) throws KiranaStoreException{
        ProductInventoryInteractor productInventoryInteractor = getProductInventoryInteractor();
        return productInventoryInteractor.saveProductInventoryWithInvoiceDetail(productInvoiceMaster);
    }

    public int generateBarCode() throws KiranaStoreException {
        ProductInventoryInteractor productInventoryInteractor = getProductInventoryInteractor();
        return productInventoryInteractor.generateBarCode();
    }
}
