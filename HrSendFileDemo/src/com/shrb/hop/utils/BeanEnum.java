package com.shrb.hop.utils;

public enum BeanEnum {

    BalanceQueryBean("com.shrb.hop.beans.account.BalanceQueryReq", "com.shrb.hop.beans.account.BalanceQueryRsp", "/secapi/account/balanceQuery");

    private String reqValue;
    private String rspValue;
    private String subUrl;

    private BeanEnum(String reqValue, String rspValue, String subUrl) {
        this.reqValue = reqValue;
        this.rspValue = rspValue;
        this.subUrl = subUrl;
    }

    public String getReqValue() {
        return reqValue;
    }

    public void setReqValue(String reqValue) {
        this.reqValue = reqValue;
    }

    public String getRspValue() {
        return rspValue;
    }

    public void setRspValue(String rspValue) {
        this.rspValue = rspValue;
    }

    public String getSubUrl() {
        return subUrl;
    }

    public void setSubUrl(String subUrl) {
        this.subUrl = subUrl;
    }

}
