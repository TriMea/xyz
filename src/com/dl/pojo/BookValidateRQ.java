package com.dl.pojo;

import net.sf.json.JSONArray;

public class BookValidateRQ {

	
	private String hotelid;
	private String rmtype;
	private String rpcode;
	private JSONArray dailyinfos;
	public String getHotelid() {
		return hotelid;
	}
	public void setHotelid(String hotelid) {
		this.hotelid = hotelid;
	}
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
	public JSONArray getDailyinfos() {
		return dailyinfos;
	}
	public void setDailyinfos(JSONArray dailyinfos) {
		this.dailyinfos = dailyinfos;
	}
	@Override
	public String toString() {
		return "BookValidateRQ [dailyinfos=" + dailyinfos + ", hotelid="
				+ hotelid + ", rmtype=" + rmtype + ", rpcode=" + rpcode + "]";
	}
	
	
	
}
