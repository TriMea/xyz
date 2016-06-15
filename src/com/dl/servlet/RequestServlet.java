package com.dl.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.dl.dao.CommonDao;
import com.dl.dao.DispatcherDao;
import com.dl.dao.DogNoValidateDao;
import com.dl.dao.OrderDao;
import com.dl.dao.PollingOrderDao;
import com.dl.dao.SmsDao;
import com.dl.pojo.BookRQ;
import com.dl.pojo.CancelRQ;
import com.dl.pojo.DailyInfo;
import com.dl.pojo.OrderGuest;
import com.dl.pojo.ReceiptAddress;
import com.dl.pojo.ReceiptInfo;
import com.dl.pojo.SmsMessage;
import com.dl.pojo.ValidateRQ;
import com.dl.service.Aladin_OrderService;
import com.dl.utl.CommonTool;
import com.dl.utl.HttpSend_GET;
import com.dl.utl.HttpUtil;
import com.dl.utl.Http_Info_Offline;
import com.dl.utl.SmsUtil;
import com.dl.utl.XMLGenerator;

public class RequestServlet extends HttpServlet {

	//BOOKRQ_URL
	private static final String SMSACCNT = "dlhis";
	private static final String SMSPWD = "419xtd";
//	private static final String PHONE = "";
//	private static final String SMSCONTENT = "";
	//正式
	private static final String ISONLINE_URL_PMS = "http://Order-Dispatch.dlhis.com:9979/srv/";
	private static final String ISONLINE_URL_NO_PMS = "http://polling.dlhis.com:8080/xyz_lunxun/Query_off_line";
	private static final String RESOURCE_INFO_URL = "http://resource.dlhis.com:8080/xyz_findroom/DownServlet";
	private static final String VALIDATERQ_URL = "http://resource.dlhis.com:8080/xyz_findroom/roomServlet";
	private static final String BOOKRQ_URL = "http://Order-Dispatch.dlhis.com:9979/srv/?handler=order";
	private static final String BOOKCANCELRQ_URL = "http://Order-Dispatch.dlhis.com:9979/srv/?handler=order";
	private static final String PUSH_ORDER_URL = "http://polling.dlhis.com:8080/xyz_lunxun/transportServlet";
	
