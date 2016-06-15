package com.dl.utl;

import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPPart;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
/*
 * 入住通，生成xml
 */
public class XMLGenerator_RZT {

	
	
	
	private static Logger logger = Logger.getLogger(XMLGenerator_RZT.class); 
	/*
	 * 生成房态xml
	 */
	public static Document  XMLGenerator_NotifRQ(String hotelid,JSONArray ja)
	{
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("OTA_HotelAvailNotifRQ", "http://www.opentravel.org/OTA/2003/05");
		
		root.addNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance").addAttribute("version", "0.0");
		doc.setRootElement(root);
		Element AvailStatusMessages = root.addElement("AvailStatusMessages");
		
		AvailStatusMessages.addAttribute("HotelCode", hotelid);
		
		//多个政策
		for(int i=0;i<ja.size();i++)
		{
			Element asm_e = AvailStatusMessages.addElement("AvailStatusMessage").addAttribute("BookingLimit", null).addAttribute("BookingLimitMessageType", "SetLimit");
//			Element sac =  DocumentHelper.createElement("StatusApplicationControl")
			Element sac = asm_e.addElement("StatusApplicationControl");
			sac.addAttribute("Start", ja.getJSONObject(i).getString("bdate"))
			.addAttribute("End", ja.getJSONObject(i).getString("edate"))
			.addAttribute("InvTypeCode", ja.getJSONObject(i).getString("rmtype"))
			.addAttribute("RatePlanCode", ja.getJSONObject(i).getString("rp_code"))
			.addAttribute("Mon", ja.getJSONObject(i).getString("mon"))
			.addAttribute("Tue", ja.getJSONObject(i).getString("TUE"))
			.addAttribute("Weds", ja.getJSONObject(i).getString("WED"))
			.addAttribute("Thur", ja.getJSONObject(i).getString("TUR"))
			.addAttribute("Fri", ja.getJSONObject(i).getString("FRI"))
			.addAttribute("Sat", ja.getJSONObject(i).getString("SAT"))
			.addAttribute("Sun", ja.getJSONObject(i).getString("SUN"));
			
			Element dsc =  sac.addElement("DestinationSystemCodes");
			dsc.addElement("DestinationSystemCode").setText(ja.getJSONObject(i).getString("channel"));
//			sac.add(dsc);
//			asm_e.add(sac);
			Element los =  asm_e.addElement("LengthsOfStay"); 
			los.addElement("LengthOfStay")
			.addAttribute("MinMaxMessageType",ja.getJSONObject(i).getString("type_set"))
			.addAttribute("Time",ja.getJSONObject(i).getString("day_set"))
			.addAttribute("TimeUnit","Day");
//			asm_e.add(los);
			if(ja.getJSONObject(i).getInt("status")==1)
			{
				asm_e.addElement("RestrictionStatus").addAttribute("Restriction", "Master").addAttribute("Status", "Open");
			}else{
				asm_e.addElement("RestrictionStatus").addAttribute("Restriction", "Master").addAttribute("Status", "Close");
			}
			
			
		}
		
		return doc;
	}
	
