package com.pc.retail.ui.controller;

import com.pc.retail.ui.helper.BaseProductFormHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.awt.*;

/**
 * Created by pavanc on 8/19/18.
 */
public class BaseProductFormController {

    @FXML
    TextField baseProductCodeTxt;

    public void submitFormAndClose(ActionEvent actionEvent) {
        BaseProductFormHelper baseProductFormHelper = new BaseProductFormHelper();
        baseProductFormHelper.submitForm(this);
    }

    public void closeForm(ActionEvent actionEvent) {
    }

    public TextField getBaseProductCodeTxt() {
        return baseProductCodeTxt;
    }
}
