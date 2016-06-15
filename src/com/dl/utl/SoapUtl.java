package com.dl.utl;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.w3c.dom.NodeList;

import com.dl.pojo.DailyInfo;
import com.dl.pojo.OTABookRQ;
import com.dl.pojo.OTACancelRQ;
import com.dl.pojo.OTAUpdateRQ;


public class SoapUtl {

	private static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd\'T\'hh:ss:mm.SSS");
	/*
	 * 用于接收soap响应
	 * 将字符串转换成SOAPMessage
	 */
	 private static SOAPMessage formatSoapString(String soapString) {
	        MessageFactory msgFactory;
	        try {
	            msgFactory = MessageFactory.newInstance();
	            SOAPMessage reqMsg = msgFactory.createMessage(new MimeHeaders(),
	                    new ByteArrayInputStream(soapString.getBytes("UTF-8")));
	            reqMsg.saveChanges();
	            return reqMsg;
	        } catch (Exception e) {
	        	
	            e.printStackTrace();
	            return null;
	        }
	    }
	 
	 
	 public static JSONObject soap2OTABookRQ(SOAPBody body)
	 {
		    OTABookRQ otaBookRQ = new OTABookRQ();
			otaBookRQ.setTaoBaoOrderId(body.getElementsByTagName("HotelReservationID").item(0).getAttributes().getNamedItem("ResID_Value").getNodeValue());
			otaBookRQ.setHotelid(body.getElementsByTagName("BasicPropertyInfo").item(0).getAttributes().getNamedItem("HotelCode").getNodeValue());
			NodeList n_RoomRate = body.getElementsByTagName("RoomRate");
			otaBookRQ.setRoomTypeId(n_RoomRate.item(0).getAttributes().getNamedItem("RoomTypeCode").getNodeValue());
			otaBookRQ.setRatePlanCode(n_RoomRate.item(0).getAttributes().getNamedItem("RatePlanCode").getNodeValue());
			otaBookRQ.setRoomNum(n_RoomRate.item(0).getAttributes().getNamedItem("NumberOfUnits").getNodeValue());
			otaBookRQ.setPaymentType(n_RoomRate.item(0).getAttributes().getNamedItem("RatePlanCategory").getNodeValue());
			NodeList n_Rate = body.getElementsByTagName("Rate");
			NodeList n_Base = body.getElementsByTagName("Base");
			List<DailyInfo> list = new ArrayList<DailyInfo>();
			for (int i = 0; i < n_Rate.getLength(); i++) {
				DailyInfo dailyInfo = new DailyInfo();
	        	 dailyInfo.setDay(n_Rate.item(i).getAttributes().getNamedItem("EffectiveDate").getNodeValue());
	        	 dailyInfo.setPrice(n_Base.item(i).getAttributes().getNamedItem("AmountAfterTax").getNodeValue());
	        	 list.add(dailyInfo);
			}
			otaBookRQ.setDailyInfos(JSONArray.fromObject(list));
			NodeList n_TimeSpan = body.getElementsByTagName("TimeSpan");
			otaBookRQ.setCheckIn(n_TimeSpan.item(0).getAttributes().getNamedItem("Start").getNodeValue());
			otaBookRQ.setCheckOut(n_TimeSpan.item(0).getAttributes().getNamedItem("End").getNodeValue());
			NodeList n_ResGuestRPHs = body.getElementsByTagName("ResGuestRPHs");
			otaBookRQ.setGuestnum(n_ResGuestRPHs.item(0).getTextContent());
			NodeList n_Total = body.getElementsByTagName("Total");
			otaBookRQ.setTotalPrice(n_Total.item(0).getAttributes().getNamedItem("AmountAfterTax").getNodeValue());
			NodeList n_PersonName = body.getElementsByTagName("PersonName");
			NodeList n_Surname = body.getElementsByTagName("Surname");
			NodeList n_GivenName = body.getElementsByTagName("GivenName");
			List<Map<String, String>> list_name = new ArrayList<Map<String,String>>();
			for (int i = 0; i < n_PersonName.getLength(); i++) {
				Map<String, String> mp = new HashMap<String,String>();
//				n_PersonName.item(i).getChildNodes().item(3).getTextContent();
				mp.put("name", n_Surname.item(i).getTextContent()+n_GivenName.item(i).getTextContent());
				System.out.println("姓名："+ n_Surname.item(i).getTextContent()+n_GivenName.item(i).getTextContent());
				list_name.add(mp);
			}
			otaBookRQ.setContactName(JSONArray.fromObject(list_name).toString());
			otaBookRQ.setOrderGuests(JSONArray.fromObject(list_name));
			NodeList n_RequestorID = body.getElementsByTagName("RequestorID");
			if(n_RequestorID.item(0).getAttributes().getNamedItem("ID")==null)
			{
				otaBookRQ.setMkt("Ctrip");
			}else{
				otaBookRQ.setMkt(n_RequestorID.item(0).getAttributes().getNamedItem("ID").getNodeValue());
			}
			
			
			
			System.out.println("commit");
			NodeList n_Text = body.getElementsByTagName("Text");
			StringBuilder sb_text = new StringBuilder();
			for (int i = 0; i < n_Text.getLength(); i++) {
				
				sb_text.append(n_Text.item(i).getTextContent());
			}
			otaBookRQ.setRemark(sb_text.toString());
			NodeList n_TransportInfo = body.getElementsByTagName("TransportInfo");
			otaBookRQ.setLatestarrivetime(n_TransportInfo.item(0).getAttributes().getNamedItem("Time").getNodeValue().replace("T", " "));
			NodeList n_Guarantee = body.getElementsByTagName("Guarantee");
//			System.out.println(n_Guarantee.item(0));
			if(n_Guarantee.item(0)!=null)
			{
				otaBookRQ.setGuaranteetype(n_Guarantee.item(0).getAttributes().getNamedItem("GuaranteeType").getNodeValue());
			}else{
				otaBookRQ.setGuaranteetype("None");
			}
			
			return JSONObject.fromObject(otaBookRQ);
	 }
	 
