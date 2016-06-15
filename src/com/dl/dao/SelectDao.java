package com.dl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import com.dl.datasource.DBPool;
import com.dl.pojo.Hotel_info;
import com.dl.pojo.Mstinfo_his;
import com.dl.pojo.Rateplan;
import com.dl.pojo.Rm_type;
import com.dl.utl.CommonTool;

public class SelectDao {
	
	
	     //酒店信息
          public List<Hotel_info> hotel_info(String hotelid){
        	  
        	  
        	List<Hotel_info> list = new ArrayList<Hotel_info>();
  			Connection conn=null;
  			ResultSet rs=null;
  			PreparedStatement pstmt=null;
  			try {
  				conn = DBPool.getPool().getConnection();
  				conn.setAutoCommit(false);
  				String sql="SELECT * from hotel_info where hotelid=?";
  		        Object[] param={hotelid};
  		        pstmt=conn.prepareStatement(sql);
  		        for (int i = 0; i < param.length; i++) {
					pstmt.setObject(i+1, param[i]);
				}
  		        rs=pstmt.executeQuery();
  		        while(rs.next()){
  		        	Hotel_info hotel=new Hotel_info();
  		        	hotel.setHotelid(rs.getString("hotelid"));
  		        	hotel.setName(rs.getString("name"));
  		        	hotel.setDomestic(rs.getInt("domestic"));
  		        	hotel.setCity(rs.getString("city"));
  		        	hotel.setAddress(rs.getString("address"));
  		        	hotel.setTel(rs.getString("tel"));
  		        	hotel.setVendor(rs.getString("vendor"));
  		        	hotel.setFloor(rs.getString("floor"));
  		        	hotel.setRooms(rs.getInt("rooms"));
  		        	hotel.setDescription(rs.getString("description"));
  		        	hotel.setPics(rs.getString("pics"));
  		        	hotel.setDistrict(rs.getString("district"));
  		        	hotel.setLog_date(rs.getString("log_date"));
  		        	list.add(hotel);
  		        }
  		        conn.commit();
  			} catch (SQLException e) {
  				// TODO Auto-generated catch block
  				e.printStackTrace();
  			}finally{
  				try {
  					CommonTool.closeResultSet(rs);
  					CommonTool.closeConnection(conn);
  					CommonTool.closePreparedStatement(pstmt);
  				} catch (SQLException e) {
  					// TODO Auto-generated catch block
  					e.printStackTrace();
  				}
  		        
  			}
        	  
			return list;
        	  
          }
          
          
          //房型
          public List<Rm_type> Rm_type(String hotelid){
        	
        	List<Rm_type> list = new ArrayList<Rm_type>();
  			Connection conn=null;
  			ResultSet rs=null;
  			PreparedStatement pstmt=null;
  			try {
  				conn = DBPool.getPool().getConnection();
  				conn.setAutoCommit(false);
  				String sql="SELECT * from roomtype where hotelid=?";
  		        Object[] param={hotelid};
  		        pstmt=conn.prepareStatement(sql);
		        for (int i = 0; i < param.length; i++) {
					pstmt.setObject(i+1, param[i]);
				}
		        rs=pstmt.executeQuery();
  		        while(rs.next()){
  		        	Rm_type type=new Rm_type();
  		        	type.setHotelid(rs.getString("hotelid"));
  		        	type.setInternet(rs.getString("internet"));
  		        	type.setMax_occupancy(rs.getInt("max_occupancy"));
  		        	type.setName(rs.getString("name"));
  		        	type.setRoomtype_id(rs.getString("roomtype_id"));
  		        	type.setVendor(rs.getString("vendor"));
  		        	type.setWindow_type(rs.getInt("window_type"));
  		        	list.add(type);
  		        }
  		        conn.commit();
  			} catch (SQLException e) {
  				// TODO Auto-generated catch block
  				e.printStackTrace();
  			}finally{
  				try {
  					CommonTool.closeResultSet(rs);
  					CommonTool.closeConnection(conn);
  					CommonTool.closePreparedStatement(pstmt);
  				} catch (SQLException e) {
  					// TODO Auto-generated catch block
  					e.printStackTrace();
  				}
  		        
  			}
			return list;
          }
          
          
          
