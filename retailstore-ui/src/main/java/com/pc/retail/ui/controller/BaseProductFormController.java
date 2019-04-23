package com.pc.retail.ui.controller;

import com.pc.retail.client.services.LocalCacheService;
import com.pc.retail.client.services.ProductInventoryService;
import com.pc.retail.client.services.RetailAppClient;
import com.pc.retail.client.services.RetailAppClientLocator;
import com.pc.retail.dao.util.FilterModel;
import com.pc.retail.interactor.KiranaStoreException;
import com.pc.retail.vo.Product;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by pavanc on 8/19/18.
 */
public class BaseProductFormController implements Initializable {

    @FXML
    TextField productCodeTxt;

    private String barCode;

    public BaseProductFormController() {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productCodeTxt.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                System.out.println(event.getCharacter());
                System.out.println(event.getCode().getName());
                if("\r".equals(event.getCharacter()) && productCodeTxt.getText().length() > 0){
                    submitFormAndClose();
                }
            }
        });

        HashSet<Object> productCodeSet = new HashSet<>();
        productCodeSet.addAll(LocalCacheService.getProductCodeToBarCodeMap().keySet());
        AutoCompletionBinding<Object> objectAutoCompletionBinding
                = TextFields.bindAutoCompletion(productCodeTxt, productCodeSet);
        objectAutoCompletionBinding.setVisibleRowCount(10);
    }

    private void submitFormAndClose() {
        this.barCode = LocalCacheService.getProductCodeToBarCodeMap().get(productCodeTxt.getText());
        Stage stage = (Stage)productCodeTxt.getScene().getWindow();
        System.out.println(productCodeTxt.getText());
        System.out.println(this.getBarCode());
        stage.close();
    }


    public String getBarCode() {
        return barCode;
    }
}
