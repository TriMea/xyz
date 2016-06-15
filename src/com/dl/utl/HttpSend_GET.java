package com.dl.utl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.log4j.Logger;

import com.sun.xml.internal.ws.resources.SoapMessages;



import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class HttpSend_GET {

	
	private static Logger logger = Logger.getLogger(HttpSend_GET.class);
	public static JSONObject SendGET(String url1,String params){
		  
	    HttpURLConnection con = null;
	    OutputStream os = null;
	    InputStream is = null;
	    BufferedReader bin = null;
	    URL url = null;
	    JSONObject jo_result = null;
	    
	    try {
	      url = new URL(url1+"?"+params);
	      
	      con = (HttpURLConnection) url.openConnection();
	      con.setRequestMethod("GET");
	      con.setDoInput(true);
	      con.setDoOutput(true);
	      con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	      con.connect();
	      String sCurrentLine = "";
	      String sTotalString = "";
	      if(con.getResponseCode()==200)
	      {
	    	  is = con.getInputStream();
	    	  bin = new BufferedReader(new InputStreamReader(is,"utf-8"));
		      while ((sCurrentLine = bin.readLine()) != null) {  
	  	            sTotalString += sCurrentLine;  
	  	        }  
		      jo_result = JSONObject.fromObject(sTotalString);
	      }
	    
	      con.disconnect();
	    }
	    catch (ConnectException ce) {ce.printStackTrace();}
	    catch (IOException ie) {ie.printStackTrace();}
	    catch (Exception e) {e.printStackTrace();}
	    finally{
	    	try {
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
			} catch (IOException e) {
			
				e.printStackTrace();
			}  
	    }
	   return jo_result;
	    
	  }
	
public static JSONObject SendSyncPOST(String url1,JSONObject params){
		  
		
	    HttpURLConnection con = null;
	    JSONObject jo_result = null;
	    OutputStream os = null;
	    InputStream is = null;
	    BufferedReader bin = null;
	    URL url = null;
	    System.out.println("weburl:"+url1+" content:"+params);
	    
//		    System.out.println("ʵ�����"+content);
	    try {
	      url = new URL(url1);
	      
	      con = (HttpURLConnection) url.openConnection();
	      con.setRequestMethod("POST");
	      con.setDoInput(true);
	      con.setDoOutput(true);
	      con.setReadTimeout(15000);
	      con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//	      con.setRequestProperty("Content-Type", "multipart/form-data");
	      os = con.getOutputStream();
	      os.write(params.toString().getBytes("utf-8"),0,params.toString().getBytes("utf-8").length);
	      os.flush();
	      os.close();
	      String sCurrentLine = "";
	      String sTotalString = "";
	      System.out.println("响应码："+con.getResponseCode());
	      if(con.getResponseCode()==200)
	      {
	    	  is = con.getInputStream();
	    	  bin = new BufferedReader(new InputStreamReader(is,"utf-8"));
		      while ((sCurrentLine = bin.readLine()) != null) {  
	  	            sTotalString += sCurrentLine;  
	  	        } 
		      jo_result = JSONObject.fromObject(sTotalString);
		      System.out.println("收到同步订单返回："+jo_result);
		     logger.info("收到同步订单返回："+jo_result);
	      }
	      con.disconnect();
	    } catch (SocketTimeoutException e) {
			//下发的订单超时，链路器可能不在线
	        logger.error(e);
	        logger.info("读取时间超时");
	    	e.printStackTrace();
	    	System.out.println("该酒店推送服务有异常");
		}catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e);
			e.printStackTrace();
		}
	    
	    finally{
	    	try {
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
			} catch (IOException e) {
				logger.error(e);
				e.printStackTrace();
			}
	    	
		     
	    }

	   return jo_result;
	    
	  }
	
	public static JSONObject SendPOST(String url1,JSONObject params){
		  
		
	    HttpURLConnection con = null;
	    JSONObject jo_result = null;
	    OutputStream os = null;
	    InputStream is = null;
	    BufferedReader bin = null;
	    URL url = null;
	    System.out.println("同步ztx订单参数2:"+params);
	    
//		    System.out.println("ʵ�����"+content);
	    try {
	      url = new URL(url1);
	      
	      con = (HttpURLConnection) url.openConnection();
	      con.setRequestMethod("POST");
	      con.setDoInput(true);
	      con.setDoOutput(true);
	      con.setReadTimeout(10000);
	      con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//	      con.setRequestProperty("Content-Type", "multipart/form-data");
	      os = con.getOutputStream();
	      os.write(params.toString().getBytes("utf-8"),0,params.toString().getBytes("utf-8").length);
	      os.flush();
	      os.close();
	      String sCurrentLine = "";
	      String sTotalString = "";
	      System.out.println("响应码："+con.getResponseCode());
	      if(con.getResponseCode()==200)
	      {
	    	  is = con.getInputStream();
	    	  bin = new BufferedReader(new InputStreamReader(is,"utf-8"));
		      while ((sCurrentLine = bin.readLine()) != null) {  
	  	            sTotalString += sCurrentLine;  
	  	        } 
		      jo_result = JSONObject.fromObject(sTotalString);
		      System.out.println("收到同步订单返回："+jo_result);
		     logger.info("收到同步订单返回："+jo_result);
	      }
	      con.disconnect();
	    } catch (SocketTimeoutException e) {
			//下发的订单超时，链路器可能不在线
	        logger.error(e);
	    	e.printStackTrace();
	    	System.out.println("该酒店推送服务有异常");
		}catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e);
			e.printStackTrace();
		}
	    
	    finally{
	    	try {
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
			} catch (IOException e) {
				logger.error(e);
				e.printStackTrace();
			}
	    	
		     
	    }

	   return jo_result;
	    
	  }
	
	
	 

