package com.dl.pojo;

public class Set_roommap {

	private String hotelid;
	private String bdate;
	private String edate;	
	private String rmtype;	
	private String rp_code;	
	private String channel;
	private String Mon;	
	private String TUE;	
	private String WED;	
	private String TUR;
	private String FRI;	
	private String SAT;
	private String SUN;
	private String status;
	private String type_set;
	private String day_set;
	public  String getHotelid() {
		return hotelid;
	}
	public void setHotelid(String hotelid) {
		this.hotelid = hotelid;
	}
	public String getBdate() {
		return bdate;
	}
	public void setBdate(String bdate) {
		this.bdate = bdate;
	}
	public String getEdate() {
		return edate;
	}
	public void setEdate(String edate) {
		this.edate = edate;
	}
	public String getRmtype() {
		return rmtype;
	}
	public void setRmtype(String rmtype) {
		this.rmtype = rmtype;
	}
	public String getRp_code() {
		return rp_code;
	}
	public void setRp_code(String rpCode) {
		rp_code = rpCode;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getMon() {
		return Mon;
	}
	public void setMon(String mon) {
		Mon = mon;
	}
	public String getTUE() {
		return TUE;
	}
	public void setTUE(String tUE) {
		TUE = tUE;
	}
	public String getWED() {
		return WED;
	}
	public void setWED(String wED) {
		WED = wED;
	}
	public String getTUR() {
		return TUR;
	}
	public void setTUR(String tUR) {
		TUR = tUR;
	}
	public String getFRI() {
		return FRI;
	}
	public void setFRI(String fRI) {
		FRI = fRI;
	}
	public String getSAT() {
		return SAT;
	}
	public void setSAT(String sAT) {
		SAT = sAT;
	}
	public String getSUN() {
		return SUN;
	}
	public void setSUN(String sUN) {
		SUN = sUN;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType_set() {
		return type_set;
	}
	public void setType_set(String typeSet) {
		type_set = typeSet;
	}
	public String getDay_set() {
		return day_set;
	}
	public void setDay_set(String daySet) {
		day_set = daySet;
	}
	@Override
	public String toString() {
		return "Set_roommap [FRI=" + FRI + ", Mon=" + Mon + ", SAT=" + SAT
				+ ", SUN=" + SUN + ", TUE=" + TUE + ", TUR=" + TUR + ", WED="
				+ WED + ", bdate=" + bdate + ", channel=" + channel
				+ ", day_set=" + day_set + ", edate=" + edate + ", hotelid="
				+ hotelid + ", rmtype=" + rmtype + ", rp_code=" + rp_code
				+ ", status=" + status + ", type_set=" + type_set + "]";
	}
	
	
}
