package com.dl.pojo;

public class Rateplan {
         private String rateplan_code;
         private String hotelid;
         private String name;
         private int payment_type;
         private int breakfast_count;
         private String cancel_policy;
         private int status;
         private String vendor;
		public String getRateplan_code() {
			return rateplan_code;
		}
		public void setRateplan_code(String rateplanCode) {
			rateplan_code = rateplanCode;
		}
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
		public int getPayment_type() {
			return payment_type;
		}
		public void setPayment_type(int paymentType) {
			payment_type = paymentType;
		}
		public int getBreakfast_count() {
			return breakfast_count;
		}
		public void setBreakfast_count(int breakfastCount) {
			breakfast_count = breakfastCount;
		}
		public String getCancel_policy() {
			return cancel_policy;
		}
		public void setCancel_policy(String cancelPolicy) {
			cancel_policy = cancelPolicy;
		}
		public int getStatus() {
			return status;
		}
		public void setStatus(int status) {
			this.status = status;
		}
		public String getVendor() {
			return vendor;
		}
		public void setVendor(String vendor) {
			this.vendor = vendor;
		}
		@Override
		public String toString() {
			return "Rateplan [breakfast_count=" + breakfast_count
					+ ", cancel_policy=" + cancel_policy + ", hotelid="
					+ hotelid + ", name=" + name + ", payment_type="
					+ payment_type + ", rateplan_code=" + rateplan_code
					+ ", status=" + status + ", vendor=" + vendor + "]";
		}
         
         
}
