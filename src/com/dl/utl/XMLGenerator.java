package com.dl.utl;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import com.dl.pojo.InventoryPrice;
import com.dl.servlet.BasicDataServlet;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
public class XMLGenerator {

	private Logger logger = Logger.getLogger(XMLGenerator.class); 
	public static Document  XMLGenerator_ValidateRQ(Integer code,JSONArray ja)
	{
		Document doc = DocumentHelper.createDocument();
		Element root = DocumentHelper.createElement("Result");
		doc.setRootElement(root);
		
		switch (code) {
		case 0:
			List<InventoryPrice> list = new ArrayList<InventoryPrice>();
			for(int i=0;i<ja.size();i++)
			{
				InventoryPrice ip = new InventoryPrice();
				ip.setDate(ja.getJSONObject(i).getString("sysdate"));
				ip.setPrice(ja.getJSONObject(i).getInt("rm_price"));
				ip.setQuota(ja.getJSONObject(i).getInt("rm_avl"));
				list.add(ip);
			}
			JSONArray ja_ip = JSONArray.fromObject(list);
			root.addElement("Message").setText("");
			root.addElement("CreateOrderValidateKey").setText("");
			Element resultCode = root.addElement("ResultCode");
			resultCode.setText("0");
			Element inventoryPrice  = root.addElement("InventoryPrice");
			inventoryPrice.setText(ja_ip.toString());
			
			break;
        case 1:
        	Element message = root.addElement("Message");
        	message.setText("满房");
        	Element resultCode1 = root.addElement("ResultCode");
			resultCode1.setText("-1");
			break;	
        case 2:
        	Element message1 = root.addElement("Message");
        	message1.setText("订单下发器不在线");
        	Element resultCode2 = root.addElement("ResultCode");
			resultCode2.setText("-3");
			break;		

		default:
			break;
		}
		return doc;
	}
	
	public static Document  XMLGenerator_BookRQ(Integer code)
	{
		Document doc = DocumentHelper.createDocument();
		Element root = DocumentHelper.createElement("Result");
		doc.setRootElement(root);
		
		switch (code) {
		case 0:
			root.addElement("Message").setText("");
			root.addElement("OrderId").setText("");
        	root.addElement("ResultCode").setText("0");
			break;
        case 1:
        	Element message1 = root.addElement("Message");
        	message1.setText("插入订单失败");
        	Element resultCode2 = root.addElement("ResultCode");
			resultCode2.setText("-102");
			break;	
        case 2:
        	Element message2 = root.addElement("Message");
        	message2.setText("重复预定");
        	Element resultCode3 = root.addElement("ResultCode");
			resultCode3.setText("-106");
			break;	
        case 3:
        	Element message3 = root.addElement("Message");
        	message3.setText("收单服务器未响应（阿拉丁）");
        	Element resultCode4 = root.addElement("ResultCode");
			resultCode4.setText("-107");
			break;		

		default:
			break;
		}
		return doc;
	}
	
