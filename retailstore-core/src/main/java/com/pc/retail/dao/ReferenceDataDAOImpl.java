package com.pc.retail.dao;

import com.pc.retail.api.BaseProductInfo;
import com.pc.retail.cache.GSTCache;
import com.pc.retail.cache.SupplierCache;
import com.pc.retail.dao.referencedata.ReferenceDataStorageClient;
import com.pc.retail.vo.GSTGroupModel;
import com.pc.retail.vo.ProductCategory;
import com.pc.retail.vo.ProductSupplier;
import com.pc.retail.vo.ProductUOM;

import java.util.*;

/**
 * Created by pavanc on 6/10/17.
 */
public class ReferenceDataDAOImpl implements ReferenceDataDAO {

    private StorageManager storageManager;

    public ReferenceDataDAOImpl(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    @Override
    public List<ProductCategory> getProductCategories() throws DataAccessException{
        return storageManager.getReferenceDataStorageClient().getProductCategories();
    }




    @Override
    public List<ProductUOM> getProductUOMList() throws DataAccessException {
        return storageManager.getReferenceDataStorageClient().getProductUOMList();
    }

    @Override
    public List<String> getProductCompanies() throws DataAccessException {
        return storageManager.getReferenceDataStorageClient().getProductCompanies();
    }

    @Override
    public List<ProductSupplier> getSuppliers() throws DataAccessException {
        return storageManager.getReferenceDataStorageClient().getProductSuppliers();
    }

    public void saveProductSupplier(ProductSupplier productSupplier) throws DataAccessException{
        List<ProductSupplier> productSupplierList = getSuppliers();
        Optional<ProductSupplier> any = productSupplierList.stream().filter(productSupplier1 -> productSupplier1.getGstnId() == productSupplier.getGstnId()).findAny();
        if(any.isPresent()){
            throw new DataAccessException("Supplier exists with given GSTN");
        }
        ReferenceDataStorageClient referenceDataStorageClient = storageManager.getReferenceDataStorageClient();
        referenceDataStorageClient.saveProductSupplier(productSupplier);
        SupplierCache.getInstance().updateCache(productSupplier);

    }

    @Override
    public List<GSTGroupModel> getGSTModelList() throws DataAccessException {
        return storageManager.getReferenceDataStorageClient().getGSTGroupList();
    }

    @Override
    public void saveGSTGroupModel(GSTGroupModel gstGroupModel) throws DataAccessException {
        gstGroupModel.setEffectiveDate(new Date(System.currentTimeMillis()));
        storageManager.getReferenceDataStorageClient().saveGSTGroupModel(gstGroupModel);
        GSTCache.getInstance().updateCache(gstGroupModel);
    }

    @Override
    public void saveBaseProductInfo(BaseProductInfo baseProductInfo) throws DataAccessException {
        storageManager.getReferenceDataStorageClient().saveBaseProductInfo(baseProductInfo);
    }

    @Override
    public List<BaseProductInfo> getAllBaseProductInfo() throws DataAccessException{
        return storageManager.getReferenceDataStorageClient().getAllBaseProductInfo();
    }


}
