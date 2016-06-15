package com.dl.pojo;

public class ReceiptAddress {

	private String name;
	private String address;
	private String postCode;
	private String mobile;
	private String phone;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Override
	public String toString() {
		return "ReceiptAddress [address=" + address + ", mobile=" + mobile
				+ ", name=" + name + ", phone=" + phone + ", postCode="
				+ postCode + "]";
	}
	
	
}
