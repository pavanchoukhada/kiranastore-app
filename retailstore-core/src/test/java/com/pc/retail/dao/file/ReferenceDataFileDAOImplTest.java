package com.pc.retail.dao.file;

import com.pc.retail.client.services.ReferenceDataConstants;
import com.pc.retail.dao.*;
import com.pc.retail.vo.ProductCategory;
import com.pc.retail.vo.ProductSupplier;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;


/**
 * Created by pavanc on 6/18/17.
 */
public class ReferenceDataFileDAOImplTest {

    private static String dataStorePath = "c:\\datastore\\";
    private static StorageManager storageManager;
    private static ReferenceDataDAO referenceDataDAO;

    @BeforeClass
    public static void init() throws IOException, DataAccessException{
        storageManager = FileStorageManagerFactory.getFileStorageManager(dataStorePath);
        referenceDataDAO = new ReferenceDataDAOImpl(storageManager);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(getFile(ReferenceDataConstants.PRODUCT_CATEGORY_OBJECT_NAME), getProductCategories());
        objectMapper.writeValue(getFile(ReferenceDataConstants.PRODUCT_COMPANY_OBJECT_NAME), getProductCompanies());
    }

    private static List<String> getProductCompanies() {
        List<String> productCompanies = new ArrayList<>();
        productCompanies.add("HLL");
        productCompanies.add("P&G");
        productCompanies.add("Dabur");
        productCompanies.add("ITC");
        productCompanies.add("Nirma");
        return productCompanies;
    }


    private static File getFile(String fileName) {
        return new File(dataStorePath + fileName + ".json");
    }

    private static List<ProductCategory> getProductCategories() {
        List<ProductCategory> productCategories = new ArrayList<>();
        productCategories.add(getNewProductCategory("Spice",true));
        productCategories.add(getNewProductCategory("Toothpaste",false));
        productCategories.add(getNewProductCategory("Washing Powder",false));
        productCategories.add(getNewProductCategory("Bathing Soap",false));
        productCategories.add(getNewProductCategory("Washing Soap",false));
        productCategories.add(getNewProductCategory("Pulses",true));
        productCategories.add(getNewProductCategory("Rice",true));
        productCategories.add(getNewProductCategory("Shampoo",false));
        return productCategories;
    }

    private static ProductCategory getNewProductCategory(String categoryName, boolean isFoodGrade) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryName(categoryName);
        productCategory.setFoodGrade(isFoodGrade);
        return productCategory;
    }


    @Test
    public void testGetProductCategories() throws DataAccessException{
        List<ProductCategory> productCategories = referenceDataDAO.getProductCategories();
        assertThat(productCategories, hasSize(8));
    }

    @Test
    public void testGetProductCompanies() throws DataAccessException{
        List<String> productCompanies = referenceDataDAO.getProductCompanies();
        assertThat(productCompanies, hasSize(5));
    }

    @Test
    public void testSaveAndGetProductSuppliers() throws DataAccessException{
        referenceDataDAO.saveProductSupplier(createProductSupplier(101, "RKR MANASA", "GSTN01"));
        referenceDataDAO.saveProductSupplier(createProductSupplier(102, "NAH MANASA", "GSTN02"));
        referenceDataDAO.saveProductSupplier(createProductSupplier(103, "KLR", "GSTN03"));
        referenceDataDAO.saveProductSupplier(createProductSupplier(101, "RKR MANASA", "GSTN01"));
        List<ProductSupplier> productSuppliers = referenceDataDAO.getSuppliers();
        assertThat(productSuppliers, hasSize(3));
    }

    private ProductSupplier createProductSupplier(int id, String code, String gstnId) {
        ProductSupplier productSupplier = new ProductSupplier();
        productSupplier.setId(id);
        productSupplier.setCode(code);
        productSupplier.setGdtnId( gstnId );
        return productSupplier;
    }
}
