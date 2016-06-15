package com.dl.servlet;

import java.awt.print.Book;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.dl.dao.BaseDao;
import com.dl.dao.CommonDao;
import com.dl.dao.HotelInfoDao;
import com.dl.dao.PollingOrderDao;
import com.dl.dao.SelectDao;
import com.dl.datasource.DBPool;
import com.dl.pojo.Advertisement;
import com.dl.pojo.BookRQ;
import com.dl.pojo.Hotel_info;
import com.dl.pojo.Mstinfo_his;
import com.dl.pojo.OrderInfo;
import com.dl.pojo.Rateplan;
import com.dl.pojo.Rm_type;
import com.dl.utl.CommonTool;
import com.dl.utl.HttpSend_GET;

public class NStandardServlet extends HttpServlet {

	private final static String SYNC_ORDER_URL = "http://polling.dlhis.com:8080/xyz_lunxun/requestServlet";
//	private final static String SYNC_ORDER_URL = "http://127.0.0.1:8080/xyz_lunxun/requestServlet";
	private Logger logger = Logger.getLogger(NStandardServlet.class); 
	/**
	 * Constructor of the object.
	 */
	public NStandardServlet() {
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
	public void doGet(HttpServletRequest request,HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		SelectDao selectDao=new SelectDao();
		StringBuilder sb = new StringBuilder(request.getParameter("data"));
		JSONObject json_data = null;
		try {
			json_data = JSONObject.fromObject(sb.toString());
			 
			 
		} catch (Exception e) {
			logger.error(e);
			out.write(CommonDao.getInstance().generateResponseJSon("params_error","1","1000","参数转换JSon错误("+e.getMessage()+")",null));
			out.flush();
			out.close();
			return;
		}
		logger.info("轮询插件获取订单信息："+json_data);
		System.out.println("收到参数111："+json_data.toString());
		String hotelid=json_data.getString("hotelid");
		
		//获取订单信息
		if("orders_get".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			PollingOrderDao pd = PollingOrderDao.getInstance();
		    

					List<BookRQ> list = new ArrayList<BookRQ>();
					for(int i=0;i<json_data.getJSONArray("orders_create").size();i++)
					{
						list.add(pd.getBookOrderByTaobaoid(json_data.getJSONArray("orders_create").getJSONObject(i)));
					}
					logger.info("轮询插件获取订单成功");
					out.write(CommonDao.getInstance().generateResponseJSon("orders_get","0","0000","获取订单成功",JSONArray.fromObject(list)));
					out.flush();
					out.close();
		}
		
		if(!hotelid.equals("")){
			
		 if("hotel_info".equalsIgnoreCase(json_data.getString("serviceName").trim())){
			 
			    List<Hotel_info> list = new ArrayList<Hotel_info>();
			    list=selectDao.hotel_info(hotelid);
			 
		        out.print(JSONArray.fromObject(list));
		    	out.flush();
		    	out.close();
			
		}else if("rm_type".equalsIgnoreCase(json_data.getString("serviceName").trim())){
			
			List<Rm_type> list = new ArrayList<Rm_type>();
			list=selectDao.Rm_type(hotelid);
			
		        out.print(JSONArray.fromObject(list));
		    	out.flush();
		    	out.close();
			
			
			
		}else if("rate_plan".equalsIgnoreCase(json_data.getString("serviceName").trim())){
			List<Rateplan> list = new ArrayList<Rateplan>();
			list=selectDao.Rateplan(hotelid);
			
		        out.print(JSONArray.fromObject(list));
		    	out.flush();
		    	out.close();
			
			
		}else if("mstinfo".equalsIgnoreCase(json_data.getString("serviceName").trim())){
			String bdate=json_data.getString("bdate");
			String edate=json_data.getString("edate");
			
			List<Mstinfo_his> list = new ArrayList<Mstinfo_his>();
			list=selectDao.Mstinfo_his(hotelid, bdate, edate);
		    out.print(JSONArray.fromObject(list));
		    out.flush();
		    out.close();
			
		}
		}else{
			out.print("{'msg':'酒店Id不能为空'}");
	    	out.flush();
	    	out.close();
		}
		
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

		response.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = response.getWriter();
		StringBuilder sb = new StringBuilder(request.getParameter("data"));
//		BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream(),"utf-8"));
//		String line = null;
//		StringBuilder sb = new StringBuilder();
//		while ((line = br.readLine()) != null) {
//			sb.append(line);
//		}
	
		JSONObject json_data = null;
//		System.out.println(request.getParameter("data"));
		try {
			json_data = JSONObject.fromObject(sb.toString());
		} catch (Exception e) {
			logger.error(e);
			out.write(CommonDao.getInstance().generateResponseJSon("params_error","1","1000","参数转换JSon错误("+e.getMessage()+")",null));
			out.flush();
			out.close();
			return;
		}
		
		System.out.println("参数2222："+json_data);
		//订单反馈接收
		if("orders_feedback".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			PollingOrderDao pollingOrderDao = PollingOrderDao.getInstance();
			if(json_data.has("orders_create"))
			{
				if(json_data.has("orders_cancel"))
				{
					if(pollingOrderDao.order_create_update_batch(json_data.getJSONArray("orders_create"))&&pollingOrderDao.order_cancel_update_batch(json_data.getJSONArray("orders_cancel")))
					{
						//修改订单状态成功
						logger.info("插件订单反馈订单成功");
						//同步订单状态
						if(sync_order(json_data.getJSONArray("orders_create"),json_data.getJSONArray("orders_cancel")))
						{   
							logger.info("插件同步订单成功");
							out.write(CommonDao.getInstance().generateResponseJSon("orders_feedback","0","0000","订单反馈成功",null));
							out.flush();
							out.close();
						}else{
							logger.info("插件同步订单失败");
							out.write(CommonDao.getInstance().generateResponseJSon("orders_feedback","1","1000","订单反馈失败(订单同步失败)",null));
							out.flush();
							out.close();
						}
						
					}else{
						//修改订单状态失败
						out.write(CommonDao.getInstance().generateResponseJSon("orders_feedback","1","1000","订单反馈失败",null));
						out.flush();
						out.close();
					}
				}else{
					//orders_create存在，orders_cancel不存在
					if(pollingOrderDao.order_create_update_batch(json_data.getJSONArray("orders_create")))
					{
						//修改订单状态成功
						//同步订单状态
						if(sync_order(json_data.getJSONArray("orders_create"),null))
						{
							out.write(CommonDao.getInstance().generateResponseJSon("orders_feedback","0","0000","订单反馈成功",null));
							out.flush();
							out.close();
						}else{
							out.write(CommonDao.getInstance().generateResponseJSon("orders_feedback","1","1000","订单反馈失败(订单同步失败)",null));
							out.flush();
							out.close();
						}
						
					}else{
						//修改订单状态失败
						out.write(CommonDao.getInstance().generateResponseJSon("orders_feedback","1","1000","订单反馈失败",null));
						out.flush();
						out.close();
					}
				}
			}else{
				if(json_data.has("orders_cancel"))
				{
					if(pollingOrderDao.order_cancel_update_batch(json_data.getJSONArray("orders_cancel")))
					{
						//修改订单状态成功
						//同步订单状态
						if(sync_order(null,json_data.getJSONArray("orders_cancel")))
						{
							out.write(CommonDao.getInstance().generateResponseJSon("orders_feedback","0","0000","订单反馈成功",null));
							out.flush();
							out.close();
						}else{
							out.write(CommonDao.getInstance().generateResponseJSon("orders_feedback","1","1000","订单反馈失败(订单同步失败)",null));
							out.flush();
							out.close();
						}
					}else{
						//修改订单状态失败
						out.write(CommonDao.getInstance().generateResponseJSon("orders_feedback","1","1000","订单反馈失败",null));
						out.flush();
						out.close();
					}
				}
			}
		
			
		}
		
		
		else if("ota_orders_feedback".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			PollingOrderDao pollingOrderDao = PollingOrderDao.getInstance();
			if(json_data.has("orders_create"))
			{
				if(json_data.has("orders_cancel"))
				{
					if(pollingOrderDao.order_create_update_batch(json_data.getJSONArray("orders_create"))&&pollingOrderDao.order_cancel_update_batch(json_data.getJSONArray("orders_cancel")))
					{
						//修改订单状态成功
						logger.info("插件订单反馈订单成功");
						out.write(CommonDao.getInstance().generateResponseJSon("ota_orders_feedback","0","0000","订单反馈成功",null));
						out.flush();
						out.close();
						
					}else{
						//修改订单状态失败
						out.write(CommonDao.getInstance().generateResponseJSon("ota_orders_feedback","1","1000","订单反馈失败",null));
						out.flush();
						out.close();
					}
				}else{
					//orders_create存在，orders_cancel不存在
					if(pollingOrderDao.order_create_update_batch(json_data.getJSONArray("orders_create")))
					{
						//修改订单状态成功
						//同步订单状态
						if(sync_order(json_data.getJSONArray("orders_create"),null))
						{
							out.write(CommonDao.getInstance().generateResponseJSon("orders_feedback","0","0000","订单反馈成功",null));
							out.flush();
							out.close();
						}else{
							out.write(CommonDao.getInstance().generateResponseJSon("orders_feedback","1","1000","订单反馈失败(订单同步失败)",null));
							out.flush();
							out.close();
						}
						
					}else{
						//修改订单状态失败
						out.write(CommonDao.getInstance().generateResponseJSon("orders_feedback","1","1000","订单反馈失败",null));
						out.flush();
						out.close();
					}
				}
			}else{
				if(json_data.has("orders_cancel"))
				{
					if(pollingOrderDao.order_cancel_update_batch(json_data.getJSONArray("orders_cancel")))
					{
						//修改订单状态成功
						//同步订单状态
						if(sync_order(null,json_data.getJSONArray("orders_cancel")))
						{
							out.write(CommonDao.getInstance().generateResponseJSon("orders_feedback","0","0000","订单反馈成功",null));
							out.flush();
							out.close();
						}else{
							out.write(CommonDao.getInstance().generateResponseJSon("orders_feedback","1","1000","订单反馈失败(订单同步失败)",null));
							out.flush();
							out.close();
						}
					}else{
						//修改订单状态失败
						out.write(CommonDao.getInstance().generateResponseJSon("orders_feedback","1","1000","订单反馈失败",null));
						out.flush();
						out.close();
					}
				}
			}
		
			
		}
	}
	
	private boolean sync_order(JSONArray ja_order_create,JSONArray ja_order_cancel)
	{
		boolean flag = false;
		List<OrderInfo> list = new ArrayList<OrderInfo>();
		if(ja_order_create!=null)
		{
			for(int i=0;i<ja_order_create.size();i++)
			{
				OrderInfo oi = new OrderInfo();
				oi.setTaobaoid(ja_order_create.getJSONObject(i).getString("taobaoid"));
				oi.setHotelid(ja_order_create.getJSONObject(i).getString("hotelid"));
				oi.setMrk(ja_order_create.getJSONObject(i).getString("mrk"));
				list.add(oi);
			}
		}
		if(ja_order_cancel!=null)
		{
			for(int i=0;i<ja_order_cancel.size();i++)
			{
				OrderInfo oi = new OrderInfo();
				oi.setTaobaoid(ja_order_cancel.getJSONObject(i).getString("taobaoid"));
				oi.setHotelid(ja_order_cancel.getJSONObject(i).getString("hotelid"));
				oi.setMrk(ja_order_cancel.getJSONObject(i).getString("mrk"));
				list.add(oi);
			}
		}
		
		Map<String, Object> mp = new HashMap<String, Object>();
		mp.put("serviceName","orders_sync");
		mp.put("requestData",JSONArray.fromObject(list));
		
		JSONObject params = JSONObject.fromObject(mp);
		JSONObject result_jo = HttpSend_GET.SendPOST(SYNC_ORDER_URL, params);
		if(Integer.valueOf(result_jo.getString("status"))==0)
		{
			flag = true;
		}
		
		return flag;
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
