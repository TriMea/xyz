package com.dl.dao;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



import com.dl.datasource.DBPool;
import com.dl.pojo.OrderInfo;
import com.dl.servlet.BasicDataServlet;




















public class CommonDao {
	/*
	 * 单例模式
	 */
	private CommonDao() {}  
    
    private static final CommonDao commonDao = new CommonDao();  
//    private Logger logger = Logger.getLogger(CommonDao.class);  
    public static CommonDao getInstance() {  
        return commonDao;  
    }  
	
	

	public String generateResponseJSon(String responseOp,String status,String responseCode,String responseDec,JSONArray responseData)
	{
		JSONObject jo_response = new JSONObject();
		jo_response.put("responseOp", responseOp);
		jo_response.put("status", status);
		jo_response.put("responseCode", responseCode);
		String dec = null;
		try {
			if(responseDec!=null)
			dec = URLEncoder.encode(responseDec, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jo_response.put("responseDec",dec);
		if(responseData==null)
		{
			jo_response.put("responseData", "no data");
		}else{
			jo_response.put("responseData", responseData);
		}
//		logger.info(message)
		return jo_response.toString();
	} 
	
	
	public String generateResponseJSon_SHR(String responseOp,String status,String responseCode,String responseDec,JSONArray responseData)
	{
		JSONObject jo_response = new JSONObject();
		jo_response.put("responseOp", responseOp);
		jo_response.put("status", status);
		jo_response.put("responseCode", responseCode);
		String dec = null;
//		try {
//			dec = URLEncoder.encode(responseDec, "utf-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		jo_response.put("responseDec",responseDec);
		if(responseData==null)
		{
			jo_response.put("responseData", "no data");
		}else{
			jo_response.put("responseData", responseData);
		}
//		logger.info(message)
		return jo_response.toString();
	} 
	
	
	public String generateSimpleResponseJSon(String responseOp,String status,String responseCode,String responseDec,String responseContent)
	{
		JSONObject jo_response = new JSONObject();
		jo_response.put("responseOp", responseOp);
		jo_response.put("status", status);
		jo_response.put("responseCode", responseCode);
		jo_response.put("responseData",responseContent);
		
		
		return jo_response.toString();
	} 
	
	public JSONObject generateOrders_transport(String type,String taobaoid,String hotelid,String mrk,String latestarrivetime)
	{
		JSONObject jo_response = new JSONObject();
		HashMap<String, Object> mp = new HashMap<String, Object>();
		mp.put("serviceName", "orders_transport");
		if("orders_create".equals(type))
		{
			OrderInfo orderInfo = new OrderInfo();
			orderInfo.setHotelid(hotelid);
			orderInfo.setTaobaoid(taobaoid);
			orderInfo.setMrk(mrk);
			orderInfo.setLatestarrivetime(latestarrivetime);
			mp.put("orders_create", orderInfo);
		}else if("orders_cancel".equals(type))
		{
			OrderInfo orderInfo = new OrderInfo();
			orderInfo.setHotelid(hotelid);
			orderInfo.setTaobaoid(taobaoid);
			orderInfo.setMrk(mrk);
//			orderInfo.setLatestarrivetime(latestarrivetime);
			mp.put("orders_cancel", orderInfo);
		}
		
		
		
		return JSONObject.fromObject(mp);
	} 
	
	public JSONObject generateOrders_motify(String taobaoid_create,String taobaoid_cancel,String hotelid,String mrk,String latestarrivetime,String latestarrivetime_c)
	{
		JSONObject jo_response = new JSONObject();
		HashMap<String, Object> mp = new HashMap<String,Object>();
		mp.put("serviceName", "orders_transport");
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setHotelid(hotelid);
		orderInfo.setTaobaoid(taobaoid_create);
		orderInfo.setMrk(mrk);
		orderInfo.setLatestarrivetime(latestarrivetime);
		mp.put("orders_create", orderInfo);
		OrderInfo orderInfo_c = new OrderInfo();
		orderInfo_c.setHotelid(hotelid);
		orderInfo_c.setTaobaoid(taobaoid_cancel);
		orderInfo_c.setMrk(mrk);
		orderInfo_c.setLatestarrivetime(latestarrivetime_c);
		mp.put("orders_cancel", orderInfo_c);
	
		
		
		
		return JSONObject.fromObject(mp);
	} 
	
	
	
	
}
