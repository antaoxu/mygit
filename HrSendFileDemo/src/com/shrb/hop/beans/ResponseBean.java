package com.shrb.hop.beans;


import com.shrb.hop.annotation.NotNull;

/**
 * 公共头
 *
 * @param <T>
 * @author duxiaoyang
 */
public class ResponseBean<T extends ResponseBody> implements Bean {

    @NotNull(message = "appID不能为空")
    private String appID;

    @NotNull(message = "encryptMethod不能为空")
    private String encryptMethod;

    private String errorCode;

    private String errorMsg;

    private String liveMode;

    private String noticeType;

    @NotNull(message = "returnCode不能为空")
    private String returnCode;

    private String returnMsg;

    //	private ResponseBody rspData;
    private T rspData;

    @NotNull(message = "sequenceID不能为空")
    private String sequenceID;

    private String settlementDate;

    private String sign;

    @NotNull(message = "signMethod不能为空")
    private String signMethod;

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

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getLiveMode() {
        return liveMode;
    }

    public void setLiveMode(String liveMode) {
        this.liveMode = liveMode;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public T getRspData() {
        return rspData;
    }

//	public ResponseBody getRspData() {
//		return rspData;
//	}

//	public void setRspData(ResponseBody rspData) {
//		this.rspData = rspData;
//	}

    public void setRspData(T rspData) {
        this.rspData = rspData;
    }

    public String getSequenceID() {
        return sequenceID;
    }

    public void setSequenceID(String sequenceID) {
        this.sequenceID = sequenceID;
    }

    public String getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(String settlementDate) {
        this.settlementDate = settlementDate;
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
