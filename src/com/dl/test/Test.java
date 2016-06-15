package com.dl.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.soap.SOAPMessage;

import org.dom4j.Element;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;




import com.dl.pojo.DailyInfo;
import com.dl.pojo.OrderGuest;
import com.dl.pojo.ReceiptAddress;
import com.dl.pojo.ReceiptInfo;
import com.dl.pojo.Set_roommap;
import com.dl.utl.CommonTool;
import com.dl.utl.HttpUtil;
import com.dl.utl.XMLGenerator_RZT;
import com.show.api.ShowApiRequest;
import com.thoughtworks.xstream.XStream;

public class Test {

	private static String xml = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>"+
"<BookRQ><AuthenticationToken><CreateToken>taobao193910410955113-1449564155187</CreateToken></AuthenticationToken>"+
    "<TaoBaoOrderId>193910410955113</TaoBaoOrderId><TaoBaoHotelId>172 82415491</TaoBaoHotelId><HotelId>dlhis123</HotelId>"+
    "<TaoBaoRoomTypeId>35991004491</TaoBaoRoomTypeId><RoomTypeId>dlhis123_BJ</RoomTypeId><TaoBaoRatePlanId>4511002491</TaoBaoRatePlanId>"+
    "<RatePlanCode>RP100031</RatePlanCode>"+
    "<TaoBaoGid>1241500449 1</TaoBaoGid>"+
    "<CheckIn>2015-12-09</CheckIn>"+
    "<CheckOut>2015-12-11</CheckOut>"+
    "<EarliestArriveTime>2015-12-09 16:00:00</EarliestArriveTime><LatestArriveTime>2015-12-0 9 17:00:00</LatestArriveTime><RoomNum>1</RoomNum><TotalPrice>73600</TotalPrice>"+
    "<Currency>CNY</Currency><ContactName>张丽莉</ContactName><ContactTel>15821158772</ContactTel><PaymentType>6</PaymentType><DailyInfos>"+
    "<DailyInfo><Day>2015-12-09</Day><Price>36800</Price></DailyInfo><DailyInfo><Day>2015-12-10</Day><Price>36800</Price></DailyInfo></DailyInfos>"+
    "<OrderGuests><OrderGuest><Name>张丽莉</Name><RoomPos>1</RoomPos></OrderGuest></OrderGuests>"+
    "<Comment>该用户参与了阿里旅行2015信 用住产品立减营销活动。</Comment>"+
    "<Extensions>{\"CreateOrderValidateKey\":\"\",\"rpType \":null,\"taobaoGuaranteeType\":\"0\",\"occupancy\":\"2\"}</Extensions>"
    +"</BookRQ>";
	private static JSONObject xml2json_order(Element root)
	{
		JSONObject jo = new JSONObject();
		
		jo.put("TaoBaoOrderId", root.elementTextTrim("TaoBaoOrderId"));
		jo.put("TaoBaoHotelId", root.elementTextTrim("TaoBaoHotelId"));
		jo.put("HotelId", root.elementTextTrim("HotelId"));
		jo.put("TaoBaoRoomTypeId", root.elementTextTrim("TaoBaoRoomTypeId"));
		jo.put("RoomTypeId", root.elementTextTrim("RoomTypeId"));
		jo.put("TaoBaoRatePlanId", root.elementTextTrim("TaoBaoRatePlanId"));
		jo.put("RatePlanCode", root.elementTextTrim("RatePlanCode"));
		jo.put("TaoBaoGid", root.elementTextTrim("TaoBaoGid"));
		jo.put("CheckIn", root.elementTextTrim("CheckIn"));
		jo.put("CheckOut", root.elementTextTrim("CheckOut"));
		jo.put("EarliestArriveTime", root.elementTextTrim("EarliestArriveTime"));
		jo.put("LatestArriveTime", root.elementTextTrim("LatestArriveTime"));
		jo.put("RoomNum", root.elementTextTrim("RoomNum"));
		jo.put("TotalPrice", root.elementTextTrim("TotalPrice"));
		jo.put("SellerDiscount", root.elementTextTrim("SellerDiscount"));
		jo.put("AlitripDiscount", root.elementTextTrim("AlitripDiscount"));
		jo.put("PaymentType", root.elementTextTrim("PaymentType"));
		jo.put("ContactName", root.elementTextTrim("ContactName"));
		jo.put("ContactTel", root.elementTextTrim("ContactTel"));
		jo.put("ContactEmail", root.elementTextTrim("ContactEmail"));
		 Element dailyInfos = root.element("DailyInfos"); ///获取根节点下的子节点AuthenticationToken
         Iterator iterss1 = dailyInfos.elementIterator("DailyInfo");
         List<DailyInfo> list = new ArrayList<DailyInfo>();
         while(iterss1.hasNext())
         {
        	 Element recordEle  = (Element)iterss1.next();
        	 DailyInfo dailyInfo = new DailyInfo();
        	 dailyInfo.setDay(recordEle.elementTextTrim("Day"));
        	 dailyInfo.setPrice(recordEle.elementTextTrim("Price"));
        	 list.add(dailyInfo);
         }
         jo.put("DailyInfos",JSONArray.fromObject(list));
		
//         System.out.println("");
         Element orderGuests = root.element("OrderGuests"); ///获取根节点下的子节点AuthenticationToken
         Iterator iterss2 = orderGuests.elementIterator("OrderGuest");
         List<OrderGuest> list1 = new ArrayList<OrderGuest>();
         while(iterss2.hasNext())
         {
        	 Element recordEle  = (Element)iterss2.next();
        	 OrderGuest orderGuest = new OrderGuest();
        	 orderGuest.setName(recordEle.elementTextTrim("Name"));
        	 orderGuest.setRoomPos(recordEle.elementTextTrim("RoomPos"));
        	 list1.add(orderGuest);
         }
//         jo.put("DailyInfos",JSONArray.fromObject(list));
         jo.put("OrderGuests", JSONArray.fromObject(list1));
        if(root.element("Comment")!=null)
 		{
        	jo.put("Comment", root.elementTextTrim("Comment"));
 		}else{
 			jo.put("Comment", "");
 		}
        if(root.element("GuaranteeType")!=null)
 		{
        	jo.put("GuaranteeType", root.elementTextTrim("GuaranteeType"));
 		}else{
 			jo.put("GuaranteeType", "");
 		}
        if(root.element("MemberCardNo")!=null)
 		{
        	jo.put("MemberCardNo", root.elementTextTrim("MemberCardNo"));
 		}else{
 			jo.put("MemberCardNo", "");
 		}
		
//		jo.put("MemberCardNo", root.elementTextTrim("MemberCardNo"));
		if(root.element("ReceiptInfo")!=null)
		{
			 Element receiptInfo_et = root.element("ReceiptInfo"); ///获取根节点下的子节点AuthenticationToken
			 ReceiptInfo receiptInfo = new ReceiptInfo();
			 receiptInfo.setReceiptTitle(receiptInfo_et.elementTextTrim("ReceiptTitle"));
			 receiptInfo.setReceiptType(receiptInfo_et.elementTextTrim("ReceiptType"));
			 ReceiptAddress ra = new ReceiptAddress();
			 JSONObject jo_receiptAddress = JSONObject.fromObject(receiptInfo_et.elementTextTrim("ReceiptAddress"));
			 ra.setAddress(jo_receiptAddress.getString("address"));
			 ra.setMobile(jo_receiptAddress.getString("mobile"));
			 ra.setName(jo_receiptAddress.getString("name"));
			 ra.setPhone(jo_receiptAddress.getString("phone"));
			 ra.setPostCode(jo_receiptAddress.getString("postCode"));
			 receiptInfo.setReceiptAddress(ra);
			jo.put("ReceiptInfo", JSONObject.fromObject(receiptInfo)); 
		}else{
//			Element receiptInfo_et = root.element("ReceiptInfo"); 
			 ReceiptInfo receiptInfo = new ReceiptInfo();
			 receiptInfo.setReceiptTitle("");
			 receiptInfo.setReceiptType("");
			 ReceiptAddress ra = new ReceiptAddress();
//			 JSONObject jo_receiptAddress = JSONObject.fromObject(receiptInfo_et.elementTextTrim("ReceiptAddress"));
			 ra.setAddress("");
			 ra.setMobile("");
			 ra.setName("");
			 ra.setPhone("");
			 ra.setPostCode("");
			 receiptInfo.setReceiptAddress(ra);
			jo.put("ReceiptInfo", JSONObject.fromObject(receiptInfo)); 
		}
		
	
		
		System.out.println("xml2json:"+jo.toString());
		return jo;
		
	}
	private static String getSystime()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}
	
	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {

		
//		Map<String, Object> mp_data = new HashMap<String, Object>();
//		Map<String, Object> mp = new HashMap<String, Object>();
//		JSONObject jo = new JSONObject();
//		mp.put("code", 50);
//		mp.put("reason", "asas");
//		System.out.println("结果1："+JSONObject.fromObject(mp));
////		mp_data.put("data",mp);
////		System.out.println("mp_data:"+mp_data);
//		jo.put("data",JSONObject.fromObject(mp));
//		CommonTool.getMD5Str("HD100359"+"registered");
		System.out.println("结果："+CommonTool.getMD5Str(""));
		
//		String res=new ShowApiRequest("http://route.showapi.com/860-1","17301","f8e0257b601f4097ad35baf0ec8bb629")
//        .addTextPara("mobile","15279100893")
//        .addTextPara("title","迪联信用住")
//        .addTextPara("content","该条为测试短信，请忽略！")
//        .addTextPara("sendTime","")
//        .addTextPara("big_msg","1")
//        .post();
//		System.out.println(CommonTool.getToday());
//        System.out.println(res);
//		String json_str = "{\"age\":\"34\"}";
		
//	     String str = "2015-12-16T18:00:00";
//	     System.out.println(str.replace("T", " "));
//		try {
//			System.out.println(CommonTool.getDays("2016-02-23", "2016-02-26"));
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(new Date().getTime());
//		Client client;
//		try {
//			client = new Client(new URL(
//			"http://ask.i-hotel.cn:30015/DlSmsService.asmx?wsdl"));
//			Object[] results = client.invoke("Sms_Submit", new Object[] {
//					"dlhis", "419xtd", "13456831133", "信用住短信测试" });
//			for (int i = 0; i < results.length; i++) {
//				System.out.println(i+":"+results[i]);
//			}
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(CommonTool.getMD5Str("HD100622"));
//         HttpUtil.getInstance().sendGet_isOnline("http://hotel.alitrip.com/hscf/freelogin/ExtHotelQueryTool.htm", "vendor=dilian&token=9b5a45b6c738278adde00dca369b6854");
//		System.out.println(Double.valueOf("0.01"));
//		Calendar ca = Calendar.getInstance();
//		ca.set(Calendar.HOUR_OF_DAY, 1);
//		ca.add(Calendar.DAY_OF_MONTH, +1);
//		ca.set(Calendar.MINUTE, 0);
//		ca.set(Calendar.SECOND, 0);
//		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		System.out.println(sd.format(ca.getTime()));
		

//		int i=16856;
//		System.out.println(((float)i)/100);
//		JSONArray ja_rmtype = new JSONArray();
//		jo_rmtype.put("lyy", "asda");
//		System.out.println(jo_rmtype.containsValue("sda"));
//		try {
//			System.out.println(CommonTool.getDateOfLaterDays("2016-02-23",0));
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		  HttpURLConnection con = null;
//		    OutputStream os = null;
//		    InputStream is = null;
//		    BufferedReader bin = null;
//		    URL url = null;
//		    JSONObject jo_result = null;
//		    
//		    try {
//		      url = new URL("http://124.127.242.67/PMSGateway39/PMSGatewayService.asmx");
//		      
//		      con = (HttpURLConnection) url.openConnection();
//		      con.setRequestMethod("POST");
//		      con.setDoInput(true);
//		      con.setDoOutput(true);
//		      con.setRequestProperty("Content-Type", "text/xml");
////		      con.setRequestMethod(method)
//		      con.connect();
//		      String sCurrentLine = "";
//		      String sTotalString = "";
//		      if(con.getResponseCode()==200)
//		      {
//		    	  is = con.getInputStream();
//		    	  bin = new BufferedReader(new InputStreamReader(is,"utf-8"));
//			      while ((sCurrentLine = bin.readLine()) != null) {  
//		  	            sTotalString += sCurrentLine;  
//		  	        }  
//			      jo_result = JSONObject.fromObject(sTotalString);
//		      }
//		    
//		      con.disconnect();
//		    }
//		    catch (ConnectException ce) {ce.printStackTrace();}
//		    catch (IOException ie) {ie.printStackTrace();}
//		    catch (Exception e) {e.printStackTrace();}
//		    finally{
//		    	try {
//					if(is!=null)
//					{
//						is.close();
//						is = null;
//					}
//					if(bin!=null)
//					{
//						 bin.close();
//						 bin = null;
//					}
//					if(os!=null)
//					{
//						os.close(); 
//						os = null;
//					}
//				} catch (IOException e) {
//				
//					e.printStackTrace();
//				}
//		    	
//			     
//		    }
//		Set_roommap rs = new Set_roommap();
//		List<Set_roommap> list = new ArrayList<Set_roommap>();
//		rs.setHotelid("dlhis123");
//		rs.setBdate("2016-1-27");
//		rs.setEdate("2016-2-1");
//		rs.setRmtype("dlhis123_BJ");
//		rs.setRp_code("RP10001");
//		rs.setChannel("CTRIP");
//		rs.setMon("1");
//		rs.setTUE("1");
//		rs.setWED("1");
//		rs.setTUR("1");
//		rs.setFRI("1");
//		rs.setSAT("1");
//		rs.setSUN("1");
//		rs.setStatus("1");
//		rs.setType_set("SetMaxLOS");
//		rs.setDay_set("7");
//		list.add(rs);
//		Set_roommap rs1 = new Set_roommap();
//		rs1.setHotelid("dlhis123");
//		rs1.setBdate("2016-1-27");
//		rs1.setEdate("2016-2-1");
//		rs1.setRmtype("dlhis123_DJ");
//		rs1.setRp_code("RP10002");
//		rs1.setChannel("CTRIP");
//		rs1.setMon("1");
//		rs1.setTUE("1");
//		rs1.setWED("1");
//		rs1.setTUR("1");
//		rs1.setFRI("1");
//		rs1.setSAT("1");
//		rs1.setSUN("1");
//		rs1.setStatus("1");
//		rs1.setType_set("SetMaxLOS");
//		rs1.setDay_set("7");
//		list.add(rs1);
//		System.out.println("list:"+JSONArray.fromObject(list).toString());
//		Document doc = XMLGenerator_RZT.XMLGenerator_NotifRQ("dlhis123", JSONArray.fromObject(list));
//		System.out.println(doc.asXML());
		
	}
	
	
	

}
