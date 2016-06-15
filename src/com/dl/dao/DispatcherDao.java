package com.dl.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dl.datasource.DBPool;
import com.dl.servlet.RequestServlet;
import com.dl.utl.CommonTool;

public class DispatcherDao {

	/*
	 * 单例模式
	 */
	private DispatcherDao() {}  
    
    private static final DispatcherDao dispatcherDao = new DispatcherDao();  
    private Logger logger = Logger.getLogger(DispatcherDao.class); 
    public static DispatcherDao getInstance() {  
        return dispatcherDao;  
    }  
    
    public boolean isDL_PMS (String hotelid)
    {
    	Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		boolean flag = false;
		   
		    String sql = "select dl_flag from hotellist_ota where hotelid ='"+hotelid.trim()+"'"; 
		 
			try {
				conn = DBPool.getPool().getConnection();
				st = conn.createStatement();
				rs = st.executeQuery(sql);
				if(rs.next())
				{
					String dl_flag = rs.getString(1);
					if(dl_flag!=null&&"T".equals(dl_flag))
					{
						flag = true;
					}

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
		    
	    	

		return flag;
    }
}
