package com.dl.pojo;

public class Advertisement {

	private String name;
	private String url;
	private String max_zf;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getMax_zf() {
		return max_zf;
	}
	public void setMax_zf(String maxZf) {
		max_zf = maxZf;
	}
	@Override
	public String toString() {
		return "Advertisement [max_zf=" + max_zf + ", name=" + name + ", url="
				+ url + "]";
	}
	
}
