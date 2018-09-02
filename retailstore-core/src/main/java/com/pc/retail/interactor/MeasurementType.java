package com.pc.retail.interactor;

/**
 * Created by pavanc on 5/13/17.
 */
public enum MeasurementType {
    WEIGHT(1), COUNT(2);

    private int value;

    MeasurementType(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

    public static MeasurementType valueOf(int value){
        if(value == 1)
            return MeasurementType.WEIGHT;
        else
            return MeasurementType.COUNT;
    }
}
