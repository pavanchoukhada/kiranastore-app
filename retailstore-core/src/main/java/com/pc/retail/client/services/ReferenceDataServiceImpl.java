package com.pc.retail.client.services;

import com.pc.retail.api.BaseProductInfo;
import com.pc.retail.cache.ReferenceDataCacheImpl;
import com.pc.retail.cache.SupplierCache;
import com.pc.retail.dao.DataAccessException;
import com.pc.retail.dao.DataSourceManager;
import com.pc.retail.dao.ReferenceDataDAO;
import com.pc.retail.interactor.KiranaAppResult;
import com.pc.retail.interactor.ResultType;
import com.pc.retail.vo.GSTGroupModel;
import com.pc.retail.vo.ProductCategory;
import com.pc.retail.vo.ProductSupplier;
import com.pc.retail.vo.ProductUOM;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pavanc on 6/10/17.
 */
public class ReferenceDataServiceImpl implements ReferenceDataService{

    @Override
    public Collection<ProductCategory> getProductCategories() throws DataAccessException{
        ReferenceDataCacheImpl referenceDataCache = ReferenceDataCacheImpl.getInstance();
        Map productCategoryMap = referenceDataCache.get(ReferenceDataCacheImpl.PRODUCT_CATEGORY_CACHE);
        Collection<ProductCategory> productCategories;
        if(productCategoryMap == null) {
            ReferenceDataDAO referenceDataDAO = DataSourceManager.getInstance().getReferenceDataDAO();
            productCategories = referenceDataDAO.getProductCategories();
            productCategoryMap = new HashMap<>();
            for(ProductCategory productCategory : productCategories){
                productCategoryMap.put( productCategory.getCategoryName(), productCategory);
            }
            referenceDataCache.put(ReferenceDataCacheImpl.PRODUCT_CATEGORY_CACHE, productCategoryMap);
        }else{
            productCategories = productCategoryMap.values();
        }

        return  productCategories;
    }

    @Override
    public Collection<ProductUOM> getUOMList() throws DataAccessException{
        ReferenceDataCacheImpl referenceDataCache = ReferenceDataCacheImpl.getInstance();
        Map productUOMMap = referenceDataCache.get(ReferenceDataCacheImpl.PRODUCT_UOM_CACHE);
        Collection<ProductUOM> productUOMs;
        if(productUOMMap == null) {
            ReferenceDataDAO referenceDataDAO = DataSourceManager.getInstance().getReferenceDataDAO();
            productUOMs = referenceDataDAO.getProductUOMList();
            productUOMMap = new HashMap<>();
            for(ProductUOM productUOM : productUOMs){
                productUOMMap.put( productUOM.getUomCode(), productUOM);
            }
            referenceDataCache.put(ReferenceDataCacheImpl.PRODUCT_UOM_CACHE, productUOMMap);

        }else{
            productUOMs = productUOMMap.values();
        }
        return  productUOMs;
    }

    @Override
    public List<String> getProductCompanies() throws DataAccessException {
        ReferenceDataCacheImpl referenceDataCache = ReferenceDataCacheImpl.getInstance();
        List<String> productCompanies= referenceDataCache.getList(ReferenceDataCacheImpl.PRODUCT_COMPANY_CACHE);
        if ( productCompanies == null){
            ReferenceDataDAO referenceDataDAO = DataSourceManager.getInstance().getReferenceDataDAO();
            productCompanies = referenceDataDAO.getProductCompanies();
        }
        return productCompanies;
    }

    @Override
    public List<ProductSupplier> getSuppliers() throws DataAccessException {
        return SupplierCache.getInstance().getAll();
    }

    @Override
    public KiranaAppResult saveSupplier(ProductSupplier productSupplier) throws DataAccessException {
        ReferenceDataDAO referenceDataDAO = DataSourceManager.getInstance().getReferenceDataDAO();
        referenceDataDAO.saveProductSupplier(productSupplier);
        return new KiranaAppResult(ResultType.SUCCESS,"Supplier Info is saved successfully");
    }

    @Override
    public List<GSTGroupModel> getGSTGroupModeList() throws DataAccessException {
        ReferenceDataDAO referenceDataDAO = DataSourceManager.getInstance().getReferenceDataDAO();
        return referenceDataDAO.getGSTModelList();
    }

    @Override
    public KiranaAppResult saveGSTGroupModel(GSTGroupModel gstGroupModel) throws DataAccessException {
        DataSourceManager.getInstance().getReferenceDataDAO().saveGSTGroupModel(gstGroupModel);
        return new KiranaAppResult(ResultType.SUCCESS,"GST Info is saved successfully");
    }

    @Override
    public KiranaAppResult saveBaseProductInfo(BaseProductInfo baseProductInfo) throws DataAccessException {
        DataSourceManager.getInstance().getReferenceDataDAO().saveBaseProductInfo(baseProductInfo);
        return new KiranaAppResult(ResultType.SUCCESS,"GST Info is saved successfully");
    }

    @Override
    public List<BaseProductInfo> getAllBaseProductInfo(BaseProductInfo baseProductInfo) throws DataAccessException {
        return DataSourceManager.getInstance().getReferenceDataDAO().getAllBaseProductInfo();
    }

}
