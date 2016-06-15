package com.dl.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.dl.datasource.DBPool;
import com.dl.pojo.Advertisement;
import com.dl.utl.CommonTool;

public class HotelInfoDao {

	/*
	 * 单例模式
	 */
	private HotelInfoDao() {}  
      
    private static final HotelInfoDao hotelindoDao = new HotelInfoDao();  
   
    public static HotelInfoDao getInstance() {  
        return hotelindoDao;  
    }  
    
    /*
	 * 酒店信息的添加或修改
	 */
	public  boolean hotel_update_add(JSONObject jo,String hotelid)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		boolean issuccess = false;
		try {
			conn = DBPool.getPool().getConnection();
			
			String sql_del = "delete from hotel_info where hotelid='"+hotelid.trim()+"'" ;
			String sql_add = null;
			if(jo.has("district"))
			{
				sql_add = "insert into hotel_info(hotelid,name,domestic,city,address,tel,vendor,floor,rooms,description,pics,district,log_date) values('"+hotelid.trim()+"','"+jo.getString("name").trim()+"',0,'"+jo.getString("city").trim()+"','"+jo.getString("address").trim()+"','"+jo.getString("tel").trim()+"','"+jo.getString("vendor").trim()+"','0',0,'"+jo.getString("description").trim()+"','','"+jo.getString("district")+"','"+CommonTool.getToday()+"')" ;
			}else{
				sql_add = "insert into hotel_info(hotelid,name,domestic,city,address,tel,vendor,floor,rooms,description,pics,district,log_date) values('"+hotelid.trim()+"','"+jo.getString("name").trim()+"',0,'"+jo.getString("city").trim()+"','"+jo.getString("address").trim()+"','"+jo.getString("tel").trim()+"','"+jo.getString("vendor").trim()+"','0',0,'"+jo.getString("description").trim()+"','','','"+CommonTool.getToday()+"')" ;
			}
		 
			conn.setAutoCommit(false);
			st = conn.createStatement();
			st.addBatch(sql_del);
			st.addBatch(sql_add);
			int[] result = st.executeBatch();
			if(result[0]>=0)
			{
				issuccess = true;
			}
			
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
	 * 酒店信息的添加或修改
	 */
	public  boolean rmtype_update_add(JSONObject jo,String hotelid)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		boolean issuccess = false;
		try {
			conn = DBPool.getPool().getConnection();
			
			String sql_del = "delete from roomtype where roomtype_id='"+jo.getString("rmtype_id").trim()+"'" ;
			String sql_add = "insert into roomtype(roomtype_id,name,max_occupancy,area,floor,bed_type,bed_size,internet,service,window_type,hotelid,vendor,pics) values('"+jo.getString("rmtype_id").trim()+"','"+jo.getString("name").trim()+"',0,'','','','','"+jo.getString("internet").trim()+"','',"+Integer.valueOf(jo.getString("windows_type").trim())+",'"+hotelid.trim()+"','"+jo.getString("vendor").trim()+"','')" ;
			conn.setAutoCommit(false);
			st = conn.createStatement();
			st.addBatch(sql_del);
			st.addBatch(sql_add);
			int[] result = st.executeBatch();
			if(result[0]>=0)
			{
				issuccess = true;
			}
			
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
	 * RP信息的添加或修改
	 */
	public  boolean rateplan_update_add(JSONObject jo,String hotelid)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		boolean issuccess = false;
		try {
			conn = DBPool.getPool().getConnection();
			
			String sql_del = "delete from rateplan where rateplan_code='"+jo.getString("rateplan_code").trim()+"'" ;
			String sql_add = "insert into rateplan(rateplan_code,hotelid,name,payment_type,breakfast_count,min_days,man_days,min_amount,min_adv_hours,max_adv_hours,start_time,end_time,cancel_policy,status,english_name,guarantee_type,guarantee_start_time,member_level,channel,vendor,first_stay,effective_time,deadline_time) values('"+jo.getString("rateplan_code").trim()+"','"+hotelid.trim()+"','"+jo.getString("name").trim()+"',"+Integer.valueOf(jo.getString("payment_type"))+","+Integer.valueOf(jo.getString("breakfast_count"))+",0,0,0,0,0,'','','"+jo.getString("cancel_policy").trim()+"',"+Integer.valueOf(jo.getString("status").trim())+",'',0,'','','','"+jo.getString("vendor").trim()+"',0,'','')" ;
			conn.setAutoCommit(false);
			st = conn.createStatement();
			st.addBatch(sql_del);
			st.addBatch(sql_add);
			int[] result = st.executeBatch();
			if(result[0]>=0)
			{
				issuccess = true;
			}
			
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
	
	public Advertisement getAdvertisement()
	{
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		    String hotelid = null;
		    Advertisement ad = new Advertisement();
		   
		    String sql = "select name,url,max_zf from advertisement where id =1"; 
		 
			try {
				conn = DBPool.getPool().getConnection();
				st = conn.createStatement();
				rs = st.executeQuery(sql);
				if(rs.next())
				{
					ad.setName(rs.getString(1));
					ad.setUrl(rs.getString(2));
					ad.setMax_zf(rs.getString(3));
//					hotelid = rs.getString(1);
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
		    
		return ad;
	
	
	}
	
	
	/*
	 * RP唯一编码的获取
	 */
	public  String rateplan_code_GET(String type)
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
    
    
}
