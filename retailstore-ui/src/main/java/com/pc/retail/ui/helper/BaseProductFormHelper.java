package com.pc.retail.ui.helper;

import com.pc.retail.api.BaseProductInfo;
import com.pc.retail.client.services.ReferenceDataService;
import com.pc.retail.client.services.ReferenceDataServiceImpl;
import com.pc.retail.interactor.KiranaAppResult;
import com.pc.retail.interactor.ResultType;
import com.pc.retail.ui.controller.BaseProductFormController;
import com.pc.retail.vo.GSTGroupModel;

/**
 * Created by pavanc on 8/19/18.
 */
public class BaseProductFormHelper {

    public KiranaAppResult submitForm(BaseProductFormController baseProductFormController){
        ReferenceDataService referenceDataService = new ReferenceDataServiceImpl();
        try {
            BaseProductInfo baseProductInfo = new BaseProductInfo();
            baseProductInfo.setBaseProductName(baseProductFormController.getBaseProductCodeTxt().getText());
            return referenceDataService.saveBaseProductInfo(baseProductInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return new KiranaAppResult(ResultType.APP_ERROR, e.getMessage());
        }
    }
}
