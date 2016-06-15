package com.dl.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dl.datasource.DBPool;
import com.dl.servlet.LoginValidateServlet;
import com.dl.utl.CommonTool;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;












public class DogNoValidateDao {
	private Logger logger = Logger.getLogger(DogNoValidateDao.class);
	
	/*
	 * ����ģʽ
	 */
	private DogNoValidateDao() {}  
    //�Ѿ�����ʵ��   
    private static final DogNoValidateDao dogNoValidateDao = new DogNoValidateDao();  
    //��̬��������   
    public static DogNoValidateDao getInstance() {  
        return dogNoValidateDao;  
    }  
	
    
    /**
	 * 信用住用户登录校验
	 * 
	 */
	public boolean isLoginSuccess(String hotelid,String rsa)
	{
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		boolean issuccess = false;    
	    Map<String, Object> mp = new HashMap<String, Object>();
	    mp.put("hotelid", null);
	    String sql = "select count(*) from hotellist_ota where hotelid =? and rsa=? and is_used=? "; 
	 
		try {
			conn = DBPool.getPool().getConnection();
			pst = conn.prepareStatement(sql);
			pst.setString(1, hotelid.trim());
			pst.setString(2, rsa.trim());
			pst.setInt(3, 1);
			rs = pst.executeQuery();
			rs.next();
			if(rs.getInt(1)>0)
			{
				issuccess = true;
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(e);
			e.printStackTrace();
		}finally{
			try {
				CommonTool.closeResultSet(rs);
				CommonTool.closePreparedStatement(pst);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
		    
	    	

		return issuccess;
	}
	

	/**
	 * 是否是信用住用户酒店
	 * 
	 */
	public Map<String, Object> isXYZ(String dogno)
	{
		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		    String hotelid = null;
		    Map<String, Object> mp = new HashMap<String, Object>();
		    mp.put("hotelid", null);
		    String sql = "select hotelid,hotelname,is_hotel,is_used from hotellist_ota where regcode ='"+dogno.trim()+"'"; 
		 
			try {
				conn = DBPool.getPool().getConnection();
				st = conn.createStatement();
				rs = st.executeQuery(sql);
				if(rs.next())
				{
					mp.put("hotelid", rs.getString(1));
					mp.put("hotelname", rs.getString(2));
					mp.put("ishotel", String.valueOf(rs.getInt(3)));
					mp.put("isused", String.valueOf(rs.getInt(4)));
//					hotelid = rs.getString(1);
				}
				
				
			} catch (SQLException e) {
				logger.error(e);
				e.printStackTrace();
			}finally{
				try {
					CommonTool.closeResultSet(rs);
					CommonTool.closeStatement(st);
					CommonTool.closeConnection(conn);
				} catch (SQLException e) {
					logger.error(e);
					e.printStackTrace();
				}
			}
		    
	    	

		return mp;
	}
	
	
	/**
	 * 更新注册次数
	 * 
	 */
	public boolean updateRegTimers(String dogno,String rsa)
	{
		
		Connection conn = null;
		Statement st = null;
	    boolean issuccess = false;
	    
	    String sql = "update hotellist_ota set is_used=is_used+1,rsa='"+rsa+"' where regcode ='"+dogno.trim()+"'"; 
	 
		try {
			conn = DBPool.getPool().getConnection();
			st = conn.createStatement();
			int rows = st.executeUpdate(sql);
			if(rows>0)
			{
				issuccess = true;
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		    
	    	

		return issuccess;
	}
	
}
