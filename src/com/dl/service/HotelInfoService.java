package com.dl.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;





import com.dl.dao.CommonDao;
import com.dl.dao.HotelInfoDao;
import com.dl.servlet.RequestServlet;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.XhotelAddRequest;
import com.taobao.api.request.XhotelOrderStatementGetRequest;
import com.taobao.api.request.XhotelRateplanAddRequest;
import com.taobao.api.request.XhotelRoomtypeAddRequest;
import com.taobao.api.response.XhotelAddResponse;
import com.taobao.api.response.XhotelOrderStatementGetResponse;
import com.taobao.api.response.XhotelRateplanAddResponse;
import com.taobao.api.response.XhotelRoomtypeAddResponse;





public class HotelInfoService {

	private Logger logger = Logger.getLogger(RequestServlet.class); 
	/*
	 * 酒店信息的添加/修改
	 */
	public void hotelInfo_add_update(PrintWriter out,JSONObject json_data) throws SQLException
	{
		//===============================
		
			CommonDao commonDao = CommonDao.getInstance();
			HotelInfoDao hotelInfoDao = HotelInfoDao.getInstance();
			if(hotelInfoDao.hotel_update_add(json_data.getJSONArray("requestData").getJSONObject(0), json_data.getString("hotelid")))
			{
				out.write(commonDao.generateResponseJSon("hotel_add_update","0","0000","添加/修改酒店信息成功",null));
				out.flush();
				out.close();
			}
		
		
	 }
	
	/*
	 * 账单信息查询
	 */
	public void ordersQuery(PrintWriter out,JSONArray ja_orders_info) throws SQLException
	{
		//===============================
		
			CommonDao commonDao = CommonDao.getInstance();
//			HotelInfoDao hotelInfoDao = HotelInfoDao.getInstance();
//			if(hotelInfoDao.hotel_update_add(json_data.getJSONArray("requestData").getJSONObject(0), json_data.getString("hotelid")))
//			{
				out.write(commonDao.generateResponseJSon_SHR("orders_query","0","0000","查询账单信息成功",ja_orders_info));
				out.flush();
				out.close();
//			}
		
		
	 }
	
	/*
	 * 酒店房型信息的添加/修改
	 */
	public void rmtype_add_update(PrintWriter out,JSONObject json_data) throws SQLException
	{
		//===============================
		
			CommonDao commonDao = CommonDao.getInstance();
			HotelInfoDao hotelInfoDao = HotelInfoDao.getInstance();
			if(hotelInfoDao.rmtype_update_add(json_data.getJSONArray("requestData").getJSONObject(0), json_data.getString("hotelid")))
			{
				out.write(commonDao.generateResponseJSon("rmtype_update_add","0","0000","添加/修改房型信息成功",null));
				out.flush();
				out.close();
			}
		
		
	 }
	
	/*
	 * 酒店RP信息的添加/修改
	 */
	public void rateplan_add_update(PrintWriter out,JSONObject json_data) throws SQLException
	{
		//===============================
		
			CommonDao commonDao = CommonDao.getInstance();
			HotelInfoDao hotelInfoDao = HotelInfoDao.getInstance();
			if(hotelInfoDao.rateplan_update_add(json_data.getJSONArray("requestData").getJSONObject(0), json_data.getString("hotelid")))
			{
				out.write(commonDao.generateResponseJSon("rateplan_add_update","0","0000","添加/修改RP信息成功",null));
				out.flush();
				out.close();
			}
		
		
	 }
	
	
	
	
	public  HashMap<String, String> HotelAdd(String url,String appkey,String secret,String sessionKey,String hotelid,JSONObject requestData)
	{
		/*
		 * 新增酒店,id重复自动更新
		 */
		HashMap<String, String> mp = new HashMap<String, String>();
		mp.put("code", "-1");
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		XhotelAddRequest req = new XhotelAddRequest();
		req.setOuterId(hotelid);
		req.setName(requestData.getString("name").trim());
//		req.setUsedName("北京饭店");
		req.setDomestic(0L);
//		req.setCountry("China");
//		req.setProvince(110000L);
		req.setCity(Long.valueOf(requestData.getString("city").trim()));
		if(requestData.has("district"))
		{
			req.setDistrict(Long.valueOf(requestData.getString("district").trim()));	
		}
//		req.setDistrict(110101L);
//		req.setBusiness("aaa");
		req.setAddress(requestData.getString("address").trim());
//		req.setLongitude("111.13");
//		req.setLatitude("111.1222");
//		req.setPositionType("G");
		req.setTel(requestData.getString("tel").trim());
//		req.setExtend("{\"keyword\":”上地”}");
//		req.setShid(123123L);
		req.setVendor(requestData.getString("vendor").trim());
		try {
			XhotelAddResponse rsp = client.execute(req, sessionKey);
			if(rsp.getXhotel()!=null)
			{
				mp.put("code", "0");
				mp.put("msg", rsp.getBody());
			}else{
				mp.put("code", "1");
				System.out.println("去啊接口响应:"+rsp.getBody());
				mp.put("sub_code", rsp.getSubCode());
				mp.put("sub_msg", rsp.getSubMsg());
				mp.put("msg", rsp.getBody());
			}
			System.out.println("阿里返回："+rsp.getBody());
		} catch (ApiException e) {
			logger.error(e);
			e.printStackTrace();
		}
		
		return mp;
		
		
	}
	
