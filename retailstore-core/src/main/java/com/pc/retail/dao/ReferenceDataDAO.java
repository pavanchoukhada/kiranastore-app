package com.pc.retail.dao;

import com.pc.retail.api.BaseProductInfo;
import com.pc.retail.vo.GSTGroupModel;
import com.pc.retail.vo.ProductCategory;
import com.pc.retail.vo.ProductSupplier;
import com.pc.retail.vo.ProductUOM;

import java.util.List;

/**
 * Created by pavanc on 6/10/17.
 */
public interface ReferenceDataDAO {

    public List<ProductCategory> getProductCategories() throws DataAccessException;;

    public List<ProductUOM> getProductUOMList() throws DataAccessException;;

    List<String> getProductCompanies() throws DataAccessException;

    public List<ProductSupplier> getSuppliers() throws DataAccessException;

    public void saveProductSupplier(ProductSupplier productSupplier) throws DataAccessException;

    List<GSTGroupModel> getGSTModelList() throws DataAccessException;

    void saveGSTGroupModel(GSTGroupModel gstGroupModel) throws DataAccessException;

    void saveBaseProductInfo(BaseProductInfo baseProductInfo) throws DataAccessException;

    List<BaseProductInfo> getAllBaseProductInfo() throws DataAccessException;
}
