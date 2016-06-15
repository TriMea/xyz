package com.dl.pojo;

public class DailyInfo {

	private String day;
	private String price;
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	@Override
	public String toString() {
		return "DailyInfo [day=" + day + ", price=" + price + "]";
	}
	
	
}
