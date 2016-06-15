package com.dl.pojo;

import net.sf.json.JSONArray;

public class RoomRates {

	private String rmtype;
	private String rpcode;
	private String totalPrice;
	private JSONArray rates;
	public String getRmtype() {
		return rmtype;
	}
	public void setRmtype(String rmtype) {
		this.rmtype = rmtype;
	}
	public String getRpcode() {
		return rpcode;
	}
	public void setRpcode(String rpcode) {
		this.rpcode = rpcode;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	public JSONArray getRates() {
		return rates;
	}
	public void setRates(JSONArray rates) {
		this.rates = rates;
	}
	@Override
	public String toString() {
		return "RoomRates [rates=" + rates + ", rmtype=" + rmtype + ", rpcode="
				+ rpcode + ", totalPrice=" + totalPrice + "]";
	}
	
	
	
}
