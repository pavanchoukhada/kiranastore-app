package com.pc.retail.ui.helper;

import com.pc.retail.client.services.ReferenceDataService;
import com.pc.retail.client.services.ReferenceDataServiceImpl;
import com.pc.retail.dao.DataAccessException;
import com.pc.retail.interactor.KiranaAppResult;
import com.pc.retail.interactor.ResultType;
import com.pc.retail.ui.controller.GSTFormController;
import com.pc.retail.util.DataUtil;
import com.pc.retail.vo.GSTGroupModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavanc on 8/21/17.
 */
public class GSTGridFormHelper {

    public List<GSTGroupModel> getGSTGroupModelList(){
        ReferenceDataService referenceDataService = new ReferenceDataServiceImpl();
        try {
            return referenceDataService.getGSTGroupModeList();
        } catch (DataAccessException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public KiranaAppResult submit(GSTFormController gstFormController) {
        ReferenceDataService referenceDataService = new ReferenceDataServiceImpl();
        try {
            GSTGroupModel gstGroupModel = getGSTGroupModel(gstFormController);
            return referenceDataService.saveGSTGroupModel(gstGroupModel);
        } catch (Exception e) {
            e.printStackTrace();
            KiranaAppResult kiranaAppResult = new KiranaAppResult(ResultType.APP_ERROR, e.getMessage());
            return kiranaAppResult;
        }
    }

    private GSTGroupModel getGSTGroupModel(GSTFormController gstFormController) throws Exception {
        GSTGroupModel gstGroupModel = new GSTGroupModel();
        gstGroupModel.setGstGroupId(gstFormController.getGstGroupId());
        gstGroupModel.setGroupCode(gstFormController.getGstGroupCodeTxt().getText());
        gstGroupModel.setTaxRate(DataUtil.getDoubleValue(gstFormController.getGstRateTxt().getText(),"GST Rate"));
        gstGroupModel.setsGSTRate(DataUtil.getDoubleValue(gstFormController.getsGSTRateTxt().getText(),"SGST Rate"));
        gstGroupModel.setcGSTRate(DataUtil.getDoubleValue(gstFormController.getcGSTRateTxt().getText(),"CGST Rate"));
        return gstGroupModel;
    }
}
