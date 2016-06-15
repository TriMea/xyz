package com.dl.utl;


import java.net.URL;

import org.apache.log4j.Logger;
import org.codehaus.xfire.client.Client;

public class SmsUtil {

private static Logger logger = Logger.getLogger(SmsUtil.class);


	
	
	
	public static void sendSmsMessage(String smsAccnt,String smsPwd,String phone,String content,String hotelid,String hotelname)
	{
		Object[] results;
		Client client = null;
		try {
			client = new Client(new URL(
			"http://ask.i-hotel.cn:30015/DlSmsService.asmx?wsdl"));
//			results = client.invoke("Sms_Submit", new Object[] {
//					"dlhis", "419xtd", "13456831133", "信用住短信测试"});
			results = client.invoke("Sms_Submit", new Object[] {
					smsAccnt, smsPwd, phone, content});
			logger.info("向"+hotelname+"["+hotelid+"#"+phone+"]发送离线提醒短信");
			logger.info("短信提交结果："+results[0]);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}finally{
			
			if(client!=null)
			{
				client.close();
				client = null;
			}
		}
		
	}
}
