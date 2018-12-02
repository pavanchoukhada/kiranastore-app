package com.pc.retail.ui.helper;

import com.pc.retail.ui.controller.ProductInventoryEntryGridFormController;
import com.pc.retail.vo.ProductInvoiceMasterDO;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.swing.event.HyperlinkListener;
import java.io.IOException;

/**
 * Created by pavanc on 9/16/18.
 */
public class ProductInvMasterEventHandler implements EventHandler<MouseEvent> {
    @Override
    public void handle(MouseEvent event) {
        Hyperlink hyperlink = (Hyperlink)event.getSource();
        try {
            launchInventoryGridForm(Integer.parseInt(hyperlink.getId()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void launchInventoryGridForm(int invoiceId) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("ProductInventoryEntryGridForm.fxml"));
        Parent root1 = fxmlLoader.load();
        ProductInventoryEntryGridFormController productInventoryEntryGridFormController = (ProductInventoryEntryGridFormController)fxmlLoader.getController();
        productInventoryEntryGridFormController.initData(invoiceId);
        Stage stage = new Stage();
        stage.setTitle("Invoice Detail Form");
        stage.setScene(new Scene(root1));
        stage.show();
    }

}
