package com.dl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.dl.datasource.DBPool;
import com.dl.pojo.SmsMessage;
import com.dl.utl.CommonTool;

public class AladinDao {

  private static final AladinDao aladinDao = new AladinDao();  
    private Logger logger = Logger.getLogger(AladinDao.class);  
    public static AladinDao getInstance() {  
        return aladinDao;  
    }  
    
    
    public boolean isAladinHotel(String hotelid)
    {
    	Connection conn = null;
		PreparedStatement pst  = null;
		ResultSet rs = null;
	    Boolean isAladinHotel = false;
		try {
			conn = DBPool.getPool().getConnection();
			
			String sql_query = "SELECT b.isaladin FROM `hotellist_ota` a LEFT JOIN hotellist b ON a.regcode = b.regcode  where a.hotelid=?";
			
			pst = conn.prepareStatement(sql_query);
			pst.setString(1,hotelid);
			rs = pst.executeQuery();
			if(rs.next())
			{
				if(rs.getInt("isaladin")==1)
				{
					isAladinHotel = true;
				}
			}
			
		} catch (SQLException e) {
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
	    
		return isAladinHotel;
    }
}
