package com.pc.retail.dao.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pavanc on 7/23/17.
 */
public class FilterModel {

    public static final String PRODUCT_COMPANY = "PRODUCT_COMPANY";
    public static final String PRODUCT_CATEGORY = "PRODUCT_CATEGORY";
    public static final String PRODUCT_DESC  = "PRODUCT_DESC";
    public static final String SEARCH_IN_ALL_ATTRIBUTES = "ALL_ATTRIBUTES";
    public static final String BAR_CODE = "BAR_CODE";

    Map<String, String> filters = new HashMap<>();

    public void addFilter(String filterKey, String filterValue){
        filters.put(filterKey, filterValue);
    }

    public String getFilterValue(String filterKey){
        return filters.get( filterKey );
    }
}
