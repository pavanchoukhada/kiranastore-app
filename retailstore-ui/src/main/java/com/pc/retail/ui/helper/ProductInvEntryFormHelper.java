package com.pc.retail.ui.helper;


import com.pc.retail.api.FilterKeyConstants;
import com.pc.retail.client.services.*;
import com.pc.retail.dao.DataAccessException;
import com.pc.retail.dao.util.FilterModel;
import com.pc.retail.interactor.KiranaAppResult;
import com.pc.retail.interactor.KiranaStoreException;
import com.pc.retail.interactor.MeasurementType;
import com.pc.retail.interactor.ResultType;
import com.pc.retail.ui.controller.ComboKeyValuePair;
import com.pc.retail.ui.controller.ProductFormController;
import com.pc.retail.ui.controller.ProductInvEntryFormController;
import com.pc.retail.vo.*;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by pavanc on 5/7/17.
 */
public class ProductInvEntryFormHelper {

    public String validateForm(ProductAndInvDetail productAndInvDetail){
        String valMSg = "";
        if(isEmpty(productAndInvDetail.getProduct().getBarcode()) && productAndInvDetail.getProduct().getProductId() == 0){
            valMSg = "Please enter BarCode or Product Code";
        }

        return valMSg;
    }

    public KiranaAppResult submitForm(ProductInvEntryFormController productInvEntryFormController) {

        ProductAndInvDetail productAndInvDetail = transform(productInvEntryFormController);
        validateForm(productAndInvDetail);
        RetailAppClientLocator retailAppClientLocator = new RetailAppClientLocator();
        RetailAppClient retailAppClient = retailAppClientLocator.getRetailAppClient();
        ProductInventoryService productInventoryService =  retailAppClient.getProductInventoryService();
        KiranaAppResult kiranaAppResult;
        try {
            kiranaAppResult = productInventoryService.saveProductAndInvDetails(productAndInvDetail);
        } catch (KiranaStoreException e) {
            kiranaAppResult = new KiranaAppResult(ResultType.APP_ERROR, e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            kiranaAppResult = new KiranaAppResult(ResultType.SYSTEM_ERROR, e.getMessage());
            e.printStackTrace();
        }

        return kiranaAppResult;
    }

    public KiranaAppResult submitForm(ProductFormController productFormController) {

        Product product = transform(productFormController);
        RetailAppClientLocator retailAppClientLocator = new RetailAppClientLocator();
        RetailAppClient retailAppClient = retailAppClientLocator.getRetailAppClient();
        ProductInventoryService productInventoryService =  retailAppClient.getProductInventoryService();
        KiranaAppResult kiranaAppResult;
        try {
            kiranaAppResult = productInventoryService.saveProduct(product);
        } catch (KiranaStoreException e) {
            kiranaAppResult = new KiranaAppResult(ResultType.APP_ERROR, e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            kiranaAppResult = new KiranaAppResult(ResultType.SYSTEM_ERROR, e.getMessage());
            e.printStackTrace();
        }

        return kiranaAppResult;
    }


    private ProductAndInvDetail transform(ProductInvEntryFormController productInvEntryFormController) {
        ProductAndInvDetail productAndInvDetail = new ProductAndInvDetail();
        productAndInvDetail.setProductId(productInvEntryFormController.getProductId());
        productAndInvDetail.setBarcode( productInvEntryFormController.getPrdBarCodeTxt().getText() );
        productAndInvDetail.setCategory(productInvEntryFormController.getPrdCategoryCB().getValue());
        productAndInvDetail.setPrdCode(productInvEntryFormController.getPrdShortCodeTxt().getText());
        productAndInvDetail.setPrdDesc( productInvEntryFormController.getPrdDescriptionTxt().getText());
        productAndInvDetail.setSearchKey( productInvEntryFormController.getPrdSearchKeyTxt().getText());
        productAndInvDetail.setCompanyCode( productInvEntryFormController.getPrdCompanyCB().getValue());
        productAndInvDetail.setMeasurementType(MeasurementType.valueOf(Integer.parseInt( (String) productInvEntryFormController.getPrdMeasurementGroup().getSelectedToggle().getUserData()) ));
      //  productAndInvDetail.setBaseProduct( productInvEntryFormController.baseProductTxt.getText());
        productAndInvDetail.setProductWeight(getNumber(productInvEntryFormController.getPrdWeightTxt()));
        productAndInvDetail.setCurrentSellingPrice( getNumber( productInvEntryFormController.getPrdCSPTxt() ) );
        productAndInvDetail.setPriceUomCd( productInvEntryFormController.getPrdPriceUOMCB().getValue());
        productAndInvDetail.setQtyUomCd( productInvEntryFormController.getPrdWeightUnitCB().getValue());
        productAndInvDetail.setGSTCode(productInvEntryFormController.getPrdGSTTaxCB().getValue());

        productAndInvDetail.setQuantity( getNumber(productInvEntryFormController.getPrdQuantityTxt()));
        //productAndInvDetail.setExpiryDate( productInvEntryFormController.getPrdExpiryDtTxt());
        productAndInvDetail.setMRP( getNumber( productInvEntryFormController.getPrdMRPTxt() ) );
        productAndInvDetail.setInvoiceRef( productInvEntryFormController.getPrdInvoiceRefTxt().getText() );
        return productAndInvDetail;
    }

    private Product transform(ProductFormController productFormController) {
        Product product = new Product();
        product.setBarcode( productFormController.getPrdBarCodeTxt().getText() );
        product.setProductId( productFormController.getProductId());
        if(productFormController.getPrdBaseProduct().getValue() != null) {
            product.setBaseProductBarCode(productFormController.getPrdBaseProduct().getValue().getId());
        }
        product.setBaseProductFlag(Boolean.valueOf((String)productFormController.getPrdBaseProductChoiceGrp().getSelectedToggle().getUserData()));
        product.setCategory( productFormController.getPrdCategoryCB().getValue());
        product.setPrdCode(productFormController.getPrdShortCodeTxt().getText());
        product.setPrdDesc( productFormController.getPrdDescriptionTxt().getText());
        product.setSearchKey( productFormController.getPrdSearchKeyTxt().getText());
        product.setCompanyCode( productFormController.getPrdCompanyCB().getValue());
        product.setMeasurementType(MeasurementType.valueOf(Integer.parseInt((String) productFormController.getPrdMeasurementGroup().getSelectedToggle().getUserData())));
        product.setWeight(getNumber(productFormController.getPrdWeightTxt()));
        product.setCurrentSellingPrice( getNumber( productFormController.getPrdCSPTxt() ) );
        product.setPriceUomCd( productFormController.getPrdPriceUOMCB().getValue());
        product.setQtyUomCd( productFormController.getPrdWeightUnitCB().getValue());
        product.setGstTaxGroup(productFormController.getPrdGSTTaxCB().getValue());
        product.setProductId(productFormController.getProductId());
        return product;
    }

    private double getDouble(String text) {
        return Double.parseDouble(text);
    }

    private Double getNumber(TextField textField) {
        if(isEmpty(textField.getText()))
            return 0.0d;
        else
            return getDouble(textField.getText());
    }

    private boolean isEmpty(String text) {
        return text == null || "".equals(text);
    }

    public List<String> getProductCategories(){
        try {
            ReferenceDataService referenceDataService = new ReferenceDataServiceImpl();
            Collection<ProductCategory> categories = referenceDataService.getProductCategories();
            List<String> prdCatValues = new ArrayList<>();
            for(ProductCategory productCategory : categories){
                prdCatValues.add(productCategory.getCategoryName());
            }
            return prdCatValues;
        }catch (DataAccessException ex){
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<String> getUOMList(){
        try {
            ReferenceDataService referenceDataService = new ReferenceDataServiceImpl();
            Collection<ProductUOM> uomList = referenceDataService.getUOMList();
            List<String> prdUOMValues = new ArrayList<>();
            for(ProductUOM productUOM : uomList){
                prdUOMValues.add(productUOM.getUomCode());
            }
            return prdUOMValues;
        }catch (DataAccessException ex){
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<String> getProductCompanies(){
        try {
            ReferenceDataService referenceDataService = new ReferenceDataServiceImpl();
            List<String> companies = referenceDataService.getProductCompanies();
            companies.add("");
            return companies;
        }catch (DataAccessException ex){
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<String> getGSTGroupCode() {
        try {
            ReferenceDataService referenceDataService = new ReferenceDataServiceImpl();
            return getGSTGroupCodes(referenceDataService.getGSTGroupModeList());
        }catch (DataAccessException ex){
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<String> getGSTGroupCodes(List<GSTGroupModel> gstGroupModeList) {
        List<String> gstGroupCodes = new ArrayList<>();
        for (GSTGroupModel gstGroupModel : gstGroupModeList){
            gstGroupCodes.add(gstGroupModel.getGroupCode());
        }
        return gstGroupCodes;
    }

    public int generateBarCode() throws KiranaStoreException {
        ProductInventoryService productInventoryService = new ProductInventoryServiceImpl();
        return productInventoryService.generateBarCode();
    }

    public List<ComboKeyValuePair> getBaseProductList() {
        ProductInventoryService productInventoryService = new ProductInventoryServiceImpl();
        FilterModel filterModel = new FilterModel();
        filterModel.addFilter(FilterKeyConstants.BASE_PRODUCT_FLAG, Boolean.TRUE.toString());
        try {
            List<Product> products = productInventoryService.getProducts(filterModel);
            return transformToComboKeyValuePair(products);
        } catch (KiranaStoreException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private List<ComboKeyValuePair> transformToComboKeyValuePair(List<Product> products) {
        List<ComboKeyValuePair> productCodeList = new ArrayList<>();
        //productCodeList.add(new ComboKeyValuePair(null,"Select"));
        for(Product product : products){
            productCodeList.add(new ComboKeyValuePair(product.getBarcode(), product.getPrdCode()));

        }
        return productCodeList;
    }

    public ProductDO getProductInfo(String barCode) {
        ProductInventoryService productInventoryService = new ProductInventoryServiceImpl();
        FilterModel filterModel = new FilterModel();
        filterModel.addFilter(FilterKeyConstants.BARCODE, barCode);
        try {
            return productInventoryService.getProductDetails(filterModel);
        }catch (KiranaStoreException e){
            e.printStackTrace();
            return null;
        }

    }
}
