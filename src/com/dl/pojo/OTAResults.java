package com.dl.pojo;

public class OTAResults {

	private String hotelid;
	private String hotelname;
	private String date;
	private String rpcode;
	private String rpcode_des;
	private String rmtype;
	private String rmtype_des;
	private String price;
	public String getHotelid() {
		return hotelid;
	}
	public void setHotelid(String hotelid) {
		this.hotelid = hotelid;
	}
	public String getHotelname() {
		return hotelname;
	}
	public void setHotelname(String hotelname) {
		this.hotelname = hotelname;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getRpcode() {
		return rpcode;
	}
	public void setRpcode(String rpcode) {
		this.rpcode = rpcode;
	}
	public String getRpcode_des() {
		return rpcode_des;
	}
	public void setRpcode_des(String rpcodeDes) {
		rpcode_des = rpcodeDes;
	}
	public String getRmtype() {
		return rmtype;
	}
	public void setRmtype(String rmtype) {
		this.rmtype = rmtype;
	}
	public String getRmtype_des() {
		return rmtype_des;
	}
	public void setRmtype_des(String rmtypeDes) {
		rmtype_des = rmtypeDes;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	@Override
	public String toString() {
		return "OTAResults [date=" + date + ", hotelid=" + hotelid
				+ ", hotelname=" + hotelname + ", price=" + price + ", rmtype="
				+ rmtype + ", rmtype_des=" + rmtype_des + ", rpcode=" + rpcode
				+ ", rpcode_des=" + rpcode_des + "]";
	}
	
	
	
}