	public static Document  XMLGenerator_CancelRQ(Integer code,String orderid)
	{
		Document doc = DocumentHelper.createDocument();
		Element root = DocumentHelper.createElement("Result");
		doc.setRootElement(root);
		
		switch (code) {
		case 0:
			Element message = root.addElement("Message");
        	message.setText("取消失败");
        	Element resultCode1 = root.addElement("ResultCode");
			resultCode1.setText("-204");
			break;
        case 1:
        	Element message1 = root.addElement("Message");
        	message1.setText("取消失败");
        	Element resultCode2 = root.addElement("ResultCode");
			resultCode2.setText("-205");
			break;	
        case 2:
        	Element message2 = root.addElement("Message");
        	message2.setText("成功");
        	root.addElement("ResultCode").setText("0");
        	root.addElement("OrderId").setText(orderid);
        	System.out.println("取消订单成功返回去啊："+doc.asXML());
//        	Element message2 = root.addElement("Message");
//        	message2.setText("已收到取消订单请求");
//        	Element resultCode3 = root.addElement("ResultCode");
//			resultCode3.setText("-100");
			break;		

		default:
			break;
		}
		return doc;
	}
	
	
	public static Document  XMLGenerator_QueryRQ(Integer code,Map<String, Object> mp)
	{
		Document doc = DocumentHelper.createDocument();
		Element root = DocumentHelper.createElement("Result");
		doc.setRootElement(root);
		
		switch (code) {
		case 0:
			Element message = root.addElement("Message");
        	message.setText("查询失败");
        	root.addElement("ResultCode").setText("-302");
			root.addElement("OrderId").setText("");
			break;
		case 2:
			root.addElement("Message").setText("订单未处理");
        	root.addElement("ResultCode").setText("-301");
			root.addElement("OrderId").setText((String)mp.get("orderId"));
			break;
		case 10:
			root.addElement("Message").setText("订单未处理");
        	root.addElement("ResultCode").setText("-303");
			root.addElement("OrderId").setText((String)mp.get("orderId"));
			break;
        case 1:
        	Element message1 = root.addElement("Message");
        	message1.setText("处理成功");
        	Element resultCode3 = root.addElement("ResultCode");
			resultCode3.setText("0");
			root.addElement("Status").setText("1");
			root.addElement("OrderId").setText((String)mp.get("orderid"));
			root.addElement("TaoBaoOrderId").setText((String)mp.get("TaoBaoOrderId"));
			root.addElement("PmsResID").setText((String)mp.get("PmsResID"));
			
			break;	
        case 4:
        	Element message2 = root.addElement("Message");
        	message2.setText("处理成功");
        	Element resultCode4 = root.addElement("ResultCode");
			resultCode4.setText("0");
			root.addElement("Status").setText("4");
			root.addElement("OrderId").setText((String)mp.get("orderid"));
			root.addElement("TaoBaoOrderId").setText((String)mp.get("TaoBaoOrderId"));
			root.addElement("PmsResID").setText((String)mp.get("PmsResID"));
			break;	
        case 8:
        	Element message3 = root.addElement("Message");
        	message3.setText("处理成功");
        	Element resultCode5 = root.addElement("ResultCode");
			resultCode5.setText("0");
			root.addElement("Status").setText("8");
			root.addElement("OrderId").setText((String)mp.get("orderid"));
			root.addElement("TaoBaoOrderId").setText((String)mp.get("TaoBaoOrderId"));
			root.addElement("PmsResID").setText((String)mp.get("PmsResID"));
			break;	

		 case 5:
	        	 
	        	root.addElement("Message").setText("处理成功");
	        	root.addElement("ResultCode").setText("0");
				root.addElement("Status").setText("5");
				root.addElement("OrderId").setText((String)mp.get("orderid"));
				root.addElement("TaoBaoOrderId").setText((String)mp.get("TaoBaoOrderId"));
				root.addElement("PmsResID").setText((String)mp.get("PmsResID"));
//				Element OrderInfo = DocumentHelper.createElement("OrderInfo");
//				OrderInfo.addElement("CheckIn").setText((String)mp.get("CheckIn"));
//				OrderInfo.addElement("CheckOut").setText((String)mp.get("CheckOut"));
//				OrderInfo.addElement("RoomQuantity").setText((String)mp.get("RoomQuantity"));
//				OrderInfo.addElement("Hotel").setText((String)mp.get("Hotel"));
//				OrderInfo.addElement("RoomType").setText("");
//				root.add(OrderInfo);
				
				break;				
			 case 9:
				 
//				 Element message1 = root.addElement("Message");
//		        	message1.setText("处理成功");
//		        	Element resultCode3 = root.addElement("ResultCode");
//					resultCode3.setText("0");
//					root.addElement("Status").setText("1");
//					root.addElement("OrderId").setText((String)mp.get("orderid"));
//					root.addElement("TaoBaoOrderId").setText((String)mp.get("TaoBaoOrderId"));
//					root.addElement("PmsResID").setText((String)mp.get("PmsResID"));
				 
				 root.addElement("Message").setText("处理成功");
		        	root.addElement("ResultCode").setText("0");
					root.addElement("Status").setText("9");
					root.addElement("OrderId").setText((String)mp.get("orderid"));
					root.addElement("TaoBaoOrderId").setText((String)mp.get("TaoBaoOrderId"));
					root.addElement("PmsResID").setText((String)mp.get("PmsResID"));
					Element billInfo = DocumentHelper.createElement("BillInfo");
					billInfo.addElement("RoomNo").setText(mp.get("RoomNo")==null?"":(String)mp.get("RoomNo"));
					billInfo.addElement("TotalRoomFee").setText((Integer)mp.get("TotalRoomFee")+"");
					Element dailyPrice = DocumentHelper.createElement("DailyPrice");
					JSONArray ja_DailyPrice = JSONArray.fromObject(mp.get("DailyPrice"));
					for(int i=0;i<ja_DailyPrice.size();i++)
					{
						Element unit = DocumentHelper.createElement("Unit");
						unit.addElement("Date").setText(ja_DailyPrice.getJSONObject(i).getString("day"));
						unit.addElement("Price").setText(ja_DailyPrice.getJSONObject(i).getString("price"));
						dailyPrice.add(unit);
					}
					billInfo.add(dailyPrice);
					billInfo.addElement("OtherFee").setText((Integer)mp.get("OtherFee")+"");
					Element otherFeeDetail = DocumentHelper.createElement("OtherFeeDetail");
					JSONObject jo_otherFeeDetail = JSONObject.fromObject((String)mp.get("OtherFeeDetail"));
					Iterator it = jo_otherFeeDetail.keys();
					while(it.hasNext())
					{
						String key = it.next().toString();
						Element unit = DocumentHelper.createElement("Unit");
						unit.addElement("Name").setText(key);
						unit.addElement("Price").setText(jo_otherFeeDetail.getString(key));
						otherFeeDetail.add(unit);
					}
					billInfo.add(otherFeeDetail);
					billInfo.addElement("Remark").setText("");
					root.add(billInfo);
					break;	

				default:
		
		}
		return doc;
	}
}
