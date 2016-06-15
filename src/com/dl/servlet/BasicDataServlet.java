package com.dl.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.SOAPBody;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.dom4j.Document;

import com.dl.dao.CheckDao;
import com.dl.dao.CommonDao;
import com.dl.dao.HotelInfoDao;
import com.dl.pojo.CheckInResult;
import com.dl.pojo.OTAResults;
import com.dl.pojo.Rate;
import com.dl.pojo.RmType;
import com.dl.pojo.RoomRates;
import com.dl.pojo.RpCode;
import com.dl.pojo.Set_roommap;
import com.dl.pojo.Set_roomprice;
import com.dl.service.HotelInfoService;
import com.dl.utl.CommonTool;
import com.dl.utl.HttpSend_GET;
import com.dl.utl.SoapUtl;
import com.dl.utl.XMLGenerator_RZT;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.StatementOrder;
import com.taobao.api.request.XhotelAddRequest;
import com.taobao.api.request.XhotelOrderStatementGetRequest;
import com.taobao.api.request.XhotelRateplanAddRequest;
import com.taobao.api.request.XhotelRoomtypeAddRequest;
import com.taobao.api.response.XhotelAddResponse;
import com.taobao.api.response.XhotelOrderStatementGetResponse;
import com.taobao.api.response.XhotelRateplanAddResponse;
import com.taobao.api.response.XhotelRoomtypeAddResponse;

public class BasicDataServlet extends HttpServlet {

	//测试
//	private final static String URL = "http://gw.api.tbsandbox.com/router/rest";
//	private final static String APPKEY = "1023269815";
//	private final static String SECRET = "sandbox2c2edcd6c88993ef24981cad9";
//	private final static String SESSIONKEY = "6102409601b12fd8a154c9aad46fb89dced3d3781a35e0e3688302491";
//	private final static String KEZHAN_SESSIONKEY = "6101b242adf1cb5497539e7a7dbaa97d107aa9297261a0f31998728";
	//正式
	private final static String URL = "http://gw.api.taobao.com/router/rest";
	private final static String APPKEY = "23269815";
	private final static String SECRET = "b7287242c2edcd6c88993ef24981cad9";
	private final static String SESSIONKEY = "6101813d6bee40f3b0a061dc0eb6ac054d428a1efcb59032708043296";
	private final static String KEZHAN_SESSIONKEY = "6101b242adf1cb5497539e7a7dbaa97d107aa9297261a0f31998728";
	
	
	//众荟测试
	private final static String RPCODE_URL = "http://124.127.242.67/PMSGateway39/PMSGatewayService.asmx";
	private final static String RMMAP_URL = "http://124.127.242.67/PMSGateway39/PMSGatewayService.asmx";
	
	/**
	 * Constructor of the object.
	 */
	private Logger logger = Logger.getLogger(BasicDataServlet.class);  
	public BasicDataServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>javascript:;
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

		//获取房价计划编码
		response.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = response.getWriter();
		JSONObject json_data = null;
		try {
			json_data = JSONObject.fromObject(request.getParameter("data"));
		} catch (Exception e) {
			logger.error(e);
			out.write(CommonDao.getInstance().generateResponseJSon("ratePlan_code_get","1","1000","参数转换JSon错误("+e.getMessage()+")",null));
			out.flush();
			out.close();
			return;
		}
		System.out.println("get:"+json_data);
		//获取唯一rateplan_code
		if("ratePlan_code_get".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			HotelInfoDao hd = HotelInfoDao.getInstance();
			try {
				String rateplan_code = hd.rateplan_code_GET(json_data.getJSONArray("requestData").getJSONObject(0).getString("type"));
				out.write(CommonDao.getInstance().generateSimpleResponseJSon("ratePlan_code_get","0","0000","获取RPcode成功",rateplan_code));
				out.flush();
				out.close();
			} catch (Exception e) {
				logger.error(e);
				out.write(CommonDao.getInstance().generateResponseJSon("ratePlan_code_get","1","6000",e.getMessage(),null));
				out.flush();
				out.close();
				e.printStackTrace();
			}
		}else if("orders_query".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			HashMap<String, Object> mp = OrdersQuery(URL, APPKEY, SECRET, SESSIONKEY, json_data.getJSONArray("requestData").getJSONObject(0));
			if("0".equals(mp.get("code")))
			{
				HotelInfoService vs = new HotelInfoService();
				try {
					vs.ordersQuery(out,JSONArray.fromObject((List<StatementOrder> )mp.get("msg")));
				} catch (Exception e) {
					logger.error(e);
					out.write(CommonDao.getInstance().generateResponseJSon_SHR("orders_query","1","6000",e.getMessage(),null));
					out.flush();
					out.close();
					e.printStackTrace();
				}
			}else if("-1".equals(mp.get("code"))){
				out.write(CommonDao.getInstance().generateResponseJSon_SHR("orders_query","-1","6000","调用去啊接口（statement.get）出错",null));
				out.flush();
				out.close();
			}else{
				out.write(CommonDao.getInstance().generateResponseJSon_SHR("orders_query",(String)mp.get("sub_code"),"6000",(String)mp.get("msg"),null));
				out.flush();
				out.close();
			}
		}
		
