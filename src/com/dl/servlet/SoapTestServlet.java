package com.dl.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import net.sf.json.JSONArray;


import org.dom4j.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.dl.pojo.Set_roommap;
import com.dl.pojo.Set_roomprice;
import com.dl.pojo.Set_roomrate;
import com.dl.utl.HttpSend_GET;
import com.dl.utl.SoapUtl;

import com.dl.utl.XMLGenerator_RZT;

public class SoapTestServlet extends HttpServlet {

	private int i = 0;
	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}
	
	
	

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		i++;
		System.out.println("i:"+i);
		super.doGet(req, resp);
	}




	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/xml");
		System.out.println("调用了 post方法");
		PrintWriter out = response.getWriter();
		BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream(),"utf-8"));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		System.out.println("收到请求参数："+sb.toString());
//		BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream(),"utf-8"));
//		String line = null;
//		StringBuilder sb = new StringBuilder();
//		while ((line = br.readLine()) != null) {
//			sb.append(line);
//		}
		/*
		 * 库存推送(实时房量)
		 */
//		try {			
//			Set_roomrate rs = new Set_roomrate();
//			List<Set_roomrate> list = new ArrayList<Set_roomrate>();
//			rs.setHotelid("dlhis123");
//			rs.setBdate("2016-2-4");
//			rs.setEdate("2016-2-10");
//			rs.setRmtype("dlhis123_BJ");
//			rs.setMon("1");
//			rs.setTUE("1");
//			rs.setWED("1");
//			rs.setTUR("1");
//			rs.setFRI("1");
//			rs.setSAT("1");
//			rs.setSUN("1");
//		    rs.setQuote("10");
//			list.add(rs);
//			Set_roomrate rs1 = new Set_roomrate();
//			rs1.setHotelid("dlhis123");
//			rs1.setBdate("2016-2-4");
//			rs1.setEdate("2016-2-10");
//			rs1.setRmtype("dlhis123_DJ");
//			rs1.setMon("1");
//			rs1.setTUE("1");
//			rs1.setWED("1");
//			rs1.setTUR("1");
//			rs1.setFRI("1");
//			rs1.setSAT("1");
//			rs1.setSUN("1");
//		    rs1.setQuote("8");
//			list.add(rs1);
//			System.out.println("list:"+JSONArray.fromObject(list).toString());
//			Document doc = XMLGenerator_RZT.XMLGenerator_InvCountNotifRQ("dlhis123", JSONArray.fromObject(list));
//			String result = HttpSend_GET.SendPOST_Soap("http://124.127.242.67/PMSGateway39/PMSGatewayService.asmx", doc.asXML());
//			System.out.println("结果："+result);
//			SOAPBody body = SoapUtl.str2soapbody(result);
//			if(body.getFirstChild().hasChildNodes())
//			{
//				System.out.println("content:"+body.getFirstChild().getFirstChild().getFirstChild().getTextContent());
//
//			}else{
//				System.out.println("推送成功");
//			}
//			
//			
//		} 
//
//		 catch (Exception e) {
//				e.printStackTrace();
//				}
		 
		 /*
		  * 房价推送
		  */
//		try {			
//			Set_roomprice rs = new Set_roomprice();
//			List<Set_roomprice> list = new ArrayList<Set_roomprice>();
//			rs.setHotelid("dlhis123");
//			rs.setRm_bdate("2016-2-4");
//			rs.setRm_edate("2016-2-5");
//			rs.setRmtype("dlhis123_BJ");
//			rs.setRp_code("RP10001");
//			rs.setRp_bdate("2016-2-4");
//			rs.setRp_edate("2016-2-8");
//			rs.setMon("1");
//			rs.setTUE("1");
//			rs.setWED("1");
//			rs.setTUR("1");
//			rs.setFRI("1");
//			rs.setSAT("1");
//			rs.setSUN("1");
//		    rs.setRm_price("300");
//			list.add(rs);
//			Set_roomprice rs1 = new Set_roomprice();
//			rs1.setHotelid("dlhis123");
//			rs1.setRm_bdate("2016-2-6");
//			rs1.setRm_edate("2016-2-8");
//			rs1.setRmtype("");
//			rs1.setRp_code("RP10003");
//			rs1.setRp_bdate("2016-2-4");
//			rs1.setRp_edate("2016-2-8");
//			rs1.setMon("1");
//			rs1.setTUE("1");
//			rs1.setWED("1");
//			rs1.setTUR("1");
//			rs1.setFRI("1");
//			rs1.setSAT("1");
//			rs1.setSUN("1");
//		    rs1.setRm_price("400");
//			list.add(rs1);
//			System.out.println("list:"+JSONArray.fromObject(list).toString());
//			Document doc = XMLGenerator_RZT.XMLGenerator_RatePlanNotifRQ("dlhis123", JSONArray.fromObject(list));
//			String result = HttpSend_GET.SendPOST_Soap("http://124.127.242.67/PMSGateway39/PMSGatewayService.asmx", doc.asXML());
//			System.out.println("结果："+result);
//			SOAPBody body = SoapUtl.str2soapbody(result);
//			System.out.println("result:"+body.getFirstChild().hasChildNodes());
//			System.out.println("content:"+body.getFirstChild().getFirstChild().getFirstChild().getTextContent());
//
//		} 
//
//		 catch (Exception e) {
//				e.printStackTrace();
//				}
		 /*
		  * 房态推送
		  */
