package com.dl.datasource;

import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBPool { 
	private static DataSource pool; 
	static { 
		 try {

			   Context ctx = new  InitialContext();
			   Context envContext = (Context)ctx.lookup("java:/comp/env");//java:/comp/env为固定路径
			   pool = (DataSource)envContext.lookup("mysql_db");//tomcat中设置的数据源
			   if(pool==null) 
					System.err.println("'DBPool' is an unknown DataSource"); 
			  } catch (Exception e) {
			   e.printStackTrace();

			  }
	} 
	public static DataSource getPool() { 
	return pool; 
	} 
	public static void main(String[] args) throws SQLException{
		java.sql.Connection conn = DBPool.getPool().getConnection();
		System.out.print(conn);
	}
	} 