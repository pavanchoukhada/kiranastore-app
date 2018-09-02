package com.ptc.retail.test.datasource;

import com.pc.retail.dao.*;
import com.pc.retail.dao.file.FileStorageClientImpl;
import com.pc.retail.dao.file.FileStorageManagerFactory;
import com.pc.retail.dao.util.FilterModel;
import com.pc.retail.vo.ProductInvoiceMaster;
import com.pc.retail.vo.ProductAndInvDetail;
import com.pc.retail.vo.ProductCurrentInvDetail;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

/**
 * Created by pavanc on 5/13/17.
 */
public class ProductInvDAOImplTest {

    private static String dataStorePath = "c:\\datastore\\";

    @Before
    public void setUp(){
        cleanup(dataStorePath + FileStorageClientImpl.PRD_INV_TRANS_FILE);
        cleanup(dataStorePath + FileStorageClientImpl.PRD_CURR_INV_FILE);
    }

    private void cleanup(String filePath) {
        File invTransFile = new File(filePath);
        if(invTransFile.exists()){
            invTransFile.delete();
        }
    }

    @Test
    public void testSaveProductWithInvDetail() throws DataAccessException{
        StorageManager storageManager = new StorageManagerStub();
        ProductInvDAO productInvDAO = new ProductInvDAOImpl( storageManager );
        ProductAndInvDetail productAndInvDetail = new ProductAndInvDetail();
        productAndInvDetail.setInvoiceRef("INVREF1001");
        productAndInvDetail.setProductId(1001);
        productAndInvDetail.setBarcode("1001");
        productAndInvDetail.setPrdCode("REDCHILLI 100G");
        productAndInvDetail.setQuantity(50);

        productInvDAO.saveProductWithInvDetail(productAndInvDetail);

        productAndInvDetail.setProductId(1002);
        productAndInvDetail.setBarcode("1002");
        productAndInvDetail.setInvoiceRef("INVREF002");
        productInvDAO.saveProductWithInvDetail(productAndInvDetail);
        List<InventoryTransactionModel> inventoryTransactionModelList =
                productInvDAO.getInvTransactionForProduct( 1001 );
        assertThat(inventoryTransactionModelList, hasSize(1) );
        //List<ProductCurrentInvDetail> productCurrentInvDetails = productInvDAO.getCurrentInventoryDetail(1001);
        //assertThat( productCurrentInvDetails.get( 0 ).getQuantity(), is(50.0) );

        FilterModel filterModel = new FilterModel();
        filterModel.addFilter("InvoiceStatus", InvoiceStatus.INVENTORY_VERIFIED.getName());
        List<ProductInvoiceMaster> productInvoiceMasterList = productInvDAO.getProductInvoiceMasterList(filterModel);
        assertThat(productInvoiceMasterList, hasSize( 2 ) );
    }

    @Test
    public void testSaveProductWithInvDetailUsingFileDAO() throws DataAccessException{
        StorageManager storageManager = FileStorageManagerFactory.getFileStorageManager(dataStorePath);
        ProductInvDAO productInvDAO = new ProductInvDAOImpl( storageManager );
        ProductAndInvDetail productAndInvDetail = new ProductAndInvDetail();
        productAndInvDetail.setInvoiceRef("INVREF1001");
        productAndInvDetail.setProductId(1001);
        productAndInvDetail.setBarcode("1001");
        productAndInvDetail.setPrdCode("REDCHILLI 100G");
        productAndInvDetail.setQuantity(50);

        productInvDAO.saveProductWithInvDetail(productAndInvDetail);

        productAndInvDetail.setProductId(1002);
        productAndInvDetail.setBarcode("1002");
        productAndInvDetail.setInvoiceRef("INVREF002");
        productInvDAO.saveProductWithInvDetail(productAndInvDetail);
        List<InventoryTransactionModel> inventoryTransactionModelList = productInvDAO.getInvTransactionForProduct( 1001 );
        assertThat(inventoryTransactionModelList, hasSize(1) );
        //List<ProductCurrentInvDetail> productCurrentInvDetails = productInvDAO.getCurrentInventoryDetail(1001);
        //assertThat( productCurrentInvDetails.get( 0 ).getQuantity(), is(50.0) );
    }


}
