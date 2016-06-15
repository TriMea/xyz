package com.dl.service;

import net.sf.json.JSONObject;

import com.dl.utl.CommonTool;
import com.dl.utl.VarUtil;

public class RZT_OrderService {

	public boolean IsRightPrice(JSONObject params)
	{
		JSONObject jo_result = null;
		boolean isOk = false;
		jo_result = CommonTool.SendGET(VarUtil.RZT_VALIDATE_PRICE,"data="+params.toString());
		if(jo_result!=null)
		{
			if(jo_result.getInt("code")==0)
			{
				//价格匹配
				isOk = true;
			}else{
				//价格不匹配
				isOk = false;
			}
		}
		
		return isOk;
	}
	
	
}
