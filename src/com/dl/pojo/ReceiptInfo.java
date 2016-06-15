package com.dl.pojo;

public class ReceiptInfo {

	private String receiptTitle;
	private String receiptType;
	private ReceiptAddress receiptAddress;
	public String getReceiptTitle() {
		return receiptTitle;
	}
	public void setReceiptTitle(String receiptTitle) {
		this.receiptTitle = receiptTitle;
	}
	public String getReceiptType() {
		return receiptType;
	}
	public void setReceiptType(String receiptType) {
		this.receiptType = receiptType;
	}
	public ReceiptAddress getReceiptAddress() {
		return receiptAddress;
	}
	public void setReceiptAddress(ReceiptAddress receiptAddress) {
		this.receiptAddress = receiptAddress;
	}
	@Override
	public String toString() {
		return "ReceiptInfo [receiptAddress=" + receiptAddress
				+ ", receiptTitle=" + receiptTitle + ", receiptType="
				+ receiptType + "]";
	}
	
	
}
