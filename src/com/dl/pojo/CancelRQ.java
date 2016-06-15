package com.dl.pojo;

import java.io.Serializable;

public class CancelRQ implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String hotelid;
	private String taoBaoOrderId;
	private String mkt;
	private String reason;
	public String getHotelid() {
		return hotelid;
	}
	public void setHotelid(String hotelid) {
		this.hotelid = hotelid;
	}
	public String getTaoBaoOrderId() {
		return taoBaoOrderId;
	}
	public void setTaoBaoOrderId(String taoBaoOrderId) {
		this.taoBaoOrderId = taoBaoOrderId;
	}
	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getMkt() {
		return mkt;
	}
	public void setMkt(String mkt) {
		this.mkt = mkt;
	}
	@Override
	public String toString() {
		return "CancelRQ [hotelid=" + hotelid + ", mkt=" + mkt + ", reason="
				+ reason + ", taoBaoOrderId=" + taoBaoOrderId + "]";
	}
	
	
	
}
