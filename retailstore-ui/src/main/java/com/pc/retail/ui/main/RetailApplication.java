/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pc.retail.ui.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author pavanc
 */
public class RetailApplication extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {

        //Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("InvoiceMasterGrid.fxml"));
        //Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ProductInventoryEntryGridForm.fxml"));
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("MainForm.fxml"));
        //Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ProductLookupForm.fxml"));
        Scene scene = new Scene(root, 300, 275);
        stage.setTitle("Welcome Screen");
        stage.setScene(scene);
        stage.setMaximized(true);        
        stage.show();
    }
}