	public  HashMap<String, String> RmtypeAdd(String url,String appkey,String secret,String sessionKey,String hotelid,JSONObject requestData)
	{
		/*
		 * 新增酒店房型，id重复自动更新
		 */
		HashMap<String, String> mp = new HashMap<String, String>();
		mp.put("code", "-1");
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		XhotelRoomtypeAddRequest req = new XhotelRoomtypeAddRequest();
		System.out.println("房型id:"+requestData.getString("rmtype_id").trim());
		req.setOuterId(requestData.getString("rmtype_id").trim());
//		req.setHid(1L);
		req.setName(requestData.getString("name").trim());
//		req.setMaxOccupancy(2L);
//		req.setArea("A");
//		req.setFloor("3-8层");
//		req.setBedType("小床");
//		req.setBedSize("大床2.1米");
		req.setInternet(requestData.getString("internet").trim());
//		req.setService("{\"bar\":false,\"catv\":false,\"ddd\":false,\"idd\":false,\"pubtoilet\":false,\"toilet\":false}");
//		req.setExtend("空");
		req.setWindowType(Long.valueOf(requestData.getString("windows_type").trim()));
//		req.setSrid(123123L);
		req.setOutHid(hotelid.trim());
		req.setVendor(requestData.getString("vendor").trim());
		
		
		try {
			XhotelRoomtypeAddResponse rsp = client.execute(req,sessionKey);
			if(rsp.getXroomtype()!=null)
			{
				mp.put("code", "0");
				mp.put("msg", rsp.getBody());
			}else{
				mp.put("code", "1");
				mp.put("sub_code", rsp.getSubCode());
				mp.put("sub_msg", rsp.getSubMsg());
				mp.put("msg", rsp.getBody());
			}
		} catch (ApiException e) {
			logger.error(e);
			e.printStackTrace();
		}
		
		return mp;
		
		
	}
	
	
	public  HashMap<String, Object> OrdersQuery(String url,String appkey,String secret,String sessionKey,JSONObject requestData)
	{
		/*
		 * 查询订单信息
		 */
		HashMap<String, Object> mp = new HashMap<String, Object>();
		mp.put("code", "-1");
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		XhotelOrderStatementGetRequest req = new XhotelOrderStatementGetRequest();
		req.setOrderTids(requestData.getString("orderids"));
		
		
		try {
			XhotelOrderStatementGetResponse rsp = client.execute(req, sessionKey);
			if(rsp.getStatements()!=null&&rsp.getStatements().size()>0)
			{
				mp.put("code", "0");
				mp.put("msg", rsp.getStatements());
			}else{
				mp.put("code", "1");
				System.out.println("去啊接口响应:"+rsp.getBody());
				mp.put("sub_code", rsp.getSubCode());
				mp.put("sub_msg", rsp.getMsg());
				mp.put("msg", rsp.getBody());
			}
			System.out.println("阿里返回："+rsp.getBody());
		} catch (ApiException e) {
			logger.error(e);
			e.printStackTrace();
		}
		
		return mp;
		
		
	}
	
