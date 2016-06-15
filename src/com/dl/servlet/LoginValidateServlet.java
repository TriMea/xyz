package com.dl.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

import com.dl.dao.DogNoValidateDao;
import com.dl.utl.CommonTool;

public class LoginValidateServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	private Logger logger = Logger.getLogger(LoginValidateServlet.class); 
	public LoginValidateServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		String hotelid = request.getParameter("hotelid");
		String rsa = request.getParameter("rsa");
		logger.info("登录校验:"+hotelid+";"+rsa);
		DogNoValidateDao dd = DogNoValidateDao.getInstance();
	     boolean isLoginSuccess = dd.isLoginSuccess(hotelid,rsa);
		HashMap<String, Object> mp = new HashMap<String, Object>();
		if(isLoginSuccess)
		{
			mp.put("code",0);
			mp.put("msg", "登录校验成功");
		}else{
			mp.put("code",1);
			mp.put("msg", "检验码["+rsa+"]登录校验失败");
		}
		PrintWriter out = response.getWriter();
		out.write(JSONObject.fromObject(mp).toString());
		out.flush();
		out.close();
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
