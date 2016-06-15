package com.dl.pojo;

public class Set_roomrate {

	private String hotelid;
	private String bdate;
	private String edate;
	private String rmtype;	
	private String Mon;	
	private String TUE;	
	private String WED;	
	private String TUR;
	private String FRI;	
	private String SAT;
	private String SUN;
	private String quote;
	public  String getHotelid() {
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
	public String getQuote() {
		return quote;
	}
	public void setQuote(String quote) {
		this.quote = quote;
	}
	@Override
	public String toString() {
		return "Set_roomrate [FRI=" + FRI + ", Mon=" + Mon + ", SAT=" + SAT
				+ ", SUN=" + SUN + ", TUE=" + TUE + ", TUR=" + TUR + ", WED="
				+ WED + ", bdate=" + bdate + ", edate=" + edate + ", hotelid="
				+ hotelid + ", quote=" + quote + ", rmtype=" + rmtype + "]";
	}


	
	
	
}
