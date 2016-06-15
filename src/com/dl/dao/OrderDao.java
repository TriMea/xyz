package com.dl.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.dl.datasource.DBPool;
import com.dl.pojo.OTA_RetrieveBookRQ;
import com.dl.servlet.RequestServlet;
import com.dl.utl.CommonTool;


public class OrderDao {

	/*
	 * 单例模式
	 */
	private OrderDao() {}  
	private Logger logger = Logger.getLogger(OrderDao.class); 
    private static final OrderDao orderdao = new OrderDao();  
   
    public static OrderDao getInstance() {  
        return orderdao;  
    }  
    
    /*
	 * Order唯一编码的获取
	 */
	public  String order_id_GET(String type)
	{
		 CallableStatement  cs = null;
		 String rpCode = null;
		 Connection conn = null;
		 
		 try {
			 conn = DBPool.getPool().getConnection();
			 cs = conn.prepareCall("{call p_ota_getno(?)}");
			 cs.setString(1,type);
			
			 if(cs.execute())
			 {
				 ResultSet rs = cs.getResultSet();
				 rs.next();
				 rpCode = rs.getString(1);
			 }
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			
			try {
				
				CommonTool.closeCallableStatement(cs);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
	    
		return rpCode;
	}
	
	
	/*
	 * 检测订单号是否重复
	 */
	public  Map<String, Object> ota_validate_order_id(String taobaoorder_id)
	{
		 CallableStatement  cs = null;
		 Integer rpCode = null;
		 Connection conn = null;
		 Map<String, Object> mp = new HashMap<String, Object>();
		 
		 try {
			 conn = DBPool.getPool().getConnection();
			 cs = conn.prepareCall("{call p_ota_validate_order_lyy(?)}");
			 cs.setString(1,taobaoorder_id);
			
			 if(cs.execute())
			 {
				 ResultSet rs = cs.getResultSet();
				 rs.next();
				 rpCode = rs.getInt(1);
				 switch (rpCode) {
				case 0:
					mp.put("code", 0);
					break;
                 case 1:
                	 mp.put("code", 1);
                	 mp.put("orderid", rs.getString(2));
					break;
                 case 2:
                	 mp.put("code", 2);
                	 mp.put("orderid", rs.getString(2));
                	 mp.put("pmsresid", rs.getString(3));
 					break;
				default:
					break;
				}
			 }
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			
			try {	
				CommonTool.closeCallableStatement(cs);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
	    System.out.println("返回："+(Integer)mp.get("code"));
		return mp;
	}
	
	
	/*
	 * 检测订单号是否重复
	 */
	public  Map<String, Object> validate_order_id(String taobaoorder_id)
	{
		 CallableStatement  cs = null;
		 Integer rpCode = null;
		 Connection conn = null;
		 Map<String, Object> mp = new HashMap<String, Object>();
		 
		 try {
			 conn = DBPool.getPool().getConnection();
			 cs = conn.prepareCall("{call p_xyz_validate_order_lyy(?)}");
			 cs.setString(1,taobaoorder_id);
			
			 if(cs.execute())
			 {
				 ResultSet rs = cs.getResultSet();
				 rs.next();
				 rpCode = rs.getInt(1);
				 switch (rpCode) {
				case 0:
					mp.put("code", 0);
					break;
                 case 1:
                	 mp.put("code", 1);
                	 mp.put("orderid", rs.getString(2));
					break;
                 case 2:
                	 mp.put("code", 2);
                	 mp.put("orderid", rs.getString(2));
                	 mp.put("pmsresid", rs.getString(3));
 					break;
				default:
					break;
				}
			 }
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			
			try {	
				CommonTool.closeCallableStatement(cs);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
	    System.out.println("返回："+(Integer)mp.get("code"));
		return mp;
	}
	
	
	/*
	 * 检测待修改订单是否可以修改
	 */
	public  Integer validate_update_order(JSONObject jo)
	{
		 CallableStatement  cs = null;
		 Integer rpCode = null;
		 Connection conn = null;
		 Integer result_code = 0;
		 Map<String, Object> mp = new HashMap<String, Object>();
		 
		 try {
			 conn = DBPool.getPool().getConnection();
			 cs = conn.prepareCall("{call p_updateOrder_validate_lyy(?,?,?)}");
			 logger.info("参数："+jo.getString("pmsresid")+","+jo.getString("hotelid")+","+jo.getString("mkt"));
			 cs.setString(1,jo.getString("pmsresid"));
			 cs.setString(2,jo.getString("hotelid"));
			 cs.setString(3,jo.getString("mkt"));
			 if(cs.execute())
			 {
				 ResultSet rs = cs.getResultSet();
				 rs.next();
				 rpCode = rs.getInt(1);
				 switch (rpCode) {
				
                 case 1:
                	 result_code = 1;
					break;
                 
                 case 0:
                	 result_code = 0;
					break;	
				default:
					result_code = rpCode;
					break;
				}
			 }
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			
			try {	
				CommonTool.closeCallableStatement(cs);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
	    System.out.println("返回："+result_code);
		return result_code;
	}
	
	/*
	 * mstinfo订单的添加
	 */
	public  boolean order_add(JSONObject jo)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		boolean issuccess = false;
		try {
			
				conn = DBPool.getPool().getConnection();
			
			
			
			String sql_add = "INSERT INTO mstinfo(taobaoorderid,orderid,taobaohotelid,hotelid,taobaoroomtypeid,roomtypeid,taobaorateplanid,rateplancode,"+
					     "taobaogid,checkin,checkout,earliestarrivetime,latestarrivetime,roomnum,totalprice,paymenttype,contactname,contacttel,contactemail,dailyinfos,"+
					      "orderguests,`comment`,membercardno,guaranteetype,receiptinfo,mrk,isonline,pmsresid,orderstatus,dispatchersta,dispatcherdesc,log_date)"+
					     "VALUES ('"+jo.getString("TaoBaoOrderId")+"',f_ota_getorderid(),'"+jo.getString("TaoBaoHotelId")+"','"+jo.getString("HotelId")+"','"+jo.getString("TaoBaoRoomTypeId")+"','"+jo.getString("RoomTypeId")+"','"+jo.getString("TaoBaoRatePlanId")+"','"+jo.getString("RatePlanCode")+"','"+
					      jo.getString("TaoBaoGid")+"','"+jo.getString("CheckIn")+"','"+jo.getString("CheckOut")+"','"+jo.getString("EarliestArriveTime")+"','"+jo.getString("LatestArriveTime")+"',"+Integer.valueOf(jo.getString("RoomNum"))+","+Integer.valueOf(jo.getString("TotalPrice"))+","+Integer.valueOf(jo.getString("PaymentType"))+",'"+jo.getString("ContactName")+"','"+jo.getString("ContactTel")+"','"+jo.getString("ContactEmail")+"','"+jo.getString("DailyInfos")+"','"+
					      jo.getString("OrderGuests")+"','"+jo.getString("Comment")+"','"+jo.getString("MemberCardNo")+"','"+jo.getString("GuaranteeType")+"','"+jo.getString("ReceiptInfo")+"','QUA','1','',0,'F','','"+getSystime()+"')"; 
//			String sql_add = "insert into rateplan(rateplan_code,hotelid,name,payment_type,breakfast_count,min_days,man_days,min_amount,min_adv_hours,max_adv_hours,start_time,end_time,cancel_policy,status,english_name,guarantee_type,guarantee_start_time,member_level,channel,vendor,first_stay,effective_time,deadline_time) values('"+jo.getString("rateplan_code").trim()+"','"+hotelid.trim()+"','"+jo.getString("name").trim()+"',"+Integer.valueOf(jo.getString("payment_type"))+","+Integer.valueOf(jo.getString("breakfast_count"))+",0,0,0,0,0,'','','"+jo.getString("cancel_policy").trim()+"',"+Integer.valueOf(jo.getString("status").trim())+",'',0,'','','','"+jo.getString("vendor").trim()+"',0,'','')" ;
//			conn.setAutoCommit(false);
			System.out.println("下订单sql:"+sql_add);
			st = conn.createStatement();
			int rows = st.executeUpdate(sql_add);
			if(rows>0)
			{
				issuccess = true;
			}
			
		} catch (Exception e) {
			System.out.println("插入出错："+e.getMessage());
			e.printStackTrace();
		}finally{
			
			try {
				CommonTool.closeResultSet(rs);
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
				System.out.println("插入结果1:"+issuccess);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		System.out.println("插入结果2:"+issuccess);
		return issuccess;
	}
	
	
	/*
	 * 众荟mstinfo订单的添加
	 */
	public  boolean ota_order_add(JSONObject jo,String pmsresid)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		boolean issuccess = false;
		try {
			
				conn = DBPool.getPool().getConnection();
			
			
			
			String sql_add = "INSERT INTO mstinfo_ota(taobaoorderid,orderid,hotelid,roomtypeid,rateplancode,"+
					     "checkin,checkout,latestarrivetime,roomnum,totalprice,paymenttype,contactname,dailyinfos,"+
					      "orderguests,`comment`,guaranteetype,mrk,pmsresid,orderstatus,log_date,guestnum)"+
					     "VALUES ('"+jo.getString("taoBaoOrderId")+"',f_ota_getorderid(),'"+jo.getString("hotelid")+"','"+jo.getString("roomTypeId")+"','"+jo.getString("ratePlanCode")+"','"+
					      jo.getString("checkIn")+"','"+jo.getString("checkOut")+"','"+jo.getString("latestarrivetime")+"',"+Integer.valueOf(jo.getString("roomNum"))+","+Float.parseFloat(jo.getString("totalPrice"))*100+","+Integer.valueOf(jo.getString("paymentType"))+",'"+jo.getString("contactName")+"','"+jo.getJSONArray("dailyInfos").toString()+"','"+
					      jo.getJSONArray("orderGuests").toString()+"','"+jo.getString("remark")+"','"+jo.getString("guaranteetype")+"','"+jo.getString("mkt")+"','"+pmsresid+"',0,'"+getSystime()+"','"+jo.getString("guestnum")+"')"; 
//			String sql_add = "insert into rateplan(rateplan_code,hotelid,name,payment_type,breakfast_count,min_days,man_days,min_amount,min_adv_hours,max_adv_hours,start_time,end_time,cancel_policy,status,english_name,guarantee_type,guarantee_start_time,member_level,channel,vendor,first_stay,effective_time,deadline_time) values('"+jo.getString("rateplan_code").trim()+"','"+hotelid.trim()+"','"+jo.getString("name").trim()+"',"+Integer.valueOf(jo.getString("payment_type"))+","+Integer.valueOf(jo.getString("breakfast_count"))+",0,0,0,0,0,'','','"+jo.getString("cancel_policy").trim()+"',"+Integer.valueOf(jo.getString("status").trim())+",'',0,'','','','"+jo.getString("vendor").trim()+"',0,'','')" ;
//			conn.setAutoCommit(false);
			System.out.println("下订单sql:"+sql_add);
			st = conn.createStatement();
			int rows = st.executeUpdate(sql_add);
			if(rows>0)
			{
				issuccess = true;
			}
		} catch (Exception e) {
//			System.out.println("插入出错："+e.getMessage());
			logger.error(e);
			e.printStackTrace();
		}finally{
			
			try {
				CommonTool.closeResultSet(rs);
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
				System.out.println("插入结果1:"+issuccess);
			} catch (SQLException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
		System.out.println("插入结果2:"+issuccess);
		return issuccess;
	}
	
	
	/*
	 * mstinfo订单状态的更新
	 */
	public  boolean order_update(String resno,String taobaoorderid,String mrk)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		boolean issuccess = false;
		try {
			conn = DBPool.getPool().getConnection();
			String sql_update = null;
			if(resno==null)
			{
				sql_update = "update mstinfo set dispatchersta='T' where taobaoorderid='"+taobaoorderid+"' and mrk='"+mrk+"'"; 
			}else{
				sql_update = "update mstinfo set pmsresid='"+resno+"',dispatchersta='T',orderstatus=1 where taobaoorderid='"+taobaoorderid+"' and mrk='"+mrk+"'"; 
			}
			
//			String sql_add = "insert into rateplan(rateplan_code,hotelid,name,payment_type,breakfast_count,min_days,man_days,min_amount,min_adv_hours,max_adv_hours,start_time,end_time,cancel_policy,status,english_name,guarantee_type,guarantee_start_time,member_level,channel,vendor,first_stay,effective_time,deadline_time) values('"+jo.getString("rateplan_code").trim()+"','"+hotelid.trim()+"','"+jo.getString("name").trim()+"',"+Integer.valueOf(jo.getString("payment_type"))+","+Integer.valueOf(jo.getString("breakfast_count"))+",0,0,0,0,0,'','','"+jo.getString("cancel_policy").trim()+"',"+Integer.valueOf(jo.getString("status").trim())+",'',0,'','','','"+jo.getString("vendor").trim()+"',0,'','')" ;
//			conn.setAutoCommit(false);
			st = conn.createStatement();
			int rows = st.executeUpdate(sql_update);
			if(rows>0)
			{
				issuccess = true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			
			try {
				CommonTool.closeResultSet(rs);
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	    
		return issuccess;
	}
	
	/*
	 * mstinfo订单下发状态的更新
	 */
	public  boolean order_update2F(String taobaoorderid,String mrk)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		boolean issuccess = false;
		try {
			conn = DBPool.getPool().getConnection();
			String sql_update = null;
			
		    sql_update = "update mstinfo set dispatchersta='F',orderstatus=10,dispatcherdesc='订单下发到云助手服务器出错' where taobaoorderid='"+taobaoorderid+"' and mrk='"+mrk+"'"; 
			
			
//			String sql_add = "insert into rateplan(rateplan_code,hotelid,name,payment_type,breakfast_count,min_days,man_days,min_amount,min_adv_hours,max_adv_hours,start_time,end_time,cancel_policy,status,english_name,guarantee_type,guarantee_start_time,member_level,channel,vendor,first_stay,effective_time,deadline_time) values('"+jo.getString("rateplan_code").trim()+"','"+hotelid.trim()+"','"+jo.getString("name").trim()+"',"+Integer.valueOf(jo.getString("payment_type"))+","+Integer.valueOf(jo.getString("breakfast_count"))+",0,0,0,0,0,'','','"+jo.getString("cancel_policy").trim()+"',"+Integer.valueOf(jo.getString("status").trim())+",'',0,'','','','"+jo.getString("vendor").trim()+"',0,'','')" ;
//			conn.setAutoCommit(false);
			st = conn.createStatement();
			int rows = st.executeUpdate(sql_update);
			if(rows>0)
			{
				issuccess = true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				CommonTool.closeResultSet(rs);
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	    
		return issuccess;
	}
	
	
	/*
	 * mstinfo订单状态修改成4：取消状态
	 */
	public  boolean order_updateForCancel(String taobaoorderid,String mrk)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		boolean issuccess = false;
		try {
			conn = DBPool.getPool().getConnection();
			String sql_update = "update mstinfo set dispatchersta='T',orderstatus=4 where taobaoorderid='"+taobaoorderid+"' and mrk='"+mrk+"'"; 
			
			
//			String sql_add = "insert into rateplan(rateplan_code,hotelid,name,payment_type,breakfast_count,min_days,man_days,min_amount,min_adv_hours,max_adv_hours,start_time,end_time,cancel_policy,status,english_name,guarantee_type,guarantee_start_time,member_level,channel,vendor,first_stay,effective_time,deadline_time) values('"+jo.getString("rateplan_code").trim()+"','"+hotelid.trim()+"','"+jo.getString("name").trim()+"',"+Integer.valueOf(jo.getString("payment_type"))+","+Integer.valueOf(jo.getString("breakfast_count"))+",0,0,0,0,0,'','','"+jo.getString("cancel_policy").trim()+"',"+Integer.valueOf(jo.getString("status").trim())+",'',0,'','','','"+jo.getString("vendor").trim()+"',0,'','')" ;
//			conn.setAutoCommit(false);
			st = conn.createStatement();
			int rows = st.executeUpdate(sql_update);
			if(rows>0)
			{
				issuccess = true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			
			try {
				CommonTool.closeResultSet(rs);
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	    
		return issuccess;
	}
	/*
	 * mstinfo_ota OTA订单状态修改成4：取消状态
	 */
	public  boolean order_updateForOTACancel(String taobaoorderid,String mrk)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		boolean issuccess = false;
		try {
			conn = DBPool.getPool().getConnection();
			String sql_update = "update mstinfo_ota set orderstatus=4 where taobaoorderid='"+taobaoorderid+"' and mrk='"+mrk+"'"; 
			
			
//			String sql_add = "insert into rateplan(rateplan_code,hotelid,name,payment_type,breakfast_count,min_days,man_days,min_amount,min_adv_hours,max_adv_hours,start_time,end_time,cancel_policy,status,english_name,guarantee_type,guarantee_start_time,member_level,channel,vendor,first_stay,effective_time,deadline_time) values('"+jo.getString("rateplan_code").trim()+"','"+hotelid.trim()+"','"+jo.getString("name").trim()+"',"+Integer.valueOf(jo.getString("payment_type"))+","+Integer.valueOf(jo.getString("breakfast_count"))+",0,0,0,0,0,'','','"+jo.getString("cancel_policy").trim()+"',"+Integer.valueOf(jo.getString("status").trim())+",'',0,'','','','"+jo.getString("vendor").trim()+"',0,'','')" ;
//			conn.setAutoCommit(false);
			st = conn.createStatement();
			int rows = st.executeUpdate(sql_update);
			if(rows>0)
			{
				issuccess = true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			
			try {
				CommonTool.closeResultSet(rs);
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	    
		return issuccess;
	}
	/*
	 * OTA mstinfo订单状态修改成4：取消状态
	 */
	public  boolean OTAorder_updateForCancel(JSONObject jo,String pmsresid)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		boolean issuccess = false;
		try {
			conn = DBPool.getPool().getConnection();
			conn.setAutoCommit(false);
			String sql_update = "update mstinfo_ota set canceled='F',orderstatus=4,orderid_new='"+jo.getString("taoBaoOrderId")+"' where pmsresid='"+jo.getString("pmsresid").trim()+"' and mrk='"+jo.getString("mkt")+"' and hotelid='"+jo.getString("hotelid")+"'"; 
			String sql_insert ="INSERT INTO mstinfo_ota(taobaoorderid,orderid,hotelid,roomtypeid,rateplancode,"+
		     "checkin,checkout,latestarrivetime,roomnum,totalprice,paymenttype,contactname,dailyinfos,"+
		      "orderguests,`comment`,guaranteetype,mrk,pmsresid,orderstatus,log_date,guestnum)"+
		     "VALUES ('"+jo.getString("taoBaoOrderId")+"',f_ota_getorderid(),'"+jo.getString("hotelid")+"','"+jo.getString("roomTypeId")+"','"+jo.getString("ratePlanCode")+"','"+
		      jo.getString("checkIn")+"','"+jo.getString("checkOut")+"','"+jo.getString("latestarrivetime")+"',"+Integer.valueOf(jo.getString("roomNum"))+","+Float.parseFloat(jo.getString("totalPrice"))*100+","+Integer.valueOf(jo.getString("paymentType"))+",'"+jo.getString("contactName")+"','"+jo.getJSONArray("dailyInfos").toString()+"','"+
		      jo.getJSONArray("orderGuests").toString()+"','"+jo.getString("remark")+"','"+jo.getString("guaranteetype")+"','"+jo.getString("mkt")+"','"+pmsresid+"',0,'"+getSystime()+"','"+jo.getString("guestnum")+"')"; 
			st = conn.createStatement();
			st.addBatch(sql_update);
			st.addBatch(sql_insert);
			st.executeBatch();
			conn.commit();
			issuccess = true;
		
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			
			try {
				conn.setAutoCommit(true);
				CommonTool.closeResultSet(rs);
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	    
		return issuccess;
	}
	
	/*
	 * mstinfo订单状态修改成100：自定义-满房状态
	 * 当通过试单之后，在pms校验满房的情况下修改订单状态
	 */
	public  boolean order_updateForFull(String taobaoorderid,String mrk)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		boolean issuccess = false;
		try {
			conn = DBPool.getPool().getConnection();
			String sql_update = "update mstinfo set dispatchersta='T',orderstatus=100 where taobaoorderid='"+taobaoorderid+"' and mrk='"+mrk+"'"; 
			
			
//			String sql_add = "insert into rateplan(rateplan_code,hotelid,name,payment_type,breakfast_count,min_days,man_days,min_amount,min_adv_hours,max_adv_hours,start_time,end_time,cancel_policy,status,english_name,guarantee_type,guarantee_start_time,member_level,channel,vendor,first_stay,effective_time,deadline_time) values('"+jo.getString("rateplan_code").trim()+"','"+hotelid.trim()+"','"+jo.getString("name").trim()+"',"+Integer.valueOf(jo.getString("payment_type"))+","+Integer.valueOf(jo.getString("breakfast_count"))+",0,0,0,0,0,'','','"+jo.getString("cancel_policy").trim()+"',"+Integer.valueOf(jo.getString("status").trim())+",'',0,'','','','"+jo.getString("vendor").trim()+"',0,'','')" ;
//			conn.setAutoCommit(false);
			st = conn.createStatement();
			int rows = st.executeUpdate(sql_update);
			if(rows>0)
			{
				issuccess = true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			
			try {
				CommonTool.closeResultSet(rs);
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	    
		return issuccess;
	}
	/*
	 * 
	 * 查询订单状态
	 */
	public  Map<String, Object> query_order_id(String taobaoorder_id,String hotelid)
	{
		 CallableStatement  cs = null;
		 Integer rpCode = null;
		 Connection conn = null;
		 Map<String, Object> mp = new HashMap<String, Object>();
		 
		 try {
			 conn = DBPool.getPool().getConnection();
			 cs = conn.prepareCall("{call p_xyz_query_order_lyy(?,?)}");
			 cs.setString(1,taobaoorder_id);
			 cs.setString(2,hotelid);
			 if(cs.execute())
			 {
				 ResultSet rs = cs.getResultSet();
				 rs.next();
				 rpCode = rs.getInt(1);
				 switch (rpCode) {
				case 0:
					//待查询的订单号不存在 -302
					mp.put("code", 0);
					break;
				case 10:
					//待查询的订单下发到dp服务器失败
					mp.put("code", 10);
					mp.put("orderId", rs.getString(2));
					break;	
				case 2:
					//待查询的订单号未被处理 -300
					mp.put("code", 2);
					mp.put("orderId", rs.getString(2));
					break;	
                 case 1:
                	 //待查询订单确认状态
                	 mp.put("code",1);
                	 mp.put("orderid", rs.getString(2));
                	 mp.put("TaoBaoOrderId", rs.getString(3));
                	 mp.put("PmsResID", rs.getString(4));
					break;
                 case 4:
                	//待查询订单取消状态
                	 mp.put("code",4);
                	 mp.put("orderid", rs.getString(2));
                	 mp.put("TaoBaoOrderId", rs.getString(3));
                	 mp.put("PmsResID", rs.getString(4));
 					break;
                 case 8:
                	//待查询订单noshow状态
                	 mp.put("code",8);
                	 mp.put("orderid", rs.getString(2));
                	 mp.put("TaoBaoOrderId", rs.getString(3));
                	 mp.put("PmsResID", rs.getString(4));
 					break;
                 case 5:
                 	//待查询订单checkin状态
                 	 mp.put("code",5);
                 	 mp.put("orderid", rs.getString(2));
                 	 mp.put("TaoBaoOrderId",rs.getString(3));
                 	 mp.put("PmsResID", rs.getString(4));
//                 	 mp.put("Hotel", rs.getString(5));
//                 	 mp.put("CheckIn", rs.getString(6));
//                 	 mp.put("CheckOut", rs.getString(7));
//                 	 mp.put("RoomQuantity",rs.getInt(8));
//                 	 mp.put("RoomType",rs.getString(9));
                 	
//                 	 mp.
  					break;
                 case 9:
                  	//待查询订单noshow状态
                  	 mp.put("code",9);
                  	 mp.put("orderid", rs.getString(2));
                	 mp.put("TaoBaoOrderId", rs.getString(3));
                	 mp.put("PmsResID", rs.getString(4));
                	 mp.put("RoomNo", rs.getString(5));
                	 mp.put("TotalRoomFee", rs.getInt(6));
                	 mp.put("OtherFee", rs.getInt(7));
                	 mp.put("DailyPrice", rs.getString(8));
                	 mp.put("OtherFeeDetail", rs.getString(9));
   					break;
				default:
					break;
				}
			 }
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			
			try {	
				CommonTool.closeCallableStatement(cs);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
	    
		return mp;
	}
	
	/*
	 * 检测信用住订单号是否重复
	 */
	public  Map<String, Object> cancel_order_id(String orderid)
	{
		 CallableStatement  cs = null;
		 Integer rpCode = null;
		 Connection conn = null;
		 Map<String, Object> mp = new HashMap<String, Object>();
		 
		 try {
			 conn = DBPool.getPool().getConnection();
			 cs = conn.prepareCall("{call p_xyz_cancel_order_lyy(?)}");
			 cs.setString(1,orderid);
		
			 if(cs.execute())
			 {
				 ResultSet rs = cs.getResultSet();
				 rs.next();
				 rpCode = rs.getInt(1);
				 switch (rpCode) {
				case 0:
					//待取消的订单号不存在
					mp.put("code", 0);
					break;
                 case 4:
                	 //待取消订单已处于取消状态，无需取消
                	 mp.put("code", 4);
                	 mp.put("orderid", rs.getString(2));
					break;
                 case 2:
                	 //可以取消
                	 mp.put("code", 2);
                	 mp.put("orderid", rs.getString(2));
                	 mp.put("mrk", rs.getString(3));
                	 mp.put("hotelid", rs.getString(4));
 					break;
				default:
					break;
				}
			 }
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			
			try {	
				CommonTool.closeCallableStatement(cs);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
	    
		return mp;
	}
	
	
	/*
	 * 检测ota订单号是否重复
	 */
	public  Map<String, Object> cancel_ota_order(String orderid,String hotelid,String resno)
	{
		 CallableStatement  cs = null;
		 Integer rpCode = null;
		 Connection conn = null;
		 Map<String, Object> mp = new HashMap<String, Object>();
		 
		 try {
			 conn = DBPool.getPool().getConnection();
			 cs = conn.prepareCall("{call p_ota_cancel_validate_lyy(?,?,?)}");
			 cs.setString(1,hotelid.trim());
			 cs.setString(2,resno.trim());
			 cs.setString(3,orderid.trim());
		
			 if(cs.execute())
			 {
				 ResultSet rs = cs.getResultSet();
				 rs.next();
				 rpCode = rs.getInt(1);
				 switch (rpCode) {
				case 0:
					//待取消的订单号不存在
					mp.put("code", 0);
					break;
                 case 4:
                	 //待取消订单已处于取消状态，无需取消
                	 mp.put("code", 4);
                	 mp.put("orderid", rs.getString(2));
					break;
                 case 5:
                	 //待取消订单已处于入住状态，取消失败
                	 mp.put("code", 5);
                	 mp.put("orderid", rs.getString(2));
					break;
                 case 9:
                	 //待取消订单已处于离店状态，取消失败
                	 mp.put("code", 9);
                	 mp.put("orderid", rs.getString(2));
					break;	
                 case 8:
                	 //待取消订单已处于noshow状态，取消失败
                	 mp.put("code", 8);
                	 mp.put("orderid", rs.getString(2));
					break;		
                 case 2:
                	 //可以取消
                	 mp.put("code", 2);
                	 mp.put("orderid", rs.getString(2));
                	 mp.put("mrk", rs.getString(3));
                	 mp.put("hotelid", rs.getString(4));
 					break;
                 case 3:
                	 //可以取消
                	 mp.put("code", 3);
                	 mp.put("taobaoid", rs.getString(5));
                	 mp.put("mrk", rs.getString(3));
                	 mp.put("hotelid", rs.getString(4));
 					break;		
				default:
					break;
				}
			 }
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			
			try {	
				CommonTool.closeCallableStatement(cs);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
	    
		return mp;
	}
	
	/*
	 * 
	 * 当取消订单的时候，在pms返回取消订单的情况下，需要添加指定订单的失败原因
	 */
	public  boolean order_updateForCancelFailed(String taobaoorderid,String mrk,String errorMsg)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		boolean issuccess = false;
		try {
			conn = DBPool.getPool().getConnection();
			String sql_update = "update mstinfo set dispatcherdesc='"+errorMsg+"' where taobaoorderid='"+taobaoorderid+"' and mrk='"+mrk+"'"; 
			
			
//			String sql_add = "insert into rateplan(rateplan_code,hotelid,name,payment_type,breakfast_count,min_days,man_days,min_amount,min_adv_hours,max_adv_hours,start_time,end_time,cancel_policy,status,english_name,guarantee_type,guarantee_start_time,member_level,channel,vendor,first_stay,effective_time,deadline_time) values('"+jo.getString("rateplan_code").trim()+"','"+hotelid.trim()+"','"+jo.getString("name").trim()+"',"+Integer.valueOf(jo.getString("payment_type"))+","+Integer.valueOf(jo.getString("breakfast_count"))+",0,0,0,0,0,'','','"+jo.getString("cancel_policy").trim()+"',"+Integer.valueOf(jo.getString("status").trim())+",'',0,'','','','"+jo.getString("vendor").trim()+"',0,'','')" ;
//			conn.setAutoCommit(false);
			st = conn.createStatement();
			int rows = st.executeUpdate(sql_update);
			if(rows>0)
			{
				issuccess = true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			
			try {
				CommonTool.closeResultSet(rs);
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	    
		return issuccess;
	}
	/*
	 * 根据确认号获取订单号
	 */
	public  String getOrderId(JSONObject jo)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		String orderid = null;
		try {
			conn = DBPool.getPool().getConnection();
			
			String sql_query = "select taobaoorderid from mstinfo_ota  where pmsresid='"+jo.getString("pmsresid").trim()+"' and mrk='"+jo.getString("mkt")+"' and hotelid='"+jo.getString("hotelid")+"'"; 
			
			st = conn.createStatement();
			rs = st.executeQuery(sql_query);
			if(rs.next())
			{
				orderid = rs.getString(1);
			}
			
		
		
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			
			try {
				CommonTool.closeResultSet(rs);
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	    
		return orderid;
	}
	/*
	 * 
	 * OTA发起取消订单，更新订单状态
	 */
	public  boolean OTA_order_cancel_success(String taobaoorderid,String mrk)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		boolean issuccess = false;
		try {
			conn = DBPool.getPool().getConnection();
			String sql_update = "update mstinfo_ota set canceled='F',orderstatus=4 where taobaoorderid='"+taobaoorderid+"' and mrk='"+mrk+"'"; 
			st = conn.createStatement();
			int rows = st.executeUpdate(sql_update);
			if(rows>0)
			{
				issuccess = true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			
			try {
				CommonTool.closeResultSet(rs);
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	    
		return issuccess;
	}
	
	
	/*
	 * 
	 * OTA发起取消订单，更新订单状态(在待取消订单未被pms抓取的情况下)
	 */
	public  boolean OTA_order_cancel_success1(String taobaoorderid,String mrk)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		boolean issuccess = false;
		try {
			conn = DBPool.getPool().getConnection();
			String sql_update = "update mstinfo_ota set canceled='F',orderstatus=4,order_c_sta='T' where taobaoorderid='"+taobaoorderid+"' and mrk='"+mrk+"'"; 
			st = conn.createStatement();
			int rows = st.executeUpdate(sql_update);
			if(rows>0)
			{
				issuccess = true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			
			try {
				CommonTool.closeResultSet(rs);
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	    
		return issuccess;
	}
	
	/*
	 * 
	 * OTA发起预定订单，更新订单状态
	 */
	public  boolean OTA_order_book_success(String taobaoorderid,String mrk,String  resno)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		boolean issuccess = false;
		try {
			conn = DBPool.getPool().getConnection();
			String sql_update = "update mstinfo set orderstatus=1,order_r_sta='T',pmsresid='"+resno+"' where taobaoorderid='"+taobaoorderid+"' and mrk='"+mrk+"'"; 
			
			
//			String sql_add = "insert into rateplan(rateplan_code,hotelid,name,payment_type,breakfast_count,min_days,man_days,min_amount,min_adv_hours,max_adv_hours,start_time,end_time,cancel_policy,status,english_name,guarantee_type,guarantee_start_time,member_level,channel,vendor,first_stay,effective_time,deadline_time) values('"+jo.getString("rateplan_code").trim()+"','"+hotelid.trim()+"','"+jo.getString("name").trim()+"',"+Integer.valueOf(jo.getString("payment_type"))+","+Integer.valueOf(jo.getString("breakfast_count"))+",0,0,0,0,0,'','','"+jo.getString("cancel_policy").trim()+"',"+Integer.valueOf(jo.getString("status").trim())+",'',0,'','','','"+jo.getString("vendor").trim()+"',0,'','')" ;
//			conn.setAutoCommit(false);
			st = conn.createStatement();
			int rows = st.executeUpdate(sql_update);
			if(rows>0)
			{
				issuccess = true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			
			try {
				CommonTool.closeResultSet(rs);
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	    
		return issuccess;
	}
	
	public OTA_RetrieveBookRQ getRetrieveOrder(JSONObject jo_params)
	{
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		OTA_RetrieveBookRQ otaRetrieveBookRQ = null;  
		    String sql = "select taobaoorderid,hotelid,roomtypeid,rateplancode,"+
		     "checkin,checkout,latestarrivetime,roomnum,totalprice,paymenttype,contactname,contacttel,dailyinfos,"+
		      "orderguests,`comment`,guaranteetype,mrk,pmsresid,guestnum from mstinfo_ota where hotelid=? and mrk=? and orderstatus=0 and (taobaoorderid=? or pmsresid=?)  "; 
		 
			try {
				conn = DBPool.getPool().getConnection();
				pst = conn.prepareStatement(sql);
				System.out.println("参数："+jo_params.getString("hotelid")+","+jo_params.getString("mrk")+","+jo_params.getString("orderid"));
				pst.setString(1,jo_params.getString("hotelid"));
				pst.setString(2,jo_params.getString("mrk"));
				pst.setString(3,jo_params.getString("orderid"));
				pst.setString(4,jo_params.getString("orderid"));
				rs = pst.executeQuery();
				if(rs.next())
				{
					otaRetrieveBookRQ = new OTA_RetrieveBookRQ();
					otaRetrieveBookRQ.setTaoBaoOrderId(rs.getString(1));
					otaRetrieveBookRQ.setHotelid(rs.getString(2));
					otaRetrieveBookRQ.setRoomTypeId(rs.getString(3));
					otaRetrieveBookRQ.setRatePlanCode(rs.getString(4));
					otaRetrieveBookRQ.setCheckIn(rs.getString(5));
					otaRetrieveBookRQ.setCheckOut(rs.getString(6));
					otaRetrieveBookRQ.setLatestarrivetime(rs.getString(7));
					otaRetrieveBookRQ.setRoomNum(rs.getInt(8)+"");
					otaRetrieveBookRQ.setTotalPrice(((float)rs.getInt(9))/100+"");
					otaRetrieveBookRQ.setPaymentType(rs.getInt(10)+"");
					otaRetrieveBookRQ.setContactName(rs.getString(11));
					otaRetrieveBookRQ.setContactTel(rs.getString(12));
					otaRetrieveBookRQ.setDailyInfos(JSONArray.fromObject(rs.getString(13)));
					otaRetrieveBookRQ.setOrderGuests(JSONArray.fromObject(rs.getString(14)));
					otaRetrieveBookRQ.setRemark(rs.getString(15));
					otaRetrieveBookRQ.setGuaranteetype(rs.getString(16));
					otaRetrieveBookRQ.setMkt(rs.getString(17));
					otaRetrieveBookRQ.setPmsresid(rs.getString(18));
					otaRetrieveBookRQ.setGuestnum(rs.getString(19));
					
				}
				
				
			} catch (SQLException e) {
				
				e.printStackTrace();
			}finally{
				try {
					CommonTool.closeResultSet(rs);
					CommonTool.closeStatement(pst);
					CommonTool.closeConnection(conn);
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
			}
			
			return otaRetrieveBookRQ;
	}
	private String getSystime()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}
    
    
}