	 public static JSONObject soap2OTACancelRQ(SOAPBody body)
	 {
		    OTACancelRQ otaCancelRQ = new OTACancelRQ();
		
		    if(body.getElementsByTagName("RequestorID").item(0).getAttributes().getNamedItem("ID")==null)
		    {
		    	otaCancelRQ.setMrk("Ctrip");
		    }else{
		    	 String mrk = body.getElementsByTagName("RequestorID").item(0).getAttributes().getNamedItem("ID").getNodeValue();
				 otaCancelRQ.setMrk(mrk);
		    }
		   
		    NodeList n_HotelReservationID = body.getElementsByTagName("HotelReservationID");
		    for (int i = 0; i < n_HotelReservationID.getLength(); i++) {
				String ResID_Type = n_HotelReservationID.item(i).getAttributes().getNamedItem("ResID_Type").getNodeValue();
				if("10".equals(ResID_Type.trim()))
				{
					//PMS确认单号
					otaCancelRQ.setPmsresid(n_HotelReservationID.item(i).getAttributes().getNamedItem("ResID_Value").getNodeValue());
				}else if("24".equals(ResID_Type.trim()))
				{
					//订单号
					otaCancelRQ.setTaoBaoOrderId(n_HotelReservationID.item(i).getAttributes().getNamedItem("ResID_Value").getNodeValue());
				}
			}
		    otaCancelRQ.setHotelid(body.getElementsByTagName("BasicPropertyInfo").item(0).getAttributes().getNamedItem("HotelCode").getNodeValue());
			return JSONObject.fromObject(otaCancelRQ);
	 }
	 
	 
	 public static JSONObject soap2OTAUpdateRQ(SOAPBody body)
	 {
		    OTAUpdateRQ otaBookRQ = new OTAUpdateRQ();
		    NodeList n_HotelReservationID = body.getElementsByTagName("HotelReservationID");
		    for (int i = 0; i < n_HotelReservationID.getLength(); i++) {
				String ResID_Type = n_HotelReservationID.item(i).getAttributes().getNamedItem("ResID_Type").getNodeValue();
				if("10".equals(ResID_Type.trim()))
				{
					//PMS确认单号
					otaBookRQ.setPmsresid(n_HotelReservationID.item(i).getAttributes().getNamedItem("ResID_Value").getNodeValue());
				}else if("24".equals(ResID_Type.trim()))
				{
					//新订单号
					otaBookRQ.setTaoBaoOrderId(n_HotelReservationID.item(i).getAttributes().getNamedItem("ResID_Value").getNodeValue());
				}
			}
//			otaBookRQ.setTaoBaoOrderId(body.getElementsByTagName("HotelReservationID").item(1).getAttributes().getNamedItem("ResID_Value").getNodeValue());
			otaBookRQ.setHotelid(body.getElementsByTagName("BasicPropertyInfo").item(0).getAttributes().getNamedItem("HotelCode").getNodeValue());
			NodeList n_RoomRate = body.getElementsByTagName("RoomRate");
			otaBookRQ.setRoomTypeId(n_RoomRate.item(0).getAttributes().getNamedItem("RoomTypeCode").getNodeValue());
			otaBookRQ.setRatePlanCode(n_RoomRate.item(0).getAttributes().getNamedItem("RatePlanCode").getNodeValue());
			otaBookRQ.setRoomNum(n_RoomRate.item(0).getAttributes().getNamedItem("NumberOfUnits").getNodeValue());
			otaBookRQ.setPaymentType(n_RoomRate.item(0).getAttributes().getNamedItem("RatePlanCategory").getNodeValue());
			NodeList n_Rate = body.getElementsByTagName("Rate");
			NodeList n_Base = body.getElementsByTagName("Base");
			List<DailyInfo> list = new ArrayList<DailyInfo>();
			for (int i = 0; i < n_Rate.getLength(); i++) {
				DailyInfo dailyInfo = new DailyInfo();
	        	 dailyInfo.setDay(n_Rate.item(i).getAttributes().getNamedItem("EffectiveDate").getNodeValue());
	        	 dailyInfo.setPrice(n_Base.item(i).getAttributes().getNamedItem("AmountAfterTax").getNodeValue());
	        	 list.add(dailyInfo);
			}
			otaBookRQ.setDailyInfos(JSONArray.fromObject(list));
			NodeList n_TimeSpan = body.getElementsByTagName("TimeSpan");
			otaBookRQ.setCheckIn(n_TimeSpan.item(0).getAttributes().getNamedItem("Start").getNodeValue());
			otaBookRQ.setCheckOut(n_TimeSpan.item(0).getAttributes().getNamedItem("End").getNodeValue());
			NodeList n_ResGuestRPHs = body.getElementsByTagName("ResGuestRPHs");
			otaBookRQ.setGuestnum(n_ResGuestRPHs.item(0).getTextContent());
			NodeList n_Total = body.getElementsByTagName("Total");
			otaBookRQ.setTotalPrice(n_Total.item(0).getAttributes().getNamedItem("AmountAfterTax").getNodeValue());
			NodeList n_PersonName = body.getElementsByTagName("PersonName");
			NodeList n_Surname = body.getElementsByTagName("Surname");
			NodeList n_GivenName = body.getElementsByTagName("GivenName");
			List<Map<String, String>> list_name = new ArrayList<Map<String,String>>();
			for (int i = 0; i < n_PersonName.getLength(); i++) {
				Map<String, String> mp = new HashMap<String,String>();
//				n_PersonName.item(i).getChildNodes().item(3).getTextContent();
				mp.put("name", n_Surname.item(i).getTextContent()+n_GivenName.item(i).getTextContent());
				list_name.add(mp);
			}
			otaBookRQ.setContactName(JSONArray.fromObject(list_name).toString());
			otaBookRQ.setOrderGuests(JSONArray.fromObject(list_name));
			NodeList n_RequestorID = body.getElementsByTagName("RequestorID");
			if(n_RequestorID.item(0).getAttributes().getNamedItem("ID")==null)
			{
				otaBookRQ.setMkt("Ctrip");
			}else{
				otaBookRQ.setMkt(n_RequestorID.item(0).getAttributes().getNamedItem("ID").getNodeValue());
			}
			
			
			
			System.out.println("commit");
			NodeList n_Text = body.getElementsByTagName("Text");
			StringBuilder sb_text = new StringBuilder();
			for (int i = 0; i < n_Text.getLength(); i++) {
				
				sb_text.append(n_Text.item(i).getTextContent());
			}
			otaBookRQ.setRemark(sb_text.toString());
			NodeList n_TransportInfo = body.getElementsByTagName("TransportInfo");
			otaBookRQ.setLatestarrivetime(n_TransportInfo.item(0).getAttributes().getNamedItem("Time").getNodeValue().replace("T", " "));
			NodeList n_Guarantee = body.getElementsByTagName("Guarantee");
			otaBookRQ.setGuaranteetype(n_Guarantee.item(0).getAttributes().getNamedItem("GuaranteeType").getNodeValue());
			return JSONObject.fromObject(otaBookRQ);
	 }
	 /*
	  * 用于接收soap响应
	  * 从字符串获取SOAPBody
	  */
	  public static SOAPBody str2soapbody(String soapString) throws SOAPException {
		  
		  SOAPMessage msg = formatSoapString(soapString);
	       return msg.getSOAPBody();
	   
	    }
	  
	  
	  /*
	   * 获取当前时间戳
	   */
	  		public static  String getTimeStamp()
	  		{
	  			
	  			return sf.format(new Date());
	  		}
//	public static SOAPPart initSoapPart() throws SOAPException {  
//		  
//        SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();  
//  
//        SOAPPart soapPart = soapMessage.getSOAPPart();  
//  
//        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();  
//        SOAPBody soapBody = soapEnvelope.getBody(); 
//        SOAPElement cwmp = soapEnvelope.addNamespaceDeclaration("soap",  
//                "http://schemas.xmlsoap.org/soap/envelope/");  
//        SOAPElement xsi = soapEnvelope.addNamespaceDeclaration("xsi",  
//                "http://www.w3.org/2001/XMLSchema-instance");  
//        SOAPElement xsd = soapEnvelope.addNamespaceDeclaration("xsd",  
//                "http://www.w3.org/2001/XMLSchema");
//  
////        SOAPElement id = soapBody.addChildElement("lyy"); 
////        id.setTextContent("this is a test!");  
//        return soapPart;  
//    }  
	
}
