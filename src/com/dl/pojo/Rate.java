package com.dl.pojo;

public class Rate {
 private String bdate;
 private String edate;
 private String price;
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
public String getPrice() {
	return price;
}
public void setPrice(String price) {
	this.price = price;
}
@Override
public String toString() {
	return "Rate [bdate=" + bdate + ", edate=" + edate + ", price=" + price
			+ "]";
}
 
 
}
