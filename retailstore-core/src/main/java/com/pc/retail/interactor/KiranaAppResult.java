package com.pc.retail.interactor;

/**
 * Created by pavanc on 5/13/17.
 */
public class KiranaAppResult {

    private ResultType resultType;
    private String message;

    public KiranaAppResult(ResultType resultType, String message ){
        this.resultType = resultType;
        this.message = message;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
