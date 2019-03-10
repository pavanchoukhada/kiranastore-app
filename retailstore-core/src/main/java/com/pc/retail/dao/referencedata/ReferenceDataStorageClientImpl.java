package com.pc.retail.dao.referencedata;

import com.pc.retail.api.BaseProductInfo;
import com.pc.retail.dao.DataAccessException;
import com.pc.retail.dao.RetailDataSource;
import com.pc.retail.vo.GSTGroupModel;
import com.pc.retail.vo.ProductCategory;
import com.pc.retail.vo.ProductSupplier;
import com.pc.retail.vo.ProductUOM;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavanc on 6/28/17.
 */
public class ReferenceDataStorageClientImpl implements ReferenceDataStorageClient{

    private RetailDataSource dataSource;

    public ReferenceDataStorageClientImpl(RetailDataSource dataSource){
        this.dataSource = dataSource;
    }


    public int getNextVal(String key) throws DataAccessException{
        try(Connection connection = dataSource.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT nextval('" + key +"')")){
            if(resultSet.next()){
                int id = resultSet.getInt(1);
                return id;
            }
        }catch (SQLException sqlEx){
            throw new DataAccessException(sqlEx.getMessage());
        }
        return -1;
    }


    @Override
    public List<String> getProductCompanies() throws DataAccessException {
        List<String> companies = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select company_name from product_company")) {
            while (resultSet.next()){
                companies.add(resultSet.getString("company_name"));
            }
        }catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return companies;
    }

    @Override
    public void saveProductSupplier(ProductSupplier productSupplier) throws DataAccessException {
        if(productSupplier.getId() > 0) {
            updateProductSupplier(productSupplier);
        }else{
            insertProductSupplier(productSupplier);
        }
    }

    private void updateProductSupplier(ProductSupplier productSupplier) throws DataAccessException{
        String updateQuery = "update product_supplier set code = ?, name = ?, mobileno = ? , phoneno = ?, " +
                " address = ?, gstn_no = ? where id= ? ";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateQuery)){
            int index=1;
            statement.setString(index++, productSupplier.getCode());
            statement.setString(index++, productSupplier.getName());
            statement.setString(index++, productSupplier.getMobileNo());
            statement.setString(index++, productSupplier.getPhoneNo());
            statement.setString(index++, productSupplier.getAddress());
            statement.setString(index++, productSupplier.getGstnId());
            statement.setInt(index, productSupplier.getId());
            statement.execute();
            connection.commit();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private void insertProductSupplier(ProductSupplier productSupplier) throws DataAccessException {
        productSupplier.setId(getNextVal("product_supplier_id_seq"));
        String insertQuery = "insert into product_supplier(id, code, name, mobileno, phoneno, address, gstn_no) values(?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(insertQuery)){
            statement.setInt(1, productSupplier.getId());
            statement.setString(2, productSupplier.getCode());
            statement.setString(3, productSupplier.getName());
            statement.setString(4, productSupplier.getMobileNo());
            statement.setString(5, productSupplier.getPhoneNo());
            statement.setString(6, productSupplier.getAddress());
            statement.setString(7, productSupplier.getGstnId());
            statement.execute();
            connection.commit();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public List<ProductSupplier> getProductSuppliers() throws DataAccessException {
        String selectQuery = "select id, code, name, mobileno, phoneno, address, gstn_no from product_supplier";
        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery)) {
            List<ProductSupplier> productSuppliers = new ArrayList<>();
            while(resultSet.next()){
                ProductSupplier productSupplier = new ProductSupplier();
                productSupplier.setId(resultSet.getInt("id"));
                productSupplier.setCode(resultSet.getString("code"));
                productSupplier.setName(resultSet.getString("name"));
                productSupplier.setMobileNo(resultSet.getString("mobileno"));
                productSupplier.setPhoneNo(resultSet.getString("phoneno"));
                productSupplier.setAddress(resultSet.getString("address"));
                productSupplier.setGstnId(resultSet.getString("gstn_no"));
                productSuppliers.add(productSupplier);
            }
            return productSuppliers;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public List<ProductCategory> getProductCategories() throws DataAccessException {
        String selectQuery = "select name, is_food_grade from product_category";
        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery)) {
            List<ProductCategory> productCategories = new ArrayList<>();
            while(resultSet.next()){
                ProductCategory productCategory = new ProductCategory();
                productCategory.setCategoryName(resultSet.getString("name"));
                productCategory.setFoodGrade(resultSet.getBoolean("is_food_grade"));
                productCategories.add(productCategory);
            }
            return productCategories;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public List<ProductUOM> getProductUOMList() throws DataAccessException {
        String selectQuery = "select name, factor from product_uom";
        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery)) {
            List<ProductUOM> productUOMS = new ArrayList<>();
            while(resultSet.next()){
                ProductUOM productUOM = new ProductUOM();
                productUOM.setUomCode(resultSet.getString("name"));
                productUOM.setFactor(resultSet.getDouble("factor"));
                productUOMS.add(productUOM);
            }
            return productUOMS;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public List<GSTGroupModel> getGSTGroupList() throws DataAccessException {
        String selectQuery = "select gst_group_id, code, tax_rate, effective_date, cgst_rate, sgst_rate from product_gst_group";
        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery)) {
            List<GSTGroupModel> gstGroupModelList = new ArrayList<>();
            while(resultSet.next()){
                GSTGroupModel gstGroupModel = new GSTGroupModel();
                gstGroupModel.setGstGroupId(resultSet.getInt("gst_group_id"));
                gstGroupModel.setGroupCode(resultSet.getString("code"));
                gstGroupModel.setTaxRate(resultSet.getDouble("tax_rate"));
                gstGroupModel.setEffectiveDate(resultSet.getDate("effective_date"));
                gstGroupModel.setcGSTRate(resultSet.getDouble("cgst_rate"));
                gstGroupModel.setsGSTRate(resultSet.getDouble("sgst_rate"));
                gstGroupModelList.add(gstGroupModel);
            }
            return gstGroupModelList;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void saveGSTGroupModel(GSTGroupModel gstGroupModel) throws DataAccessException {
        if(gstGroupModel.getGstGroupId() > 0){
            updateGSTGroupModel(gstGroupModel);
        }else {
            int gstGroupId = getNextVal("gst_group_id_seq");
            String insertQuery = "insert into product_gst_group(gst_group_id, code, tax_rate, effective_date, cgst_rate, sgst_rate) " +
                                "   values(?, ?, ?, ?, ?, ?)";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(insertQuery)) {
                statement.setInt(1, gstGroupId);
                statement.setString(2, gstGroupModel.getGroupCode());
                statement.setDouble(3, gstGroupModel.getTaxRate());
                statement.setDate(4, new Date(gstGroupModel.getEffectiveDate().getTime()));
                statement.setDouble(5, gstGroupModel.getcGSTRate());
                statement.setDouble(6, gstGroupModel.getsGSTRate());
                statement.execute();
                connection.commit();
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
        }
    }

    private void updateGSTGroupModel(GSTGroupModel gstGroupModel) throws DataAccessException{
        String insertQuery = "update product_gst_group set code = ?, tax_rate = ?, cgst_rate = ?, sgst_rate = ?, effective_date = ? where gst_group_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, gstGroupModel.getGroupCode());
            statement.setDouble(2, gstGroupModel.getTaxRate());
            statement.setDouble(3, gstGroupModel.getcGSTRate());
            statement.setDouble(4, gstGroupModel.getsGSTRate());
            statement.setDate(5, new Date(gstGroupModel.getEffectiveDate().getTime()));
            statement.setInt(6, gstGroupModel.getGstGroupId());
            statement.execute();
            connection.commit();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public String generateBarCode() throws DataAccessException {
        try(Connection connection = dataSource.getConnection();
            Statement stmt = connection.createStatement()){
            ResultSet resultSet = stmt.executeQuery("SELECT nextval(barcode_seq)");
            if(resultSet.next()){
                int id = resultSet.getInt(0);
                return String.valueOf(id);
            }
        }catch (SQLException sqlEx){
            throw new DataAccessException(sqlEx.getMessage());
        }
        return "";
    }

    @Override
    public void saveBaseProductInfo(BaseProductInfo baseProductInfo) throws DataAccessException {
        String insertQuery = "insert into base_product_info(base_product_name) " +
                "   values(?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, baseProductInfo.getBaseProductName());
            statement.execute();
            connection.commit();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public List<BaseProductInfo> getAllBaseProductInfo() throws DataAccessException{
        String selectQuery = "select base_product_name from base_product_info";
        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery)) {
            List<BaseProductInfo> baseProductInfoList = new ArrayList<>();
            while(resultSet.next()){
                BaseProductInfo baseProductInfo = new BaseProductInfo();
                baseProductInfo.setBaseProductName(resultSet.getString("base_product_name"));
                baseProductInfoList.add(baseProductInfo);
            }
            return baseProductInfoList;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

}
