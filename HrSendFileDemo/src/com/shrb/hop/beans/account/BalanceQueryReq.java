package com.shrb.hop.beans.account;


import com.shrb.hop.annotation.NotNull;
import com.shrb.hop.beans.RequestBody;

public class BalanceQueryReq extends RequestBody {

    @NotNull(message = "openID不能为空")
    private String openID;

    @NotNull(message = "digitalAccount不能为空")
    private String digitalAccount;

    private String productNo;

    private String mchID;

    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }

    public String getDigitalAccount() {
        return digitalAccount;
    }

    public void setDigitalAccount(String digitalAccount) {
        this.digitalAccount = digitalAccount;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getMchID() {
        return mchID;
    }

    public void setMchID(String mchID) {
        this.mchID = mchID;
    }


}
