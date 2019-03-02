package com.pc.retail.interactor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pc.retail.api.FilterKeyConstants;
import com.pc.retail.cache.GSTCache;
import com.pc.retail.cache.ProductCache;
import com.pc.retail.cache.SupplierCache;
import com.pc.retail.dao.DataAccessException;
import com.pc.retail.dao.ProductInvDAO;
import com.pc.retail.dao.util.FilterModel;
import com.pc.retail.vo.*;

public class ProductInventoryInteractor {

    private ProductInvDAO productInvDAO;

    public ProductInventoryInteractor(ProductInvDAO productInvDAO){
        this.productInvDAO = productInvDAO;
    }

	public KiranaAppResult saveProduct(Product product) throws KiranaStoreException {
        try {
            productInvDAO.saveProduct( product );
            refreshProductAndInvCache(product);
        }catch (DataAccessException daExc){
            return new KiranaAppResult(ResultType.APP_ERROR, daExc.getMessage());
        }
        return new KiranaAppResult(ResultType.SUCCESS, "Product and Invetory Details are saved");
	}

	public void saveProductInv(ProductInventory prdInvRec) {

	}

	public KiranaAppResult saveProductWithInvDetail(ProductAndInvDetail productAndInvDetail) throws KiranaStoreException {
        try {
            productInvDAO.saveProductWithInvDetail( productAndInvDetail );
            refreshProductAndInvCache(productAndInvDetail.getProduct());
        }catch (DataAccessException daExc){
            return new KiranaAppResult(ResultType.APP_ERROR, daExc.getMessage());
        }
        return new KiranaAppResult(ResultType.SUCCESS, "Product and Invetory Details are saved");
	}

    private void refreshProductAndInvCache(Product product) throws DataAccessException{
        ProductCache.getInstance().updateCache(product);
    }

    /**
	 * Return Map<barCode, Short Desc>
	 * 
	 * @param searchKey
	 * @return
	 */
	public Map<String, String> productLookupQuery(String searchKey) throws DataAccessException{
		return ProductCache.getInstance().productLookupQuery(searchKey);
	}

    public ProductDO getProduct(FilterModel filterModel) throws KiranaStoreException{
        ProductDO productDO = null;
	    try {
	        ProductFilter productFilter = parseFilter(filterModel);
            Product product = ProductCache.getInstance().getProduct(productFilter);
            if(product != null) {
                productDO = new ProductDO(product);
                if(productDO.getGstTaxGroup() != null && !"".equals(productDO.getGstTaxGroup())) {
                    GSTGroupModel gstGroupModel = GSTCache.getInstance().get(productDO.getGstTaxGroup());
                    productDO.setcGSTRate(gstGroupModel.getcGSTRate());
                    productDO.setsGSTRate(gstGroupModel.getsGSTRate());
                    productDO.setGstRate(gstGroupModel.getTaxRate());
                }
                if(product.getBaseProductBarCode() != null){
                    Product baseProduct = ProductCache.getInstance().getProductFromBarCode(product.getBaseProductBarCode());
                    productDO.setBaseProductCode(baseProduct.getPrdCode());
                }
            }
            return productDO;
        }catch (DataAccessException ex){
	        throw new KiranaStoreException(ex.getMessage());
        }
    }

    public List<Product> getAllProducts(FilterModel filterModel) throws KiranaStoreException{
        List<Product> filteredProducts = new ArrayList<>();
        try {
            List<Product> products = ProductCache.getInstance().getAllProducts();
            ProductFilter productFilter = parseFilter(filterModel);
            for(Product product : products){
                if(filter(product, productFilter)){
                    filteredProducts.add(product);
                }
            }
            return filteredProducts;
        }catch (DataAccessException ex){
            throw new KiranaStoreException(ex.getMessage());
        }
    }

    private boolean filter(Product product, ProductFilter productFilter) {
	    Boolean isBaseProductFlag = productFilter.getIsBaseProductFlag();
	    boolean filterResult = true;
	    if(isBaseProductFlag != null && isBaseProductFlag){
	        filterResult = product.isBaseProductFlag();
        }
        return filterResult;
    }

    private ProductFilter parseFilter(FilterModel filterModel) {
	    ProductFilter productFilter = new ProductFilter();
	    productFilter.setBarCode(filterModel.getFilterValue(FilterKeyConstants.BARCODE));
        if(filterModel.getFilterValue(FilterKeyConstants.PRODUCT_ID) != null) {
            productFilter.setProductId(Integer.parseInt(filterModel.getFilterValue(FilterKeyConstants.PRODUCT_ID)));
        }
        if(filterModel.getFilterValue(FilterKeyConstants.BASE_PRODUCT_FLAG) != null) {
            productFilter.setIsBaseProductFlag(Boolean.valueOf(filterModel.getFilterValue(FilterKeyConstants.BASE_PRODUCT_FLAG)));
        }
	    return productFilter;

    }

