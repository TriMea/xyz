package com.dl.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

import com.dl.dao.CommonDao;
import com.dl.service.HotelInfoService;

public class Aladin_BasicDataServlet extends HttpServlet {

	//测试
	private final static String URL = "http://gw.api.tbsandbox.com/router/rest";
	private final static String APPKEY = "1023269815";
	private final static String SECRET = "sandbox2c2edcd6c88993ef24981cad9";
	private final static String SESSIONKEY = "6102409601b12fd8a154c9aad46fb89dced3d3781a35e0e3688302491";
	private final static String KEZHAN_SESSIONKEY = "6101b242adf1cb5497539e7a7dbaa97d107aa9297261a0f31998728";
	
	//正式
//	private final static String URL = "http://gw.api.taobao.com/router/rest";
//	private final static String APPKEY = "23269815";
//	private final static String SECRET = "b7287242c2edcd6c88993ef24981cad9";
//	private final static String SESSIONKEY = "6101813d6bee40f3b0a061dc0eb6ac054d428a1efcb59032708043296";
//	private final static String KEZHAN_SESSIONKEY = "6101b242adf1cb5497539e7a7dbaa97d107aa9297261a0f31998728";
	/**
	 * Constructor of the object.
	 */
	private Logger logger = Logger.getLogger(BasicDataServlet.class);  
	public Aladin_BasicDataServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out
				.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream(),"utf-8"));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		JSONObject json_data = null;
		try {
			logger.info("基础数据提交:"+sb.toString());
//			System.out.println("data:"+request.getParameter("data"));
			json_data = JSONObject.fromObject(sb.toString());
		} catch (Exception e) {
			logger.error(e);
			out.write(CommonDao.getInstance().generateResponseJSon("PARAMS_ERROR","1","1000","参数转换JSon错误("+e.getMessage()+")",null));
			out.flush();
			out.close();
			e.printStackTrace();
			return;
		}
		System.out.println("post:"+json_data);
		//酒店信息添加或修改
		HotelInfoService hotelInfoService = new HotelInfoService();
		if("hotel_add_update".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			if(!json_data.has("ishotel"))
			{//兼容老版本
				
//				HashMap<String,String> mp = HotelAdd(URL, APPKEY, SECRET, SESSIONKEY, json_data.getString("hotelid"), json_data.getJSONArray("requestData").getJSONObject(0));
//				if("0".equals(mp.get("code")))
//				{
					HotelInfoService vs = new HotelInfoService();
					try {
						vs.hotelInfo_add_update(out,json_data);
						logger.info(json_data.getString("hotelid")+"提交酒店信息成功");
					} catch (Exception e) {
						logger.error(e);
						out.write(CommonDao.getInstance().generateResponseJSon("hotel_add_update","1","6000",e.getMessage(),null));
						out.flush();
						out.close();
						e.printStackTrace();
					}
//				}else if("-1".equals(mp.get("code"))){
//					logger.error(json_data.getString("hotelid")+"调用去啊接口（xhotel.add）出错");
//					out.write(CommonDao.getInstance().generateResponseJSon("hotel_add_update","-1","6000","调用去啊接口（xhotel.add）出错",null));
//					out.flush();
//					out.close();
//				}else{
//					logger.error(json_data.getString("hotelid")+"调用去啊接口（xhotel.add）出错["+mp.get("sub_msg")+"]");
//					out.write(CommonDao.getInstance().generateResponseJSon("hotel_add_update",(String)mp.get("sub_code"),"6000",(String)mp.get("sub_msg"),null));
//					out.flush();
//					out.close();
//				}
			}else{
				HashMap<String,String> mp = null;
				if("1".equals(json_data.getString("ishotel").trim()))
				{//酒店
					
					 mp = hotelInfoService.HotelAdd(URL, APPKEY, SECRET, SESSIONKEY, json_data.getString("hotelid"), json_data.getJSONArray("requestData").getJSONObject(0));
				}else{
				//客栈
					
					mp = hotelInfoService.HotelAdd(URL, APPKEY, SECRET, KEZHAN_SESSIONKEY, json_data.getString("hotelid"), json_data.getJSONArray("requestData").getJSONObject(0));
				}
				
				if("0".equals(mp.get("code")))
				{
					HotelInfoService vs = new HotelInfoService();
					try {
						vs.hotelInfo_add_update(out,json_data);
						logger.info(json_data.getString("hotelid")+"提交酒店信息成功");
					} catch (Exception e) {
						logger.error(e);
						out.write(CommonDao.getInstance().generateResponseJSon("hotel_add_update","1","6000",e.getMessage(),null));
						out.flush();
						out.close();
						e.printStackTrace();
					}
				}else if("-1".equals(mp.get("code"))){
					logger.error(json_data.getString("hotelid")+"调用去啊接口（xhotel.add）出错");
					out.write(CommonDao.getInstance().generateResponseJSon("hotel_add_update","-1","6000","调用去啊接口（xhotel.add）出错",null));
					out.flush();
					out.close();
				}else{
					logger.error(json_data.getString("hotelid")+"调用去啊接口（xhotel.add）出错["+mp.get("sub_msg")+"]");
					out.write(CommonDao.getInstance().generateResponseJSon("hotel_add_update",(String)mp.get("sub_code"),"6000",(String)mp.get("sub_msg"),null));
					out.flush();
					out.close();
				}
			}
			
			
			
		}//房型信息的添加/修改
		else if("rmtype_update_add".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			if(!json_data.has("ishotel"))
			{//兼容老版本
				
//				HashMap<String, String> mp = hotelInfoService.RmtypeAdd(URL, APPKEY, SECRET, SESSIONKEY, json_data.getString("hotelid"), json_data.getJSONArray("requestData").getJSONObject(0));
//				
//				
//				if("0".equals(mp.get("code")))
//				{
					HotelInfoService vs = new HotelInfoService();
					try {
						vs.rmtype_add_update(out,json_data);
						logger.info(json_data.getString("hotelid")+"提交房型信息成功");
					} catch (Exception e) {
						logger.error(e);
						out.write(CommonDao.getInstance().generateResponseJSon("rmtype_update_add","1","6000",e.getMessage(),null));
						out.flush();
						out.close();
						e.printStackTrace();
					}
//				}else if("-1".equals(mp.get("code"))){
//					logger.error(json_data.getString("hotelid")+"调用去啊接口（rmtype.add）出错");
//					out.write(CommonDao.getInstance().generateResponseJSon("rmtype_update_add","-1","6000","调用去啊接口（rmtype.add）出错",null));
//					out.flush();
//					out.close();
//				}else{
//					logger.error(json_data.getString("hotelid")+"调用去啊接口（rmtype.add）出错["+mp.get("sub_msg")+"]");
//					out.write(CommonDao.getInstance().generateResponseJSon("rmtype_update_add",mp.get("sub_code"),"6000",mp.get("sub_msg"),null));
//					out.flush();
//					out.close();
//				}
			}else{
				//新版本
				HashMap<String,String> mp = null;
				if("1".equals(json_data.getString("ishotel").trim()))
				{//酒店
					
					 mp = hotelInfoService.RmtypeAdd(URL, APPKEY, SECRET, SESSIONKEY, json_data.getString("hotelid"), json_data.getJSONArray("requestData").getJSONObject(0));
				}else{
				//客栈
					
					 mp = hotelInfoService.RmtypeAdd(URL, APPKEY, SECRET, KEZHAN_SESSIONKEY, json_data.getString("hotelid"), json_data.getJSONArray("requestData").getJSONObject(0));
				}
				if("0".equals(mp.get("code")))
				{
					HotelInfoService vs = new HotelInfoService();
					try {
						vs.rmtype_add_update(out,json_data);
						logger.info(json_data.getString("hotelid")+"提交房型信息成功");
					} catch (Exception e) {
						logger.error(e);
						out.write(CommonDao.getInstance().generateResponseJSon("rmtype_update_add","1","6000",e.getMessage(),null));
						out.flush();
						out.close();
						e.printStackTrace();
					}
				}else if("-1".equals(mp.get("code"))){
					logger.error(json_data.getString("hotelid")+"调用去啊接口（rmtype.add）出错");
					out.write(CommonDao.getInstance().generateResponseJSon("rmtype_update_add","-1","6000","调用去啊接口（rmtype.add）出错",null));
					out.flush();
					out.close();
				}else{
					logger.error(json_data.getString("hotelid")+"调用去啊接口（rmtype.add）出错["+mp.get("sub_msg")+"]");
					out.write(CommonDao.getInstance().generateResponseJSon("rmtype_update_add",mp.get("sub_code"),"6000",mp.get("sub_msg"),null));
					out.flush();
					out.close();
				}
			}
			
			
			
		}
		//RP信息的添加/修改
		else if("rateplan_add_update".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			
			if(!json_data.has("ishotel"))
			{//兼容老版本
				
//				HashMap<String, String> mp = hotelInfoService.RatePlanAdd(URL, APPKEY, SECRET, SESSIONKEY, json_data.getString("hotelid"),json_data.getJSONArray("requestData").getJSONObject(0));
////				System.out.println("RP修改："+mp.get("msg"));
//				if("0".equals(mp.get("code")))
//				{
					HotelInfoService vs = new HotelInfoService();
					try {
						vs.rateplan_add_update(out,json_data);
						logger.info(json_data.getString("hotelid")+"提交RP信息成功");
					} catch (Exception e) {
						logger.error(e);
						out.write(CommonDao.getInstance().generateResponseJSon("rateplan_add_update","1","6000",e.getMessage(),null));
						out.flush();
						out.close();
						e.printStackTrace();
					}
//				}else if("-1".equals(mp.get("code"))){
//					logger.error(json_data.getString("hotelid")+"调用去啊接口（rateplan.add）出错");
//					out.write(CommonDao.getInstance().generateResponseJSon("rateplan_add_update","-1","6000","调用去啊接口（rateplan.add）出错",null));
//					out.flush();
//					out.close();
//				}else{
//					logger.error(json_data.getString("hotelid")+"调用去啊接口（rateplan.add）出错["+mp.get("sub_msg")+"]");
//					out.write(CommonDao.getInstance().generateResponseJSon("rateplan_add_update",mp.get("sub_code"),"6000",mp.get("sub_msg"),null));
//					out.flush();
//					out.close();
//				}
			}else{
				//新版本
				HashMap<String,String> mp = null;
				if("1".equals(json_data.getString("ishotel").trim()))
				{//酒店
					
					mp = hotelInfoService.RatePlanAdd(URL, APPKEY, SECRET, SESSIONKEY, json_data.getString("hotelid"),json_data.getJSONArray("requestData").getJSONObject(0));
				}else{
				//客栈
				
					mp = hotelInfoService.RatePlanAdd(URL, APPKEY, SECRET, KEZHAN_SESSIONKEY, json_data.getString("hotelid"),json_data.getJSONArray("requestData").getJSONObject(0));
				}
				
				if("0".equals(mp.get("code")))
				{
					HotelInfoService vs = new HotelInfoService();
					try {
						vs.rateplan_add_update(out,json_data);
						logger.info(json_data.getString("hotelid")+"提交RP信息成功");
					} catch (Exception e) {
						logger.error(e);
						out.write(CommonDao.getInstance().generateResponseJSon("rateplan_add_update","1","6000",e.getMessage(),null));
						out.flush();
						out.close();
						e.printStackTrace();
					}
				}else if("-1".equals(mp.get("code"))){
					logger.error(json_data.getString("hotelid")+"调用去啊接口（rateplan.add）出错");
					out.write(CommonDao.getInstance().generateResponseJSon("rateplan_add_update","-1","6000","调用去啊接口（rateplan.add）出错",null));
					out.flush();
					out.close();
				}else{
					logger.error(json_data.getString("hotelid")+"调用去啊接口（rateplan.add）出错["+mp.get("sub_msg")+"]");
					out.write(CommonDao.getInstance().generateResponseJSon("rateplan_add_update",mp.get("sub_code"),"6000",mp.get("sub_msg"),null));
					out.flush();
					out.close();
				}
			}
			
			
			
		}
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
