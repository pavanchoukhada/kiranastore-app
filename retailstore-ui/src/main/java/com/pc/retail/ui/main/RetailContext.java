package com.pc.retail.ui.main;

/**
 * Created by pavanc on 8/29/18.
 */
public class RetailContext {
    private String currentManager;
    private String currentForm;

    public String getCurrentManager() {
        return currentManager;
    }

    public void setCurrentManager(String currentManager) {
        this.currentManager = currentManager;
    }

    public String getCurrentForm() {
        return currentForm;
    }

    public void setCurrentForm(String currentForm) {
        this.currentForm = currentForm;
    }
}