		//=======================================================================
//		if("check".equalsIgnoreCase(json_data.getString("serviceName").trim()))
//		{
//			JSONObject jo_check = new JSONObject();
//			try {
//				int days = CommonTool.getDays(jo_check.getString("bdate"),jo_check.getString("edate"));
//				
//				List<CheckInResult> list = CheckDao.getInstance().getResultsByCondition("NoType_NoCode", jo_check, days);
//				JSONArray ja = JSONArray.fromObject(list);
//				if(ja.size()>0)
//				{
//					//有满足条件的数据
//				    List<RoomRates> list_roomRates = new ArrayList<RoomRates>();
//				    StringBuilder sb_rmtype = new StringBuilder();
//				    StringBuilder sb_rpcode = new StringBuilder();
//					for (int i = 0; i < ja.size(); i++) {
//						RoomRates roomrates = new RoomRates();
//						roomrates.setRmtype(ja.getJSONObject(i).getString("rmtype"));
//						roomrates.setRpcode(ja.getJSONObject(i).getString("rpcode"));
//						float totalprice = 0;
//						List<Rate> rate_list = new ArrayList<Rate>(); 
//						for (int j = 0; j <= days; j++) {
//							Rate rate = new Rate();
//							
//							OTAResults otaResults = CheckDao.getInstance().getRoomRatesByConditions(ja.getJSONObject(i),CommonTool.getDateOfLaterDays(jo_check.getString("bdate"),j));
//							
//							if(sb_rmtype.indexOf(otaResults.getRmtype())==-1)
//							{
//								sb_rmtype.append(otaResults.getRmtype()+"#");
//								sb_rmtype.append(otaResults.getRmtype_des()+";");
//							}
//							if(sb_rpcode.indexOf(otaResults.getRpcode())==-1)
//							{
//								sb_rpcode.append(otaResults.getRpcode()+"#");
//								sb_rpcode.append(otaResults.getRpcode_des()+";");
//							}
//							totalprice = totalprice+Float.parseFloat(otaResults.getPrice());
//							rate.setBdate(CommonTool.getDateOfLaterDays(jo_check.getString("bdate"),j));
//							rate.setEdate(CommonTool.getDateOfLaterDays(jo_check.getString("bdate"),j+1));
//							rate.setPrice(otaResults.getPrice());
//							rate_list.add(rate);
//						}
//						roomrates.setTotalPrice(totalprice+"");
//						roomrates.setRates(JSONArray.fromObject(rate_list));
//						list_roomRates.add(roomrates);
//					}
//					
//					String[] rmtypes = sb_rmtype.toString().split(";");
//					List<RmType> list_rmtype = new ArrayList<RmType>();
//					for (int i = 0; i < rmtypes.length; i++) {
//						RmType rmtype = new RmType();
//						String[] rmtypes_detail = rmtypes[i].split("#");
//						for (int j = 0; j < rmtypes_detail.length; j++) {
//							rmtype.setRmtype(rmtypes_detail[0]);
//							rmtype.setRmtype_des(rmtypes_detail[1]);
//						}
//						list_rmtype.add(rmtype);
//					}
//					
//					String[] rpcodes = sb_rpcode.toString().split(";");
//					List<RpCode> list_rpcode = new ArrayList<RpCode>();
//					for (int i = 0; i < rpcodes.length; i++) {
//						RpCode rpcode = new RpCode();
//						String[] rpcodes_detail = rpcodes[i].split("#");
//						for (int j = 0; j < rpcodes_detail.length; j++) {
//							rpcode.setRpcode(rpcodes_detail[0]);
//							rpcode.setRpcode_des(rpcodes_detail[1]);
//						}
//						list_rpcode.add(rpcode);
//					}
//					
//					JSONObject jo_result = new JSONObject();
//					jo_result.put("hotelid", jo_check.getString("hotelid"));
//					jo_result.put("hotelname", list.get(0).getHotelName());
//					jo_result.put("start", jo_check.getString("bdate"));
//					jo_result.put("end", jo_check.getString("edate"));
//					jo_result.put("rmtypes", JSONArray.fromObject(list_rmtype));
//					jo_result.put("rateplans", JSONArray.fromObject(list_rpcode));
//					jo_result.put("rmrates", JSONArray.fromObject(list_roomRates));
//					System.out.println("最终结果："+jo_result);
//					
//				}else{
//					//无满足条件的数据
//					
//				}
//			
//			} catch (ParseException e) {
//				
//				e.printStackTrace();
//			}
//		}
		
		
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
		JSONObject json_data = null;
		try {
			logger.info("基础数据提交:"+request.getParameter("data"));
//			System.out.println("data:"+request.getParameter("data"));
			json_data = JSONObject.fromObject(request.getParameter("data"));
		} catch (Exception e) {
			logger.error(e);
			out.write(CommonDao.getInstance().generateResponseJSon("hotel_add_update","1","1000","参数转换JSon错误("+e.getMessage()+")",null));
			out.flush();
			out.close();
			e.printStackTrace();
			return;
		}
		System.out.println("post:"+json_data);
		//酒店信息添加或修改
		if("hotel_add_update".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			if(!json_data.has("ishotel"))
			{//兼容老版本
				
				HashMap<String,String> mp = HotelAdd(URL, APPKEY, SECRET, SESSIONKEY, json_data.getString("hotelid"), json_data.getJSONArray("requestData").getJSONObject(0));
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
			}else{
				HashMap<String,String> mp = null;
				if("1".equals(json_data.getString("ishotel").trim()))
				{//酒店
					
					 mp = HotelAdd(URL, APPKEY, SECRET, SESSIONKEY, json_data.getString("hotelid"), json_data.getJSONArray("requestData").getJSONObject(0));
				}else{
				//客栈
					
					mp = HotelAdd(URL, APPKEY, SECRET, KEZHAN_SESSIONKEY, json_data.getString("hotelid"), json_data.getJSONArray("requestData").getJSONObject(0));
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
				
				HashMap<String, String> mp = RmtypeAdd(URL, APPKEY, SECRET, SESSIONKEY, json_data.getString("hotelid"), json_data.getJSONArray("requestData").getJSONObject(0));
				
				
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
			}else{
				//新版本
				HashMap<String,String> mp = null;
				if("1".equals(json_data.getString("ishotel").trim()))
				{//酒店
					
					 mp = RmtypeAdd(URL, APPKEY, SECRET, SESSIONKEY, json_data.getString("hotelid"), json_data.getJSONArray("requestData").getJSONObject(0));
				}else{
				//客栈
					
					 mp = RmtypeAdd(URL, APPKEY, SECRET, KEZHAN_SESSIONKEY, json_data.getString("hotelid"), json_data.getJSONArray("requestData").getJSONObject(0));
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
				
				HashMap<String, String> mp = RatePlanAdd(URL, APPKEY, SECRET, SESSIONKEY, json_data.getString("hotelid"),json_data.getJSONArray("requestData").getJSONObject(0));
//				System.out.println("RP修改："+mp.get("msg"));
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
			}else{
				//新版本
				HashMap<String,String> mp = null;
				if("1".equals(json_data.getString("ishotel").trim()))
				{//酒店
					
					mp = RatePlanAdd(URL, APPKEY, SECRET, SESSIONKEY, json_data.getString("hotelid"),json_data.getJSONArray("requestData").getJSONObject(0));
				}else{
				//客栈
				
					mp = RatePlanAdd(URL, APPKEY, SECRET, KEZHAN_SESSIONKEY, json_data.getString("hotelid"),json_data.getJSONArray("requestData").getJSONObject(0));
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
		/*
		 * 众荟房间计划推送
		 */
		else if("RatePlanNotifRQ".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			try {	
					JSONArray ja_roomprice = json_data.getJSONArray("requestData");
					List<Set_roomprice> list = new ArrayList<Set_roomprice>();
					for(int i=1;i<ja_roomprice.size();i++)
					{
						Set_roomprice rs = new Set_roomprice();
						rs.setHotelid(json_data.getString("hotelid"));
						rs.setRm_bdate(ja_roomprice.getJSONObject(i).getString("rm_bdate"));
						rs.setRm_edate(ja_roomprice.getJSONObject(i).getString("rm_edate"));
						rs.setRmtype(ja_roomprice.getJSONObject(i).getString("rmtype"));
						rs.setRp_code(ja_roomprice.getJSONObject(i).getString("rpcode"));
						rs.setRp_bdate(ja_roomprice.getJSONObject(i).getString("rp_bdate"));
						rs.setRp_edate(ja_roomprice.getJSONObject(i).getString("rp_edate"));
						rs.setMon(ja_roomprice.getJSONObject(i).getString("Mon"));
						rs.setTUE(ja_roomprice.getJSONObject(i).getString("Tue"));
						rs.setWED(ja_roomprice.getJSONObject(i).getString("Wed"));
						rs.setTUR(ja_roomprice.getJSONObject(i).getString("Tur"));
						rs.setFRI(ja_roomprice.getJSONObject(i).getString("Fri"));
						rs.setSAT(ja_roomprice.getJSONObject(i).getString("Sat"));
						rs.setSUN(ja_roomprice.getJSONObject(i).getString("Sun"));
					    rs.setRm_price(ja_roomprice.getJSONObject(i).getString("rm_price"));
						list.add(rs);
					}
					System.out.println("list:"+JSONArray.fromObject(list).toString());
					Document doc = XMLGenerator_RZT.XMLGenerator_RatePlanNotifRQ(json_data.getString("hotelid"), JSONArray.fromObject(list));
					String result = HttpSend_GET.SendPOST_Soap(RPCODE_URL, doc.asXML());
					System.out.println("结果："+result);
					SOAPBody body = SoapUtl.str2soapbody(result);
					if(body.getFirstChild().hasChildNodes())
					{
						System.out.println("content:"+body.getFirstChild().getFirstChild().getFirstChild().getTextContent());
						logger.info(json_data.getString("hotelid")+":众荟房间计划推送失败("+body.getFirstChild().getFirstChild().getFirstChild().getTextContent()+")");
						out.write(CommonDao.getInstance().generateResponseJSon("RatePlanNotifRQ","1","1000",body.getFirstChild().getFirstChild().getFirstChild().getTextContent(),null));
						out.flush();
						out.close();
					}else{
						logger.info(json_data.getString("hotelid")+":众荟房间计划推送成功");
						out.write(CommonDao.getInstance().generateResponseJSon("RatePlanNotifRQ","0","0000","众荟房间计划推送成功",null));
						out.flush();
						out.close();
						
					}
				

			   }catch (Exception e) {
					e.printStackTrace();
					logger.error(e);
					}
		}
		
		/*
		 * 众荟房态推送
		 */
		else if("AvailNotifRQ".equalsIgnoreCase(json_data.getString("serviceName").trim()))
		{
			try {	
				JSONArray ja_roommap = json_data.getJSONArray("requestData");
				
				List<Set_roommap> list = new ArrayList<Set_roommap>();
				for(int i=1;i<ja_roommap.size();i++)
				{
					Set_roommap rs = new Set_roommap();
					rs.setHotelid(json_data.getString("hotelid"));
					rs.setBdate(ja_roommap.getJSONObject(i).getString("bdate"));
					rs.setEdate(ja_roommap.getJSONObject(i).getString("edate"));
					rs.setRmtype(ja_roommap.getJSONObject(i).getString("rmtype"));
					rs.setRp_code(ja_roommap.getJSONObject(i).getString("rp_code"));
					rs.setChannel(ja_roommap.getJSONObject(i).getString("channel"));
					rs.setMon(ja_roommap.getJSONObject(i).getString("Mon"));
					rs.setTUE(ja_roommap.getJSONObject(i).getString("Tue"));
					rs.setWED(ja_roommap.getJSONObject(i).getString("Wed"));
					rs.setTUR(ja_roommap.getJSONObject(i).getString("Tur"));
					rs.setFRI(ja_roommap.getJSONObject(i).getString("Fri"));
					rs.setSAT(ja_roommap.getJSONObject(i).getString("Sat"));
					rs.setSUN(ja_roommap.getJSONObject(i).getString("Sun"));
					rs.setStatus(ja_roommap.getJSONObject(i).getString("status"));
					rs.setType_set(ja_roommap.getJSONObject(i).getString("type_set"));
					rs.setDay_set(ja_roommap.getJSONObject(i).getString("day_set"));
					list.add(rs);
				}
				System.out.println("list:"+JSONArray.fromObject(list).toString());
				Document doc = XMLGenerator_RZT.XMLGenerator_NotifRQ(json_data.getString("hotelid"), JSONArray.fromObject(list));
				String result = HttpSend_GET.SendPOST_Soap(RMMAP_URL, doc.asXML());
				SOAPBody body = SoapUtl.str2soapbody(result);
				if(body.getFirstChild().hasChildNodes())
				{
					System.out.println("content:"+body.getFirstChild().getFirstChild().getFirstChild().getTextContent());
					logger.info(json_data.getString("hotelid")+":众荟房态推送失败("+body.getFirstChild().getFirstChild().getFirstChild().getTextContent()+")");
					out.write(CommonDao.getInstance().generateResponseJSon("AvailNotifRQ","1","1000",body.getFirstChild().getFirstChild().getFirstChild().getTextContent(),null));
					out.flush();
					out.close();
				}else{
					System.out.println("推送成功");
					logger.info(json_data.getString("hotelid")+":众荟房态推送成功");
					out.write(CommonDao.getInstance().generateResponseJSon("AvailNotifRQ","0","0000","众荟房态推送成功",null));
					out.flush();
					out.close();
				}

			}
			 catch (Exception e) {
					e.printStackTrace();
					logger.error(e);
				}
			   
		}
		
		
		
	}
	
	private  HashMap<String, String> HotelAdd(String url,String appkey,String secret,String sessionKey,String hotelid,JSONObject requestData)
	{
		/*
		 * 新增酒店,id重复自动更新
		 */
		HashMap<String, String> mp = new HashMap<String, String>();
		mp.put("code", "-1");
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		XhotelAddRequest req = new XhotelAddRequest();
		req.setOuterId(hotelid);
		req.setName(requestData.getString("name").trim());
//		req.setUsedName("北京饭店");
		req.setDomestic(0L);
//		req.setCountry("China");
//		req.setProvince(110000L);
		req.setCity(Long.valueOf(requestData.getString("city").trim()));
		if(requestData.has("district"))
		{
			req.setDistrict(Long.valueOf(requestData.getString("district").trim()));	
		}
//		req.setDistrict(110101L);
//		req.setBusiness("aaa");
		req.setAddress(requestData.getString("address").trim());
//		req.setLongitude("111.13");
//		req.setLatitude("111.1222");
//		req.setPositionType("G");
		req.setTel(requestData.getString("tel").trim());
//		req.setExtend("{\"keyword\":”上地”}");
//		req.setShid(123123L);
		req.setVendor(requestData.getString("vendor").trim());
		try {
			XhotelAddResponse rsp = client.execute(req, sessionKey);
			if(rsp.getXhotel()!=null)
			{
				mp.put("code", "0");
				mp.put("msg", rsp.getBody());
			}else{
				mp.put("code", "1");
				System.out.println("去啊接口响应:"+rsp.getBody());
				mp.put("sub_code", rsp.getSubCode());
				mp.put("sub_msg", rsp.getSubMsg());
				mp.put("msg", rsp.getBody());
			}
			System.out.println("阿里返回："+rsp.getBody());
		} catch (ApiException e) {
			logger.error(e);
			e.printStackTrace();
		}
		
		return mp;
		
		
	}
	
	private  HashMap<String, String> RmtypeAdd(String url,String appkey,String secret,String sessionKey,String hotelid,JSONObject requestData)
	{
		/*
		 * 新增酒店房型，id重复自动更新
		 */
		HashMap<String, String> mp = new HashMap<String, String>();
		mp.put("code", "-1");
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		XhotelRoomtypeAddRequest req = new XhotelRoomtypeAddRequest();
		System.out.println("房型id:"+requestData.getString("rmtype_id").trim());
		req.setOuterId(requestData.getString("rmtype_id").trim());
//		req.setHid(1L);
		req.setName(requestData.getString("name").trim());
//		req.setMaxOccupancy(2L);
//		req.setArea("A");
//		req.setFloor("3-8层");
//		req.setBedType("小床");
//		req.setBedSize("大床2.1米");
		req.setInternet(requestData.getString("internet").trim());
//		req.setService("{\"bar\":false,\"catv\":false,\"ddd\":false,\"idd\":false,\"pubtoilet\":false,\"toilet\":false}");
//		req.setExtend("空");
		req.setWindowType(Long.valueOf(requestData.getString("windows_type").trim()));
//		req.setSrid(123123L);
		req.setOutHid(hotelid.trim());
		req.setVendor(requestData.getString("vendor").trim());
		
		
		try {
			XhotelRoomtypeAddResponse rsp = client.execute(req,sessionKey);
			if(rsp.getXroomtype()!=null)
			{
				mp.put("code", "0");
				mp.put("msg", rsp.getBody());
			}else{
				mp.put("code", "1");
				mp.put("sub_code", rsp.getSubCode());
				mp.put("sub_msg", rsp.getSubMsg());
				mp.put("msg", rsp.getBody());
			}
		} catch (ApiException e) {
			logger.error(e);
			e.printStackTrace();
		}
		
		return mp;
		
		
	}
	
	
	private  HashMap<String, Object> OrdersQuery(String url,String appkey,String secret,String sessionKey,JSONObject requestData)
	{
		/*
		 * 查询订单信息
		 */
		HashMap<String, Object> mp = new HashMap<String, Object>();
		mp.put("code", "-1");
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		XhotelOrderStatementGetRequest req = new XhotelOrderStatementGetRequest();
		req.setOrderTids(requestData.getString("orderids"));
		
		
		try {
			XhotelOrderStatementGetResponse rsp = client.execute(req, sessionKey);
			if(rsp.getStatements()!=null&&rsp.getStatements().size()>0)
			{
				mp.put("code", "0");
				mp.put("msg", rsp.getStatements());
			}else{
				mp.put("code", "1");
				System.out.println("去啊接口响应:"+rsp.getBody());
				mp.put("sub_code", rsp.getSubCode());
				mp.put("sub_msg", rsp.getMsg());
				mp.put("msg", rsp.getBody());
			}
			System.out.println("阿里返回："+rsp.getBody());
		} catch (ApiException e) {
			logger.error(e);
			e.printStackTrace();
		}
		
		return mp;
		
		
	}
	
	private  HashMap<String, String> RatePlanAdd(String url,String appkey,String secret,String sessionKey,String hotelid,JSONObject requestData)
	{
		/*
		 * 新增酒店RatePlan,id重复自动更新
		 */
		HashMap<String, String> mp = new HashMap<String, String>();
		mp.put("code", "-1");
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		XhotelRateplanAddRequest req = new XhotelRateplanAddRequest();
		req.setRateplanCode(requestData.getString("rateplan_code").trim());
		req.setName(requestData.getString("name").trim());
		req.setPaymentType(Long.valueOf(requestData.getString("payment_type")));
		req.setBreakfastCount(Long.valueOf(requestData.getString("breakfast_count")));
//		req.setFeeBreakfastCount(1L);
//		req.setFeeBreakfastAmount(1L);
//		req.setFeeGovTaxAmount(1L);
//		req.setFeeGovTaxPercent(1L);
//		req.setFeeServiceAmount(150L);
//		req.setFeeServicePercent(15L);
//		req.setExtendFee("aaa");
//		req.setMinDays(1L);
//		req.setMaxDays(90L);
//		req.setMinAmount(1L);
//		req.setMinAdvHours(1L);
//		req.setMaxAdvHours(3L);
//		req.setStartTime("00:00");
//		req.setEndTime("00:00");
//		System.out.println("requestData:"+requestData);
//		System.out.println("cancel_policy:"+requestData.getString("breakfast_count"));
		System.out.println("退订政策："+requestData.getString("cancel_policy"));
//		try {
//			System.out.println("担保政策："+requestData.getString("guarantee_type"));
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
		
		
//		req.setExtend("1");
		req.setStatus(Long.valueOf(requestData.getString("status")));
//		req.setEnglishName("aaa");
		if(requestData.has("guarantee_type"))
		{
			req.setCancelPolicy(requestData.getString("cancel_policy"));
			req.setGuaranteeType(Long.valueOf(requestData.getString("guarantee_type")));
			System.out.println("担保政策："+requestData.getString("guarantee_type"));
		}else{
			System.out.println("无担保政策");
			req.setCancelPolicy("{\"cancelPolicyType\":1}");
		}
		
//		requestData.has("")
//		req.setGuaranteeStartTime("18:00");
//		req.setMemberLevel("1,2,3");
//		req.setChannel("A");
//		req.setOccupancy(3L);
		req.setVendor(requestData.getString("vendor").trim());
//		req.setFirstStay(1L);
//		req.setAgreement(1L);
//		req.setBreakfastCal("{\"date\":\"yyyy-MM-dd\",\"startDate\":\"yyyy-MM-dd\",\"endDate\":\"yyyy-MM-dd\",\"breakfast_count\":0},{\"date\":\"yyyy-MM-dd\",\"startDate\":\"yyyy-MM-dd\",\"endDate\":\"yyyy-MM-dd\",\"breakfast_count\":1}");
//		req.setCancelPolicyCal("{\"date\":\"yyyy-MM-dd\",\"startDate\":\"yyyy-MM-dd\",\"endDate\":\"yyyy-MM-dd\",\"cancel_policy\":{\"cancelPolicyType\":1} },{\"date\":\"yyyy-MM-dd\",\"startDate\":\"yyyy-MM-dd\",\"endDate\":\"yyyy-MM-dd\",\"cancel_policy\":{\"cancelPolicyType\":4,\"policyInfo\":{\"48\":10,\"24\":20}}}");
//		req.setGuaranteeCal("{\"date\":\"yyyy-MM-dd\",\"startDate\":\"yyyy-MM-dd\",\"endDate\":\"yyyy-MM-dd\",\"guarantee\":{\"guaranteeType\":2,\"guaranteeStartTime\":\"HH:mm\"}},{\"date\":\"yyyy-MM-dd\",\"startDate\":\"yyyy-MM-dd\",\"endDate\":\"yyyy-MM-dd\",\"guarantee\":{\"guaranteeType\":3,\"guaranteeStartTime\":\"HH:mm\"}}");
//		req.setCancelBeforeHour("6");
//		req.setCancelBeforeDay(2L);
//		req.setEffectiveTime(StringUtils.parseDateTime("2015-11-21 00:00:00"));
//		req.setDeadlineTime(StringUtils.parseDateTime("2016-04-01 00:00:00"));
//		req.setRpType("1");
//		req.setHourage("4");
//		req.setCanCheckinEnd("08:00");
//		req.setCanCheckinStart("16:00");
		
		
		
		try {
			XhotelRateplanAddResponse rsp = client.execute(req,sessionKey);
			System.out.println("rpid:"+rsp.getRpid());
			if(rsp.getRpid()!=null)
			{
				mp.put("code", "0");
				mp.put("msg", rsp.getBody());
			}else{
				
				mp.put("code", "1");
				mp.put("sub_code", rsp.getSubCode());
				mp.put("sub_msg", rsp.getSubMsg());
				mp.put("msg", rsp.getBody());
			}
		} catch (ApiException e) {
			logger.error(e);
			e.printStackTrace();
		}
		
		return mp;
		
		
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
