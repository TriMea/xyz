package com.dl.pojo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class BookRQ {

	private String taoBaoOrderId;
	private String hotelid;
	private String roomTypeId;
	private String ratePlanCode;
	private String roomNum;
	private String orderStatus;
	private String checkIn;
	private String checkOut;
	private String totalPrice;
	private String paymentType;
	private String contactName;
	private String contactTel;
	private JSONArray dailyInfos;
	private JSONArray orderGuests;
	private String mkt;
	private String remark;
	private String orderType;
	private JSONObject receiptInfo;
	private String log_date;
	private String fakeaccnt;
	private String guaranteetype;
	private String latestarrivetime;
	public String getTaoBaoOrderId() {
		return taoBaoOrderId;
	}
	public void setTaoBaoOrderId(String taoBaoOrderId) {
		this.taoBaoOrderId = taoBaoOrderId;
	}
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
	public String getRatePlanCode() {
		return ratePlanCode;
	}
	public void setRatePlanCode(String ratePlanCode) {
		this.ratePlanCode = ratePlanCode;
	}
	public String getRoomNum() {
		return roomNum;
	}
	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
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
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactTel() {
		return contactTel;
	}
	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}
	
	public String getMkt() {
		return mkt;
	}
	public void setMkt(String mkt) {
		this.mkt = mkt;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	public String getLog_date() {
		return log_date;
	}
	public void setLog_date(String logDate) {
		log_date = logDate;
	}
	public JSONArray getDailyInfos() {
		return dailyInfos;
	}
	public void setDailyInfos(JSONArray dailyInfos) {
		this.dailyInfos = dailyInfos;
	}
	public JSONArray getOrderGuests() {
		return orderGuests;
	}
	public void setOrderGuests(JSONArray orderGuests) {
		this.orderGuests = orderGuests;
	}
	public JSONObject getReceiptInfo() {
		return receiptInfo;
	}
	public void setReceiptInfo(JSONObject receiptInfo) {
		this.receiptInfo = receiptInfo;
	}
	
	
	public String getFakeaccnt() {
		return fakeaccnt;
	}
	public void setFakeaccnt(String fakeaccnt) {
		this.fakeaccnt = fakeaccnt;
	}
	public String getGuaranteetype() {
		return guaranteetype;
	}
	public void setGuaranteetype(String guaranteetype) {
		this.guaranteetype = guaranteetype;
	}
	public String getLatestarrivetime() {
		return latestarrivetime;
	}
	public void setLatestarrivetime(String latestarrivetime) {
		this.latestarrivetime = latestarrivetime;
	}
	@Override
	public String toString() {
		return "BookRQ [checkIn=" + checkIn + ", checkOut=" + checkOut
				+ ", contactName=" + contactName + ", contactTel=" + contactTel
				+ ", dailyInfos=" + dailyInfos + ", fakeaccnt=" + fakeaccnt
				+ ", guaranteetype=" + guaranteetype + ", hotelid=" + hotelid
				+ ", latestarrivetime=" + latestarrivetime + ", log_date="
				+ log_date + ", mkt=" + mkt + ", orderGuests=" + orderGuests
				+ ", orderStatus=" + orderStatus + ", orderType=" + orderType
				+ ", paymentType=" + paymentType + ", ratePlanCode="
				+ ratePlanCode + ", receiptInfo=" + receiptInfo + ", remark="
				+ remark + ", roomNum=" + roomNum + ", roomTypeId="
				+ roomTypeId + ", taoBaoOrderId=" + taoBaoOrderId
				+ ", totalPrice=" + totalPrice + "]";
	}
	
	
	
	
	
	
	
}
