package com.dl.utl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import com.dl.dao.CheckDao;
import com.dl.datasource.DBPool;
import com.dl.pojo.OTAResults;

public class Generator {

	
	
	private Generator() {}  
	private Logger logger = Logger.getLogger(Generator.class); 
    private static final Generator generator = new Generator();  
    public static Generator getInstance() {  
        return generator;  
    }  
    
    public synchronized String getPMSOrderComfireId()
    {
    	 CallableStatement  cs = null;
		 String rpCode = null;
		 Connection conn = null;
		 
		 try {
			 conn = DBPool.getPool().getConnection();
			 cs = conn.prepareCall("{call p_ota_getno(?)}");
			 cs.setString(1,"CF");
			
			 if(cs.execute())
			 {
				 ResultSet rs = cs.getResultSet();
				 rs.next();
				 rpCode = rs.getString(1);
				 rpCode = rpCode+new Date().getTime();
			 }
			
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		}finally{
			
			try {
				
				CommonTool.closeCallableStatement(cs);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
	    
		return rpCode;
    }
    
  //批量修改新订单状态
	public  boolean order_create_update_batch(JSONArray ja_order_create)
	{
		Connection conn = null;
		PreparedStatement pst  = null;
		boolean issuccess = true;
		try {
			conn = DBPool.getPool().getConnection();
			String sql_create = "update mstinfo_ota set orderstatus=?,order_r_sta='T' where taobaoorderid= ? and mrk= ? and hotelid=?"; 
//			String sql_cancel = "update mstinfo set orderstatus=? where taobaoorderid= ? and mrk= ? and hotelid=?";
			conn.setAutoCommit(false); 
			pst = conn.prepareStatement(sql_create);
			if(ja_order_create!=null)
			{
				for(int i=0;i<ja_order_create.size();i++)
				{
					pst.setInt(1,1);
					pst.setString(2,ja_order_create.getJSONObject(i).getString("taobaoid"));  
					pst.setString(3,ja_order_create.getJSONObject(i).getString("mrk"));  
					pst.setString(4,ja_order_create.getJSONObject(i).getString("hotelid"));  
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
//			String sql_create = "update mstinfo set orderstatus=?,pmsresid=? where taobaoorderid= ? and mrk= ? and hotelid=?"; 
			String sql_cancel = "update mstinfo_ota set orderstatus=?,order_c_sta='T' where taobaoorderid= ? and mrk= ? and hotelid=?";
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(sql_cancel);
//			if(ja_order_create!=null)
//			{
//				
//				for(int i=0;i<ja_order_create.size();i++)
//				{
//					pst.setInt(1,1);
//					pst.setString(2,ja_order_create.getJSONObject(i).getString("taobaoid"));  
//					pst.setString(3, ja_order_create.getJSONObject(i).getString("mrk"));  
//					pst.setString(4, ja_order_create.getJSONObject(i).getString("hotelid"));  
//	                conn.setAutoCommit(false);   //若改为true 插入中遇到主键冲突还会继续插入，具体看需求  
//	                pst.addBatch();  
//				}
//				
//			}
			
			if(ja_order_cancel!=null)
			{
				
				for(int i=0;i<ja_order_cancel.size();i++)
				{
					pst.setInt(1,4);
					pst.setString(2,ja_order_cancel.getJSONObject(i).getString("taobaoid"));  
					pst.setString(3, ja_order_cancel.getJSONObject(i).getString("mrk"));  
					pst.setString(4, ja_order_cancel.getJSONObject(i).getString("hotelid"));  
	                   //若改为true 插入中遇到主键冲突还会继续插入，具体看需求  
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
    
    
}
