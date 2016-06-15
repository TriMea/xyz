package com.dl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dl.utl.CommonTool;


public class BaseDao {
      
      public static ResultSet getRs(Connection conn,String sql,Object[] param){
    	  ResultSet rs=null;
    	  PreparedStatement pstmt=null;
    	  try {
			pstmt=conn.prepareStatement(sql);
			  if (param!=null) {
				for (int i = 0; i < param.length; i++) {
					pstmt.setObject(i+1, param[i]);
				}
			}
			 rs=pstmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				CommonTool.closePreparedStatement(pstmt);
				CommonTool.closeResultSet(rs);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return rs;
      }
      

}
