package com.dl.pojo;

public class Rm_type {
        private String roomtype_id;
        private String name;
        private int max_occupancy;
        private String internet;
        private int window_type;
        private String hotelid;
        private String vendor;
		public String getRoomtype_id() {
			return roomtype_id;
		}
		public void setRoomtype_id(String roomtypeId) {
			roomtype_id = roomtypeId;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getMax_occupancy() {
			return max_occupancy;
		}
		public void setMax_occupancy(int maxOccupancy) {
			max_occupancy = maxOccupancy;
		}
		public String getInternet() {
			return internet;
		}
		public void setInternet(String internet) {
			this.internet = internet;
		}
		public int getWindow_type() {
			return window_type;
		}
		public void setWindow_type(int windowType) {
			window_type = windowType;
		}
		public String getHotelid() {
			return hotelid;
		}
		public void setHotelid(String hotelid) {
			this.hotelid = hotelid;
		}
		public String getVendor() {
			return vendor;
		}
		public void setVendor(String vendor) {
			this.vendor = vendor;
		}
		@Override
		public String toString() {
			return "Rm_type [hotelid=" + hotelid + ", internet=" + internet
					+ ", max_occupancy=" + max_occupancy + ", name=" + name
					+ ", roomtype_id=" + roomtype_id + ", vendor=" + vendor
					+ ", window_type=" + window_type + "]";
		}
        
        
}