	/*
	 * 生成房价xml
	 */
	public static Document  XMLGenerator_RatePlanNotifRQ(String hotelid,JSONArray ja)
	{
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("OTA_HotelRatePlanNotifRQ", "http://www.opentravel.org/OTA/2003/05");
		root.addNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance").addAttribute("version", "0.0");
		doc.setRootElement(root);
		Element RatePlans = root.addElement("RatePlans");
		
		RatePlans.addAttribute("HotelCode", hotelid);
		
		//多个房型和RP的组合
		for(int i=0;i<ja.size();i++)
		{
			Element RatePlan = RatePlans.addElement("RatePlan").addAttribute("RatePlanCode",ja.getJSONObject(i).getString("rp_code"))
			.addAttribute("Start", ja.getJSONObject(i).getString("rp_bdate"))
			.addAttribute("End", ja.getJSONObject(i).getString("rp_edate"))
			.addAttribute("CurrencyCode","CNY");
//			Element sac =  DocumentHelper.createElement("StatusApplicationControl")
			Element Rates = RatePlan.addElement("Rates");
			Element Rate = Rates.addElement("Rate")
			.addAttribute("Start", ja.getJSONObject(i).getString("rm_bdate"))
			.addAttribute("End", ja.getJSONObject(i).getString("rm_edate"))
			.addAttribute("InvTypeCode", ja.getJSONObject(i).getString("rmtype"))
			.addAttribute("Mon", ja.getJSONObject(i).getString("mon"))
			.addAttribute("Tue", ja.getJSONObject(i).getString("TUE"))
			.addAttribute("Weds", ja.getJSONObject(i).getString("WED"))
			.addAttribute("Thur", ja.getJSONObject(i).getString("TUR"))
			.addAttribute("Fri", ja.getJSONObject(i).getString("FRI"))
			.addAttribute("Sat", ja.getJSONObject(i).getString("SAT"))
			.addAttribute("Sun", ja.getJSONObject(i).getString("SUN"))
			.addAttribute("RateTimeUnit", "Day")
			.addAttribute("CurrencyCode", "CNY");
			
			Element BaseByGuestAmts =  Rate.addElement("BaseByGuestAmts");
			BaseByGuestAmts.addElement("BaseByGuestAmt")
			.addAttribute("NumberOfGuests","1")
			.addAttribute("AgeQualifyingCode","10")
			.addAttribute("AmountBeforeTax",ja.getJSONObject(i).getString("rm_price"))
			.addAttribute("AmountAfterTax",ja.getJSONObject(i).getString("rm_price"));
			BaseByGuestAmts.addElement("BaseByGuestAmt")
			.addAttribute("NumberOfGuests","2")
			.addAttribute("AgeQualifyingCode","10")
			.addAttribute("AmountBeforeTax",ja.getJSONObject(i).getString("rm_price"))
			.addAttribute("AmountAfterTax",ja.getJSONObject(i).getString("rm_price"));
			BaseByGuestAmts.addElement("BaseByGuestAmt")
			.addAttribute("NumberOfGuests","3")
			.addAttribute("AgeQualifyingCode","10")
			.addAttribute("AmountBeforeTax",ja.getJSONObject(i).getString("rm_price"))
			.addAttribute("AmountAfterTax",ja.getJSONObject(i).getString("rm_price"));
			BaseByGuestAmts.addElement("BaseByGuestAmt")
			.addAttribute("NumberOfGuests","4")
			.addAttribute("AgeQualifyingCode","10")
			.addAttribute("AmountBeforeTax",ja.getJSONObject(i).getString("rm_price"))
			.addAttribute("AmountAfterTax",ja.getJSONObject(i).getString("rm_price"));
//			
			
			
		}
		
		return doc;
	}
	
	
	
