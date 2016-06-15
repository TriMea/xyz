package com.dl.pojo;

public class RpCode {
private String rpcode;
private String rpcode_des;
public String getRpcode() {
	return rpcode;
}
public void setRpcode(String rpcode) {
	this.rpcode = rpcode;
}
public String getRpcode_des() {
	return rpcode_des;
}
public void setRpcode_des(String rpcodeDes) {
	rpcode_des = rpcodeDes;
}
@Override
public String toString() {
	return "RpCode [rpcode=" + rpcode + ", rpcode_des=" + rpcode_des + "]";
}


}
