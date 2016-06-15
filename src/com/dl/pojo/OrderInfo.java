package com.dl.pojo;

public class OrderInfo {

	private String taobaoid;
	private String hotelid;
	private String mrk;
	private String latestarrivetime;
	public String getTaobaoid() {
		return taobaoid;
	}
	public void setTaobaoid(String taobaoid) {
		this.taobaoid = taobaoid;
	}
	public String getHotelid() {
		return hotelid;
	}
	public void setHotelid(String hotelid) {
		this.hotelid = hotelid;
	}
	public String getMrk() {
		return mrk;
	}
	public void setMrk(String mrk) {
		this.mrk = mrk;
	}
	
	
	public String getLatestarrivetime() {
		return latestarrivetime;
	}
	public void setLatestarrivetime(String latestarrivetime) {
		this.latestarrivetime = latestarrivetime;
	}
	@Override
	public String toString() {
		return "OrderInfo [hotelid=" + hotelid + ", latestarrivetime="
				+ latestarrivetime + ", mrk=" + mrk + ", taobaoid=" + taobaoid
				+ "]";
	}
	
	
	
	
}