//		try {			
//			Set_roommap rs = new Set_roommap();
//			List<Set_roommap> list = new ArrayList<Set_roommap>();
//			rs.setHotelid("dlhis123");
//			rs.setBdate("2016-3-2");
//			rs.setEdate("2016-3-3");
//			rs.setRmtype("dlhis123_BJ");
//			rs.setRp_code("RP10001");
//			rs.setChannel("CTRIP");
//			rs.setMon("1");
//			rs.setTUE("1");
//			rs.setWED("1");
//			rs.setTUR("1");
//			rs.setFRI("1");
//			rs.setSAT("1");
//			rs.setSUN("1");
//			rs.setStatus("0");
//			rs.setType_set("SetMinLOS");
//			rs.setDay_set("1");
//			list.add(rs);
//			Set_roommap rs1 = new Set_roommap();
//			rs1.setHotelid("dlhis123");
//			rs1.setBdate("2016-3-2");
//			rs1.setEdate("2016-3-3");
//			rs1.setRmtype("dlhis123_DJ");
//			rs1.setRp_code("RP10003");
//			rs1.setChannel("CTRIP");
//			rs1.setMon("1");
//			rs1.setTUE("1");
//			rs1.setWED("1");
//			rs1.setTUR("1");
//			rs1.setFRI("1");
//			rs1.setSAT("1");
//			rs1.setSUN("1");
//			rs1.setStatus("0");
//			rs1.setType_set("SetMinLOS");
//			rs1.setDay_set("1");
//			list.add(rs1);
//			System.out.println("list:"+JSONArray.fromObject(list).toString());
//			Document doc = XMLGenerator_RZT.XMLGenerator_NotifRQ("dlhis123", JSONArray.fromObject(list));
//			String result = HttpSend_GET.SendPOST_Soap("http://124.127.242.67/PMSGateway39/PMSGatewayService.asmx", doc.asXML());
//			System.out.println("结果："+result);
//
//		} 
//
//		 catch (Exception e) {
//				e.printStackTrace();
//				}
////				
			}
	
	public void soap2String(Source source) throws Exception {  
        if (source != null) {  
            Node root = null;  
            if (source instanceof DOMSource) {  
                root =  ((DOMSource) source).getNode();
                
            } else if (source instanceof SAXSource) {  
                InputSource inSource = ((SAXSource) source).getInputSource();  
                DocumentBuilderFactory dbf = DocumentBuilderFactory  
                        .newInstance();  
                dbf.setNamespaceAware(true);  
                org.w3c.dom.Document doc = dbf.newDocumentBuilder().parse(inSource);  
                root = (Node) doc.getDocumentElement();  
                System.out.println("doc:"+doc.toString());
            }  
            System.out.println("root:"+root.toString());
            Transformer transformer = TransformerFactory.newInstance()  
                    .newTransformer();  
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");  
            transformer.transform(new DOMSource(root), new StreamResult(  
                    System.out));  
        }  
    }  
	public SOAPPart initSoapPart() throws SOAPException {  
		  
        SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();  
  
        SOAPPart soapPart = soapMessage.getSOAPPart();  
  
        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();  
        SOAPBody soapBody = soapEnvelope.getBody(); 
        SOAPElement cwmp = soapEnvelope.addNamespaceDeclaration("soap",  
                "http://schemas.xmlsoap.org/soap/envelope/");  
        SOAPElement xsi = soapEnvelope.addNamespaceDeclaration("xsi",  
                "http://www.w3.org/2001/XMLSchema-instance");  
        SOAPElement xsd = soapEnvelope.addNamespaceDeclaration("xsd",  
                "http://www.w3.org/2001/XMLSchema");
  
//        SOAPElement id = soapBody.addChildElement("lyy"); 
//        id.setTextContent("this is a test!");  
        return soapPart;  
    }  
	

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
