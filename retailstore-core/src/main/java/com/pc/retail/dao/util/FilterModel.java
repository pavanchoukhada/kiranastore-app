package com.pc.retail.dao.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pavanc on 7/23/17.
 */
public class FilterModel {


    Map<String, String> filters = new HashMap<>();

    public void addFilter(String filterKey, String filterValue){
        filters.put(filterKey, filterValue);
    }

    public String getFilterValue(String filterKey){
        return filters.get( filterKey );
    }
}