          //房价代码
          public List<Rateplan> Rateplan(String hotelid){
        	  
        	List<Rateplan> list = new ArrayList<Rateplan>();
  			Connection conn=null;
  			ResultSet rs=null;
  			PreparedStatement pstmt=null;
  			try {
  				conn = DBPool.getPool().getConnection();
  				conn.setAutoCommit(false);
  				String sql="SELECT * from rateplan where hotelid=?";
  		        Object[] param={hotelid};
  		        pstmt=conn.prepareStatement(sql);
		        for (int i = 0; i < param.length; i++) {
					pstmt.setObject(i+1, param[i]);
				}
		        rs=pstmt.executeQuery();
  		        while(rs.next()){
  		        	Rateplan plan=new Rateplan();
  		            plan.setRateplan_code(rs.getString("rateplan_code"));
  		            plan.setHotelid(rs.getString("hotelid"));
  		            plan.setName(rs.getString("name"));
  		            plan.setPayment_type(rs.getInt("payment_type"));
  		            plan.setBreakfast_count(rs.getInt("breakfast_count"));
  		            plan.setCancel_policy(rs.getString("cancel_policy"));
  		            plan.setStatus(rs.getInt("status"));
  		            plan.setVendor(rs.getString("vendor"));
  		            list.add(plan);
  		        }
  		        conn.commit();
  			} catch (SQLException e) {
  				// TODO Auto-generated catch block
  				e.printStackTrace();
  			}finally{
  				try {
  					CommonTool.closePreparedStatement(pstmt);
  					CommonTool.closeResultSet(rs);
  					CommonTool.closeConnection(conn);
  				} catch (SQLException e) {
  					// TODO Auto-generated catch block
  					e.printStackTrace();
  				}
  		        
  			}
			return list;
        	  
          }
          
