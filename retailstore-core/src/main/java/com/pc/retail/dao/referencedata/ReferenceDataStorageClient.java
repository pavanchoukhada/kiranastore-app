package com.pc.retail.dao.referencedata;

import com.pc.retail.api.BaseProductInfo;
import com.pc.retail.dao.DataAccessException;
import com.pc.retail.dao.QueryModel;
import com.pc.retail.vo.GSTGroupModel;
import com.pc.retail.vo.ProductCategory;
import com.pc.retail.vo.ProductSupplier;
import com.pc.retail.vo.ProductUOM;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by pavanc on 6/28/17.
 */
public interface ReferenceDataStorageClient {

    List<String> getProductCompanies() throws DataAccessException;

    void saveProductSupplier(ProductSupplier productSupplier) throws DataAccessException;

    List<ProductSupplier> getProductSuppliers() throws DataAccessException;

    List<ProductCategory> getProductCategories() throws DataAccessException;

    List<ProductUOM> getProductUOMList() throws DataAccessException;

    List<GSTGroupModel> getGSTGroupList() throws DataAccessException;

    void saveGSTGroupModel(GSTGroupModel gstGroupModel) throws DataAccessException;

    String generateBarCode() throws DataAccessException;

    void saveBaseProductInfo(BaseProductInfo baseProductInfo) throws DataAccessException;

    List<BaseProductInfo> getAllBaseProductInfo() throws DataAccessException;
}
