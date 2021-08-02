package com.shrb.hop.beans;


import com.shrb.hop.annotation.NotNull;

/**
 * 公共头
 *
 * @author duxiaoyang
 */
public class RequestBean implements Bean {

    private String appAuthToken;

    @NotNull(message = "appID不能为空")
    private String appID;

    @NotNull(message = "encryptMethod不能为空")
    private String encryptMethod;

    private RequestBody reqData;

    @NotNull(message = "sequenceID不能为空")
    private String sequenceID;

    private String sign;

    @NotNull(message = "signMethod不能为空")
    private String signMethod;

    public String getAppAuthToken() {
        return appAuthToken;
    }

    public void setAppAuthToken(String appAuthToken) {
        this.appAuthToken = appAuthToken;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getEncryptMethod() {
        return encryptMethod;
    }

    public void setEncryptMethod(String encryptMethod) {
        this.encryptMethod = encryptMethod;
    }

    public RequestBody getReqData() {
        return reqData;
    }

    public void setReqData(RequestBody reqData) {
        this.reqData = reqData;
    }

    public String getSequenceID() {
        return sequenceID;
    }

    public void setSequenceID(String sequenceID) {
        this.sequenceID = sequenceID;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSignMethod() {
        return signMethod;
    }

    public void setSignMethod(String signMethod) {
        this.signMethod = signMethod;
    }

}
