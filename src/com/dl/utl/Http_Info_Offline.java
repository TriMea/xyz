package com.dl.utl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

public class Http_Info_Offline {

	private Http_Info_Offline() {}  
	private Logger logger = Logger.getLogger(Http_Info_Offline.class); 
    private static final Http_Info_Offline httpUtil = new Http_Info_Offline();  
    public static Http_Info_Offline getInstance() {  
        return httpUtil;  
    }  
    
    /*
	 * 通知指定酒店下线资源
	 */
	public boolean sendGet_infoOffline(String url1,String params){
		  
	    HttpURLConnection con = null;
	    OutputStream os = null;
	    InputStream is = null;
	    BufferedReader bin = null;
	    URL url = null;
	    boolean issuccess = false;
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
		      System.out.println("返回结果："+jo_result);
		      if("0".equals(jo_result.getString("code").trim()))
		      {
		    	  //成功
		    	  issuccess = true;
		    	  
		      }
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
				if(jo_result!=null)
				{
					jo_result = null;
				}
			} catch (IOException e) {
			
				e.printStackTrace();
			}  
	    }
	   return issuccess;
	    
	  }
    
}
