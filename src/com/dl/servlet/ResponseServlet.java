package com.dl.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.dl.dao.CommonDao;
import com.dl.dao.HotelInfoDao;
import com.dl.dao.OrderDao;
import com.dl.pojo.Advertisement;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ResponseServlet extends HttpServlet {

	private Logger logger = Logger.getLogger(ResponseServlet.class); 
	/**
	 * Constructor of the object.
	 */
	public ResponseServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}
	
	

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException,IOException {
		resp.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = resp.getWriter();
		JSONObject json_data = null;
		System.out.println(req.getParameter("data"));
		try {
			json_data = JSONObject.fromObject(req.getParameter("data"));
		} catch (Exception e) {
			logger.error(e);
			out.write(CommonDao.getInstance().generateResponseJSon("advertisement_get","1","1000","参数转换JSon错误("+e.getMessage()+")",null));
			out.flush();
			out.close();
			return;
		}
		
		if("advertisement_get".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			logger.info("获取广告信息："+json_data);
			Advertisement ad = HotelInfoDao.getInstance().getAdvertisement();
			List<Advertisement> list = new ArrayList<Advertisement>();
			list.add(ad);
			out.write(CommonDao.getInstance().generateResponseJSon("advertisement_get","0","0000","返回成功",JSONArray.fromObject(list)));
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

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
	   JSONObject jo_data = JSONObject.fromObject(sb.toString());
	   OrderDao orderdao = OrderDao.getInstance();
	   logger.info("获取链路器订单反馈信息："+jo_data);
	   if(jo_data.getInt("Command")==1001)
	   {//下单反馈
		  System.out.println("下单反馈");
		   if(jo_data.getInt("BackCode")==0)
		   {
			   //确认订单
			 //创建订单成功
			   System.out.println("下单反馈确认成功");
			   logger.info("下单反馈确认成功");
				orderdao.order_update(jo_data.getString("BackMsg"), jo_data.getString("TaoBaoOrderId"),jo_data.getString("Mkt"));
			   
		   }else{
			 //创建订单失败（满房）
			   System.out.println("下单反馈确认成功(满房)");
			   logger.info("下单反馈确认成功(满房)");
				orderdao.order_updateForFull(jo_data.getString("TaoBaoOrderId"),jo_data.getString("Mkt"));
		   }
	   }else if(jo_data.getInt("Command")==1011)
	   {//取消订单反馈
		   if(jo_data.getInt("BackCode")==0)
		   {
			   //取消订单成功
			   logger.info("取消订单反馈确认成功");
			   orderdao.order_updateForCancel(jo_data.getString("TaoBaoOrderId"),jo_data.getString("Mkt"));
//				orderdao.order_update(jo_data.getString("backmsg"),jo_data.getString("taoBaoOrderId"),jo_data.getString("mkt"));
			   
		   }else{
			 //取消订单失败
			   logger.info("取消订单反馈确认失败");
				orderdao.order_updateForCancelFailed(jo_data.getString("TaoBaoOrderId"),jo_data.getString("Mkt"),jo_data.getString("BackMsg"));
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
