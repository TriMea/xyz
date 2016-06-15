package com.dl.pojo;

public class RmType {

	private String rmtype;
	private String rmtype_des;
	public String getRmtype() {
		return rmtype;
	}
	public void setRmtype(String rmtype) {
		this.rmtype = rmtype;
	}
	public String getRmtype_des() {
		return rmtype_des;
	}
	public void setRmtype_des(String rmtypeDes) {
		rmtype_des = rmtypeDes;
	}
	@Override
	public String toString() {
		return "RmType [rmtype=" + rmtype + ", rmtype_des=" + rmtype_des + "]";
	}
	
	
}
