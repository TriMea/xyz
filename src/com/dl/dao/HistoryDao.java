package com.dl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.dl.datasource.DBPool;
import com.dl.pojo.Mstinfo_his;
import com.dl.task.HistoricTask;
import com.dl.utl.CommonTool;

public class HistoryDao {

	/*
	 * 单例模式
	 */
	private HistoryDao() {}  
	
	private Logger logger = Logger.getLogger(HistoryDao.class); 
    private static final HistoryDao orderdao = new HistoryDao();  
    public static HistoryDao getInstance() {  
        return orderdao;  
    }  
    
    public  List<Mstinfo_his> getMstinfoForHistory()
	{
		Connection conn = null;
		Statement st  = null;
		ResultSet rs = null;
		List<Mstinfo_his> list = new ArrayList<Mstinfo_his>();
		try {
			conn = DBPool.getPool().getConnection();
			String sql_query = "SELECT date_sub(curdate(),interval 4 day),mstinfo.* FROM mstinfo where checkout <=date_sub(curdate(),interval 4 day)";
//			String sql_query = "SELECT curdate(),mstinfo.* FROM mstinfo where checkout <=curdate() and (orderstatus=0 or orderstatus=4 ororderstatus=6)";
			
			st = conn.createStatement();
			rs = st.executeQuery(sql_query);
			while(rs.next())
			{
				Mstinfo_his mstinfoHis = new Mstinfo_his();
				mstinfoHis.setExecDate(rs.getString(1).trim());
				mstinfoHis.setTaoBaoOrderId(rs.getString(2).trim());
				mstinfoHis.setOrderid(rs.getString(3));
				mstinfoHis.setTaobaohotelid(rs.getString(4));
				mstinfoHis.setHotelid(rs.getString(5));
				mstinfoHis.setTaobaoroomtypeid(rs.getString(6));
				mstinfoHis.setRoomTypeId(rs.getString(7));
				mstinfoHis.setTaobaorateplanid(rs.getString(8));
				mstinfoHis.setRatePlanCode(rs.getString(9));
				mstinfoHis.setTaobaogid(rs.getString(10));
				mstinfoHis.setCheckIn(rs.getString(11));
				mstinfoHis.setCheckOut(rs.getString(12));
				mstinfoHis.setEarliestarrivetime(rs.getString(13));
				mstinfoHis.setLatestarrivetime(rs.getString(14));
				mstinfoHis.setRoomNum(rs.getInt(15));
				mstinfoHis.setTotalprice(rs.getInt(16));
				mstinfoHis.setPaymentType(rs.getInt(17));
				mstinfoHis.setContactName(rs.getString(18));
				mstinfoHis.setContactTel(rs.getString(19));
				mstinfoHis.setContactemail(rs.getString(20));
				mstinfoHis.setDailyInfos(rs.getString(21));
				mstinfoHis.setOrderGuests(rs.getString(22));
				mstinfoHis.setRemark(rs.getString(23));
				mstinfoHis.setMembercardno(rs.getString(24));
				mstinfoHis.setGuaranteetype(rs.getString(25));
				mstinfoHis.setReceiptInfo(rs.getString(26));
				mstinfoHis.setMkt(rs.getString(27));
				mstinfoHis.setIsonline(rs.getString(28));
				mstinfoHis.setPmsresid(rs.getString(29));
				mstinfoHis.setOrderStatus(rs.getInt(30));
				mstinfoHis.setDispatchersta(rs.getString(31));
				mstinfoHis.setDispatcherdesc(rs.getString(32));
				mstinfoHis.setLog_date(rs.getString(33));
				mstinfoHis.setOrder_r_sta(rs.getString(34));
				mstinfoHis.setOrder_c_sta(rs.getString(35));
				mstinfoHis.setGuestnum(rs.getString(36));
				list.add(mstinfoHis);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
		} finally{
			
			try {
				CommonTool.closeResultSet(rs);
				CommonTool.closeStatement(st);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
	    
		return list;
	}
    
   /*
    * 将满足条件的订单信息迁移到历史表 
    */
    public  boolean dataToHistory(List<Mstinfo_his> list)
	{
		Connection conn = null;
		PreparedStatement pst  = null;
		boolean issuccess = false;
		
		try {
			conn = DBPool.getPool().getConnection();
			conn.setAutoCommit(false);
			String sql_query = "insert mstinfo_his(taobaoorderid,orderid,taobaohotelid,hotelid,taobaoroomtypeid,roomtypeid,taobaorateplanid,rateplancode,taobaogid,checkin,checkout,earliestarrivetime,latestarrivetime,roomnum,totalprice,paymenttype,contactname,contacttel,contactemail,dailyinfos,orderguests,comment,membercardno,guaranteetype,receiptinfo,mrk,isonline,pmsresid,orderstatus,dispatchersta,dispatcherdesc,log_date,order_r_sta,order_c_sta,guestnum) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			
			pst = conn.prepareStatement(sql_query);
			for (int i = 0; i < list.size(); i++) {
				pst.setString(1,list.get(i).getTaoBaoOrderId());
				pst.setString(2,list.get(i).getOrderid());
				pst.setString(3,list.get(i).getTaobaohotelid());
				pst.setString(4,list.get(i).getHotelid());
				pst.setString(5,list.get(i).getTaobaoroomtypeid());
				pst.setString(6,list.get(i).getRoomTypeId());
				pst.setString(7,list.get(i).getTaobaorateplanid());
				pst.setString(8,list.get(i).getRatePlanCode());
				pst.setString(9,list.get(i).getTaobaogid());
				pst.setString(10,list.get(i).getCheckIn());
				pst.setString(11,list.get(i).getCheckOut());
				pst.setString(12,list.get(i).getEarliestarrivetime());
				pst.setString(13,list.get(i).getLatestarrivetime());
				pst.setInt(14,list.get(i).getRoomNum());
				pst.setInt(15,list.get(i).getTotalprice());
				pst.setInt(16,list.get(i).getPaymentType());
				pst.setString(17,list.get(i).getContactName());
				pst.setString(18,list.get(i).getContactTel());
				pst.setString(19,list.get(i).getContactemail());
				pst.setString(20,list.get(i).getDailyInfos());
				pst.setString(21,list.get(i).getOrderGuests());
				pst.setString(22,list.get(i).getRemark());
				pst.setString(23,list.get(i).getMembercardno());
				pst.setString(24,list.get(i).getGuaranteetype());
				pst.setString(25,list.get(i).getReceiptInfo());
				pst.setString(26,list.get(i).getMkt());
				pst.setString(27,list.get(i).getIsonline());
				pst.setString(28,list.get(i).getPmsresid());
				pst.setInt(29,list.get(i).getOrderStatus());
				pst.setString(30,list.get(i).getDispatchersta());
				pst.setString(31,list.get(i).getDispatcherdesc());
				pst.setString(32,list.get(i).getLog_date());
				pst.setString(33,list.get(i).getOrder_r_sta());
				pst.setString(34,list.get(i).getOrder_c_sta());
				pst.setString(35,list.get(i).getGuestnum());
				pst.addBatch();
			}
			
			
			  pst.executeBatch();  
		      conn.commit();  
		      issuccess = true;
			
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
			try {
				
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(e1);
				e1.printStackTrace();
			}
		} finally{
			
			try {
		        conn.setAutoCommit(true);
				CommonTool.closePreparedStatement(pst);
				CommonTool.closeConnection(conn);
			} catch (SQLException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
	    return issuccess;
		
	}
    
    
    /*
     * 将满足条件的订单信息迁移到mst_report
     */
     public  boolean dataToReport()
 	{
 		Connection conn = null;
 		Statement st  = null;
 		boolean issuccess = false;
 		
 		try {
 			conn = DBPool.getPool().getConnection();
 			
 			String sql_query = "insert into mst_report(hotelid,taobaoorderid,checkin,checkout,contactname,isonline) SELECT hotelid,taobaoorderid,checkin,checkout,contactname,isonline FROM mstinfo where checkout <=date_sub(curdate(),interval 4 day) and orderstatus=9";
// 			String sql_query = "insert into mst_report(hotelid,taobaoorderid,checkin,checkout,contactname,isonline) SELECT hotelid,taobaoorderid,checkin,checkout,contactname,isonline FROM mstinfo where checkout <=curdate() and orderstatus=9";
 			
 			st = conn.createStatement();
		    st.executeUpdate(sql_query);
	        issuccess = true;
 			
 		} catch (SQLException e) {
 			logger.error(e);
 			e.printStackTrace();
 		} finally{
 			
 			try {
 		      
 				CommonTool.closeStatement(st);
 				CommonTool.closeConnection(conn);
 			} catch (SQLException e) {
 				logger.error(e);
 				e.printStackTrace();
 			}
 		}
 	    return issuccess;
 		
 	}
    
    
    /*
     * 将满足条件的订单信息从mstinfo表中删除
     */
     public  void deleteMstinfoForHistory()
 	{
 		Connection conn = null;
 		Statement st  = null;

 		
 		try {
 			conn = DBPool.getPool().getConnection();
 			String sql_delete = "delete FROM mstinfo where checkout <=date_sub(curdate(),interval 60 day)";
 			st = conn.createStatement();
 			st.executeUpdate(sql_delete);
 			
 		} catch (SQLException e) {
 			logger.error(e);
 			e.printStackTrace();
 			
 		} finally{
 			
 			try {
 				CommonTool.closeStatement(st);
 				CommonTool.closeConnection(conn);
 			} catch (SQLException e) {
 				logger.error(e);
 				e.printStackTrace();
 			}
 		}
 	 
 		
 	}
     
     
     /*
      * 将相关数据合并到mst_report
      */
      public  void updateMstinfoForReport()
  	{
  		Connection conn = null;
  		Statement st  = null;
  		try {
  			conn = DBPool.getPool().getConnection();
  			conn.setAutoCommit(false);
  			String sql_update1 = "update  mst_report, mstinfo_log set mst_report.room_fee=mstinfo_log.room_fee/100,mst_report.other_fee=mstinfo_log.other_fee/100,mst_report.op_date=mstinfo_log.op_date where mstinfo_log.room_fee is not null and mstinfo_log.tid=mst_report.taobaoorderid ";
  			String sql_update2 = "update  mst_report, hotellist_ota set mst_report.hotelname=hotellist_ota.hotelname,mst_report.cucode=hotellist_ota.cucode where  hotellist_ota.hotelid=mst_report.hotelid ";
  			String sql_update3 = "update  mst_report, vendor set mst_report.vendor=vendor.vendor where  vendor.cucode=mst_report.cucode ";
  			String sql_delete = "delete FROM mstinfo where checkout <=date_sub(curdate(),interval 4 day)";
  			String sql_add = "INSERT INTO exec_log(exec_date,op_date) VALUES(date_sub(curdate(),interval 4 day),'"+CommonTool.getToday()+"')";
//  			String sql_delete = "delete FROM mstinfo where checkout <=curdate()";
//  			String sql_add = "INSERT INTO exec_log(exec_date,op_date) VALUES(curdate(),'"+CommonTool.getToday()+"')";
  			st = conn.createStatement();
  			st.addBatch(sql_update1);
  			st.addBatch(sql_update2);
  			st.addBatch(sql_update3);
  			st.addBatch(sql_delete);
  			st.addBatch(sql_add);
  			st.executeBatch();
  			conn.commit();
  			
  		} catch (SQLException e) {
  			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
  			logger.error(e);
  			e.printStackTrace();
  			
  		} finally{
  			
  			try {
  				conn.setAutoCommit(true);
  				CommonTool.closeStatement(st);
  				CommonTool.closeConnection(conn);
  			} catch (SQLException e) {
  				logger.error(e);
  				e.printStackTrace();
  			}
  		}
  	 
  		
  	}  
}
