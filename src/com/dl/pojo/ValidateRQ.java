package com.dl.pojo;

public class ValidateRQ {

	private String hotelid;
	private String roomTypeId;
	private String checkIn;
	private String checkOut;
	private String roomNum;
	private String paymentType;
	private String ratePlan;
	private String mrk;
	
	
	
	public String getHotelid() {
		return hotelid;
	}
	public void setHotelid(String hotelid) {
		this.hotelid = hotelid;
	}
	public String getRoomTypeId() {
		return roomTypeId;
	}
	public void setRoomTypeId(String roomTypeId) {
		this.roomTypeId = roomTypeId;
	}
	public String getCheckIn() {
		return checkIn;
	}
	public void setCheckIn(String checkIn) {
		this.checkIn = checkIn;
	}
	public String getCheckOut() {
		return checkOut;
	}
	public void setCheckOut(String checkOut) {
		this.checkOut = checkOut;
	}
	public String getRoomNum() {
		return roomNum;
	}
	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getRatePlan() {
		return ratePlan;
	}
	public void setRatePlan(String ratePlan) {
		this.ratePlan = ratePlan;
	}
	public String getMrk() {
		return mrk;
	}
	public void setMrk(String mrk) {
		this.mrk = mrk;
	}
	@Override
	public String toString() {
		return "ValidateRQ [checkIn=" + checkIn + ", checkOut=" + checkOut
				+ ", hotelid=" + hotelid + ", mrk=" + mrk + ", paymentType="
				+ paymentType + ", ratePlan=" + ratePlan + ", roomNum="
				+ roomNum + ", roomTypeId=" + roomTypeId + "]";
	}
	
	
	

	
}
