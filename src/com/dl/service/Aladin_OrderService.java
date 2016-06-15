package com.dl.service;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

import com.dl.dao.AladinDao;
import com.dl.servlet.RequestServlet;
import com.dl.utl.CommonTool;
import com.dl.utl.VarUtil;

public class Aladin_OrderService {

   private Logger logger = Logger.getLogger(Aladin_OrderService.class); 
	public boolean isAladin(String hotelid)
	{
	
        boolean isAladin = false;
		if(AladinDao.getInstance().isAladinHotel(hotelid))
		{
			isAladin = true;
		}
		logger.info(hotelid+"是否为阿拉丁酒店:"+isAladin);
		return isAladin;
	
	}
	
	public JSONObject sendValidateRQ(JSONObject validateParams)
	{
		JSONObject result = null;
		result = CommonTool.SendGET(VarUtil.ALADIN_VALIDATE_ORDER,"data="+validateParams.toString());
		logger.info("阿拉丁服务器试单返回："+result);
		return result;
	}
	
	public JSONObject sendAddOrderRQ(JSONObject orderParams)
	{
		JSONObject result = null;
		result = CommonTool.SendPOST(VarUtil.ALADIN_ACCEPT_ORDER,orderParams);
		logger.info("阿拉丁服务器下单返回："+result);
		return result;
	}
	
	public JSONObject sendCancelOrderRQ(JSONObject cancelParams)
	{
		JSONObject result = null;
		result = CommonTool.SendPOST(VarUtil.ALADIN_CANCEL_ORDER,cancelParams);
		logger.info("阿拉丁服务器取消订单返回："+result);
		return result;
	}
}
