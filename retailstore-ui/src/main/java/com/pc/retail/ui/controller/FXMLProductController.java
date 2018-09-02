/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pc.retail.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;

/**
 *
 * @author pavanc
 */
public class FXMLProductController implements Initializable {
    
    @FXML
    private PasswordField passwordField;
    @FXML
    private Text actiontarget;
    
    @FXML
    private void handleSubmitButtonAction(ActionEvent event) {

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    public void saveAndCloseAction(ActionEvent event) {
        
    }

    @FXML
    public void saveAndAddNewAction(ActionEvent event) {
        
    }

    @FXML
    public void cancelAction(ActionEvent event) {
        
    }
    
}
