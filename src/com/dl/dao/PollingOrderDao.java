package com.dl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.dl.datasource.DBPool;
import com.dl.pojo.BookRQ;
import com.dl.utl.CommonTool;

public class PollingOrderDao {

	/*
	 * 单例模式
	 */
	private PollingOrderDao() {}  
    
    private static final PollingOrderDao pollingOrderDao = new PollingOrderDao();  
    public static PollingOrderDao getInstance() {  
        return pollingOrderDao;  
    }  
    
    
    /*
	 * mstinfo订单状态的更新
	 */
	public  boolean order_update(String resno,String taobaoorderid,String mrk,boolean isnew )
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
				if(isnew)
				{
					sql_update = "update mstinfo set order_r_sta='T' where taobaoorderid='"+taobaoorderid+"' and mrk='"+mrk+"'"; 
				}else{
					sql_update = "update mstinfo set order_c_sta='T' where taobaoorderid='"+taobaoorderid+"' and mrk='"+mrk+"'"; 
				}
				
			}else{
				
				sql_update = "update mstinfo set pmsresid='"+resno+"',orderstatus=1 where taobaoorderid='"+taobaoorderid+"' and mrk='"+mrk+"'"; 
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
	//批量修改新订单状态
	public  boolean order_create_update_batch(JSONArray ja_order_create)
	{
		Connection conn = null;
		PreparedStatement pst  = null;
		boolean issuccess = true;
		try {
			conn = DBPool.getPool().getConnection();
			 conn.setAutoCommit(false);   //若改为true 插入中遇到主键冲突还会继续插入，具体看需求  
			String sql_create = "update mstinfo set orderstatus=?,pmsresid=? where taobaoorderid= ? and mrk= ? and hotelid=?"; 
//			String sql_cancel = "update mstinfo set orderstatus=? where taobaoorderid= ? and mrk= ? and hotelid=?";
			pst = conn.prepareStatement(sql_create);
			if(ja_order_create!=null)
			{
				
				for(int i=0;i<ja_order_create.size();i++)
				{
					pst.setInt(1,1);
					pst.setString(2, ja_order_create.getJSONObject(i).getString("resno"));
					pst.setString(3,ja_order_create.getJSONObject(i).getString("taobaoid"));  
					pst.setString(4, ja_order_create.getJSONObject(i).getString("mrk"));  
					pst.setString(5, ja_order_create.getJSONObject(i).getString("hotelid"));  
	               
	                pst.addBatch();  
				}
				
			}
			

			pst.executeBatch();  
            conn.commit();  
			
		} catch (SQLException e) {
			issuccess = false;
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
		
			try {
				conn.setAutoCommit(true);
				CommonTool.closePreparedStatement(pst);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	    
		return issuccess;
	}
	
	
	//OTA批量修改新订单状态
	public  boolean OTAorder_create_update_batch(JSONArray ja_order_create)
	{
		Connection conn = null;
		PreparedStatement pst  = null;
		boolean issuccess = true;
		try {
			conn = DBPool.getPool().getConnection();
			 conn.setAutoCommit(false);   //若改为true 插入中遇到主键冲突还会继续插入，具体看需求  
			String sql_create = "update mstinfo_ota set orderstatus=?,order_r_sta=? where taobaoorderid= ? and mrk= ? and hotelid=?"; 
//			String sql_cancel = "update mstinfo set orderstatus=? where taobaoorderid= ? and mrk= ? and hotelid=?";
			pst = conn.prepareStatement(sql_create);
			if(ja_order_create!=null)
			{
				
				for(int i=0;i<ja_order_create.size();i++)
				{
					pst.setInt(1,1);
					pst.setString(2,"T");
					pst.setString(3,ja_order_create.getJSONObject(i).getString("taobaoid"));  
					pst.setString(4, ja_order_create.getJSONObject(i).getString("mrk"));  
					pst.setString(5, ja_order_create.getJSONObject(i).getString("hotelid"));  
	               
	                pst.addBatch();  
				}
				
			}
			

			pst.executeBatch();  
            conn.commit();  
			
		} catch (SQLException e) {
			issuccess = false;
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
		
			try {
				conn.setAutoCommit(true);
				CommonTool.closePreparedStatement(pst);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	    
		return issuccess;
	}
	
	//批量修改取消订单状态
	public  boolean order_cancel_update_batch(JSONArray ja_order_cancel)
	{
		Connection conn = null;
		PreparedStatement pst  = null;
		boolean issuccess = true;
		try {
			conn = DBPool.getPool().getConnection();
			conn.setAutoCommit(false);   //若改为true 插入中遇到主键冲突还会继续插入，具体看需求  
//			String sql_create = "update mstinfo set orderstatus=?,pmsresid=? where taobaoorderid= ? and mrk= ? and hotelid=?"; 
			String sql_cancel = "update mstinfo set orderstatus=? where taobaoorderid= ? and mrk= ? and hotelid=?";
			pst = conn.prepareStatement(sql_cancel);

			if(ja_order_cancel!=null)
			{
				
				for(int i=0;i<ja_order_cancel.size();i++)
				{
					pst.setInt(1,4);
					pst.setString(2,ja_order_cancel.getJSONObject(i).getString("taobaoid"));  
					pst.setString(3, ja_order_cancel.getJSONObject(i).getString("mrk"));  
					pst.setString(4, ja_order_cancel.getJSONObject(i).getString("hotelid"));  
	               
	                pst.addBatch();  
				}
				
			}
			
//			String sql_add = "insert into rateplan(rateplan_code,hotelid,name,payment_type,breakfast_count,min_days,man_days,min_amount,min_adv_hours,max_adv_hours,start_time,end_time,cancel_policy,status,english_name,guarantee_type,guarantee_start_time,member_level,channel,vendor,first_stay,effective_time,deadline_time) values('"+jo.getString("rateplan_code").trim()+"','"+hotelid.trim()+"','"+jo.getString("name").trim()+"',"+Integer.valueOf(jo.getString("payment_type"))+","+Integer.valueOf(jo.getString("breakfast_count"))+",0,0,0,0,0,'','','"+jo.getString("cancel_policy").trim()+"',"+Integer.valueOf(jo.getString("status").trim())+",'',0,'','','','"+jo.getString("vendor").trim()+"',0,'','')" ;
//			conn.setAutoCommit(false);
			pst.executeBatch();  
            conn.commit();  
			
		} catch (SQLException e) {
			issuccess = false;
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
		
			try {
				conn.setAutoCommit(true);
				CommonTool.closePreparedStatement(pst);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	    
		return issuccess;
	}
	
	//OTA批量修改取消订单状态
	public  boolean OTAorder_cancel_update_batch(JSONArray ja_order_cancel)
	{
		Connection conn = null;
		PreparedStatement pst  = null;
		boolean issuccess = true;
		try {
			conn = DBPool.getPool().getConnection();
			conn.setAutoCommit(false);   //若改为true 插入中遇到主键冲突还会继续插入，具体看需求  
			String sql_cancel = "update mstinfo_ota set orderstatus=?,order_c_sta=? where taobaoorderid= ? and mrk= ? and hotelid=?";
			pst = conn.prepareStatement(sql_cancel);

			if(ja_order_cancel!=null)
			{
				
				for(int i=0;i<ja_order_cancel.size();i++)
				{
					pst.setInt(1,4);
					pst.setString(2,"T");  
					pst.setString(3,ja_order_cancel.getJSONObject(i).getString("taobaoid"));  
					pst.setString(4, ja_order_cancel.getJSONObject(i).getString("mrk"));  
					pst.setString(5, ja_order_cancel.getJSONObject(i).getString("hotelid"));  
	                pst.addBatch();  
				}
				
			}
			
//			String sql_add = "insert into rateplan(rateplan_code,hotelid,name,payment_type,breakfast_count,min_days,man_days,min_amount,min_adv_hours,max_adv_hours,start_time,end_time,cancel_policy,status,english_name,guarantee_type,guarantee_start_time,member_level,channel,vendor,first_stay,effective_time,deadline_time) values('"+jo.getString("rateplan_code").trim()+"','"+hotelid.trim()+"','"+jo.getString("name").trim()+"',"+Integer.valueOf(jo.getString("payment_type"))+","+Integer.valueOf(jo.getString("breakfast_count"))+",0,0,0,0,0,'','','"+jo.getString("cancel_policy").trim()+"',"+Integer.valueOf(jo.getString("status").trim())+",'',0,'','','','"+jo.getString("vendor").trim()+"',0,'','')" ;
//			conn.setAutoCommit(false);
			pst.executeBatch();  
            conn.commit();  
			
		} catch (SQLException e) {
			issuccess = false;
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
		
			try {
				conn.setAutoCommit(true);
				CommonTool.closePreparedStatement(pst);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	    
		return issuccess;
	}
	public BookRQ getBookOrderByTaobaoid(JSONObject jo_order)
	{
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		    String hotelid = null;
		    BookRQ br = null;
		  
		    String sql = "select taobaoorderid,hotelid,roomtypeid,rateplancode,roomnum,orderstatus,checkin,checkout,totalprice,paymenttype,contactname,contacttel,dailyinfos,orderguests,mrk,comment,log_date from mstinfo where orderstatus =0 and isonline='1' and mrk='"+jo_order.getString("mrk")+"' and taobaoorderid='"+jo_order.getString("taobaoid")+"'"; 
		 
			try {
				conn = DBPool.getPool().getConnection();
				st = conn.createStatement();
				rs = st.executeQuery(sql);
				if(rs.next())
				{
					br = new BookRQ();
					br.setTaoBaoOrderId(rs.getString(1));
					br.setHotelid(rs.getString(2));
					br.setRoomTypeId(rs.getString(3));
					br.setRatePlanCode(rs.getString(4));
					br.setRoomNum(rs.getInt(5)+"");
					br.setOrderStatus(rs.getInt(6)+"");
					br.setCheckIn(rs.getString(7));
					br.setCheckOut(rs.getString(8));
					br.setTotalPrice(rs.getInt(9)+"");
					br.setPaymentType(rs.getInt(10)+"");
					br.setContactName(rs.getString(11));
					br.setContactTel(rs.getString(12));
					br.setDailyInfos(JSONArray.fromObject(rs.getString(13)));
					br.setOrderGuests(JSONArray.fromObject(rs.getString(14)));
					br.setMkt(rs.getString(15));
					br.setRemark(rs.getString(16));
					br.setLog_date(rs.getString(17));
					

				}
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					CommonTool.closeResultSet(rs);
					CommonTool.closeStatement(st);
					CommonTool.closeConnection(conn);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		    
	    	

		return br;
	
	}
	
	
	
	public BookRQ getOTABookOrderByTaobaoid(JSONObject jo_order)
	{
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		    String hotelid = null;
		    BookRQ br = null;
		  
//		    String sql = "select taobaoorderid,hotelid,roomtypeid,rateplancode,roomnum,orderstatus,checkin,checkout,totalprice,paymenttype,contactname,contacttel,dailyinfos,orderguests,mrk,comment,log_date,pmsresid,latestarrivetime,guaranteetype from mstinfo_ota where (orderstatus =0 or(orderstatus =4 and orderid_new is not null)) and  mrk='"+jo_order.getString("mrk")+"' and taobaoorderid='"+jo_order.getString("taobaoid")+"'"; 
		    String sql = "select taobaoorderid,hotelid,roomtypeid,rateplancode,roomnum,orderstatus,checkin,checkout,totalprice,paymenttype,contactname,contacttel,dailyinfos,orderguests,mrk,comment,log_date,pmsresid,latestarrivetime,guaranteetype from mstinfo_ota where (orderstatus =0 or orderstatus =4 ) and  mrk='"+jo_order.getString("mrk")+"' and taobaoorderid='"+jo_order.getString("taobaoid")+"'"; 
			try {
				conn = DBPool.getPool().getConnection();
				st = conn.createStatement();
				rs = st.executeQuery(sql);
				if(rs.next())
				{
					br = new BookRQ();
					br.setTaoBaoOrderId(rs.getString(1));
					br.setHotelid(rs.getString(2));
					br.setRoomTypeId(rs.getString(3));
					br.setRatePlanCode(rs.getString(4));
					br.setRoomNum(rs.getInt(5)+"");
					br.setOrderStatus(rs.getInt(6)+"");
					br.setCheckIn(rs.getString(7));
					br.setCheckOut(rs.getString(8));
					br.setTotalPrice(rs.getInt(9)+"");
					br.setPaymentType(rs.getInt(10)+"");
					br.setContactName(rs.getString(11));
					br.setContactTel(rs.getString(12));
					br.setDailyInfos(JSONArray.fromObject(rs.getString(13)));
					br.setOrderGuests(JSONArray.fromObject(rs.getString(14)));
					br.setMkt(rs.getString(15));
					br.setRemark(rs.getString(16));
					br.setLog_date(rs.getString(17));
					br.setFakeaccnt(rs.getString(18));
					br.setLatestarrivetime(rs.getString(19));
					br.setGuaranteetype(rs.getString(20));
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
		    
	    	

		return br;
	
	}
	
    
	
}
