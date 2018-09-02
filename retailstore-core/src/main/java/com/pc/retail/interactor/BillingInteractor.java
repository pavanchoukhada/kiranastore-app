package com.pc.retail.interactor;

import com.pc.retail.dao.BillingDAO;
import com.pc.retail.dao.DataAccessException;
import com.pc.retail.dao.DataSourceManager;
import com.pc.retail.dao.ProductInvDAO;
import com.pc.retail.vo.BillingDetail;

public class BillingInteractor {

	private static ProductInvDAO invDAO;

	public BillingInteractor(){

    }

	public void generateBill(BillingDetail billDtl){
		
	}
	
	public void saveModifyBill(BillingDetail billDtl) throws KiranaStoreException {
        BillingDAO billingDAO = getBillingDAO();
        billingDAO.saveBill(billDtl);

	}

    private BillingDAO getBillingDAO() throws KiranaStoreException {
        DataSourceManager dataSourceManager = null;
        try {
            dataSourceManager = DataSourceManager.getInstance();
        } catch (DataAccessException e) {
            throw new KiranaStoreException(e.getMessage());
        }
        return dataSourceManager.getBillingDAO();
    }

    public void cancelBill(String billingId){
		
	}
	
	public BillingDetail getBillingDetail(){
		return null;
	}
}
