package com.dl.pojo;

import java.io.Serializable;

public class OTACancelRQ implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String hotelid;
	private String taoBaoOrderId;
	private String pmsresid;
	private String mrk;
	
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
	
	public String getPmsresid() {
		return pmsresid;
	}
	public void setPmsresid(String pmsresid) {
		this.pmsresid = pmsresid;
	}
	
	
	public String getMrk() {
		return mrk;
	}
	public void setMrk(String mrk) {
		this.mrk = mrk;
	}
	@Override
	public String toString() {
		return "OTACancelRQ [hotelid=" + hotelid + ", mrk=" + mrk
				+ ", pmsresid=" + pmsresid + ", taoBaoOrderId=" + taoBaoOrderId
				+ "]";
	}
	
	
	
	
}