	//测试
//	private static final String ISONLINE_URL_PMS = "http://register2.dlhis.com:9979/srv/";
//	private static final String ISONLINE_URL_NO_PMS = "http://register2.dlhis.com:8080/xyz_lunxun/Query_off_line?hotelid=";
//	private static final String RESOURCE_INFO_URL = "http://114.215.242.222:8080/xyz_findroom/DownServlet";
//	private static final String VALIDATERQ_URL = "http://114.215.242.222:8080/xyz_findroom/roomServlet";
//	private static final String BOOKRQ_URL = "http://114.215.242.222:9979/srv/?handler=order";
//	private static final String BOOKCANCELRQ_URL = "http://114.215.242.222:9979/srv/?handler=order";
//	private static final String PUSH_ORDER_URL = "http://192.168.199.134:8080/xyz_lunxun/transportServlet";

//	private static final String SYNC_ORDER_URL = "http://192.168.199.43:8080/xyz_lunxun/requestServlet";
	private Logger logger = Logger.getLogger(RequestServlet.class); 
	/**
	 * Constructor of the object.
	 */
	public RequestServlet() {
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
		String dogno = request.getParameter("dogno");
//		System.out.println("version:"+request.getParameter("ver"));
		System.out.println("dogno:"+dogno);
		DogNoValidateDao dd = DogNoValidateDao.getInstance();
		Map<String,Object> result = dd.isXYZ(dogno);
		HashMap<String, Object> mp = new HashMap<String, Object>();
		if(request.getParameter("ver")==null)
		{
			//老版本管家
			if(result.get("hotelid")==null)
			{
				mp.put("code",1);
				mp.put("msg", "该酒店未入驻信用住");
			}else{
				
					//初次注册
					mp.put("code",0);
					mp.put("hotelid",(String)result.get("hotelid"));
					mp.put("hotelname",(String)result.get("hotelname"));
					mp.put("ishotel",(String)result.get("ishotel"));
//					String rsa = CommonTool.getMD5Str((String)result.get("hotelid"));
//					mp.put("rsa",rsa);
					
			}
		}else{
		    //新版本管家
			if(result.get("hotelid")==null)
			{
				mp.put("code",1);
				mp.put("msg", "该酒店未入驻信用住");
			}else{
				if("0".equals((String)result.get("isused")))
				{
					//初次注册
					mp.put("code",0);
					mp.put("hotelid",(String)result.get("hotelid"));
					mp.put("hotelname",(String)result.get("hotelname"));
					mp.put("ishotel",(String)result.get("ishotel"));
					String rsa = CommonTool.getMD5Str((String)result.get("hotelid"));
					mp.put("rsa",rsa);
					//更新isused字段为1
					dd.updateRegTimers(dogno,rsa);
					
				}else{
					mp.put("code",0);
					mp.put("hotelid",(String)result.get("hotelid"));
					mp.put("hotelname",(String)result.get("hotelname"));
					mp.put("ishotel",(String)result.get("ishotel"));
					String rsa = CommonTool.getMD5Str((String)result.get("hotelid")+"registered");
					mp.put("rsa",rsa);
//					//更新isused字段为1
//					dd.updateRegTimers(dogno,rsa);
//					mp.put("code",1);
//					mp.put("msg", "该注册码已被注册过，请联系代理商或PMS系统商获取注册码");
				}
				
			}
		}
		
		PrintWriter out = response.getWriter();
		out.write(JSONObject.fromObject(mp).toString());
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

		response.setContentType("text/html");
		System.out.println("调用了 post方法");
		PrintWriter out = response.getWriter();
		BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream(),"utf-8"));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		logger.info("收到来自阿里请求："+sb.toString());
		System.out.println("编码前："+sb.toString());
		Document doc = null;
		try {
			
			 doc = DocumentHelper.parseText(sb.toString());
			 Element rootElt = doc.getRootElement(); // 获取根节点
	         System.out.println("根节点：" + rootElt.getName()); // 拿到根节点的名称
	         if("ValidateRQ".equals(rootElt.getName().trim()))
	         {
	        	 //试单操作
	        	 
	        	 Iterator iterss = rootElt.elementIterator("AuthenticationToken"); ///获取根节点下的子节点AuthenticationToken
		         while(iterss.hasNext())
		         {
		        	 Element recordEle  = (Element)iterss.next();
		        	 String username = recordEle.elementTextTrim("Username");
		        	 String psd = recordEle.elementTextTrim("Password");
		        	 String createToken = recordEle.elementTextTrim("CreateToken");
		        	 //根据请求参数中的房型和数量比较实时房型资源中是否足够
		        	 //是：返回
		        	 //否：返回试单失败
		        	 
		        	 
		         }
		         System.out.println("hotelid:"+rootElt.elementTextTrim("HotelId"));
		         ValidateRQ validateRQ = new ValidateRQ();
		         validateRQ.setHotelid(rootElt.elementTextTrim("HotelId"));
		         validateRQ.setRoomTypeId(rootElt.elementTextTrim("RoomTypeId"));
		         validateRQ.setCheckIn(rootElt.elementTextTrim("CheckIn"));
		         validateRQ.setCheckOut(rootElt.elementTextTrim("CheckOut"));
		         validateRQ.setRoomNum(rootElt.elementTextTrim("RoomNum"));
		         validateRQ.setPaymentType(rootElt.elementTextTrim("PaymentType"));
		         validateRQ.setRatePlan(rootElt.elementTextTrim("RatePlanCode"));
		         validateRQ.setMrk("QUA");
		         System.out.println("RPCODE:"+rootElt.elementTextTrim("RatePlanCode"));
		         JSONObject jo_params = JSONObject.fromObject(validateRQ);
		         logger.info("试单参数："+jo_params.toString());
		         System.out.println("结果："+jo_params.toString());
		         //=========================
		         //新增判断（是否为阿拉丁魔盒酒店）
		         Aladin_OrderService aladin_OrderService = new Aladin_OrderService();
		         if(aladin_OrderService.isAladin(validateRQ.getHotelid().trim()))
		         {
		        	 //是阿拉丁魔盒酒店用户
		        	 logger.info("aladin试单请求参数："+jo_params);
		        	 JSONObject result = aladin_OrderService.sendValidateRQ(jo_params);
		        	 
			         if(result!=null)
			         {
			         
				         switch (result.getInt("code")) {
						 case 0:
							 out.write(XMLGenerator.XMLGenerator_ValidateRQ(result.getInt("code"),result.getJSONArray("room")).asXML());
					         out.flush();
					         out.close();
							break;
						 case 1 :
							 out.write(XMLGenerator.XMLGenerator_ValidateRQ(result.getInt("code"),null).asXML());
					         out.flush();
					         out.close();
					     break;
						default:
							break;
						}
			         }else{
			        	 //试单出错
			        	 out.write(XMLGenerator.XMLGenerator_ValidateRQ(2,null).asXML());
				         out.flush();
				         out.close();
			         }
		        	 
		         }else{
		        	 DispatcherDao dispatcherDao = DispatcherDao.getInstance();
				     if(dispatcherDao.isDL_PMS(validateRQ.getHotelid().trim()))
				     {
				    	 //该酒店使用迪联pms
				    	 StringBuilder sb_hotelid = new StringBuilder("handler=query&mode=s&hotelid=");
				         sb_hotelid.append(validateRQ.getHotelid().trim());
				         //在试单之前判断待试单hotelid是否在线
				         if(HttpUtil.getInstance().sendGet_isOnline_PMS(ISONLINE_URL_PMS, sb_hotelid.toString()))
				         {
				        	 //在线
				        	 JSONObject result = HttpSend_GET.SendGET(VALIDATERQ_URL,"data="+jo_params.toString());
					         logger.info("试单返回："+result);
					         System.out.println("试单返回:"+result);
					         Integer code = Integer.valueOf(result.getString("code"));
					         switch (code) {
							 case 0:
								 out.write(XMLGenerator.XMLGenerator_ValidateRQ(code,result.getJSONArray("room")).asXML());
						         out.flush();
						         out.close();
								break;
							 case 1 :
								 out.write(XMLGenerator.XMLGenerator_ValidateRQ(code,null).asXML());
						         out.flush();
						         out.close();
						     break;
							default:
								
								break;
							}
				        	 
				         }else{
				        	 //离线
				        	 //通知资源服务器下架该酒店所有资源
				        	 StringBuilder params = new StringBuilder("data=");
				        	 params.append(validateRQ.getHotelid().trim());
				        	 //短信提醒离线酒店
				        	 SmsMessage smsMessage = SmsDao.getInstance().getSmsInfo(validateRQ.getHotelid().trim());
				        	 StringBuffer content = new StringBuffer("信用住酒店：");	        	 
				        	 try {
					        		 if(smsMessage!=null)
						        	 {
						        		 content.append(smsMessage.getHotelname()).append("(").append(smsMessage.getHotelid()).append(")")
							        	 .append("已断线30分钟，为保证酒店信用住订单的正常接收，请及时通知酒店");
						        		 SmsUtil.sendSmsMessage(SMSACCNT, SMSPWD, smsMessage.getContacttel(), content.toString(), smsMessage.getHotelid(), smsMessage.getHotelname());
						        	 }else{
						        		 content.append("(").append(validateRQ.getHotelid()).append(")")
							        	 .append("已断线30分钟，为保证酒店信用住订单的正常接收，请及时通知酒店");
						        		 SmsUtil.sendSmsMessage(SMSACCNT, SMSPWD, "13456831133", content.toString(), validateRQ.getHotelid(), "未知酒店");
						        	 }
					        		 logger.info("("+smsMessage.getHotelid()+")已通知下线");
								} catch (Exception e) {
									logger.error(e);
								}finally{
									if(Http_Info_Offline.getInstance().sendGet_infoOffline(RESOURCE_INFO_URL, params.toString())) 
						        	 {
						        		 //成功通知该酒店下架
						        		 System.out.println("通知指定酒店下架资源成功");
						        		 logger.info("通知指定酒店下架资源成功："+validateRQ.getHotelid().trim());
						        		 out.write(XMLGenerator.XMLGenerator_ValidateRQ(2,null).asXML());
								         out.flush();
								         out.close();
						        	 }else{
						        		 System.out.println("通知指定酒店下架资源失败");
						        		 logger.error("通知指定酒店下架资源失败："+validateRQ.getHotelid().trim());
						        		 out.write(XMLGenerator.XMLGenerator_ValidateRQ(2,null).asXML());
								         out.flush();
								         out.close();
						        	 }
								}
				        	
				         }
				     }else{
				    	 //信用住管家酒店
				    	 StringBuilder sb_hotelid = new StringBuilder("hotelid=");
				         sb_hotelid.append(validateRQ.getHotelid().trim());
				         //在试单之前判断待试单hotelid是否在线
				         if(HttpUtil.getInstance().sendGet_isOnline(ISONLINE_URL_NO_PMS, sb_hotelid.toString()))
				         {
				        	 //在线
				        	 JSONObject result = HttpSend_GET.SendGET(VALIDATERQ_URL,"data="+jo_params.toString());
					         logger.info("试单返回："+result);
					         System.out.println("试单返回:"+result);
//					         Integer code = result.getInt("code");
					         switch (result.getInt("code")) {
							 case 0:
								 out.write(XMLGenerator.XMLGenerator_ValidateRQ(result.getInt("code"),result.getJSONArray("room")).asXML());
						         out.flush();
						         out.close();
								break;
							 case 1 :
								 out.write(XMLGenerator.XMLGenerator_ValidateRQ(result.getInt("code"),null).asXML());
						         out.flush();
						         out.close();
						     break;
							default:
								break;
							}
				        	 
				         }else{
				        	 //离线
				        	 //通知资源服务器下架该酒店所有资源
				        	
				        	 StringBuilder params = new StringBuilder("data=");
				        	 params.append(validateRQ.getHotelid().trim());
				        	 //短信提醒离线酒店
				        	 SmsMessage smsMessage = SmsDao.getInstance().getSmsInfo(validateRQ.getHotelid().trim());
				        	 StringBuffer content = new StringBuffer("信用住酒店：");
				        	try {
				        		if(smsMessage!=null)
					        	 {
					        		 content.append(smsMessage.getHotelname()).append("(").append(smsMessage.getHotelid()).append(")")
						        	 .append("已断线30分钟，为保证酒店信用住订单的正常接收，请及时通知酒店");
					        		 SmsUtil.sendSmsMessage(SMSACCNT, SMSPWD, smsMessage.getContacttel(), content.toString(), smsMessage.getHotelid(), smsMessage.getHotelname());
					        	 }else{
					        		 content.append("(").append(validateRQ.getHotelid()).append(")")
						        	 .append("已断线30分钟，为保证酒店信用住订单的正常接收，请及时通知酒店");
					        		 SmsUtil.sendSmsMessage(SMSACCNT, SMSPWD, "13456831133", content.toString(), validateRQ.getHotelid(), "未知酒店");
					        	 }
				        		 logger.info("("+smsMessage.getHotelid()+")已通知下线");
							} catch (Exception e) {
								logger.error(e);
							}finally{
								if(Http_Info_Offline.getInstance().sendGet_infoOffline(RESOURCE_INFO_URL, params.toString())) 
					        	 {
					        		 //成功通知该酒店
					        		 logger.info("通知指定酒店下架资源成功："+validateRQ.getHotelid().trim());
					        		 out.write(XMLGenerator.XMLGenerator_ValidateRQ(2,null).asXML());
							         out.flush();
							         out.close();
					        	 }else{
					        		 
					        		 logger.error("通知指定酒店下架资源失败："+validateRQ.getHotelid().trim());
					        		 out.write(XMLGenerator.XMLGenerator_ValidateRQ(2,null).asXML());
							         out.flush();
							         out.close();
					        	 }
							}
				      
				         }
				     }
		         }
		         //=========================
		         
		         
		         
		         
		         
	        	 
	         }
	         else if("BookRQ".equals(rootElt.getName().trim()))
	         {
	        	 //创建订单操作
	        	 Iterator iterss = rootElt.elementIterator("AuthenticationToken"); ///获取根节点下的子节点AuthenticationToken
		         OrderDao orderdao = OrderDao.getInstance();
		         Map<String, Object> result = orderdao.validate_order_id(rootElt.elementTextTrim("TaoBaoOrderId"));
		         switch ((Integer)result.get("code")) {
				case 0:
					//订单号未重复
					System.out.println("可以创建订单code:0");
					JSONObject jo_order = null;
					try {
						jo_order = xml2json_order(rootElt);
					} catch (Exception e) {
						logger.error(e);
						e.printStackTrace();
						out.write(XMLGenerator.XMLGenerator_BookRQ(1).asXML());
						out.flush();
						out.close();
						return;
					}
						
					System.out.println("订单未重复："+jo_order);
					boolean issuccess = orderdao.order_add(jo_order);
					System.out.println("订单插入数据库结果:"+issuccess);
					if(issuccess)
					{
						//订单插入mstinfo成功
						System.out.println("下单成功");
						BookRQ bookRQ = new BookRQ();
						bookRQ.setTaoBaoOrderId(jo_order.getString("TaoBaoOrderId"));
						bookRQ.setHotelid(jo_order.getString("HotelId"));
						bookRQ.setRoomTypeId(jo_order.getString("RoomTypeId"));
						bookRQ.setRatePlanCode(jo_order.getString("RatePlanCode"));
						bookRQ.setRoomNum(jo_order.getString("RoomNum"));
						bookRQ.setOrderStatus("0");
						bookRQ.setCheckIn(jo_order.getString("CheckIn"));
						bookRQ.setCheckOut(jo_order.getString("CheckOut"));
						bookRQ.setTotalPrice(jo_order.getString("TotalPrice"));
						bookRQ.setPaymentType(jo_order.getString("PaymentType"));
						bookRQ.setContactName(jo_order.getString("ContactName"));
						bookRQ.setContactTel(jo_order.getString("ContactTel"));
						bookRQ.setDailyInfos(jo_order.getJSONArray("DailyInfos"));
						bookRQ.setOrderGuests(jo_order.getJSONArray("OrderGuests"));
						bookRQ.setMkt("QUA");
						bookRQ.setRemark(jo_order.getString("Comment"));
						bookRQ.setOrderType("online");
						bookRQ.setReceiptInfo(jo_order.getJSONObject("ReceiptInfo"));
						bookRQ.setLog_date(getSystime());
						
						//=========================
				         //新增判断（是否为阿拉丁魔盒酒店）
				         Aladin_OrderService aladin_OrderService = new Aladin_OrderService();
				         if(aladin_OrderService.isAladin(bookRQ.getHotelid().trim()))
				         {
				        	 //是阿拉丁魔盒酒店用户
				        	 logger.info("aladin下单请求参数："+JSONObject.fromObject(bookRQ));
				        	 JSONObject result_addOrder = aladin_OrderService.sendAddOrderRQ(JSONObject.fromObject(bookRQ));
				        	
					         if(result_addOrder!=null)
					         {
						        if(result_addOrder.getInt("backCode")==0)
						        {
						        	//正常响应订单已被接收
						        	//更新订单下发状态为T
									orderdao.order_update(null,jo_order.getString("TaoBaoOrderId"),"QUA");
									out.write(XMLGenerator.XMLGenerator_BookRQ(0).asXML());
									out.flush();
									out.close();
						        }else{
						        	logger.error("订单下达失败:"+result_addOrder);
						        	//订单未被接收
						        	out.write(XMLGenerator.XMLGenerator_BookRQ(3).asXML());
									out.flush();
									out.close();
						        }
					         }else{
					        	logger.error(jo_order.getString("TaoBaoOrderId")+"阿拉丁收单服务器未响应！");
					        	orderdao.order_update2F(rootElt.elementTextTrim("TaoBaoOrderId"),"QUA");
					        	out.write(XMLGenerator.XMLGenerator_BookRQ(3).asXML());
								out.flush();
								out.close();
					         }
				        	 
				         }else{
				        	 //非阿拉丁魔盒酒店用户
				        	    DispatcherDao dispatcherDao = DispatcherDao.getInstance();
								if(dispatcherDao.isDL_PMS(jo_order.getString("HotelId").trim()))
								{
									    System.out.println("来自pms订单("+jo_order.getString("TaoBaoOrderId")+")");
									    logger.info("来自pms订单("+jo_order.getString("TaoBaoOrderId")+")");
										Map<String, Object> mp = new HashMap<String, Object>();
										mp.put("guid", getGuid());
										mp.put("command",1001);
										mp.put("bodyJson", JSONObject.fromObject(bookRQ).toString());
										JSONObject params = JSONObject.fromObject(mp);
										 logger.info("下单推送参数："+params);
										//向ldp发送订单请求
										System.out.println("下单请求参数格式："+params);
										
										JSONObject result_jo = HttpSend_GET.SendPOST(BOOKRQ_URL, params);
										System.out.println("dp返回结果："+result_jo);
										logger.info("下单推送反馈信息："+result_jo);
										if(result_jo==null)
										{
			//								orderdao.order_update(null, jo_order.getString("TaoBaoOrderId"));
											orderdao.order_update2F(rootElt.elementTextTrim("TaoBaoOrderId"),"QUA");
											out.write(XMLGenerator.XMLGenerator_BookRQ(3).asXML());
											out.flush();
											out.close();
											//不做任何操作
										}
										else if(result_jo.getInt("BackCode")==0)
										{
											System.out.println("下单返回msg："+result_jo.getString("BackMsg"));
											//更新订单下发状态为T
											orderdao.order_update(null,jo_order.getString("TaoBaoOrderId"),"QUA");
											out.write(XMLGenerator.XMLGenerator_BookRQ(0).asXML());
											out.flush();
											out.close();
											
			//								//创建订单成功
			//								orderdao.order_update(result_jo.getString("resno"), jo_order.getString("TaoBaoOrderId"));
										}
								}else{
									//非使用dlpms客户端的酒店订单，推送到轮询服务器
									 System.out.println("来自非pms订单("+jo_order.getString("TaoBaoOrderId")+")");
									logger.info("来自非pms订单("+jo_order.getString("TaoBaoOrderId")+")");
									CommonDao commonDao = CommonDao.getInstance();
									JSONObject param = commonDao.generateOrders_transport("orders_create", jo_order.getString("TaoBaoOrderId"), jo_order.getString("HotelId"), "QUA","");
									JSONObject result_jo = HttpSend_GET.SendPOST(PUSH_ORDER_URL, param);
									if(Integer.valueOf(result_jo.getString("status"))==0)
									{
										
										//更新订单下发状态为T
										PollingOrderDao.getInstance().order_update(null, jo_order.getString("TaoBaoOrderId"), "QUA", true);
//										orderdao.order_update(null,jo_order.getString("TaoBaoOrderId"),"QUA");
										out.write(XMLGenerator.XMLGenerator_BookRQ(0).asXML());
										out.flush();
										out.close();
										
//										//创建订单成功
//										orderdao.order_update(result_jo.getString("resno"), jo_order.getString("TaoBaoOrderId"));
									}
									logger.info("推送订单返回："+result_jo.getString("status")+","+result_jo.getString("responseDec"));
									System.out.println("推送订单返回："+result_jo.getString("status")+","+result_jo.getString("responseDec"));
								  }							
							
				            }
			         }else{
							System.out.println("下单失败111");
							out.write(XMLGenerator.XMLGenerator_BookRQ(1).asXML());
							out.flush();
							out.close();
					 }
						
					break;
                case 1:
					//该订单号已下过单
                	System.out.println("code:1");
                	out.write(XMLGenerator.XMLGenerator_BookRQ(2).asXML());
					out.flush();
					out.close();
					break;
                case 2:
                	System.out.println("code:2");
                	//该订单号已下过单，且pms端已确认
                	out.write(XMLGenerator.XMLGenerator_BookRQ(2).asXML());
					out.flush();
					out.close();
	                break;
				default:
					break;
				}

//	        	 
	         }
	         else if("CancelRQ".equals(rootElt.getName().trim()))
	         {
	        	 //取消订单操作
	        	 Iterator iterss = rootElt.elementIterator("AuthenticationToken"); ///获取根节点下的子节点AuthenticationToken
		         while(iterss.hasNext())
		         {
		        	 Element recordEle  = (Element)iterss.next();
		        	 String username = recordEle.elementTextTrim("Username");
		        	 String psd = recordEle.elementTextTrim("Password");
		        	 String createToken = recordEle.elementTextTrim("CreateToken"); 
		        	
		         }
//		         System.out.println("orderid:"+rootElt.elementTextTrim("OrderId"));
		         System.out.println("taobaoid:"+rootElt.elementTextTrim("TaoBaoOrderId"));
		         OrderDao orderdao = OrderDao.getInstance();
		         //检查该订单号是否已存在
		         Map<String, Object> result = orderdao.cancel_order_id(rootElt.elementTextTrim("OrderId"));
		         System.out.println("taobaoid1:"+rootElt.elementTextTrim("TaoBaoOrderId"));
//		         System.out.println("hotelid:"+rootElt.elementTextTrim("OrderId"));
		         System.out.println("code:"+(Integer)result.get("code"));
		         switch ((Integer)result.get("code")) {
				case 0:
					//订单号不存在，返回-302，订单不存在
					out.write(XMLGenerator.XMLGenerator_CancelRQ(0,null).asXML());
					out.flush();
					out.close();
					
					break;
                case 4:
                	 //待取消订单已处于取消状态，无需取消
                	out.write(XMLGenerator.XMLGenerator_CancelRQ(1,null).asXML());
					out.flush();
					out.close();
					break;
                case 2:
                	//可以取消，向pms推送取消订单请求
                	out.write(XMLGenerator.XMLGenerator_CancelRQ(2,(String)result.get("orderid")).asXML());
					out.flush();
					out.close();
					//向dp发送订单请求
//					StringBuilder params = new StringBuilder();
//					params.append("taoBaoOrderId="+rootElt.elementTextTrim("TaobaoOrderId"));
//					params.append("&mrk="+(String)result.get("mrk"));
//					params.append("&reason="+rootElt.elementTextTrim("Reason"));
					CancelRQ cancelRQ = new CancelRQ();
					cancelRQ.setHotelid((String)result.get("hotelid"));
					cancelRQ.setTaoBaoOrderId(rootElt.elementTextTrim("TaoBaoOrderId"));
					cancelRQ.setMkt("QUA");
					cancelRQ.setReason(rootElt.elementTextTrim("Reason"));
					//=========================
			         //新增判断（是否为阿拉丁魔盒酒店）
			         Aladin_OrderService aladin_OrderService = new Aladin_OrderService();
			         if(aladin_OrderService.isAladin(cancelRQ.getHotelid().trim()))
			         {
			        	 //是阿拉丁魔盒酒店用户
			        	 logger.info("aladin取消订单请求参数："+JSONObject.fromObject(cancelRQ));
			        	 JSONObject result_cancelOrder = aladin_OrderService.sendCancelOrderRQ(JSONObject.fromObject(cancelRQ));
			        	
				         if(result_cancelOrder!=null)
				         {
					        if(result_cancelOrder.getInt("backCode")==0)
					        {
					        	//成功取消订单,阿拉丁服务器成功返回
					        	orderdao.order_update(null,rootElt.elementTextTrim("TaoBaoOrderId"),"QUA");
					        }else{
					        	logger.error("取消订单失败:"+result_cancelOrder);
					        }
				         }else{
				        	 //阿拉丁服务器未响应
				        	 orderdao.order_update2F(rootElt.elementTextTrim("TaoBaoOrderId"),"QUA");
				         }
			         }else{
			        	 //非阿拉丁魔盒酒店用户
			        	 Map<String, Object> mp = new HashMap<String, Object>();
							mp.put("guid", getGuid());
							mp.put("command",1011);
							mp.put("bodyJson", JSONObject.fromObject(cancelRQ).toString());
							JSONObject params = JSONObject.fromObject(mp);
							DispatcherDao dispatcherDao = DispatcherDao.getInstance();
							if(dispatcherDao.isDL_PMS((String)result.get("hotelid")))
							{
//								System.out.println("取消订单数据格式："+params);
								logger.info("取消订单数据格式："+params);
								JSONObject result_jo = HttpSend_GET.SendPOST(BOOKCANCELRQ_URL, params);
								System.out.println("接收dp取消订单格式："+result_jo);
								if(result_jo==null)
								{
									orderdao.order_update2F(rootElt.elementTextTrim("TaoBaoOrderId"),"QUA");
								}
								else if(result_jo.getInt("BackCode")==0)
								{
									//链路器已接收该取消订单请求
									orderdao.order_update(null,rootElt.elementTextTrim("TaoBaoOrderId"),"QUA");
//									orderdao.order_updateForCancel(rootElt.elementTextTrim("TaobaoOrderId"),(String)result.get("mrk"));
								}
							}else{
								//非使用dlpms客户端的酒店订单，推送到轮询服务器
								CommonDao commonDao = CommonDao.getInstance();
								JSONObject param = commonDao.generateOrders_transport("orders_cancel", rootElt.elementTextTrim("TaoBaoOrderId"), (String)result.get("hotelid"), "QUA","");
								JSONObject result_jo = HttpSend_GET.SendPOST(PUSH_ORDER_URL, param);
								if(Integer.valueOf(result_jo.getString("status"))==0)
								{
									
									//更新订单下发状态为T
									PollingOrderDao.getInstance().order_update(null, rootElt.elementTextTrim("TaoBaoOrderId"), "QUA", false);
									
//									//创建订单成功
//									orderdao.order_update(result_jo.getString("resno"), jo_order.getString("TaoBaoOrderId"));
								}
								System.out.println("推送订单返回："+result_jo.getString("status")+","+result_jo.getString("responseDec"));
							}
			         }
			        //============================ 
					
				

	                break;
				default:
					break;
				}
		         

	         }else if("QueryStatusRQ".equals(rootElt.getName().trim()))
	         {
	        	 //查询订单状态操作
	        	 Iterator iterss = rootElt.elementIterator("AuthenticationToken"); ///获取根节点下的子节点AuthenticationToken
		         while(iterss.hasNext())
		         {
		        	 Element recordEle  = (Element)iterss.next();
		        	 String username = recordEle.elementTextTrim("Username");
		        	 String psd = recordEle.elementTextTrim("Password");
		        	 String createToken = recordEle.elementTextTrim("CreateToken"); 
		        	
		         }
		         
		         OrderDao orderdao = OrderDao.getInstance();
		         //检查该订单号是否已存在
		         Map<String, Object> result = orderdao.query_order_id(rootElt.elementTextTrim("TaoBaoOrderId"),rootElt.elementTextTrim("HotelId"));
		         switch ((Integer)result.get("code")) {
				case 0:
					//订单号不存在，返回-302，订单不存在
					out.write(XMLGenerator.XMLGenerator_QueryRQ(0,result).asXML());
					out.flush();
					out.close();
					break;
				case 10:
					//返回-303，云助手服务器未处理
					out.write(XMLGenerator.XMLGenerator_QueryRQ(10,result).asXML());
					out.flush();
					out.close();
					break;	
				case 2:
					//返回-301，订单未处理
					out.write(XMLGenerator.XMLGenerator_QueryRQ(2,result).asXML());
					out.flush();
					out.close();
					break;
                case 1:
                	 //确认状态
                	System.out.println("1:"+XMLGenerator.XMLGenerator_QueryRQ(1,result).asXML());
                	out.write(XMLGenerator.XMLGenerator_QueryRQ(1,result).asXML());
					out.flush();
					out.close();
					break;
                case 4:
                	//取消状态
                	System.out.println("cancel:"+XMLGenerator.XMLGenerator_QueryRQ(4,result).asXML());
                	out.write(XMLGenerator.XMLGenerator_QueryRQ(4,result).asXML());
					out.flush();
					out.close();
	                break;
                case 8:
                	//no show 状态
                	System.out.println("noshow:"+XMLGenerator.XMLGenerator_QueryRQ(8,result).asXML());
                	out.write(XMLGenerator.XMLGenerator_QueryRQ(8,result).asXML());
					out.flush();
					out.close();
	                break;    
                case 5:
                	//check in 状态
                	System.out.println("check_in:"+XMLGenerator.XMLGenerator_QueryRQ(5,result).asXML());
                	out.write(XMLGenerator.XMLGenerator_QueryRQ(5,result).asXML());
					out.flush();
					out.close();
	                break;   
                case 9:
                	//check out 状态
                	System.out.println("check_out:"+XMLGenerator.XMLGenerator_QueryRQ(9,result).asXML());
                	out.write(XMLGenerator.XMLGenerator_QueryRQ(9,result).asXML());
					out.flush();
					out.close();
	                break;         
				default:
					break;
				}
	         }

		} catch (DocumentException e) {
			logger.error(e);
			e.printStackTrace();
		}
