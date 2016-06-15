package com.dl.pojo;

public class CheckInResult {

	private String hotelid;
	private String rmtype;
	private String rpcode;
	private String hotelName;
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
	
	
	public String getHotelName() {
		return hotelName;
	}
	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}
	@Override
	public String toString() {
		return "CheckInResult [hotelName=" + hotelName + ", hotelid=" + hotelid
				+ ", rmtype=" + rmtype + ", rpcode=" + rpcode + "]";
	}
	
}