	public  HashMap<String, String> RatePlanAdd(String url,String appkey,String secret,String sessionKey,String hotelid,JSONObject requestData)
	{
		/*
		 * 新增酒店RatePlan,id重复自动更新
		 */
		HashMap<String, String> mp = new HashMap<String, String>();
		mp.put("code", "-1");
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		XhotelRateplanAddRequest req = new XhotelRateplanAddRequest();
		req.setRateplanCode(requestData.getString("rateplan_code").trim());
		req.setName(requestData.getString("name").trim());
		req.setPaymentType(Long.valueOf(requestData.getString("payment_type")));
		req.setBreakfastCount(Long.valueOf(requestData.getString("breakfast_count")));
//		req.setFeeBreakfastCount(1L);
//		req.setFeeBreakfastAmount(1L);
//		req.setFeeGovTaxAmount(1L);
//		req.setFeeGovTaxPercent(1L);
//		req.setFeeServiceAmount(150L);
//		req.setFeeServicePercent(15L);
//		req.setExtendFee("aaa");
//		req.setMinDays(1L);
//		req.setMaxDays(90L);
//		req.setMinAmount(1L);
//		req.setMinAdvHours(1L);
//		req.setMaxAdvHours(3L);
//		req.setStartTime("00:00");
//		req.setEndTime("00:00");
//		System.out.println("requestData:"+requestData);
//		System.out.println("cancel_policy:"+requestData.getString("breakfast_count"));
		System.out.println("退订政策："+requestData.getString("cancel_policy"));
//		try {
//			System.out.println("担保政策："+requestData.getString("guarantee_type"));
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
		
		
//		req.setExtend("1");
		req.setStatus(Long.valueOf(requestData.getString("status")));
//		req.setEnglishName("aaa");
		if(requestData.has("guarantee_type"))
		{
			req.setCancelPolicy(requestData.getString("cancel_policy"));
			req.setGuaranteeType(Long.valueOf(requestData.getString("guarantee_type")));
			System.out.println("担保政策："+requestData.getString("guarantee_type"));
		}else{
			System.out.println("无担保政策");
			req.setCancelPolicy("{\"cancelPolicyType\":1}");
		}
		
//		requestData.has("")
//		req.setGuaranteeStartTime("18:00");
//		req.setMemberLevel("1,2,3");
//		req.setChannel("A");
//		req.setOccupancy(3L);
		req.setVendor(requestData.getString("vendor").trim());
//		req.setFirstStay(1L);
//		req.setAgreement(1L);
//		req.setBreakfastCal("{\"date\":\"yyyy-MM-dd\",\"startDate\":\"yyyy-MM-dd\",\"endDate\":\"yyyy-MM-dd\",\"breakfast_count\":0},{\"date\":\"yyyy-MM-dd\",\"startDate\":\"yyyy-MM-dd\",\"endDate\":\"yyyy-MM-dd\",\"breakfast_count\":1}");
//		req.setCancelPolicyCal("{\"date\":\"yyyy-MM-dd\",\"startDate\":\"yyyy-MM-dd\",\"endDate\":\"yyyy-MM-dd\",\"cancel_policy\":{\"cancelPolicyType\":1} },{\"date\":\"yyyy-MM-dd\",\"startDate\":\"yyyy-MM-dd\",\"endDate\":\"yyyy-MM-dd\",\"cancel_policy\":{\"cancelPolicyType\":4,\"policyInfo\":{\"48\":10,\"24\":20}}}");
//		req.setGuaranteeCal("{\"date\":\"yyyy-MM-dd\",\"startDate\":\"yyyy-MM-dd\",\"endDate\":\"yyyy-MM-dd\",\"guarantee\":{\"guaranteeType\":2,\"guaranteeStartTime\":\"HH:mm\"}},{\"date\":\"yyyy-MM-dd\",\"startDate\":\"yyyy-MM-dd\",\"endDate\":\"yyyy-MM-dd\",\"guarantee\":{\"guaranteeType\":3,\"guaranteeStartTime\":\"HH:mm\"}}");
//		req.setCancelBeforeHour("6");
//		req.setCancelBeforeDay(2L);
//		req.setEffectiveTime(StringUtils.parseDateTime("2015-11-21 00:00:00"));
//		req.setDeadlineTime(StringUtils.parseDateTime("2016-04-01 00:00:00"));
//		req.setRpType("1");
//		req.setHourage("4");
//		req.setCanCheckinEnd("08:00");
//		req.setCanCheckinStart("16:00");
		
		
		
		try {
			XhotelRateplanAddResponse rsp = client.execute(req,sessionKey);
			System.out.println("rpid:"+rsp.getRpid());
			if(rsp.getRpid()!=null)
			{
				mp.put("code", "0");
				mp.put("msg", rsp.getBody());
			}else{
				
				mp.put("code", "1");
				mp.put("sub_code", rsp.getSubCode());
				mp.put("sub_msg", rsp.getSubMsg());
				mp.put("msg", rsp.getBody());
			}
		} catch (ApiException e) {
			logger.error(e);
			e.printStackTrace();
		}
		
		return mp;
		
		
	}
	
	
}
