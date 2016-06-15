package com.dl.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.dl.datasource.DBPool;
import com.dl.pojo.CheckInResult;
import com.dl.pojo.OTAResults;
import com.dl.pojo.RoomRates;
import com.dl.utl.CommonTool;

public class CheckDao {

	/*
	 * 单例模式
	 */
	private CheckDao() {}  
	private Logger logger = Logger.getLogger(CheckDao.class); 
    private static final CheckDao orderdao = new CheckDao();  
    public static CheckDao getInstance() {  
        return orderdao;  
    }  
    /*
     * 满足可定检查的结果集
     * checkType 可定检查类型|rs_params 检查参数| days 检查时间间隔
     * 
     */
    
    public  List<CheckInResult> getResultsByCondition(String checkType,JSONObject rs_params,int days)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		List<CheckInResult> list = new ArrayList<CheckInResult>();
		try {
			conn = DBPool.getPool().getConnection();
			String sql_query = "";
			if("NoType_NoCode".equals(checkType))
			{
				 sql_query = "select rmtype,rpcode,COUNT(*) m ,a.hotelName from set_room_rate b,hotelname a where b.hotelid=a.hotelid and date>='"+rs_params.getString("bdate")+"' and date<'"+CommonTool.getDateOfLaterDays(rs_params.getString("bdate"), days)+"'  and quote>"+rs_params.getInt("quote")+" and b.hotelid='"+rs_params.getString("hotelid")+"' GROUP BY rmtype,rpcode HAVING m="+days; 
			}else if("Type_NoCode".equals(checkType))
			{
				sql_query = "select rmtype,rpcode,COUNT(*) m,a.hotelName from set_room_rate b ,hotelname a where b.hotelid=a.hotelid and date>='"+rs_params.getString("bdate")+"' and date<'"+CommonTool.getDateOfLaterDays(rs_params.getString("bdate"), days)+"'  and quote>"+rs_params.getInt("quote")+" and rmtype='"+rs_params.getString("rmtype")+"' and b.hotelid='"+rs_params.getString("hotelid")+"' GROUP BY rmtype,rpcode HAVING m="+days; 
			}else if("Type_Code".equals(checkType))
			{
				sql_query = "select rmtype,rpcode,COUNT(*) m,a.hotelName from set_room_rate b ,hotelname a where b.hotelid=a.hotelid and date>='"+rs_params.getString("bdate")+"' and date<'"+CommonTool.getDateOfLaterDays(rs_params.getString("bdate"), days)+"'  and quote>"+rs_params.getInt("quote")+" and rmtype='"+rs_params.getString("rmtype")+"' and rpcode='"+rs_params.getString("rpcode")+"' and b.hotelid='"+rs_params.getString("hotelid")+"' GROUP BY rmtype,rpcode HAVING m="+days; 
			}
			
			st = conn.createStatement();
			rs = st.executeQuery(sql_query);
			while(rs.next())
			{
				CheckInResult ad = new CheckInResult();
				ad.setHotelid(rs_params.getString("hotelid"));
				ad.setRmtype(rs.getString(1));
				ad.setRpcode(rs.getString(2));
				ad.setHotelName(rs.getString(4));
				list.add(ad);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
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
	    
		return list;
	}
    
    
    /*
     * 获取满足条件下的每天的数据
     * 
     */
    
    public  OTAResults getRoomRatesByConditions(JSONObject param,String bdate)
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		OTAResults otaResults = null;
		try {
			conn = DBPool.getPool().getConnection();
			
			String sql_query = "select b.hotelid,c.hotelName,b.date,b.rpcode,a.rp_code_dec,b.rmtype,b.rmtype_dec,a.price from set_room_price a,set_room_rate b,hotelname c where a.hotelid=b.hotelid and b.hotelid=c.hotelid and a.rm_date=b.date and a.rmtype=b.rmtype and a.rp_code=b.rpcode and b.rmtype='"+param.getString("rmtype").trim()+"' and b.rpcode='"+param.getString("rpcode").trim()+"' and b.date='"+bdate+"' and b.hotelid='"+param.getString("hotelid").trim()+"'";
			
			st = conn.createStatement();
			rs = st.executeQuery(sql_query);
			if(rs.next())
			{
				otaResults = new OTAResults();
				otaResults.setHotelid(rs.getString(1));
				otaResults.setHotelname(rs.getString(2));
				otaResults.setDate(rs.getString(3));
				otaResults.setRpcode(rs.getString(4));
				otaResults.setRpcode_des(rs.getString(5));
				otaResults.setRmtype(rs.getString(6));
				otaResults.setRmtype_des(rs.getString(7));
				otaResults.setPrice(rs.getString(8));
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
	    
		return otaResults;
	}
    
    
}
