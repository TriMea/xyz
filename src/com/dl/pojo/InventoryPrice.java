package com.dl.pojo;

public class InventoryPrice {

	private String date;
	private Integer price;
	private Integer quota;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Integer getQuota() {
		return quota;
	}
	public void setQuota(Integer quota) {
		this.quota = quota;
	}
	@Override
	public String toString() {
		return "InventoryPrice [date=" + date + ", price=" + price + ", quota="
				+ quota + "]";
	}
	
	
}
