package com.dl.utl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.dl.servlet.RequestServlet;

import net.sf.json.JSONObject;

public class CommonTool {

	private static Logger logger = Logger.getLogger(CommonTool.class); 
	public static String getToday()
	{
		Calendar ca = Calendar.getInstance();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(ca.getTime());
		
	}
	
	/*
	 * �ر���ݿ�ִ��������
	 */
	public static void closeStatement(Statement st) throws SQLException
	{
		 if(st!=null)
		 {
			 
				st.close();
				st = null;
			
			
		 }
	}
	
	
	
	/*
	 * �ر���ݿ����Ӷ���
	 */
	public static void closeConnection(Connection conn) throws SQLException
	{
		 if(conn!=null&&!conn.isClosed())
		 {
			
				 conn.close();
				 conn = null;
			
			
		 }else if(conn.isClosed()&&conn!=null)
		 {
			 conn = null;
		 }
	}
	/*
	 * �ر���ݿ������
	 */
	public static void closeResultSet(ResultSet rs) throws SQLException
	{
		
		 if(rs!=null)
		 {
			 rs.close();
			 rs = null;
		 }
	}
	/*
	 * �ر���ݿ������
	 */
	public static void closePreparedStatement(PreparedStatement rs) throws SQLException
	{
		
		 if(rs!=null)
		 {
			 rs.close();
			 rs = null;
		 }
	}
	/*
	 * �ر���ݿ���ִ�ж���
	 */
	public static void closeCallableStatement(CallableStatement cs) throws SQLException
	{
		 if(cs!=null)
		 {
			
			 cs.close();
			 cs = null;

		 }
	}
	/*
	 * 获取两个日期间隔天数
	 */
	public static int getDays(String bdate,String edate) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date bdate_d = sdf.parse(bdate);
		Date edate_d = sdf.parse(edate);
		long days_l = edate_d.getTime()-bdate_d.getTime();
		int days = (int)(days_l/(24*3600*1000));
		return days;
	}
	
	/*
	 * 获取后n天的日期
	 */
	public static String getDateOfLaterDays(String date,int day) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date bdate_d = sdf.parse(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(bdate_d);	
		calendar.add(Calendar.DAY_OF_MONTH, day);
	
		return sdf.format(calendar.getTime());
	}	
	
	/*
	 * 获取25分钟之后的时间
	 */
	public static String getSystime()
	{
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.MINUTE, +25);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(ca.getTime());
	}
	
	public static String getGuid()
	{
		return new Date().getTime()+"";
	}
	
	
	//MD5加密方法

	public static String getMD5Str(String str) {
		MessageDigest messageDigest = null;
        String nodigest = str.trim()+"-dlhis";
		try {
			messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.reset();

			messageDigest.update(nodigest.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("NoSuchAlgorithmException caught!");
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5StrBuff.toString().trim();
	}
	
	
	
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
				if(con!=null)
				{
					con.disconnect();
					con = null;
				}
			} catch (IOException e) {
			
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
	    try {
	      url = new URL(url1);
	      
	      con = (HttpURLConnection) url.openConnection();
	      con.setRequestMethod("POST");
	      con.setDoInput(true);
	      con.setDoOutput(true);
	      con.setReadTimeout(10000);
	      con.setRequestProperty("Content-Type", "application/json");
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
		     logger.info("收到阿拉丁订单服务器返回："+jo_result);
	      }
	    
	    } catch (SocketTimeoutException e) {
			//阿拉丁订单服务器异常
	        logger.error(e);
	    	e.printStackTrace();
	    	
		}catch (IOException e) {
			
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
				if(con!=null)
				{
					con.disconnect();
					con = null;
				}
			} catch (IOException e) {
				logger.error(e);
				e.printStackTrace();
			}
	    	
		     
	    }

	   return jo_result;
	    
	  }
	
}
