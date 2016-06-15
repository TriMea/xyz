package com.dl.utl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

public class HttpUtil {

	private HttpUtil() {}  
	private Logger logger = Logger.getLogger(HttpUtil.class); 
    private static final HttpUtil httpUtil = new HttpUtil();  
    public static HttpUtil getInstance() {  
        return httpUtil;  
    }  
    
	/*
	 * 根据hotelid判断该酒店是否在线
	 */
	public boolean sendGet_isOnline(String url1,String params){
		  
	    HttpURLConnection con = null;
	    OutputStream os = null;
	    InputStream is = null;
	    BufferedReader bin = null;
	    URL url = null;
	    boolean isOnLine = true;
	    JSONObject jo_result = null;
	    try {
	      url = new URL(url1+"?"+params);
	      con = (HttpURLConnection) url.openConnection();
	      con.setRequestMethod("GET");
	      con.setDoInput(true);
	      con.setDoOutput(true);
//	      con.setRequestProperty("Content-Type", "application/xml");
	      con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	      con.connect();
	      String sCurrentLine = "";
	      String sTotalString = "";
//	      System.out.println("返回值："+con.getResponseCode());
	      if(con.getResponseCode()==200)
	      {
	    	  is = con.getInputStream();
	    	  bin = new BufferedReader(new InputStreamReader(is,"utf-8"));
		      while ((sCurrentLine = bin.readLine()) != null) {  
	  	            sTotalString += sCurrentLine;  
	  	        }  
		      System.out.println("数据:"+sTotalString);
		      jo_result = JSONObject.fromObject(sTotalString);
		      logger.info("试单检验是否在线:"+url1+"?"+params+"  返回结果："+jo_result);
		      if("OFF".equals(jo_result.getString("onlineStatus").trim()))
		      {
		    	  //离线
		    	  isOnLine = false;
		    	  
		      }
	      }
	    
	      con.disconnect();
	    }
	    catch (ConnectException ce) {ce.printStackTrace();}
	    catch (IOException ie) {ie.printStackTrace();}
	    catch (Exception e) {e.printStackTrace();}
	    finally{
	    	try {
	    		if(con!=null)
	    		{
	    			con.disconnect();
	    		}
				if(is!=null)
				{
					is.close();
					is = null;
				}
				if(bin!=null)
				{
					 bin.close();
					 bin = null;
				}
				if(os!=null)
				{
					os.close(); 
					os = null;
				}
				if(jo_result!=null)
				{
					jo_result = null;
				}
			} catch (IOException e) {
			
				e.printStackTrace();
			}  
	    }
	   return isOnLine;
	    
	  }
	
	
	/*
	 * 根据hotelid判断该酒店是否在线
	 */
	public boolean sendGet_isOnline_PMS(String url1,String params){
		  
	    HttpURLConnection con = null;
	    OutputStream os = null;
	    InputStream is = null;
	    BufferedReader bin = null;
	    URL url = null;
	    boolean isOnLine = true;
	    JSONObject jo_result = null;
	    try {
	      url = new URL(url1+"?"+params);
	      con = (HttpURLConnection) url.openConnection();
	      con.setRequestMethod("GET");
	      con.setDoInput(true);
	      con.setDoOutput(true);
//	      con.setRequestProperty("Content-Type", "application/xml");
	      con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	      con.connect();
	      String sCurrentLine = "";
	      String sTotalString = "";
//	      System.out.println("返回值："+con.getResponseCode());
	      if(con.getResponseCode()==200)
	      {
	    	  is = con.getInputStream();
	    	  bin = new BufferedReader(new InputStreamReader(is,"utf-8"));
		      while ((sCurrentLine = bin.readLine()) != null) {  
	  	            sTotalString += sCurrentLine;  
	  	        }  
		      System.out.println("数据:"+sTotalString);
		      jo_result = JSONObject.fromObject(sTotalString);
		      logger.info("试单检验是否在线:"+url1+"?"+params+"  返回结果："+jo_result);
		      if("OFF".equals(jo_result.getString("OnlineStatus").trim()))
		      {
		    	  //离线
		    	  isOnLine = false;
		    	  
		      }
	      }
	    
	      con.disconnect();
	    }
	    catch (ConnectException ce) {ce.printStackTrace();}
	    catch (IOException ie) {ie.printStackTrace();}
	    catch (Exception e) {e.printStackTrace();}
	    finally{
	    	try {
	    		if(con!=null)
	    		{
	    			con.disconnect();
	    		}
				if(is!=null)
				{
					is.close();
					is = null;
				}
				if(bin!=null)
				{
					 bin.close();
					 bin = null;
				}
				if(os!=null)
				{
					os.close(); 
					os = null;
				}
				if(jo_result!=null)
				{
					jo_result = null;
				}
			} catch (IOException e) {
			
				e.printStackTrace();
			}  
	    }
	   return isOnLine;
	    
	  }
	
	
	
}
