package com.pc.retail.client.services;

import com.pc.retail.api.KiranaStoreController;
import com.pc.retail.dao.util.FilterModel;
import com.pc.retail.interactor.KiranaAppResult;
import com.pc.retail.interactor.KiranaStoreException;
import com.pc.retail.vo.*;

import java.util.List;

/**
 * Created by pavanc on 6/10/17.
 */
public class ProductInventoryServiceImpl implements ProductInventoryService {
    @Override
    public KiranaAppResult saveProduct(Product product) throws KiranaStoreException {
        KiranaStoreController controller = new KiranaStoreController();
        return controller.saveProduct( product );
    }

    @Override
    public KiranaAppResult saveProductAndInvDetails(ProductAndInvDetail productAndInvDetail) throws KiranaStoreException {
        KiranaStoreController controller = new KiranaStoreController();
        return controller.saveProductWithInventory( productAndInvDetail );
    }

    @Override
    public ProductDO getProductDetails(FilterModel filterModel) throws KiranaStoreException {
        KiranaStoreController controller = new KiranaStoreController();
        return controller.getProductWithDetail(filterModel);

    }

    @Override
    public List<Product> getProducts(FilterModel filterModel) throws KiranaStoreException {
        KiranaStoreController controller = new KiranaStoreController();
        return controller.getAllProducts(filterModel);
    }

    @Override
    public List<ProductInvoiceMasterDO> getProductInvoices(FilterModel filterModel) throws KiranaStoreException {
        KiranaStoreController controller = new KiranaStoreController();
        return controller.getProductInvoices(filterModel);
    }

    @Override
    public ProductInvoiceMaster getProductInvoiceDetail(int invoiceRefId) throws KiranaStoreException {
        KiranaStoreController controller = new KiranaStoreController();
        return controller.getProductInvoiceMaster(invoiceRefId);
    }

    @Override
    public List<ProductAndInvDO> getAllProductsWithInvDetails(FilterModel filterModel) throws KiranaStoreException {
        KiranaStoreController controller = new KiranaStoreController();
        return controller.getProductsWithInvDetails(filterModel);
    }

    @Override
    public KiranaAppResult saveProductInventoryWithInvoiceDetail(ProductInvoiceMaster productInvoiceMaster) throws KiranaStoreException{
        KiranaStoreController controller = new KiranaStoreController();
        return controller.saveProductInventoryWithInvoiceDetail(productInvoiceMaster);
    }

    @Override
    public int generateBarCode() throws KiranaStoreException {
        KiranaStoreController controller = new KiranaStoreController();
        return controller.generateBarCode();
    }

    @Override
    public List<ProductInventory> getInventoryTransactionsForProduct(int productId) throws KiranaStoreException {
        KiranaStoreController controller = new KiranaStoreController();
        return controller.getProductInventories(productId);
    }

}
