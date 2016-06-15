package com.dl.pojo;

public class Set_roomprice {

	private String hotelid;
	private String rp_bdate;
	private String rp_edate;
	private String rm_bdate;
	private String rm_edate;
	private String rmtype;	
	private String rp_code;	
	private String Mon;	
	private String TUE;	
	private String WED;	
	private String TUR;
	private String FRI;	
	private String SAT;
	private String SUN;
	private String rm_price;
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
	public String getRp_code() {
		return rp_code;
	}
	public void setRp_code(String rpCode) {
		rp_code = rpCode;
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
	public String getRp_bdate() {
		return rp_bdate;
	}
	public void setRp_bdate(String rpBdate) {
		rp_bdate = rpBdate;
	}
	public String getRp_edate() {
		return rp_edate;
	}
	public void setRp_edate(String rpEdate) {
		rp_edate = rpEdate;
	}
	public String getRm_bdate() {
		return rm_bdate;
	}
	public void setRm_bdate(String rmBdate) {
		rm_bdate = rmBdate;
	}
	public String getRm_edate() {
		return rm_edate;
	}
	public void setRm_edate(String rmEdate) {
		rm_edate = rmEdate;
	}
	public String getRm_price() {
		return rm_price;
	}
	public void setRm_price(String rmPrice) {
		rm_price = rmPrice;
	}
	@Override
	public String toString() {
		return "Set_roomprice [FRI=" + FRI + ", Mon=" + Mon + ", SAT=" + SAT
				+ ", SUN=" + SUN + ", TUE=" + TUE + ", TUR=" + TUR + ", WED="
				+ WED + ", hotelid=" + hotelid + ", rm_bdate=" + rm_bdate
				+ ", rm_edate=" + rm_edate + ", rm_price=" + rm_price
				+ ", rmtype=" + rmtype + ", rp_bdate=" + rp_bdate
				+ ", rp_code=" + rp_code + ", rp_edate=" + rp_edate + "]";
	}
	
	
}
