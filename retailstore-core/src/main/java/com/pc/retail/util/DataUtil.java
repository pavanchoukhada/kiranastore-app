package com.pc.retail.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by pavanc on 8/3/17.
 */
public class DataUtil {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd--MM-yyyy");

    public static boolean isEmpty(String value){
        return value==null || "".equals(value);
    }

    public static int getInteger(String value){
        if(isEmpty(value))
            return 0;
        else
            return Integer.parseInt(value.trim());
    }

    public static double getDouble(String value){
        if(isEmpty(value))
            return 0.0d;
        else
            return Double.valueOf(value.trim());
    }

    public static Date getDate(LocalDate localDate){
        if(localDate != null) {
            String dateStr = localDate.format(DateTimeFormatter.ofPattern("dd-MM-y"));
            try {
                simpleDateFormat.parse(dateStr);
            } catch (ParseException e) {

            }
        }
        return new Date();
    }

    public static LocalDate parseDate(String date){
        if(isEmpty(date)){
            return LocalDate.now();
        }else {
            return LocalDate.parse(date.trim(), DateTimeFormatter.ofPattern("dd-MM-y"));
        }
    }

    public static double round2(double result) {
        return (Math.round(result * 100.0))/100.0;
    }

    public static double round4(double result) {
        return (Math.round(result * 10000.0))/10000.0;
    }


    public static double getDoubleValue(String text, String fieldName) throws Exception {
        try{
            if(!DataUtil.isEmpty(text)) {
                return Double.valueOf(text);
            }else{
                return 0.0d;
            }
        }catch (NumberFormatException exception){
            throw new Exception("Please enter valid Number value for " + fieldName );
        }
    }

    public static int getIntegerValue(String text, String fieldName) throws Exception {
        try{
            if(!DataUtil.isEmpty(text)) {
                return Integer.valueOf(text);
            }else{
                return 0;
            }
        }catch (NumberFormatException exception){
            throw new Exception("Please enter valid Number value for " + fieldName );
        }
    }

    public static String convertToText(double taxRate) {
        return String.valueOf(taxRate);
    }

    public static LocalDate getDateToLocalDate(Date invoiceDate) {
        return LocalDate.parse(simpleDateFormat.format(invoiceDate));
    }

    public static String getDateStr(LocalDate fromLocalDate) {
        return fromLocalDate.format(DateTimeFormatter.ofPattern("dd-MM-y"));
    }

    public static String getDateStr(Date expiryDate) {
        return simpleDateFormat.format(expiryDate);
    }
}
