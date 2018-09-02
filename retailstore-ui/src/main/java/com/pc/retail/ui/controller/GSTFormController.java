package com.pc.retail.ui.controller;

import com.pc.retail.interactor.KiranaAppResult;
import com.pc.retail.ui.helper.GSTGridFormHelper;
import com.pc.retail.util.DataUtil;
import com.pc.retail.vo.GSTGroupModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by pavanc on 7/29/17.
 */
public class GSTFormController implements Initializable{

    @FXML
    TextField gstGroupCodeTxt;
    @FXML
    TextField gstRateTxt;
    @FXML
    TextField cGSTRateTxt;
    @FXML
    TextField sGSTRateTxt;

    @FXML
    Button closeButton;

    GSTGridFormHelper gstGridFormHelper;

    private int gstGroupId;
    private GSTGridFormController gstGridFormController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gstGridFormHelper = new GSTGridFormHelper();
    }

    public void initData(GSTGroupModel gstGroupModel) {
        this.gstGroupId = gstGroupModel.getGstGroupId();
        this.gstGroupCodeTxt.setText(gstGroupModel.getGroupCode());
        this.gstRateTxt.setText(DataUtil.convertToText(gstGroupModel.getTaxRate()));
        this.cGSTRateTxt.setText(DataUtil.convertToText(gstGroupModel.getcGSTRate()));
        this.sGSTRateTxt.setText(DataUtil.convertToText(gstGroupModel.getsGSTRate()));
    }

    public void closeForm(ActionEvent actionEvent) {
        Stage stage = (Stage)closeButton.getScene().getWindow();
        stage.close();
    }

    public void submitFormAndClose(ActionEvent actionEvent) {
        KiranaAppResult kiranaAppResult = gstGridFormHelper.submit(this);
        generateResponseToUser(Alert.AlertType.INFORMATION, kiranaAppResult.getMessage());
        closeForm(actionEvent);
        gstGridFormController.loadGSTGroupGrid();
    }

    private ButtonType generateResponseToUser(Alert.AlertType warning, String message) {
        Alert alert = new Alert(warning, message);
        Optional<ButtonType> buttonType = alert.showAndWait();
        return buttonType.get();
    }

    public TextField getGstGroupCodeTxt() {
        return gstGroupCodeTxt;
    }

    public void setGstGroupCodeTxt(TextField gstGroupCodeTxt) {
        this.gstGroupCodeTxt = gstGroupCodeTxt;
    }

    public TextField getGstRateTxt() {
        return gstRateTxt;
    }

    public void setGstRateTxt(TextField gstRateTxt) {
        this.gstRateTxt = gstRateTxt;
    }

    public TextField getcGSTRateTxt() {
        return cGSTRateTxt;
    }

    public void setcGSTRateTxt(TextField cGSTRateTxt) {
        this.cGSTRateTxt = cGSTRateTxt;
    }

    public TextField getsGSTRateTxt() {
        return sGSTRateTxt;
    }

    public void setsGSTRateTxt(TextField sGSTRateTxt) {
        this.sGSTRateTxt = sGSTRateTxt;
    }

    public GSTGridFormHelper getGstGridFormHelper() {
        return gstGridFormHelper;
    }

    public void setGstGridFormHelper(GSTGridFormHelper gstGridFormHelper) {
        this.gstGridFormHelper = gstGridFormHelper;
    }

    public int getGstGroupId() {
        return gstGroupId;
    }

    public void setGstGroupId(int gstGroupId) {
        this.gstGroupId = gstGroupId;
    }

    public void hookGSTGrid(GSTGridFormController gstGridFormController) {
        this.gstGridFormController = gstGridFormController;
    }
}
