package com.dl.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.dl.datasource.DBPool;
import com.dl.pojo.OTAResults;
import com.dl.pojo.SmsMessage;
import com.dl.utl.CommonTool;

public class SmsDao {
private SmsDao() {}  
    
    private static final SmsDao commonDao = new SmsDao();  
    private Logger logger = Logger.getLogger(CommonDao.class);  
    public static SmsDao getInstance() {  
        return commonDao;  
    }  
    
    
    /*
     * 获取满足条件下的每天的数据
     * 
     */
    
    public  SmsMessage getSmsInfo(String hotelid)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		SmsMessage smsMessage = null;
		try {
			conn = DBPool.getPool().getConnection();
			
			String sql_query = "select hotelname,contactname,contacttel from sms_info where hotelid='"+hotelid+"'";
			
			st = conn.createStatement();
			rs = st.executeQuery(sql_query);
			if(rs.next())
			{
				smsMessage = new SmsMessage();
				smsMessage.setHotelid(hotelid);
				smsMessage.setHotelname(rs.getString(1));
				smsMessage.setContactname(rs.getString(2));
				smsMessage.setContacttel(rs.getString(3));
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
				e.printStackTrace();
			}
		}
	    
		return smsMessage;
	}
}
