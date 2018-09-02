package com.pc.retail.client.services;

import com.pc.retail.interactor.BillingInteractor;
import com.pc.retail.interactor.KiranaStoreException;
import com.pc.retail.vo.BillingDetail;

/**
 * Created by pavanc on 3/31/18.
 */
public class BillingServiceImpl {

    public void saveBill(BillingDetail billingDetail) throws KiranaStoreException{
        BillingInteractor billingInteractor = new BillingInteractor();
        billingInteractor.saveModifyBill(billingDetail);
    }
}