public static String SendPOST_Soap(String url1,String soap_ja){
	    soap_ja = soap_ja.replace(soap_ja.substring(soap_ja.indexOf("<"), soap_ja.indexOf(">")+1), "");

		String soapBegin = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><SOAP-ENV:Body>";
		String soapEnd = "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
		 soap_ja = soapBegin+soap_ja+soapEnd;
	    HttpURLConnection con = null;
	    JSONObject jo_result = null;
	    OutputStream os = null;
	    InputStream is = null;
	    BufferedReader bin = null;
	    String sTotalString = "";
	    URL url = null;
//	    SOAPMessage msg = null;
	    System.out.println("weburl:"+url1+" content:"+soap_ja);
	    try {
	      url = new URL(url1);
	      
	      con = (HttpURLConnection) url.openConnection();
	      con.setRequestMethod("POST");
	      con.setDoInput(true);
	      con.setDoOutput(true);
	      con.setReadTimeout(5000);
	      con.setRequestProperty("Content-Length", String.valueOf(soap_ja.length()));
	      con.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
	      con.setRequestProperty("SOAPAction", "http://htng.org/2014B/HTNG_ARIAndReservationPushService#OTA_HotelRatePlanNotifRQ");
	      os = con.getOutputStream();
	      OutputStreamWriter osw=new OutputStreamWriter(os,"utf-8");
	      osw.write(soap_ja);
	      osw.flush();
	      osw.close();
//	      os.write(soap_ja.getBytes("utf-8"),0,soap_ja.getBytes("utf-8").length);
//	      os.flush();
//	      os.close();
	      String sCurrentLine = "";
	    
	      System.out.println("响应码："+con.getResponseCode());
	      if(con.getResponseCode()==200)
	      {
	    	  is = con.getInputStream();
	    	  bin = new BufferedReader(new InputStreamReader(is,"utf-8"));
		      while ((sCurrentLine = bin.readLine()) != null) {  
	  	            sTotalString += sCurrentLine;  
	  	        } 
//		      jo_result = JSONObject.fromObject(sTotalString);
		      System.out.println("收到soap响应："+sTotalString);
//		     logger.info("收到soap响应："+sTotalString);
		      
		  
	      }
	      con.disconnect();
	    } catch (SocketTimeoutException e) {
			//下发的订单超时，链路器可能不在线
	        logger.error(e);
	    	e.printStackTrace();
	    	
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    finally{
	    	try {
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
			} catch (IOException e) {
			
				e.printStackTrace();
			}
	    	
		     
	    }

	   return sTotalString;
	    
	  }


}