//		System.out.println("编码后："+new String(sb.toString().getBytes(), "UTF-8"));
//		out
//				.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
//		out.println("<HTML>");
//		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
//		out.println("  <BODY>");
//		out.print("    This is ");
//		out.print(this.getClass());
//		out.println(", using the POST method");
//		out.println("  </BODY>");
	/*
	 * 生成xml
	 */
//		Document doc1 = DocumentHelper.createDocument();
//		Element root = DocumentHelper.createElement("Result");
//		doc1.setRootElement(root);
//		Element message = root.addElement("Message");
//		message.setText("创建订单成功");
//		Element resultCode = root.addElement("ResultCode");
//		resultCode.setText("0");
//		Element orderId  = root.addElement("OrderId ");
//		orderId.setText("21544874");
//		Element pmsResID  = root.addElement("PmsResID ");
//		pmsResID.setText("sd24343424");
//		XMLWriter xmlWriter = new XMLWriter();
//		
//		xmlWriter.write(doc1);
//		out.print(new String("结果：".getBytes(), "utf-8")+doc1.asXML());
//		out.flush();
//		out.close();
	}
	
	private String getGuid()
	{
		return new Date().getTime()+"";
	}
	private String getSystime()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	private JSONObject xml2json_order(Element root)
	{
		JSONObject jo = new JSONObject();
		
		jo.put("TaoBaoOrderId", root.elementTextTrim("TaoBaoOrderId"));
		jo.put("TaoBaoHotelId", root.elementTextTrim("TaoBaoHotelId"));
		jo.put("HotelId", root.elementTextTrim("HotelId"));
		jo.put("TaoBaoRoomTypeId", root.elementTextTrim("TaoBaoRoomTypeId"));
		jo.put("RoomTypeId", root.elementTextTrim("RoomTypeId"));
		jo.put("TaoBaoRatePlanId", root.elementTextTrim("TaoBaoRatePlanId"));
		jo.put("RatePlanCode", root.elementTextTrim("RatePlanCode"));
		jo.put("TaoBaoGid", root.elementTextTrim("TaoBaoGid"));
		jo.put("CheckIn", root.elementTextTrim("CheckIn"));
		jo.put("CheckOut", root.elementTextTrim("CheckOut"));
		jo.put("EarliestArriveTime", root.elementTextTrim("EarliestArriveTime"));
		jo.put("LatestArriveTime", root.elementTextTrim("LatestArriveTime"));
		jo.put("RoomNum", root.elementTextTrim("RoomNum"));
		jo.put("TotalPrice", root.elementTextTrim("TotalPrice"));
		if(root.element("SellerDiscount")!=null)
  		{
         	jo.put("SellerDiscount", root.elementTextTrim("SellerDiscount"));
  		}else{
  			jo.put("SellerDiscount", "");
  		}
		if(root.element("AlitripDiscount")!=null)
  		{
         	jo.put("AlitripDiscount", root.elementTextTrim("AlitripDiscount"));
  		}else{
  			jo.put("AlitripDiscount", "");
  		}
//		jo.put("SellerDiscount", root.elementTextTrim("SellerDiscount"));
//		jo.put("AlitripDiscount", root.elementTextTrim("AlitripDiscount"));
		jo.put("PaymentType", root.elementTextTrim("PaymentType"));
		jo.put("ContactName", root.elementTextTrim("ContactName"));
		jo.put("ContactTel", root.elementTextTrim("ContactTel"));
		 if(root.element("ContactEmail")!=null)
	  		{
	         	jo.put("ContactEmail", root.elementTextTrim("ContactEmail"));
	  		}else{
	  			jo.put("ContactEmail", "");
	  		}
		
		
		 Element dailyInfos = root.element("DailyInfos"); ///获取根节点下的子节点AuthenticationToken
         Iterator iterss1 = dailyInfos.elementIterator("DailyInfo");
         List<DailyInfo> list = new ArrayList<DailyInfo>();
         while(iterss1.hasNext())
         {
        	 Element recordEle  = (Element)iterss1.next();
        	 DailyInfo dailyInfo = new DailyInfo();
        	 dailyInfo.setDay(recordEle.elementTextTrim("Day"));
        	 dailyInfo.setPrice(recordEle.elementTextTrim("Price"));
        	 list.add(dailyInfo);
         }
         jo.put("DailyInfos",JSONArray.fromObject(list));
		
//         System.out.println("");
         Element orderGuests = root.element("OrderGuests"); ///获取根节点下的子节点AuthenticationToken
         Iterator iterss2 = orderGuests.elementIterator("OrderGuest");
         List<OrderGuest> list1 = new ArrayList<OrderGuest>();
         while(iterss2.hasNext())
         {
        	 Element recordEle  = (Element)iterss2.next();
        	 OrderGuest orderGuest = new OrderGuest();
        	 orderGuest.setName(recordEle.elementTextTrim("Name"));
        	 orderGuest.setRoomPos(recordEle.elementTextTrim("RoomPos"));
        	 list1.add(orderGuest);
         }
//         jo.put("DailyInfos",JSONArray.fromObject(list));
         jo.put("OrderGuests", JSONArray.fromObject(list1));
         if(root.element("Comment")!=null)
  		{
         	jo.put("Comment", root.elementTextTrim("Comment"));
  		}else{
  			jo.put("Comment", "");
  		}
         if(root.element("GuaranteeType")!=null)
  		{
         	jo.put("GuaranteeType", root.elementTextTrim("GuaranteeType"));
  		}else{
  			jo.put("GuaranteeType", "");
  		}
         if(root.element("MemberCardNo")!=null)
  		{
         	jo.put("MemberCardNo", root.elementTextTrim("MemberCardNo"));
  		}else{
  			jo.put("MemberCardNo", "");
  		}
//		jo.put("Comment", root.elementTextTrim("Comment"));
//		jo.put("GuaranteeType", root.elementTextTrim("GuaranteeType"));
//		jo.put("MemberCardNo", root.elementTextTrim("MemberCardNo"));
		if(root.element("ReceiptInfo")!=null)
		{
			Element receiptInfo_et = root.element("ReceiptInfo"); ///获取根节点下的子节点AuthenticationToken
			 ReceiptInfo receiptInfo = new ReceiptInfo();
			 receiptInfo.setReceiptTitle(receiptInfo_et.elementTextTrim("ReceiptTitle"));
			 receiptInfo.setReceiptType(receiptInfo_et.elementTextTrim("ReceiptType"));
			 ReceiptAddress ra = new ReceiptAddress();
			 JSONObject jo_receiptAddress = JSONObject.fromObject(receiptInfo_et.elementTextTrim("ReceiptAddress"));
			 ra.setAddress(jo_receiptAddress.getString("address"));
			 ra.setMobile(jo_receiptAddress.getString("mobile"));
			 ra.setName(jo_receiptAddress.getString("name"));
			 ra.setPhone(jo_receiptAddress.getString("phone"));
			 ra.setPostCode(jo_receiptAddress.getString("postCode"));
			 receiptInfo.setReceiptAddress(ra);
			jo.put("ReceiptInfo", JSONObject.fromObject(receiptInfo)); 
		}else{
//			Element receiptInfo_et = root.element("ReceiptInfo"); 
			 ReceiptInfo receiptInfo = new ReceiptInfo();
			 receiptInfo.setReceiptTitle("");
			 receiptInfo.setReceiptType("");
			 ReceiptAddress ra = new ReceiptAddress();
//			 JSONObject jo_receiptAddress = JSONObject.fromObject(receiptInfo_et.elementTextTrim("ReceiptAddress"));
			 ra.setAddress("");
			 ra.setMobile("");
			 ra.setName("");
			 ra.setPhone("");
			 ra.setPostCode("");
			 receiptInfo.setReceiptAddress(ra);
			jo.put("ReceiptInfo", JSONObject.fromObject(receiptInfo)); 
		}
		 
	
		
		System.out.println("xml2json:"+jo.toString());
		return jo;
		
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
