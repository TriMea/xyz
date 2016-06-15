package com.dl.pojo;

public class SmsMessage {

	private String hotelid;
	private String hotelname;
	private String contactname;
	private String contacttel;
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
	public String getContactname() {
		return contactname;
	}
	public void setContactname(String contactname) {
		this.contactname = contactname;
	}
	public String getContacttel() {
		return contacttel;
	}
	public void setContacttel(String contacttel) {
		this.contacttel = contacttel;
	}
	
	@Override
	public String toString() {
		return "SmsMessage [contactname=" + contactname + ", contacttel="
				+ contacttel + ", hotelid=" + hotelid + ", hotelname="
				+ hotelname + "]";
	}
	
	
	
}
