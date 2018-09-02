package com.pc.retail.client.services;

public class RetailAppClientImpl implements RetailAppClient{

    @Override
    public ProductInventoryService getProductInventoryService() {
        return new ProductInventoryServiceImpl();
    }
}
