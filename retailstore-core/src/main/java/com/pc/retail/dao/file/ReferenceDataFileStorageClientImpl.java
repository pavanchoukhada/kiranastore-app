package com.pc.retail.dao.file;

import com.pc.retail.api.BaseProductInfo;
import com.pc.retail.dao.DataAccessException;
import com.pc.retail.dao.QueryModel;
import com.pc.retail.dao.referencedata.ReferenceDataStorageClient;
import com.pc.retail.vo.GSTGroupModel;
import com.pc.retail.vo.ProductCategory;
import com.pc.retail.vo.ProductSupplier;
import com.pc.retail.vo.ProductUOM;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import sun.util.resources.cldr.om.CalendarData_om_ET;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pc.retail.client.services.ReferenceDataConstants.*;
import static com.pc.retail.dao.file.FileStorageClientImpl.BASE_IDS;
import static com.pc.retail.dao.file.FileStorageClientImpl.SUPPLIER_ID_CODE;
import static com.pc.retail.dao.file.FileStorageClientImpl.UNIQUE_IDS_FILE;

/**
 * Created by pavanc on 6/28/17.
 */
public class ReferenceDataFileStorageClientImpl implements ReferenceDataStorageClient {

    private String dataStoragePath;

    private ObjectMapper objectMapper;

    public ReferenceDataFileStorageClientImpl(String fileStoragePath) {
        this.dataStoragePath = fileStoragePath;
        objectMapper = new ObjectMapper();
    }


    @Override
    public List<String> getProductCompanies() throws DataAccessException {
        return null;
    }

    @Override
    public void saveProductSupplier(ProductSupplier productSupplier) throws DataAccessException {
        List<ProductSupplier> productSuppliers = getProductSuppliers();
        updateProductSupplierInList(productSuppliers, productSupplier);
        File file = new File(dataStoragePath + "\\" + PRODUCT_SUPPLIER_OBJECT_NAME + ".json");
        try {
            objectMapper.writeValue(file, productSuppliers);
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }

    private void updateProductSupplierInList(List<ProductSupplier> productSuppliers, ProductSupplier productSupplier) throws DataAccessException {
        boolean existingSupplier = false;
        for(int index = 0; index < productSuppliers.size(); index++){
            if(productSuppliers.get( index ).getId() == productSupplier.getId()){
                productSuppliers.set(index, productSupplier);
                existingSupplier = true;
            }
        }
        if(!existingSupplier) {
            productSupplier.setId(getUniqueId(SUPPLIER_ID_CODE));
            productSuppliers.add(productSupplier);
        }
    }

    private int getUniqueId(String key) throws DataAccessException{
        Map<String, Integer> uniqueIdMap = loadUniqueIds();
        Integer uniqueId = uniqueIdMap.get( key );
        if (uniqueId == null){
            uniqueId = BASE_IDS.get( key );
        }
        uniqueIdMap.put(key, (uniqueId + 1));
        persistUniqueIds(uniqueIdMap);
        return uniqueId;
    }

    private void persistUniqueIds(Map<String, Integer> uniqueIdMap) throws DataAccessException {
        try {
            File file = new File(dataStoragePath + "\\" + UNIQUE_IDS_FILE);
            objectMapper.writeValue(file, uniqueIdMap);
        }catch (IOException ioEx){
            throw new DataAccessException(ioEx.getMessage());
        }
    }

    private Map<String, Integer>  loadUniqueIds() throws DataAccessException{
        try {
            File file = new File(dataStoragePath + "\\" + UNIQUE_IDS_FILE);
            createIfDoesNotExist(file);
            return objectMapper.readValue(file, new TypeReference<Map<String, Integer>>(){});
        } catch (EOFException eofEx){
            return new HashMap<>();
        } catch (IOException ioEx){
            throw new DataAccessException(ioEx.getMessage());
        }
    }

    private void createIfDoesNotExist(File file) throws IOException{
        if(!file.exists())
            file.createNewFile();
    }

    @Override
    public List<ProductSupplier> getProductSuppliers() throws DataAccessException {
        File file = new File(dataStoragePath + "\\" + PRODUCT_SUPPLIER_OBJECT_NAME + ".json");
        try {
            return objectMapper.readValue(file,  new TypeReference<List<ProductSupplier>>(){});
        }catch (EOFException ioEx) {
            return new ArrayList<>();
        }
        catch (IOException ioEx) {
            throw new DataAccessException(ioEx.getMessage());
        }
    }

    @Override
    public List<ProductCategory> getProductCategories() throws DataAccessException {
        File file = new File(dataStoragePath + "\\" + PRODUCT_CATEGORY_OBJECT_NAME + ".json");
        try {
            return objectMapper.readValue(file,  new TypeReference<List<ProductCategory>>(){});
        }catch (EOFException ioEx) {
            return new ArrayList<>();
        }
        catch (IOException ioEx) {
            throw new DataAccessException(ioEx.getMessage());
        }
    }

    @Override
    public List<ProductUOM> getProductUOMList() throws DataAccessException {
        File file = new File(dataStoragePath + "\\" + PRODUCT_UOM_OBJECT_NAME + ".json");
        try {
            return objectMapper.readValue(file,  new TypeReference<List<ProductUOM>>(){});
        }catch (EOFException ioEx) {
            return new ArrayList<>();
        }
        catch (IOException ioEx) {
            throw new DataAccessException(ioEx.getMessage());
        }
    }

    @Override
    public List<GSTGroupModel> getGSTGroupList() throws DataAccessException {
        File file = new File(dataStoragePath + "\\" + PRODUCT_GST_GROUP + ".json");
        try {
            return objectMapper.readValue(file,  new TypeReference<List<GSTGroupModel>>(){});
        }catch (EOFException ioEx) {
            return new ArrayList<>();
        }
        catch (IOException ioEx) {
            throw new DataAccessException(ioEx.getMessage());
        }

    }

    @Override
    public void saveGSTGroupModel(GSTGroupModel gstGroupModel) throws DataAccessException {
        List<GSTGroupModel> gstGroupModelList = getGSTGroupList();
        updateGSTGroup(gstGroupModelList, gstGroupModel);
        File file = new File(dataStoragePath + "\\" + PRODUCT_GST_GROUP + ".json");
        try {
            objectMapper.writeValue(file, gstGroupModelList);
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }

    @Override
    public String generateBarCode() throws DataAccessException {
        return null;
    }

    @Override
    public void saveBaseProductInfo(BaseProductInfo baseProductInfo) throws DataAccessException {

    }

    @Override
    public List<BaseProductInfo> getAllBaseProductInfo() throws DataAccessException {
        return null;
    }

    private void updateGSTGroup(List<GSTGroupModel> gstGroupModelList, GSTGroupModel gstGroupModel) throws DataAccessException {
        boolean existingSupplier = false;
        for(int index = 0; index < gstGroupModelList.size(); index++){
            if(gstGroupModel.getGroupCode() == gstGroupModelList.get(index).getGroupCode()){
                gstGroupModelList.set(index, gstGroupModel);
                existingSupplier = true;
            }
        }
        if(!existingSupplier) {
            gstGroupModelList.add(gstGroupModel);
        }
    }
}