	/*
	 * 生成实时房量xml
	 */
	public static Document  XMLGenerator_InvCountNotifRQ(String hotelid,JSONArray ja)
	{
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("OTA_HotelInvCountNotifRQ", "http://www.opentravel.org/OTA/2003/05");
//		root.addNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance")
		root.addAttribute("version", "0.0")
		.addAttribute("TimeStamp", SoapUtl.getTimeStamp())
		.addAttribute("EchoToken", "25e86d37-bebd-4da4-8cd9-bfe13aa83b8c")
		.addAttribute("Target", "Production")
		;
		doc.setRootElement(root);
		Element Inventories = root.addElement("Inventories");
		
		Inventories.addAttribute("HotelCode", hotelid);
		
		//多个房型
		for(int i=0;i<ja.size();i++)
		{
			Element Inventory = Inventories.addElement("Inventory");
			Inventory.addElement("StatusApplicationControl")
			.addAttribute("Start", ja.getJSONObject(i).getString("bdate"))
			.addAttribute("End", ja.getJSONObject(i).getString("edate"))
			.addAttribute("InvTypeCode", ja.getJSONObject(i).getString("rmtype"))
			.addAttribute("Mon", ja.getJSONObject(i).getString("mon"))
			.addAttribute("Tue", ja.getJSONObject(i).getString("TUE"))
			.addAttribute("Weds", ja.getJSONObject(i).getString("WED"))
			.addAttribute("Thur", ja.getJSONObject(i).getString("TUR"))
			.addAttribute("Fri", ja.getJSONObject(i).getString("FRI"))
			.addAttribute("Sat", ja.getJSONObject(i).getString("SAT"))
			.addAttribute("Sun", ja.getJSONObject(i).getString("SUN"));
			Element InvCounts = Inventory.addElement("InvCounts");
			InvCounts.addElement("InvCount")
			.addAttribute("CountType", "2")
			.addAttribute("Count", ja.getJSONObject(i).getString("quote"));
			
			
		}
		
		return doc;
	}
	
	
	/*
	 * 生成反馈error
	 */
	public static Document  XMLGenerator_Res_Error(String error_content,String responseType)
	{
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("OTA_HotelResRS", "http://www.opentravel.org/OTA/2003/05");
//		root.addNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance")
		root.addAttribute("version", "0.0")
		.addAttribute("TimeStamp", SoapUtl.getTimeStamp())
		.addAttribute("EchoToken", "25e86d37-bebd-4da4-8cd9-bfe13aa83b8c")
		.addAttribute("ResResponseType",responseType)
		;
		//"Committed"
		doc.setRootElement(root);
		Element Errors = root.addElement("Errors");
		Element Error = Errors.addElement("Error");
		Error.addText(error_content);
		return doc;
	}
	
	
	/*
	 * 生成反馈SUCCESS
	 */
	public static Document  XMLGenerator_Res_SUCCESS(String mrk,JSONObject response,String pmsresid ,String responseType )
	{
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("OTA_HotelResRS", "http://www.opentravel.org/OTA/2003/05");
//		root.addNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance")
		root.addAttribute("version", "0.0")
		.addAttribute("TimeStamp", SoapUtl.getTimeStamp())
		.addAttribute("EchoToken", "25e86d37-bebd-4da4-8cd9-bfe13aa83b8c")
		.addAttribute("ResResponseType",responseType);
		doc.setRootElement(root);
		root.addElement("Success");
		Element HotelReservationIDs = root.addElement("HotelReservations")
		.addElement("HotelReservation")
		.addElement("ResGlobalInfo")
		.addElement("HotelReservationIDs");
		HotelReservationIDs.addElement("HotelReservationID")
		.addAttribute("ResID_Type", "24")
		.addAttribute("ResID_Value", response.getString("taoBaoOrderId"))
		.addAttribute("ResID_Source", mrk);
		HotelReservationIDs.addElement("HotelReservationID")
		.addAttribute("ResID_Type","10")
		.addAttribute("ResID_Value",pmsresid)
		.addAttribute("ResID_Source","DL_PMS");
		
		return doc;
	}
	
	
	/*
	 * 生成取消反馈SUCCESS
	 */
	public static Document  XMLGenerator_Cancel_Res_SUCCESS(String mrk,JSONObject response,String responseType )
	{
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("OTA_HotelResRS", "http://www.opentravel.org/OTA/2003/05");
//		root.addNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance")
		root.addAttribute("version", "0.0")
		.addAttribute("TimeStamp", SoapUtl.getTimeStamp())
		.addAttribute("EchoToken", "25e86d37-bebd-4da4-8cd9-bfe13aa83b8c")
		.addAttribute("ResResponseType",responseType);
		doc.setRootElement(root);
		root.addElement("Success");
		Element HotelReservationIDs = root.addElement("HotelReservations")
		.addElement("HotelReservation")
		.addElement("ResGlobalInfo")
		.addElement("HotelReservationIDs");
		HotelReservationIDs.addElement("HotelReservationID")
		.addAttribute("ResID_Type", "24")
		.addAttribute("ResID_Value", response.getString("taoBaoOrderId"))
		.addAttribute("ResID_Source", mrk);
		HotelReservationIDs.addElement("HotelReservationID")
		.addAttribute("ResID_Type", "10")
		.addAttribute("ResID_Value", response.getString("pmsresid"))
		.addAttribute("ResID_Source", "DL_PMS");
		
		return doc;
	}
}
