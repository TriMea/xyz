package com.dl.pojo;

public class Hotel_info {
         private String hotelid;
         private String name;
         private int domestic;
         private String city;
         private String address;
         private String tel;
         private String vendor;
         private String floor;
         private int rooms;
         private String description;
         private String pics;
         private String district;
         private String log_date;
		public String getHotelid() {
			return hotelid;
		}
		public void setHotelid(String hotelid) {
			this.hotelid = hotelid;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getDomestic() {
			return domestic;
		}
		public void setDomestic(int domestic) {
			this.domestic = domestic;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getTel() {
			return tel;
		}
		public void setTel(String tel) {
			this.tel = tel;
		}
		public String getVendor() {
			return vendor;
		}
		public void setVendor(String vendor) {
			this.vendor = vendor;
		}
		public String getFloor() {
			return floor;
		}
		public void setFloor(String floor) {
			this.floor = floor;
		}
		public int getRooms() {
			return rooms;
		}
		public void setRooms(int rooms) {
			this.rooms = rooms;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getPics() {
			return pics;
		}
		public void setPics(String pics) {
			this.pics = pics;
		}
		public String getDistrict() {
			return district;
		}
		public void setDistrict(String district) {
			this.district = district;
		}
		public String getLog_date() {
			return log_date;
		}
		public void setLog_date(String logDate) {
			log_date = logDate;
		}
		@Override
		public String toString() {
			return "Hotel_info [address=" + address + ", city=" + city
					+ ", description=" + description + ", district=" + district
					+ ", domestic=" + domestic + ", floor=" + floor
					+ ", hotelid=" + hotelid + ", log_date=" + log_date
					+ ", name=" + name + ", pics=" + pics + ", rooms=" + rooms
					+ ", tel=" + tel + ", vendor=" + vendor + "]";
		}
         
         
         
}