          //订单信息
          public List<Mstinfo_his> Mstinfo_his(String hotelid,String bdate,String edate){
        	  
        	Connection conn=null;
  			ResultSet rs=null;
  			ResultSet rs1=null;
  			PreparedStatement pstmt=null;
  			PreparedStatement pstmt1=null;
  			List<Mstinfo_his> list = new ArrayList<Mstinfo_his>();
  			try {
  				conn = DBPool.getPool().getConnection();
  				conn.setAutoCommit(false);
  				String sql="SELECT * from mstinfo where hotelid=? and log_date>? and log_date<? and orderstatus!=0";
  				String sql2="SELECT * from mstinfo_his where hotelid=? and log_date>? and log_date<? and orderstatus!=0";
  		        Object[] param={hotelid,bdate,edate};
  		        Object[] param2={hotelid,bdate,edate};
  		        pstmt=conn.prepareStatement(sql);
		        for (int i = 0; i < param.length; i++) {
					pstmt.setObject(i+1, param[i]);
				}
		        rs=pstmt.executeQuery();
  		        while(rs.next()){
  		        	Mstinfo_his mst=new Mstinfo_his();
                      mst.setTaoBaoOrderId(rs.getString("taobaoorderid"));
                      mst.setOrderid(rs.getString("orderid"));
                      mst.setTaobaohotelid(rs.getString("taobaohotelid"));
                      mst.setHotelid(rs.getString("hotelid"));
                      mst.setTaobaoroomtypeid(rs.getString("taobaoroomtypeid"));
                      mst.setRoomTypeId(rs.getString("roomtypeid"));
                      mst.setTaobaorateplanid(rs.getString("taobaorateplanid"));
                      mst.setRatePlanCode(rs.getString("rateplancode"));
                      mst.setTaobaogid(rs.getString("taobaogid"));
                      mst.setCheckIn(rs.getString("checkin"));
                      mst.setCheckOut(rs.getString("checkout"));
                      mst.setEarliestarrivetime(rs.getString("earliestarrivetime"));
                      mst.setLatestarrivetime(rs.getString("latestarrivetime"));
                      mst.setRoomNum(rs.getInt("roomnum"));
                      mst.setTotalprice(rs.getInt("totalprice"));
                      mst.setPaymentType(rs.getInt("paymenttype"));
                      mst.setContactName(rs.getString("contactname"));
                      mst.setContactTel(rs.getString("contacttel"));
                      mst.setContactemail(rs.getString("contactemail"));
                      mst.setDailyInfos(rs.getString("dailyinfos"));
                      mst.setOrderGuests(rs.getString("orderguests"));
                      mst.setComment(rs.getString("comment"));
                      mst.setMembercardno(rs.getString("membercardno"));
                      mst.setGuaranteetype(rs.getString("guaranteetype"));
                      mst.setReceiptInfo(rs.getString("receiptinfo"));
                      mst.setMkt(rs.getString("mrk"));
                      mst.setIsonline(rs.getString("isonline"));
                      mst.setPmsresid(rs.getString("pmsresid"));
                      mst.setOrderStatus(rs.getInt("orderstatus"));
                      mst.setDispatchersta(rs.getString("dispatchersta"));
                      mst.setDispatcherdesc(rs.getString("dispatcherdesc"));
                      mst.setLog_date(rs.getString("log_date"));
                      mst.setOrder_r_sta(rs.getString("order_r_sta"));
                      mst.setOrder_c_sta(rs.getString("order_c_sta"));
                      mst.setGuestnum(rs.getString("guestnum"));
                      list.add(mst);
  		        }
  		        pstmt1=conn.prepareStatement(sql2);
		        for (int i = 0; i < param2.length; i++) {
		        	pstmt1.setObject(i+1, param2[i]);
				}
		        rs1=pstmt1.executeQuery();
  		        while(rs1.next()){
  		        	Mstinfo_his mst=new Mstinfo_his();
                      mst.setTaoBaoOrderId(rs1.getString("taobaoorderid"));
                      mst.setOrderid(rs1.getString("orderid"));
                      mst.setTaobaohotelid(rs1.getString("taobaohotelid"));
                      mst.setHotelid(rs1.getString("hotelid"));
                      mst.setTaobaoroomtypeid(rs1.getString("taobaoroomtypeid"));
                      mst.setRoomTypeId(rs1.getString("roomtypeid"));
                      mst.setTaobaorateplanid(rs1.getString("taobaorateplanid"));
                      mst.setRatePlanCode(rs1.getString("rateplancode"));
                      mst.setTaobaogid(rs1.getString("taobaogid"));
                      mst.setCheckIn(rs1.getString("checkin"));
                      mst.setCheckOut(rs1.getString("checkout"));
                      mst.setEarliestarrivetime(rs1.getString("earliestarrivetime"));
                      mst.setLatestarrivetime(rs1.getString("latestarrivetime"));
                      mst.setRoomNum(rs1.getInt("roomnum"));
                      mst.setTotalprice(rs1.getInt("totalprice"));
                      mst.setPaymentType(rs1.getInt("paymenttype"));
                      mst.setContactName(rs1.getString("contactname"));
                      mst.setContactTel(rs1.getString("contacttel"));
                      mst.setContactemail(rs1.getString("contactemail"));
                      mst.setDailyInfos(rs1.getString("dailyinfos"));
                      mst.setOrderGuests(rs1.getString("orderguests"));
                      mst.setComment(rs1.getString("comment"));
                      mst.setMembercardno(rs1.getString("membercardno"));
                      mst.setGuaranteetype(rs1.getString("guaranteetype"));
                      mst.setReceiptInfo(rs1.getString("receiptinfo"));
                      mst.setMkt(rs1.getString("mrk"));
                      mst.setIsonline(rs1.getString("isonline"));
                      mst.setPmsresid(rs1.getString("pmsresid"));
                      mst.setOrderStatus(rs1.getInt("orderstatus"));
                      mst.setDispatchersta(rs1.getString("dispatchersta"));
                      mst.setDispatcherdesc(rs1.getString("dispatcherdesc"));
                      mst.setLog_date(rs1.getString("log_date"));
                      mst.setOrder_r_sta(rs1.getString("order_r_sta"));
                      mst.setOrder_c_sta(rs1.getString("order_c_sta"));
                      mst.setGuestnum(rs1.getString("guestnum"));
                      list.add(mst);
  		        }
  		        conn.commit();
  			} catch (SQLException e) {
  				
  				try {
					conn.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
  				e.printStackTrace();
  			}finally{
  				try {
  					CommonTool.closeResultSet(rs);
  					CommonTool.closeResultSet(rs1);
  					CommonTool.closePreparedStatement(pstmt);
  					CommonTool.closePreparedStatement(pstmt1);
  					CommonTool.closeConnection(conn);
  				} catch (SQLException e) {
  					// TODO Auto-generated catch block
  					e.printStackTrace();
  				}
  		        
  			}
        	  
			return list;
        	  
          }
          
          
          
}