    public List<ProductInvoiceMasterDO> getProductInvoices(FilterModel filterModel) throws KiranaStoreException{
	    try {
            List<ProductInvoiceMaster> productInvoiceMasterList = productInvDAO.getProductInvoiceMasterList(filterModel);
            List<ProductInvoiceMasterDO> productInvoiceMasterDOList = new ArrayList<>();
            for(ProductInvoiceMaster productInvoiceMaster : productInvoiceMasterList) {
                productInvoiceMasterDOList.add(transformInvoiceMasterToDO(productInvoiceMaster));
            }
            return productInvoiceMasterDOList;
        }catch (DataAccessException ex){
            throw new KiranaStoreException(ex.getMessage());
        }

    }

    private ProductInvoiceMasterDO transformInvoiceMasterToDO(ProductInvoiceMaster productInvoiceMaster) throws DataAccessException{
        ProductInvoiceMasterDO productInvoiceMasterDO =new ProductInvoiceMasterDO(productInvoiceMaster);
        if(productInvoiceMasterDO.getSupplierId() > 0) {
            productInvoiceMasterDO.setSupplierName(SupplierCache.getInstance().getProductSupplier(productInvoiceMasterDO.getSupplierId()).getName());
        }
	    return productInvoiceMasterDO;
    }

    public ProductInvoiceMaster getProductInvoiceMasterWithDetails(int invoiceRefId) throws KiranaStoreException {
        try {
            ProductInvoiceMaster productInvoiceMaster =  productInvDAO.getProductInvoiceMasterWithDetail(invoiceRefId);
            enrichWithProductDetails(productInvoiceMaster.getProductInventoryList());
            return productInvoiceMaster;
        }catch (DataAccessException ex){
            throw new KiranaStoreException(ex.getMessage());
        }
    }

    private void enrichWithProductDetails(List<ProductInventory> productInventoryList) throws DataAccessException {
	    for(ProductInventory productInventory : productInventoryList){
	        Product product = ProductCache.getInstance().getProductFromBarCode(productInventory.getBarCode());
	        productInventory.setProductCode(product.getPrdCode());
	        productInventory.setSalePrice(product.getCurrentSellingPrice());
	        productInventory.setSalePriceUOM(product.getPriceUomCd());
        }
    }

    public List<ProductAndInvDO> getProductsWithInvDetails(FilterModel filterModel) throws KiranaStoreException {
        try {
            List<Product> products = ProductCache.getInstance().getAllProducts();
            ProductSearchEngine productSearchEngine = new ProductSearchEngine(products);
            List<Product> filteredProducts =  productSearchEngine.filterProducts(filterModel);
            return enrichAndTransformProductInvDO(filteredProducts);
        } catch (DataAccessException e) {
            throw new KiranaStoreException(e.getMessage());
        }
    }

    private List<ProductAndInvDO> enrichAndTransformProductInvDO(List<Product> products) throws DataAccessException{
        List<ProductAndInvDO> productAndInvDOList = new ArrayList<>();
	    for(Product product : products){
            productAndInvDOList.add(new ProductAndInvDO(product));
        }
        return productAndInvDOList;
    }

    private Map<Integer, ProductCurrentInvDetail> convertoToMap(List<ProductCurrentInvDetail> allCurrentInventoryDetail) {
        Map<Integer, ProductCurrentInvDetail> productCurrentInvDetailMap = new HashMap<>();
        for(ProductCurrentInvDetail productCurrentInvDetail : allCurrentInventoryDetail){
            productCurrentInvDetailMap.put(productCurrentInvDetail.getProductId(), productCurrentInvDetail);
        }
        return productCurrentInvDetailMap;
    }

    public KiranaAppResult saveProductInventoryWithInvoiceDetail(ProductInvoiceMaster productInvoiceMaster) {
        try {
            productInvDAO.saveProductInventoryWithInvoiceDetail(productInvoiceMaster);
            //refreshProductAndInvCache();
        }catch (DataAccessException daExc){
            return new KiranaAppResult(ResultType.APP_ERROR, daExc.getMessage());
        }
        return new KiranaAppResult(ResultType.SUCCESS, "Product and Inventory Details are saved");
    }

    public List<ProductInventory> getProductInventories(int productId) throws KiranaStoreException{
        try {
            List<ProductInventory> productInventories = productInvDAO.getProductInventories(productId);
            for(ProductInventory productInventory : productInventories){
                productInventory.setSupplierCode(SupplierCache.getInstance()
                        .getProductSupplier(
                                productInventory.getSupplierId()).getCode());
                productInventory.setProductCode(ProductCache.getInstance()
                        .getProductFromBarCode(
                                productInventory.getBarCode()).getPrdCode());
            }
            return productInventories;
        }catch (DataAccessException daExc){
            throw new KiranaStoreException(daExc.getMessage()) ;
        }
    }

    public int generateBarCode() throws KiranaStoreException {
        try {
            return productInvDAO.generateBarCode();
        } catch (DataAccessException e) {
            throw new KiranaStoreException(e.getMessage());
        }
    }
}
