package com.dl.pojo;

public class Mstinfo_rep {

	private String hotelid;
	private String taobaoorderid;
	private String checkin;
	private String checkout;
	private String contactname;
	public String getHotelid() {
		return hotelid;
	}
	public void setHotelid(String hotelid) {
		this.hotelid = hotelid;
	}
	public String getTaobaoorderid() {
		return taobaoorderid;
	}
	public void setTaobaoorderid(String taobaoorderid) {
		this.taobaoorderid = taobaoorderid;
	}
	public String getCheckin() {
		return checkin;
	}
	public void setCheckin(String checkin) {
		this.checkin = checkin;
	}
	public String getCheckout() {
		return checkout;
	}
	public void setCheckout(String checkout) {
		this.checkout = checkout;
	}
	public String getContactname() {
		return contactname;
	}
	public void setContactname(String contactname) {
		this.contactname = contactname;
	}
	@Override
	public String toString() {
		return "Mstinfo_rep [checkin=" + checkin + ", checkout=" + checkout
				+ ", contactname=" + contactname + ", hotelid=" + hotelid
				+ ", taobaoorderid=" + taobaoorderid + "]";
	}
	
	
	
	
	
	
}
