package com.dl.utl;

public class VarUtil {

	//入住通下单时验证价格是否与酒店上传的价格一致的接口
	public static final String RZT_VALIDATE_PRICE = "http://register.dlhis.com:8080/ruzhut/CheckPriceServlet";
	//阿拉丁试单
	public static final String ALADIN_VALIDATE_ORDER = "http://dbcenter.dlhis.com:9081/otac/preorder";
	//阿拉丁接收订单
	public static final String ALADIN_ACCEPT_ORDER = "http://dbcenter.dlhis.com:9081/otac/accept";
	//阿拉丁取消订单
	public static final String ALADIN_CANCEL_ORDER = "http://dbcenter.dlhis.com:9081/otac/cancel";
	
//	public static final String RZT_VALIDATE_PRICE = "http://ota-resource.dlhis.com:8080/ruzhut/CheckPriceServlet";
}
