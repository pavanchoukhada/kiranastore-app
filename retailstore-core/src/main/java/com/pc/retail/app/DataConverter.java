package com.pc.retail.app;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pavanc on 5/14/17.
 */
public class DataConverter {

    public static String getFormattedDate(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("DD-MM-YYY");
        return simpleDateFormat.format( date );
    }
}
