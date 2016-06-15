package com.dl.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import com.dl.dao.CommonDao;
import com.dl.dao.DispatcherDao;
import com.dl.dao.OrderDao;
import com.dl.dao.PollingOrderDao;
import com.dl.pojo.BookRQ;
import com.dl.pojo.BookValidateRQ;
import com.dl.pojo.CancelRQ;
import com.dl.pojo.DailyInfo;
import com.dl.pojo.OTABookRQ;
import com.dl.pojo.OTA_RetrieveBookRQ;
import com.dl.pojo.OrderInfo;
import com.dl.service.RZT_OrderService;
import com.dl.utl.CommonTool;
import com.dl.utl.Generator;
import com.dl.utl.HttpSend_GET;
import com.dl.utl.SoapUtl;
import com.dl.utl.XMLGenerator;
import com.dl.utl.XMLGenerator_RZT;

public class OrderReceiveServlet extends HttpServlet {
    //德鹏下单接口url
//	private final static String BOOKRQ_URL = "http://114.215.242.222:9979/srv/?handler=order";
//	private final static String BOOKCANCELRQ_URL = "http://114.215.242.222:9979/srv/?handler=order";
//	private final static String BOOKUPDATE_URL = "http://114.215.242.222:9979/srv/?handler=order";
//	private static final String PUSH_ORDER_URL = "http://114.215.242.222:8080/xiecheng_lunxun/transportServlet";
//	private final static String SYNC_ORDER_URL = "http://114.215.242.222:8080/xiecheng_lunxun/requestServlet";
	private static final String PUSH_ORDER_URL = "http://OTA-polling.dlhis.com:8080/xiecheng_lunxun/transportServlet";
	private final static String SYNC_ORDER_URL = "http://OTA-polling.dlhis.com:8080/xiecheng_lunxun/requestServlet";
	private Logger logger = Logger.getLogger(OrderReceiveServlet.class); 
	/**
	 * Constructor of the object.
	 */
	public OrderReceiveServlet() {
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

		response.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = response.getWriter();
		JSONObject json_data = null;
		try {
			logger.info("手动或自动获取订单原始数据："+request.getParameter("data"));
			json_data = JSONObject.fromObject(request.getParameter("data"));
			logger.info("手动或自动获取订单json："+json_data);
		} catch (Exception e) {
			logger.error(e);
			out.write(CommonDao.getInstance().generateResponseJSon_SHR("GetOTAOrder","1","1000","参数转换JSon错误("+e.getMessage()+")",null));
			out.flush();
			out.close();
			return;
		}
		System.out.println("参数："+json_data);
		//手动获取订单
		if("GetOTAOrder".equalsIgnoreCase(json_data.getString("serviceName")))
		{
			OrderDao orderDao = OrderDao.getInstance();
			OTA_RetrieveBookRQ otaRetrieveBookRQ = orderDao.getRetrieveOrder(json_data);
			System.out.println("otaRetrieveBookRQ:"+otaRetrieveBookRQ);
			if(otaRetrieveBookRQ==null)
			{
				//无法获取该单号下的订单
				logger.error("手工抓单失败：无法获取该单号下的订单:"+json_data);
				logger.error(request.getRemoteAddr()+":"+request.getRemoteHost());
				out.write(CommonDao.getInstance().generateResponseJSon_SHR("GetOTAOrder","1","1000","未找到单号("+json_data.getString("orderid")+")的订单！",null));
				out.flush();
				out.close();
			}else{
				//获取指定单号下的ota订单成功
				logger.info("手动获取订单成功"+JSONArray.fromObject(otaRetrieveBookRQ));
				out.write(CommonDao.getInstance().generateResponseJSon_SHR("GetOTAOrder","0","0000","找到单号("+json_data.getString("orderid")+")的订单！",JSONArray.fromObject(otaRetrieveBookRQ)));
				out.flush();
				out.close();
			}
		}
		
		//自动获取订单信息
		else if("orders_get_auto".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			PollingOrderDao pd = PollingOrderDao.getInstance();
		    

					List<BookRQ> list = new ArrayList<BookRQ>();
					for(int i=0;i<json_data.getJSONArray("orders_create").size();i++)
					{
						list.add(pd.getOTABookOrderByTaobaoid(json_data.getJSONArray("orders_create").getJSONObject(i)));
					}
					logger.info("自动获取订单成功"+JSONArray.fromObject(list));
					System.out.println("反馈结果："+CommonDao.getInstance().generateResponseJSon_SHR("orders_get_auto","0","0000","获取订单成功",JSONArray.fromObject(list)));
					out.write(CommonDao.getInstance().generateResponseJSon_SHR("orders_get_auto","0","0000","获取订单成功",JSONArray.fromObject(list)));
					out.flush();
					out.close();
		}else if("orders_feedback".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			PollingOrderDao pollingOrderDao = PollingOrderDao.getInstance();
			if(json_data.has("orders_create"))
			{
				if(json_data.has("orders_cancel"))
				{
					if(pollingOrderDao.OTAorder_create_update_batch(json_data.getJSONArray("orders_create"))&&pollingOrderDao.OTAorder_cancel_update_batch(json_data.getJSONArray("orders_cancel")))
					{
						//修改订单状态成功
						logger.info("众荟订单反馈订单成功");
						//同步订单状态
						if(sync_order(json_data.getJSONArray("orders_create"),json_data.getJSONArray("orders_cancel")))
						{   
							logger.info("众荟同步订单成功");
							out.write(CommonDao.getInstance().generateResponseJSon_SHR("orders_feedback","0","0000","订单反馈成功",null));
							out.flush();
							out.close();
						}else{
							logger.error("众荟同步订单失败,同步参数："+json_data);
							out.write(CommonDao.getInstance().generateResponseJSon_SHR("orders_feedback","1","1000","订单反馈失败(订单同步失败)",null));
							out.flush();
							out.close();
						}
						
					}else{
						//修改订单状态失败
						logger.error(json_data.toString()+"众荟同步订单失败");
						logger.error("众荟同步订单失败，修改订单失败："+json_data);
						out.write(CommonDao.getInstance().generateResponseJSon_SHR("orders_feedback","1","1000","订单反馈失败",null));
						out.flush();
						out.close();
					}
				}else{
					//orders_create存在，orders_cancel不存在
					if(pollingOrderDao.OTAorder_create_update_batch(json_data.getJSONArray("orders_create")))
					{
						//修改订单状态成功
						//同步订单状态
						if(sync_order(json_data.getJSONArray("orders_create"),null))
						{
							
							logger.info("众荟同步订单成功");
							out.write(CommonDao.getInstance().generateResponseJSon_SHR("orders_feedback","0","0000","订单反馈成功",null));
							out.flush();
							out.close();
						}else{
							
							logger.error("众荟同步订单失败,同步参数："+json_data);
							out.write(CommonDao.getInstance().generateResponseJSon_SHR("orders_feedback","1","1000","订单反馈失败(订单同步失败)",null));
							out.flush();
							out.close();
						}
						
					}else{
						//修改订单状态失败
//						logger.error(json_data.toString()+"众荟同步订单失败");
						logger.error("众荟同步订单失败，修改订单失败："+json_data);
						out.write(CommonDao.getInstance().generateResponseJSon_SHR("orders_feedback","1","1000","订单反馈失败",null));
						out.flush();
						out.close();
					}
				}
			}else{
				if(json_data.has("orders_cancel"))
				{
					if(pollingOrderDao.OTAorder_cancel_update_batch(json_data.getJSONArray("orders_cancel")))
					{
						//修改订单状态成功
						//同步订单状态
						if(sync_order(null,json_data.getJSONArray("orders_cancel")))
						{
							logger.info("众荟同步订单成功");
							out.write(CommonDao.getInstance().generateResponseJSon_SHR("orders_feedback","0","0000","订单反馈成功",null));
							out.flush();
							out.close();
						}else{
							logger.error("众荟同步订单失败,同步参数："+json_data);
							out.write(CommonDao.getInstance().generateResponseJSon_SHR("orders_feedback","1","1000","订单反馈失败(订单同步失败)",null));
							out.flush();
							out.close();
						}
					}else{
						//修改订单状态失败
//						logger.error(json_data.toString()+"众荟同步订单失败");
//						logger.error("众荟同步订单失败");
						logger.error("众荟同步订单失败，修改订单失败："+json_data);
						out.write(CommonDao.getInstance().generateResponseJSon_SHR("orders_feedback","1","1000","订单反馈失败",null));
						out.flush();
						out.close();
					}
				}
			
			}
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

		response.setContentType("text/xml");
		System.out.println("调用了 post方法");
		PrintWriter out = response.getWriter();
		BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream(),"utf-8"));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		logger.info("收到来自众荟的请求："+sb.toString());
		try {
			SOAPBody body = SoapUtl.str2soapbody(sb.toString());
			NodeList nodelist = body.getElementsByTagName("OTA_HotelResRQ");
			String opType = nodelist.item(0).getAttributes().getNamedItem("ResStatus").getNodeValue();
			System.out.println("body:"+body);
		   System.out.println("opType:"+opType);
			if("Commit".equalsIgnoreCase(opType.trim()))
			{
				
				//预定
				JSONObject json_otaBookRQ = SoapUtl.soap2OTABookRQ(body);
				OrderDao orderdao = OrderDao.getInstance();
				Map<String, Object> result = orderdao.ota_validate_order_id(json_otaBookRQ.getString("taoBaoOrderId"));
				  switch ((Integer)result.get("code")) {
					case 0:
						//订单号未重复,校验价格是否一致
						RZT_OrderService rztOrderService = new RZT_OrderService();
						BookValidateRQ bvr = new BookValidateRQ();
						bvr.setHotelid(json_otaBookRQ.getString("hotelid"));
						bvr.setRmtype(json_otaBookRQ.getString("roomTypeId"));
						bvr.setRpcode(json_otaBookRQ.getString("ratePlanCode"));
						bvr.setDailyinfos(json_otaBookRQ.getJSONArray("dailyInfos"));
						System.out.println("请求参数："+JSONObject.fromObject(bvr));
						if(!rztOrderService.IsRightPrice(JSONObject.fromObject(bvr)))
						{
							//校验后价格不一致
							 Document doc = XMLGenerator_RZT.XMLGenerator_Res_Error("下单价格和酒店设置价格不一致","Committed");
							 String doc_str = doc.asXML();
							 doc_str = doc_str.replace(doc_str.substring(doc_str.indexOf("<"), doc_str.indexOf(">")+1), "");
							 String soapBegin = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><SOAP-ENV:Body>";
							 String soapEnd = "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
							 StringBuilder res_error = new StringBuilder(soapBegin+doc_str+soapEnd);
							 System.out.println("返回结果:"+res_error);
							 out.print(res_error.toString());
							 out.flush();
							 out.close();
							 logger.info("下单返回给众荟："+json_otaBookRQ.getString("taoBaoOrderId")+res_error.toString());
						}else{
							//生成唯一伪订单确认编号
							Generator generator = Generator.getInstance();
							String pmsresid = generator.getPMSOrderComfireId();
							if(pmsresid == null)
							{
								 logger.error("订单("+json_otaBookRQ.getString("taoBaoOrderId")+")生成确认单号失败");
								 Document doc = XMLGenerator_RZT.XMLGenerator_Res_Error("订单生成确认单号失败","Committed");
								 String doc_str = doc.asXML();
								 doc_str = doc_str.replace(doc_str.substring(doc_str.indexOf("<"), doc_str.indexOf(">")+1), "");
								 String soapBegin = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><SOAP-ENV:Body>";
								 String soapEnd = "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
								 StringBuilder res_error = new StringBuilder(soapBegin+doc_str+soapEnd);	
								 out.print(res_error.toString());
								 out.flush();
								 out.close();
								 logger.info("下单返回给众荟："+json_otaBookRQ.getString("taoBaoOrderId")+res_error.toString());
							}else{
									boolean issuccess = OrderDao.getInstance().ota_order_add(json_otaBookRQ,pmsresid);
									if(issuccess)
									{
										//接收到的订单信息插入mstinfo表成功
										//将订单推送ZTX的订单服务，并且同步接收反馈
										 logger.info("来自众荟平台订单("+json_otaBookRQ.getString("taoBaoOrderId")+")");
										 CommonDao commonDao = CommonDao.getInstance();
										JSONObject param = commonDao.generateOrders_transport("orders_create", json_otaBookRQ.getString("taoBaoOrderId"), json_otaBookRQ.getString("hotelid"), json_otaBookRQ.getString("mkt"),json_otaBookRQ.getString("latestarrivetime"));
										JSONObject result_jo = HttpSend_GET.SendPOST(PUSH_ORDER_URL,param);
										if(Integer.valueOf(result_jo.getString("status"))==0)
										{
											logger.info("来自众荟平台订单("+json_otaBookRQ.getString("taoBaoOrderId")+"),推送成功");
										}else{
											logger.info("来自众荟平台订单("+json_otaBookRQ.getString("taoBaoOrderId")+"),推送失败");
										}
										 Document doc = XMLGenerator_RZT.XMLGenerator_Res_SUCCESS(json_otaBookRQ.getString("mkt"),json_otaBookRQ,pmsresid,"Committed");
										 String doc_str = doc.asXML();
										 doc_str = doc_str.replace(doc_str.substring(doc_str.indexOf("<"), doc_str.indexOf(">")+1), "");
										 String soapBegin = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><SOAP-ENV:Body>";
										 String soapEnd = "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
										 StringBuilder res_success = new StringBuilder(soapBegin+doc_str+soapEnd);	
										 out.print(res_success.toString());
										 out.flush();
										 out.close();
										 logger.info("下单返回给众荟："+json_otaBookRQ.getString("taoBaoOrderId")+res_success.toString());
					
									}else{
										//订单插入mstinfo表失败
										 logger.info("来自众荟平台订单("+json_otaBookRQ.toString()+")插入mstinfo_ota失败");
										 Document doc = XMLGenerator_RZT.XMLGenerator_Res_Error("订单插入mstinfo_ota失败","Committed");
										 String doc_str = doc.asXML();
										 doc_str = doc_str.replace(doc_str.substring(doc_str.indexOf("<"), doc_str.indexOf(">")+1), "");
										 String soapBegin = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><SOAP-ENV:Body>";
										 String soapEnd = "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
										 StringBuilder res_error = new StringBuilder(soapBegin+doc_str+soapEnd);	
										 out.print(res_error.toString());
										 out.flush();
										 out.close();
										 logger.info("下单返回给众荟："+json_otaBookRQ.getString("taoBaoOrderId")+res_error.toString());
									}
							}
						}
						
						
						
						break;
	                case 2:
	                	 System.out.println("code:2");
	                	//该订单号已下过单，且pms端已确认
	                	 logger.info("来自众荟平台订单("+json_otaBookRQ.toString()+")重复下单");
						 Document doc = XMLGenerator_RZT.XMLGenerator_Res_Error("重复下单","Committed");
						 String doc_str = doc.asXML();
						 doc_str = doc_str.replace(doc_str.substring(doc_str.indexOf("<"), doc_str.indexOf(">")+1), "");
						 String soapBegin = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><SOAP-ENV:Body>";
						 String soapEnd = "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
						 StringBuilder res_error = new StringBuilder(soapBegin+doc_str+soapEnd);	
						 out.print(res_error.toString());
						 out.flush();
						 out.close();
						 logger.info("下单返回给众荟："+json_otaBookRQ.getString("taoBaoOrderId")+res_error.toString());
		                break;
					default:
						break;
					}	
				
			}else if("Cancel".equalsIgnoreCase(opType.trim()))
			{
				//取消订单
				
				JSONObject json_otaCancelRQ = SoapUtl.soap2OTACancelRQ(body);
				 OrderDao orderdao = OrderDao.getInstance();
		         //检查该订单号是否已存在
		         Map<String, Object> result = orderdao.cancel_ota_order(json_otaCancelRQ.getString("taoBaoOrderId"),json_otaCancelRQ.getString("hotelid"),json_otaCancelRQ.getString("pmsresid"));
		         Document doc_cancelled = null;
		         String doc_cancelled_str = null;
		         String soapBegin1 = null;
		         String soapEnd1 = null;
		         StringBuilder res_error1 = null;
		         switch ((Integer)result.get("code")) {
					case 0:
						//待取消订单号不存在
						 Document doc_not_exists = XMLGenerator_RZT.XMLGenerator_Res_Error("待取消订单号不存在","Cancelled");
						 String doc_str = doc_not_exists.asXML();
						 doc_str = doc_str.replace(doc_str.substring(doc_str.indexOf("<"), doc_str.indexOf(">")+1), "");
						 String soapBegin = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><SOAP-ENV:Body>";
						 String soapEnd = "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
						 StringBuilder res_error = new StringBuilder(soapBegin+doc_str+soapEnd);	
						 out.print(res_error.toString());
						 out.flush();
						 out.close();
						
						break;
	                case 4:
	                	 //待取消订单已处于取消状态，无需取消
	                	  doc_cancelled = XMLGenerator_RZT.XMLGenerator_Res_Error("待取消订单已处于取消状态，无需重复取消","Cancelled");
						  doc_cancelled_str = doc_cancelled.asXML();
						 doc_cancelled_str = doc_cancelled_str.replace(doc_cancelled_str.substring(doc_cancelled_str.indexOf("<"), doc_cancelled_str.indexOf(">")+1), "");
						  soapBegin1 = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><SOAP-ENV:Body>";
						  soapEnd1 = "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
						  res_error1 = new StringBuilder(soapBegin1+doc_cancelled_str+soapEnd1);	
						 out.print(res_error1.toString());
						 out.flush();
						 out.close();
						break;
	                case 5:
	                	 //待取消订单已处于入住状态，取消失败
	                	  doc_cancelled = XMLGenerator_RZT.XMLGenerator_Res_Error("待取消订单已处于入住状态，取消失败","Cancelled");
						  doc_cancelled_str = doc_cancelled.asXML();
						 doc_cancelled_str = doc_cancelled_str.replace(doc_cancelled_str.substring(doc_cancelled_str.indexOf("<"), doc_cancelled_str.indexOf(">")+1), "");
						  soapBegin1 = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><SOAP-ENV:Body>";
						  soapEnd1 = "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
						  res_error1 = new StringBuilder(soapBegin1+doc_cancelled_str+soapEnd1);	
						 out.print(res_error1.toString());
						 out.flush();
						 out.close();
						break;	
	                case 8:
	                	 //待取消订单已处于noshow状态，取消失败
	                	  doc_cancelled = XMLGenerator_RZT.XMLGenerator_Res_Error("待取消订单已处于noshow状态，取消失败","Cancelled");
						  doc_cancelled_str = doc_cancelled.asXML();
						 doc_cancelled_str = doc_cancelled_str.replace(doc_cancelled_str.substring(doc_cancelled_str.indexOf("<"), doc_cancelled_str.indexOf(">")+1), "");
						  soapBegin1 = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><SOAP-ENV:Body>";
						  soapEnd1 = "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
						  res_error1 = new StringBuilder(soapBegin1+doc_cancelled_str+soapEnd1);	
						 out.print(res_error1.toString());
						 out.flush();
						 out.close();
						break;	
	                case 9:
	                	 //待取消订单已处于离店状态，取消失败
	                	  doc_cancelled = XMLGenerator_RZT.XMLGenerator_Res_Error("待取消订单已处于离店状态，取消失败","Cancelled");
						  doc_cancelled_str = doc_cancelled.asXML();
						  doc_cancelled_str = doc_cancelled_str.replace(doc_cancelled_str.substring(doc_cancelled_str.indexOf("<"), doc_cancelled_str.indexOf(">")+1), "");
						  soapBegin1 = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><SOAP-ENV:Body>";
						  soapEnd1 = "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
						  res_error1 = new StringBuilder(soapBegin1+doc_cancelled_str+soapEnd1);	
						  out.print(res_error1.toString());
						  out.flush();
						  out.close();
						break;			
	                case 2:
	                	//可以取消，向pms推送取消订单请求
	                	//json_otaCancelRQ取消订单的参数
//						//向dp发送取消订单请求
	                	//将订单推送ldp的订单服务，并且同步接收反馈
						//取消成功
	                	
	                	 logger.info("来自众荟平台取消订单("+json_otaCancelRQ.getString("taoBaoOrderId")+")");
	                	 CommonDao commonDao = CommonDao.getInstance();
						JSONObject param = commonDao.generateOrders_transport("orders_cancel", json_otaCancelRQ.getString("taoBaoOrderId"), json_otaCancelRQ.getString("hotelid"), json_otaCancelRQ.getString("mrk"),null);
						logger.info("2取消订单同步参数："+param);
						System.out.println("2取消订单同步参数："+param);
						JSONObject result_jo = HttpSend_GET.SendPOST(PUSH_ORDER_URL,param);
						if(Integer.valueOf(result_jo.getString("status"))==0)
						{
							System.out.println("取消订单同步ok");
							OrderDao.getInstance().order_updateForOTACancel(json_otaCancelRQ.getString("taoBaoOrderId"), json_otaCancelRQ.getString("mrk"));
							logger.info("来自众荟平台取消订单("+json_otaCancelRQ.getString("taoBaoOrderId")+"),推送成功");
							
						}else{
							System.out.println("取消订单同no");
							logger.error("来自众荟平台取消订单("+json_otaCancelRQ.getString("taoBaoOrderId")+"),推送失败");
						}
	                	 
						 if(orderdao.OTA_order_cancel_success(json_otaCancelRQ.getString("taoBaoOrderId").trim(),(String)result.get("mrk")))
						 {
							 Document doc = XMLGenerator_RZT.XMLGenerator_Cancel_Res_SUCCESS(json_otaCancelRQ.getString("mrk"), json_otaCancelRQ,"Cancelled");
							 String doc_success_str = doc.asXML();
							 doc_success_str = doc_success_str.replace(doc_success_str.substring(doc_success_str.indexOf("<"), doc_success_str.indexOf(">")+1), "");
							 String soapBegin2 = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><SOAP-ENV:Body>";
							 String soapEnd2 = "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
							 StringBuilder res_success = new StringBuilder(soapBegin2+doc_success_str+soapEnd2);
							 logger.info("取消订单返回众荟结果："+res_success.toString());
							 logger.info("res响应头："+response.getContentType());
							 logger.info("res响应头："+response);
							 out.print(res_success.toString());
							 out.flush();
							 out.close();
						 }
						
	                	
						 
		                break;
		                
//	                case 3:
//	                	//可以取消，向pms推送取消订单请求
//	                	 
//	                	 logger.info("来自众荟平台取消订单("+json_otaCancelRQ.getString("taoBaoOrderId")+")");
//						 if(orderdao.OTA_order_cancel_success1(json_otaCancelRQ.getString("taoBaoOrderId").trim(),(String)result.get("mrk")))
//						 {
////							 Map<String, Object> mp = new HashMap<String, Object>();
////							 mp.put("orders_cancel", JSONArray.fromObject(result))
//							 if(sync_order(null,JSONArray.fromObject(result)))
//							 {
//								 Document doc = XMLGenerator_RZT.XMLGenerator_Cancel_Res_SUCCESS(json_otaCancelRQ.getString("mrk"), json_otaCancelRQ,"Cancelled");
//								 String doc_success_str = doc.asXML();
//								 doc_success_str = doc_success_str.replace(doc_success_str.substring(doc_success_str.indexOf("<"), doc_success_str.indexOf(">")+1), "");
//								 String soapBegin2 = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><SOAP-ENV:Body>";
//								 String soapEnd2 = "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
//								 StringBuilder res_success = new StringBuilder(soapBegin2+doc_success_str+soapEnd2);
//								 logger.info("取消订单返回众荟结果："+res_success.toString());
//								 logger.info("res响应头："+response.getContentType());
//								 logger.info("res响应头："+response);
//								 out.print(res_success.toString());
//								 out.flush();
//								 out.close();
//							 }
//							 
//						 }
//						
//	                	
//						 
//		                break;   
					default:
						break;
					}
			}else if("Modify".equalsIgnoreCase(opType.trim()))
			{
				//改单
				JSONObject json_otaUpdateRQ = SoapUtl.soap2OTAUpdateRQ(body);
				logger.info("来自众荟平台的改单请求("+json_otaUpdateRQ.getString("taoBaoOrderId")+")");
				if(!"ELONG".equals(json_otaUpdateRQ.getString("mkt").trim()))
				{
					OrderDao orderdao = OrderDao.getInstance();
					Integer result_code = orderdao.validate_update_order(json_otaUpdateRQ);
					switch (result_code) {
					case 1:
						//向LDP发送改单请求
						 logger.info("来自众荟平台订单("+json_otaUpdateRQ.getString("taoBaoOrderId")+")");
						 //检验改单后的价格是否与酒店设置的一致
						 RZT_OrderService rztOrderService = new RZT_OrderService();
						BookValidateRQ bvr = new BookValidateRQ();
						bvr.setHotelid(json_otaUpdateRQ.getString("hotelid"));
						bvr.setRmtype(json_otaUpdateRQ.getString("roomTypeId"));
						bvr.setRpcode(json_otaUpdateRQ.getString("ratePlanCode"));
						bvr.setDailyinfos(json_otaUpdateRQ.getJSONArray("dailyInfos"));
						System.out.println("请求参数："+JSONObject.fromObject(bvr));
						if(!rztOrderService.IsRightPrice(JSONObject.fromObject(bvr)))
						{
							 logger.error("订单("+json_otaUpdateRQ.getString("taoBaoOrderId")+")改单失败,改单后的价格和酒店设置的不一致");
							 Document doc = XMLGenerator_RZT.XMLGenerator_Res_Error("改单后的价格和酒店设置的不一致","Modified");
							 String doc_str = doc.asXML();
							 doc_str = doc_str.replace(doc_str.substring(doc_str.indexOf("<"), doc_str.indexOf(">")+1), "");
							 String soapBegin = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><SOAP-ENV:Body>";
							 String soapEnd = "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
							 StringBuilder res_error_cancel = new StringBuilder(soapBegin+doc_str+soapEnd);	
							 out.print(res_error_cancel.toString());
							 out.flush();
							 out.close();
						}else{
							//生成唯一伪订单确认编号
							Generator generator = Generator.getInstance();
							String pmsresid = generator.getPMSOrderComfireId();
							//改单成功之后将原来订单置为取消状态，并新增一个订单
							 
							if(OrderDao.getInstance().OTAorder_updateForCancel(json_otaUpdateRQ,pmsresid))
							{
								
								String old_orderid = OrderDao.getInstance().getOrderId(json_otaUpdateRQ);
								CommonDao commonDao = CommonDao.getInstance();
								JSONObject param = commonDao.generateOrders_motify(json_otaUpdateRQ.getString("taoBaoOrderId"),old_orderid, json_otaUpdateRQ.getString("hotelid"),json_otaUpdateRQ.getString("mkt"),json_otaUpdateRQ.getString("latestarrivetime"),json_otaUpdateRQ.getString("latestarrivetime"));
								JSONObject result_jo = HttpSend_GET.SendPOST(PUSH_ORDER_URL,param);
								if(Integer.valueOf(result_jo.getString("status"))==0)
								{
//									OrderDao.getInstance().order_updateForOTACancel(json_otaUpdateRQ.getString("taoBaoOrderId"),json_otaUpdateRQ.getString("mkt"));
									logger.info("来自众荟平台改单("+json_otaUpdateRQ.getString("taoBaoOrderId")+"),推送成功");
								}else{
									logger.error("来自众荟平台改单("+json_otaUpdateRQ.getString("taoBaoOrderId")+"),推送失败");
								}
								 
								 //向众荟反馈改单成功结果
								 Document doc = XMLGenerator_RZT.XMLGenerator_Res_SUCCESS(json_otaUpdateRQ.getString("mkt"), json_otaUpdateRQ,pmsresid,"Modified");
								 String doc_str = doc.asXML();
								 doc_str = doc_str.replace(doc_str.substring(doc_str.indexOf("<"), doc_str.indexOf(">")+1), "");
								 String soapBegin = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><SOAP-ENV:Body>";
								 String soapEnd = "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
								 StringBuilder res_success = new StringBuilder(soapBegin+doc_str+soapEnd);	
								 out.print(res_success.toString());
								 out.flush();
								 out.close();
							}else{
								logger.info("订单("+json_otaUpdateRQ.getString("taoBaoOrderId")+")改单失败（修改订单状态和新增订单时出错）");
								 Document doc = XMLGenerator_RZT.XMLGenerator_Res_Error("改单失败（插入数据失败）","Modified");
								  String doc_str = doc.asXML();
								 doc_str = doc_str.replace(doc_str.substring(doc_str.indexOf("<"), doc_str.indexOf(">")+1), "");
								  String soapBegin = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><SOAP-ENV:Body>";
								  String soapEnd = "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
								 StringBuilder res_error_cancel = new StringBuilder(soapBegin+doc_str+soapEnd);	
								 out.print(res_error_cancel.toString());
								 out.flush();
								 out.close();
							}
						}
						 
						
						
							
						break;
						
					case 0:
						
						//向LDP发送改单请求
						 logger.info("来自众荟平台订单("+json_otaUpdateRQ.getString("taoBaoOrderId")+")");
						 //检验改单后的价格是否与酒店设置的一致
						RZT_OrderService rztOrderService_0 = new RZT_OrderService();
						BookValidateRQ bvr_0 = new BookValidateRQ();
						bvr_0.setHotelid(json_otaUpdateRQ.getString("hotelid"));
						bvr_0.setRmtype(json_otaUpdateRQ.getString("roomTypeId"));
						bvr_0.setRpcode(json_otaUpdateRQ.getString("ratePlanCode"));
						bvr_0.setDailyinfos(json_otaUpdateRQ.getJSONArray("dailyInfos"));
						System.out.println("请求参数："+JSONObject.fromObject(bvr_0));
						if(!rztOrderService_0.IsRightPrice(JSONObject.fromObject(bvr_0)))
						{
							 logger.error("订单("+json_otaUpdateRQ.getString("taoBaoOrderId")+")改单失败,改单后的价格和酒店设置的不一致");
							 Document doc = XMLGenerator_RZT.XMLGenerator_Res_Error("改单后的价格和酒店设置的不一致","Modified");
							 String doc_str = doc.asXML();
							 doc_str = doc_str.replace(doc_str.substring(doc_str.indexOf("<"), doc_str.indexOf(">")+1), "");
							 String soapBegin = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><SOAP-ENV:Body>";
							 String soapEnd = "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
							 StringBuilder res_error_cancel = new StringBuilder(soapBegin+doc_str+soapEnd);	
							 out.print(res_error_cancel.toString());
							 out.flush();
							 out.close();
						}else{
							//生成唯一伪订单确认编号
							Generator generator1 = Generator.getInstance();
							String pmsresid1 = generator1.getPMSOrderComfireId();
							//改单成功之后将原来订单置为取消状态，并新增一个订单
							 
							if(OrderDao.getInstance().OTAorder_updateForCancel(json_otaUpdateRQ,pmsresid1))
							{
								
								
								String old_orderid = OrderDao.getInstance().getOrderId(json_otaUpdateRQ);
								CommonDao commonDao = CommonDao.getInstance();
								JSONObject param = commonDao.generateOrders_motify(json_otaUpdateRQ.getString("taoBaoOrderId"),old_orderid, json_otaUpdateRQ.getString("hotelid"),json_otaUpdateRQ.getString("mkt"),json_otaUpdateRQ.getString("latestarrivetime"),json_otaUpdateRQ.getString("latestarrivetime"));
								JSONObject result_jo = HttpSend_GET.SendPOST(PUSH_ORDER_URL,param);
								if(Integer.valueOf(result_jo.getString("status"))==0)
								{
//									OrderDao.getInstance().order_updateForOTACancel(json_otaUpdateRQ.getString("taoBaoOrderId"),json_otaUpdateRQ.getString("mkt"));
									logger.info("来自众荟平台改单("+json_otaUpdateRQ.getString("taoBaoOrderId")+"),推送成功");
								}else{
									logger.error("来自众荟平台改单("+json_otaUpdateRQ.getString("taoBaoOrderId")+"),推送失败");
								}
								 
								 //向众荟反馈改单成功结果
								 Document doc = XMLGenerator_RZT.XMLGenerator_Res_SUCCESS(json_otaUpdateRQ.getString("mkt"), json_otaUpdateRQ,pmsresid1,"Modified");
								 String doc_str = doc.asXML();
								 doc_str = doc_str.replace(doc_str.substring(doc_str.indexOf("<"), doc_str.indexOf(">")+1), "");
								 String soapBegin = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><SOAP-ENV:Body>";
								 String soapEnd = "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
								 StringBuilder res_success = new StringBuilder(soapBegin+doc_str+soapEnd);	
								 out.print(res_success.toString());
								 out.flush();
								 out.close();
							}else{
								logger.info("订单("+json_otaUpdateRQ.getString("taoBaoOrderId")+")改单失败（修改订单状态和新增订单时出错）");
								 Document doc = XMLGenerator_RZT.XMLGenerator_Res_Error("改单失败（插入数据失败）","Modified");
								  String doc_str = doc.asXML();
								 doc_str = doc_str.replace(doc_str.substring(doc_str.indexOf("<"), doc_str.indexOf(">")+1), "");
								  String soapBegin = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><SOAP-ENV:Body>";
								  String soapEnd = "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
								 StringBuilder res_error_cancel = new StringBuilder(soapBegin+doc_str+soapEnd);	
								 out.print(res_error_cancel.toString());
								 out.flush();
								 out.close();
							}
						}
						/*
						//向LDP发送改单请求
						 logger.info("来自众荟平台订单("+json_otaUpdateRQ.getString("taoBaoOrderId")+")");
						//生成唯一伪订单确认编号
//						Generator generator = Generator.getInstance();
						String pmsresid1 = Generator.getInstance().getPMSOrderComfireId();
						//改单成功之后将原来订单置为取消状态，并新增一个订单
						 
						if(OrderDao.getInstance().OTAorder_updateForCancel(json_otaUpdateRQ,pmsresid1))
						{
							
							
							String old_orderid = OrderDao.getInstance().getOrderId(json_otaUpdateRQ);
							CommonDao commonDao = CommonDao.getInstance();
							JSONObject param = commonDao.generateOrders_motify(json_otaUpdateRQ.getString("taoBaoOrderId"),old_orderid, json_otaUpdateRQ.getString("hotelid"),json_otaUpdateRQ.getString("mkt"),json_otaUpdateRQ.getString("latestarrivetime"),json_otaUpdateRQ.getString("latestarrivetime"));
							JSONObject result_jo = HttpSend_GET.SendPOST(PUSH_ORDER_URL,param);
							if(Integer.valueOf(result_jo.getString("status"))==0)
							{
//								OrderDao.getInstance().order_updateForOTACancel(json_otaUpdateRQ.getString("taoBaoOrderId"),json_otaUpdateRQ.getString("mkt"));
								logger.info("来自众荟平台改单("+json_otaUpdateRQ.getString("taoBaoOrderId")+"),推送成功");
							}else{
								logger.error("来自众荟平台改单("+json_otaUpdateRQ.getString("taoBaoOrderId")+"),推送失败");
							}
							 
							 //向众荟反馈改单成功结果
							 Document doc = XMLGenerator_RZT.XMLGenerator_Res_SUCCESS(json_otaUpdateRQ.getString("mkt"), json_otaUpdateRQ,pmsresid1,"Modified");
							 String doc_str = doc.asXML();
							 doc_str = doc_str.replace(doc_str.substring(doc_str.indexOf("<"), doc_str.indexOf(">")+1), "");
							 String soapBegin = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><SOAP-ENV:Body>";
							 String soapEnd = "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
							 StringBuilder res_success = new StringBuilder(soapBegin+doc_str+soapEnd);	
							 out.print(res_success.toString());
							 out.flush();
							 out.close();
						}else{
							logger.info("订单("+json_otaUpdateRQ.getString("taoBaoOrderId")+")改单失败（修改订单状态和新增订单时出错）");
							 Document doc = XMLGenerator_RZT.XMLGenerator_Res_Error("改单失败（插入数据失败）","Modified");
							  String doc_str = doc.asXML();
							 doc_str = doc_str.replace(doc_str.substring(doc_str.indexOf("<"), doc_str.indexOf(">")+1), "");
							  String soapBegin = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><SOAP-ENV:Body>";
							  String soapEnd = "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
							 StringBuilder res_error_cancel = new StringBuilder(soapBegin+doc_str+soapEnd);	
							 out.print(res_error_cancel.toString());
							 out.flush();
							 out.close();
						}
						*/
						
							
						break;

					default:
						 logger.error("订单("+json_otaUpdateRQ.getString("taoBaoOrderId")+")改单失败,该订单当前状态不允许改单");
						 Document doc = XMLGenerator_RZT.XMLGenerator_Res_Error("该订单当前状态不允许改单","Modified");
						 String doc_str = doc.asXML();
						 doc_str = doc_str.replace(doc_str.substring(doc_str.indexOf("<"), doc_str.indexOf(">")+1), "");
						 String soapBegin = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><SOAP-ENV:Body>";
						 String soapEnd = "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
						 StringBuilder res_error_cancel = new StringBuilder(soapBegin+doc_str+soapEnd);	
						 out.print(res_error_cancel.toString());
						 out.flush();
						 out.close();
						break;
					}
				}else{
					//非艺龙订单
					 logger.info("订单("+json_otaUpdateRQ.getString("taoBaoOrderId")+")改单失败（艺龙订单不支持改单）");
					 Document doc = XMLGenerator_RZT.XMLGenerator_Res_Error("改单失败（艺龙订单暂不支持改单）","Modified");
					 String doc_str = doc.asXML();
					 doc_str = doc_str.replace(doc_str.substring(doc_str.indexOf("<"), doc_str.indexOf(">")+1), "");
					 String soapBegin = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><SOAP-ENV:Body>";
					 String soapEnd = "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
					 StringBuilder res_error_cancel = new StringBuilder(soapBegin+doc_str+soapEnd);	
					 out.print(res_error_cancel.toString());
					 out.flush();
					 out.close();
				}
				
			}
		} catch (SOAPException e) {
			logger.error(e);
			e.printStackTrace();
		}
		
	}
	
	
	
	private boolean sync_order(JSONArray ja_order_create,JSONArray ja_order_cancel)
	{
		boolean flag = false;
		List<OrderInfo> list_create = new ArrayList<OrderInfo>();
		List<OrderInfo> list_cancel = new ArrayList<OrderInfo>();
		if(ja_order_create!=null)
		{
			for(int i=0;i<ja_order_create.size();i++)
			{
				OrderInfo oi = new OrderInfo();
				oi.setTaobaoid(ja_order_create.getJSONObject(i).getString("taobaoid"));
				oi.setHotelid(ja_order_create.getJSONObject(i).getString("hotelid"));
				oi.setMrk(ja_order_create.getJSONObject(i).getString("mrk"));
				list_create.add(oi);
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
				list_cancel.add(oi);
			}
		}
		
		Map<String, Object> mp = new HashMap<String, Object>();
		mp.put("serviceName","orders_sync");
		mp.put("orders_create",JSONArray.fromObject(list_create));
		mp.put("orders_cancel",JSONArray.fromObject(list_cancel));
		JSONObject params = JSONObject.fromObject(mp);
		logger.info("同步ztx参数:"+params.toString());
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
