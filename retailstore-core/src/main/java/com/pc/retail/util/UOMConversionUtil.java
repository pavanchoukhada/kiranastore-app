package com.pc.retail.util;

import com.pc.retail.cache.ReferenceDataCacheImpl;
import com.pc.retail.vo.ProductUOM;

import java.util.Map;

/**
 * Created by pavanc on 8/25/17.
 */
public class UOMConversionUtil {

    public static double convert(double value, String fromUOM, String toUOM){
        if(!DataUtil.isEmpty(fromUOM) && !DataUtil.isEmpty(toUOM) && !fromUOM.equals(toUOM)) {
            Map<String, ProductUOM> productUOMMap = ReferenceDataCacheImpl.getInstance().getUOMMap();
            ProductUOM fromProductUOM = productUOMMap.get(fromUOM);
            ProductUOM toProductUOM = productUOMMap.get(toUOM);
            double valueInBaseUOM = fromProductUOM.getFactor() * value;
            return DataUtil.round2(valueInBaseUOM / toProductUOM.getFactor());
        }else{
            return value;
        }
    }
}
