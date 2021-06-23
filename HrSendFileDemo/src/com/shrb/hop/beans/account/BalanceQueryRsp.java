package com.shrb.hop.beans.account;

import com.shrb.hop.beans.ResponseBody;

public class BalanceQueryRsp extends ResponseBody {

	private String balanceAmt;

	public String getBalanceAmt() {
		return balanceAmt;
	}

	public void setBalanceAmt(String balanceAmt) {
		this.balanceAmt = balanceAmt;
	}

}
