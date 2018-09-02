package com.pc.retail.client.services;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pavanc on 7/15/17.
 */
public class ResourceBundle {

    private Map<String, String> messages = new HashMap<>();

    public String getMessage(String key){
        return messages.get(key);
    }
}
