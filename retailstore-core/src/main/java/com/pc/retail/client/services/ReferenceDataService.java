package com.pc.retail.client.services;

import com.pc.retail.api.BaseProductInfo;
import com.pc.retail.dao.DataAccessException;
import com.pc.retail.interactor.KiranaAppResult;
import com.pc.retail.vo.GSTGroupModel;
import com.pc.retail.vo.ProductCategory;
import com.pc.retail.vo.ProductSupplier;
import com.pc.retail.vo.ProductUOM;

import java.util.Collection;
import java.util.List;

/**
 * Created by pavanc on 6/10/17.
 */
public interface ReferenceDataService {

    public Collection<ProductCategory> getProductCategories( ) throws DataAccessException;

    public Collection<ProductUOM> getUOMList() throws DataAccessException;

    public List<String> getProductCompanies() throws DataAccessException;

    public List<ProductSupplier> getSuppliers() throws DataAccessException;

    public KiranaAppResult saveSupplier(ProductSupplier productSupplier) throws DataAccessException;

    public List<GSTGroupModel> getGSTGroupModeList() throws DataAccessException;

    KiranaAppResult saveGSTGroupModel(GSTGroupModel gstGroupModel) throws DataAccessException;

    KiranaAppResult saveBaseProductInfo(BaseProductInfo baseProductInfo) throws DataAccessException;

    List<BaseProductInfo> getAllBaseProductInfo(BaseProductInfo baseProductInfo) throws DataAccessException;
}
